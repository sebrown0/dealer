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
import tasks.task_injectors.CloseDealershipInjector;
import tasks.task_injectors.LogDepartmentEmployeesInjector;
import tasks.task_injectors.ManagementTaskInjector;
import tasks.task_injectors.OpenDealershipInjector;
import tasks.task_injectors.RollCallInjector;
import tasks.task_super_objects.ManagementTask;
import timer.Timer;
import utils.logger.Log;
import utils.logger.Loggable;

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
			logDepartmentEmployees(dealership);
		}		
	}
		
	/*
	 *  Add departments to the dealer.
	 */
	private void createDepartments(CarDealer dealership) {
		DepartmentFactory dealerDepts = new DepartmentFactory(dealership.getDealerDAO());
		
		dealerDepts.createDepartments();
		List<Department> departments = dealerDepts.getDepartments();
		
		if(!departments.isEmpty()) {
			dealership.setDepartments(departments);
			rollCall(departments);
		}
	}
	
	/*
	 *  See which employees are able to work.
	 */
	private void rollCall(List<Department> departments) {
		ManagementTaskInjector injector = new RollCallInjector();

		for(Department d: departments) 
			taskManager.giveTask(injector.getNewTask(d));
	}
	
	private void logDepartmentEmployees(CarDealer dealership) {
		List<Department> departments = dealership.getDepartments();
		ManagementTaskInjector injector = new LogDepartmentEmployeesInjector();

		for(Department d: departments)
			taskManager.giveTask(injector.getNewTask(d));
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
	}
	
	/*
	 *  Check to see if our dealers should be open or closed.
	 */
	private void checkDealerStatus() {
		for (CarDealer carDealer : dealerList) {
			
			DealerWorkingDay workingDay = carDealer.getWorkingDay() ;
			boolean inBusinessHours = workingDay.checkOpeningHours(timer.time());
			
			if(inBusinessHours) {
				// Should be open. If it's not, open it.
				if(workingDay.openForBusiness() == OpeningHours.CLOSED) 
					openOrCloseDealer(new OpenDealershipInjector(), OpeningHours.OPEN, carDealer);
			}else {
				// Should be closed. If it's not, close it.
				if(workingDay.openForBusiness() == OpeningHours.OPEN)
					openOrCloseDealer(new CloseDealershipInjector(), OpeningHours.CLOSED, carDealer);
			}
		}
	}

	/*
	 * Open or close a dealer.
	 */
	private void openOrCloseDealer(ManagementTaskInjector injector, Boolean openingHours, CarDealer carDealer) {
		ManagementTask task = injector.getNewTask(carDealer.getDealerDAO());
		task.executeTask();
		carDealer.getWorkingDay().openForBusiness(openingHours);
	}

	/*
	 *  Force any dealers that are open to close.
	 */
	private void forceClosureOfDealers() {
		for (CarDealer carDealer : dealerList)
			if(carDealer.getWorkingDay().openForBusiness()) 
				openOrCloseDealer(new CloseDealershipInjector(), OpeningHours.CLOSED, carDealer);
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
