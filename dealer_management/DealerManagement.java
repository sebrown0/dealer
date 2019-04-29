/**
 * 
 */
package dealer_management;

import java.util.ArrayList;
import java.util.List;

import dealer.CarDealer;
import dealer_working_day.DealerWorkingDay;
import dealer_working_day.OpeningHours;
import departments.department.Department;
import observer.Observer;
import observer.ObserverMessage;
import task_scheduler.TaskManager;
import tasks.task_details.Details;
import tasks.task_details.TasksDetails;
import tasks.task_injectors.AtomicTaskInjector;
import tasks.task_injectors.CloseDealershipInjector;
import tasks.task_injectors.OpenDealershipInjector;
import tasks.task_injectors.RollCallInjector;
import tasks.task_super_objects.AtomicTask;
import timer.Timer;
import utils.Log;
import utils.Loggable;

/**
 * @author Steve Brown
 *
 *  Manages dealerships.
 *  	1. Creates them.
 *   	2. Opens them.
 *   	3. Closes them.
 *   
 *   If HeadOffice closes then all open dealers are told to close.
 *   This means that HO should be running continuously during opening hours. 
 */
public class DealerManagement implements Observer, Loggable{

	private Timer timer;		 								// Supplied by HeadOffice.
	private TaskManager taskManager;							// Supplied by HeadOffice.
	private Log log;											// Supplied by HeadOffice.
	private List<CarDealer> dealerList = new ArrayList<>();		// A list of all dealers.
	private int firstOpeningTime = Integer.MAX_VALUE;			// The time that the first dealer to opens.
	private int lastClosingTime = Integer.MIN_VALUE;			// The time that the first dealer closes.
	
	public DealerManagement(Timer timer, TaskManager taskManager, Log log) {
		this.timer = timer;
		this.taskManager = taskManager;
		this.log = log;
	}
	
	/*
	 *  Create a new dealer (franchise or main).
	 */
	public void createNewDealer(DealerBuilder typeOfDealerBuilder, String name, 
									DealerWorkingDay openingHours, DealerDAO dealerDAO) {
		

		CarDealer dealership = typeOfDealerBuilder.buildDealer(typeOfDealerBuilder, name, 
				openingHours, dealerDAO);

		if(dealership != null) {
			log.logEntry(this, "Created Car Dealer: " + dealership.getName());
			dealerList.add(dealership);
			getFirstAndLast();
			createDepartments(dealership);
		}		
	}
		
		
	/*
	 *  Add departments to the dealer.
	 */
	private void createDepartments(CarDealer dealership) {
		CreateDepartments dealerDepts = new CreateDepartments(dealership.getDealerDAO());
		
		dealerDepts.createDepartments();
		List<Department> departments = dealerDepts.getDepartments();
		
		if(!departments.isEmpty()) {
			dealership.setDepartments(departments);
			rollCall(departments, dealership.getDealerDAO());
		}
	}
	
	/*
	 *  See which employees are able to work.
	 */
	private void rollCall(List<Department> departments, DealerDAO dealerDAO) {
		AtomicTaskInjector injector = new RollCallInjector();

		for(Department d: departments) {
			TasksDetails tasksDetails =	new Details("Roll Call", "TODO", d); // TODO
			taskManager.giveTask(injector.getNewTask(tasksDetails, d));
		}	
	}
	
	/*
	 *  Find the first opening time and the last closing time.
	 */
	private void getFirstAndLast() {
		DealerWorkingDay workingDay = null;
		for (CarDealer carDealer : dealerList) {
			workingDay = carDealer.getWorkingDay() ;
			if(workingDay.openingTimeSeconds() < firstOpeningTime) 
				firstOpeningTime = workingDay.openingTimeSeconds();
			
			if(workingDay.closingTimeSeconds() > lastClosingTime) 
				lastClosingTime = workingDay.closingTimeSeconds();
		}
//		System.out.println("Opening time: Old = " + firstOpeningTime + " New = " + workingDay.openingTimeSeconds()); 	// TODO - Log
//		System.out.println("Closing time: Old = " + lastClosingTime + " New = " + workingDay.closingTimeSeconds());		// TODO - Log
	}

	/*
	 *  Open a dealer.
	 */
	private void openDealership(DealerWorkingDay workingDay, CarDealer carDealer) {
		AtomicTaskInjector injector = new OpenDealershipInjector();
		AtomicTask task = injector.getNewTask(new Details("Open Dealer", "objId"), null);

		taskManager.giveTask(task);
		workingDay.openForBusiness(OpeningHours.OPEN);
	}

	/*
	 *  Close a dealer.
	 */
	private void closeDealership(DealerWorkingDay workingDay, CarDealer carDealer) {
		AtomicTaskInjector injector = new CloseDealershipInjector();
		AtomicTask task = injector.getNewTask(new Details("Close Dealer", "objId"), null);
		taskManager.giveTask(task);
		workingDay.openForBusiness(OpeningHours.CLOSED);
	}
	
	/*
	 *  Check to see if our dealers should be open or closed.
	 */
	private void checkDealerStatus() {
		for (CarDealer carDealer : dealerList) {
			
			DealerWorkingDay workingDay = carDealer.getWorkingDay() ;
			boolean inBusinessHours = workingDay.checkOpeningHours(timer.time());
			
			// Check to see if the business should be open or closed.
			if(inBusinessHours) {
				// Should be open. If it's not, open it.
				if(workingDay.openForBusiness() == OpeningHours.CLOSED) {
					openDealership(workingDay, carDealer);
				}
			}else {
				// Should be closed. If it's not, close it.
				if(workingDay.openForBusiness() == OpeningHours.OPEN) {
					closeDealership(workingDay, carDealer);
				}
			}
		}
	}
	
	/*
	 *  Close any dealers that are open.
	 */
	private void forceClosureOfDealers() {
		for (CarDealer carDealer : dealerList)
			if(carDealer.getWorkingDay().openForBusiness()) 
				closeDealership(carDealer.getWorkingDay(), carDealer);
	}

	/*
	 * (non-Javadoc)
	 * @see observer.Observer#updateObserver()
	 */
	@Override
	public void updateObserver(ObserverMessage msg) {
			// Do work in here....
			switch (msg) {
			case DO_WORK:
				checkDealerStatus();
				break;

			case STOPPING:				
				// Head office has told us to close dealers that are open.
				forceClosureOfDealers();
				break;
				
			default:
				break;
			}
	}
	
	// TODO - Test purposes?
	public CarDealer getFirstDealer() {
			return dealerList.get(0);
	}
		
	public CarDealer getDealerByName(String dealerName) {
		for (CarDealer d : dealerList) 
			if(d.getName() == dealerName)
				return d;
		return null;
	}

	/*
	 *  Getters and Setters below.
	 */
	public int getFirstOpeningTime() {
		return firstOpeningTime;
	}

	public int getLastClosingTime() {
		return lastClosingTime;
	}

}
