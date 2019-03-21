/**
 * 
 */
package head_office;

import java.util.concurrent.TimeUnit;

import dealer_management.DealerManagement;
import dealer_management.FranchiseBuilder;
import dealer_management.MainDealerBuilder;
import dealer_working_day.FranchiseDealerWorkingDay;
import dealer_working_day.MainDealerWorkingDay;
import database.DatabaseDAO;
import database.MySqlDB;
import depts.Department;
import heartbeat.FastHeartbeat;
import heartbeat.SlowHeartbeat;
import observer.GenericSubject;
import observer.Observer;
import observer.Subject;
import task_scheduler.TaskScheduler;
import time.ChangeableTime;
import time.Time;
import timer.DurationInSeconds;
import timer.SlowTimer;
import timer.Timer;

/**
 * @author Steve Brown
 *
 *  The object that manages all other objects.
 */

public class HeadOffice extends Department implements Observer {

		
	// Head office's rules...
	private static final DatabaseDAO database = new MySqlDB();

	// Use officeHeartBeat for the timer. When the timer expires so will officeHeartBeat.
	private SlowHeartbeat officeHeartBeat = new SlowHeartbeat("HeadOffice");
	private final Timer timer = new SlowTimer(
			new ChangeableTime(8,59,58),					// Starting time of the timer. 
			officeHeartBeat, 								// Use the office heartbeat
			new DurationInSeconds(TimeUnit.SECONDS, 20), 	// Timer will run for this duration.
			"HeadOfficeTimer",								// Owner of the timer.
			this);											// Register us as an observer of the timer.

 
	private final DealerManagement dealershipManagement;
	private Subject dealers = new GenericSubject();
	
	int i = 0; // TODO - Remove
		
	private HeadOffice() {
		super("Dept - HeadOffice");
		timer.startTimer();
		this.dealershipManagement = new DealerManagement(timer, new TaskScheduler(timer, new FastHeartbeat("Task Scheduler")), this);
		dealers.registerObserver(dealershipManagement);
	}
	
	public DatabaseDAO getDatabase() {
		return database;
	}
	
	/*
	 * (non-Javadoc)
	 * @see observer.Observer#updateObserver()
	 */
	@Override
	public void updateObserver() {
		// Do work in here....
		dealers.notifyObservers();
		
		// TODO - Test below. 
		i++;
		if(i == 2)
			dealershipManagement.createNewDealer(new FranchiseBuilder() {
			}, "Ford", new FranchiseDealerWorkingDay(new Time(9, 00, 07), new Time(9, 00, 12)));
		
		if(i == 4)
			dealershipManagement.createNewDealer(new MainDealerBuilder() {
			}, "VW", new MainDealerWorkingDay(new Time(9, 00, 04), new Time(9, 00, 07)));
		
		if(i == 6)
			dealershipManagement.createNewDealer(new MainDealerBuilder() {
			}, "Fiat", new MainDealerWorkingDay(new Time(9, 00, 05), new Time(9, 00, 9)));
		// TODO - End of test. 		

		// Do some more work here if necessary....
//		System.out.println("Updating Observer (HeadOffice)");
//		for (ManagementPolicy manager : managers) {		// Managers checking their workers.
//			if(manager.shouldTakeAction()) {
//				if(manager.idleWorkerCheck() < 0) {		// Check the worker according to WorkerPolicy.
//					new Thread(() -> {
//						manager.stopWorker();			// Worker has been idle to long so stop it.
//					}).start();
//				}
//			}
//		}

	}
	
	/*
	 *  Create a single instance of HeadOffice. 
	 */
	public static class HeadOfficeHelper {
		private static final HeadOffice INSTANCE = new HeadOffice();
	}
	
	/*
	 *  Get the instance.
	 */
	public static HeadOffice getInstance () {
		return HeadOfficeHelper.INSTANCE;
	}

}
