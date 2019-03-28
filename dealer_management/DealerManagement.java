/**
 * 
 */
package dealer_management;

import java.util.ArrayList;
import java.util.List;

import dealer.CarDealer;
import dealer_working_day.DealerWorkingDay;
import dealer_working_day.OpeningHours;
import depts.Department;
import observer.Observer;
import observer.ObserverMessage;
import task_scheduler.TaskManager;
import tasks.CloseDealershipInjector;
import tasks.OpenDealershipInjector;
import tasks.ScheduledTime;
import tasks.TaskConsumer;
import tasks.TaskInjector;
import tasks.TypeOfTask;
import timer.Timers;

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

	private Timers timer = null; 								// Supplied by HeadOffice.
	private TaskManager taskScheduler;							// Supplied by HeadOffice.
	private Department department;								// Supplied by HeadOffice.
	private List<CarDealer> dealerList = new ArrayList<>();		// A list of all dealers.
	private int firstOpeningTime = Integer.MAX_VALUE;			// The time that the first dealer to opens.
	private int lastClosingTime = Integer.MIN_VALUE;			// The time that the first dealer closes.
			
	public DealerManagement(Timers timer, TaskManager taskScheduler, Department department) {
		this.timer = timer;
		this.taskScheduler = taskScheduler;
		this.department = department;
	}
	
	/*
	 *  Create a new dealer (franchise or main).
	 */
	public void createNewDealer(DealerBuilder typeOfDealerBuilder, String name, DealerWorkingDay openingHours) {
		typeOfDealerBuilder.buildDealer(dealerList, typeOfDealerBuilder, name, openingHours);
		getFirstAndLast();
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
		TaskInjector injector = new OpenDealershipInjector();
		TaskConsumer task = injector.getNewTask(department,TypeOfTask.REPEATABLE, 
				new ScheduledTime(timer.currentTime() + 2, timer.currentTime() + 10)); 
//		TaskConsumer task = injector.getNewTask(department);
//		System.out.println(timer.time().formattedTime() + " - Opening " + carDealer.getName() + " for business."); // TODO - Log
		workingDay.openForBusiness(OpeningHours.OPEN);
		taskScheduler.manageTask(task);
	}
	
	/*
	 *  Close a dealer.
	 */
	private void closeDealership(DealerWorkingDay workingDay, CarDealer carDealer) {
		TaskInjector injector = new CloseDealershipInjector();
		TaskConsumer task = injector.getNewTask(department);
		System.out.println(timer.time().formattedTime() + " - Closing " + carDealer.getName() + " for business."); // TODO - Log		
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
	 *  Told to close any dealers that are open.
	 */
	private void forceClosureOfDealers() {
		System.out.println("(DM)");
		for (CarDealer carDealer : dealerList) {
			if(carDealer.getWorkingDay().openForBusiness()) {
				System.out.println("(DM) closing dealers");
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
//				dealers.notifyObservers(ObserverMessage.STOPPING);
				
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
