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
import tasks.task_creators.AtomicTaskDetails;
import tasks.task_creators.AtomicTaskInjector;
import tasks.task_creators.DepartmentTaskDetails;
import tasks.task_creators.DepartmentTaskInjector;
import tasks.task_creators.DepartmentTasksDetails;
import tasks.task_creators.TaskConsumer;
import tasks.task_creators.TypeOfTask;
import tasks.task_injectors.CloseDealershipInjector;
import tasks.task_injectors.OpenDealershipInjector;
import tasks.task_injectors.RollCallInjector;
import timer.Timers;
import utils.Log;

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
public class DealerManagement implements Observer{

	private Timers timer;		 								// Supplied by HeadOffice.
	private TaskManager taskScheduler;							// Supplied by HeadOffice.
	private Log log;											// Supplied by HeadOffice.
	private List<CarDealer> dealerList = new ArrayList<>();		// A list of all dealers.
	private int firstOpeningTime = Integer.MAX_VALUE;			// The time that the first dealer to opens.
	private int lastClosingTime = Integer.MIN_VALUE;			// The time that the first dealer closes.
			
	public DealerManagement(Timers timer, TaskManager taskScheduler, Log log) {
		this.timer = timer;
		this.taskScheduler = taskScheduler;
		this.log = log;
	}
	
	/*
	 *  Create a new dealer (franchise or main).
	 */
	public void createNewDealer(
		DealerBuilder typeOfDealerBuilder, 			
		String name, 
		DealerWorkingDay openingHours,
		DealerDAO dealerDAO) {
		
		CarDealer dealership = typeOfDealerBuilder.buildDealer(typeOfDealerBuilder, name, 
				openingHours, dealerDAO);
		
		if(dealership != null) {
			dealerDAO.getLog().logEntry(name, "Created Car Dealer" + dealership.getName());
			dealerList.add(dealership);
			getFirstAndLast();
			createDepartments(dealership);
		}		
	}
		
	/*
	 *  Add departments to the dealer.
	 */
	private void createDepartments(CarDealer dealership) {
		CreateDepartments dealerDepts 
			= new CreateDepartments(dealership.getDealerDAO());
		
		dealerDepts.createDepartments();
		List<Department> departments = dealerDepts.getDepartments();
		dealership.setDepartments(departments);
		
		if(!departments.isEmpty()) {
			rollCall(departments);
		}
	}
	
	/*
	 *  See which employees are able to work.
	 */
	private void rollCall(List<Department> departments) {
		DepartmentTaskInjector injector = new RollCallInjector();
		for(Department d: departments) {
			DepartmentTasksDetails tasksDetails = 
					new DepartmentTaskDetails(TypeOfTask.ATOMIC, d.log(), "Roll Call", "id", d);
			taskScheduler.manageTask(injector.getNewTask(tasksDetails)); 
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
		TaskConsumer task = injector.getNewTask(new AtomicTaskDetails(TypeOfTask.ATOMIC, log, "Open Dealer", "objId"));

		workingDay.openForBusiness(OpeningHours.OPEN);
		taskScheduler.manageTask(task);
	}
	
	/*
	 *  Close a dealer.
	 */
	private void closeDealership(DealerWorkingDay workingDay, CarDealer carDealer) {
		AtomicTaskInjector injector = new CloseDealershipInjector();
		TaskConsumer task = injector.getNewTask(new AtomicTaskDetails(TypeOfTask.ATOMIC, log, "Close Dealer", "objId"));
		
		workingDay.openForBusiness(OpeningHours.CLOSED);
		taskScheduler.manageTask(task);
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
		for (CarDealer carDealer : dealerList) {
			if(carDealer.getWorkingDay().openForBusiness()) {
				closeDealership(carDealer.getWorkingDay(), carDealer);
			}
		}
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
