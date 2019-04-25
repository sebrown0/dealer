/**
 * 
 */
package head_office;

import dealer.CarDealer;
import dealer_management.DealerManagement;
import heartbeat.FastHeartbeat;
import heartbeat.SlowHeartbeat;
import observer.GenericSubject;
import observer.Observer;
import observer.ObserverMessage;
import observer.Subject;
import task_scheduler.TaskManager;
import time.MutableTime;
import timer.SlowTimer;
import timer.TimerDurationSeconds;
import timer.Timers;
import utils.Log;
import utils.Logger.LogHelper;
import utils.Simulator;

/**
 * @author Steve Brown
 *
 *  
 */
public class HeadOffice implements Observer {		
	private Timers timer; 
	private DealerManagement dealershipManagement;
	private TaskManager taskManager;								
	private static Log log;											
	private Subject headOffice = new GenericSubject("HeadOffice");
	
	private HeadOffice() {};
	
	/*
	 *  Initialises HeadOffice.
	 *  	1. Creates a new log file.
	 *  	2. Creates a new timer. This is used throughout the app as the current time.
	 *  	3. Gets a TaskManager - TODO remove
	 */
	public void initialise(MutableTime time, TimerDurationSeconds duration) {
		log = LogHelper.logInstance(true); 			// A new log to begin the day.
		
		timer = new SlowTimer(
				time,								// Starting time of the timer. 
				new SlowHeartbeat("HeadOffice"), 	// Use a slow heart beat.
				duration, 							// Timer will run for this duration.
				"HeadOfficeTimer",					// Owner of the timer.
				this);								// Register us as an observer of the timer.
		 
		timer.startTimer();

		// Get a TaskManager to deal with all tasks for a dealer.
		taskManager = new TaskManager(timer, new FastHeartbeat("Head Office Task Manager"), log);
		
		dealershipManagement = new DealerManagement(timer, taskManager, log);//, this);
		
		headOffice.registerObserver(taskManager);
		headOffice.registerObserver(dealershipManagement); // THIS SHOULD BE A DEALER!!!!!!!!!!!!!

	}
	
	/*
	 * 	****************************** USED FOR TESTING START******************************
	 * 	before deleting make sure that they are not needed.
	 */
	public TaskManager getTaskManager() {
		return taskManager;
	}
		
	/*
	 * 	****************************** USED FOR TESTING END******************************
	 */
	
	public CarDealer getDealerByName(String dealerName) {
		return dealershipManagement.getDealerByName(dealerName);
	}
	
	@Override
	public void updateObserver(ObserverMessage msg) {
		// TODO - Only if INITIALISED!
		// Do work in here....
		
		switch (msg) {
		case CHANGED:
			headOffice.notifyObservers(ObserverMessage.DO_WORK);

			// TODO - Test below.
//			System.out.println("-->>>" + headOffice.numberOfObservers());
//			i++;
//			if(i == 1)
//				dealershipManagement.createNewDealer(new FranchiseBuilder() {
//				}, "Ford", new FranchiseDealerWorkingDay(new Time(9, 00, 00), new Time(9, 00, 04)));
			
//			if(i == 2)
//				dealershipManagement.createNewDealer(new MainDealerBuilder() {
//				}, "VW", new MainDealerWorkingDay(new Time(9, 00, 00), new Time(9, 00, 7)));
			
//			if(i == 3)
//				dealershipManagement.createNewDealer(
//						new MainDealerBuilder() {},
//						"Fiat", 
//						new MainDealerWorkingDay(new Time(9, 00, 00), new Time(9, 00, 9)),
//						new DealerObjects(
//								new MySqlDB(log), 
//								new Spark("Fiat", "local", true, log), 
//								timer, 
//								log, 
//								new TaskManager(timer, new FastHeartbeat("Fiat Task Manager"), log))	 
//						);
			// TODO - End of test. 		

			break;

		case STOPPING:
			headOffice.notifyObservers(ObserverMessage.STOPPING);
			break;
			
		default:
			break;
		}

	}
	
	public Log appLog() {
		return log;
	}
	
	/*
	 *  If using with a simulator we need to register it as an observer.
	 */
	public void registerSimulator(Simulator sim) {
		if(sim != null)
			this.headOffice.registerObserver(sim);
	}
	
	/*
	 *  Get HO's management.
	 */
	public DealerManagement management() {
		return dealershipManagement;
	}
	
	/*
	 *  Get the App's timer.
	 */
	public Timers timer() {
		return timer;
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
	public static HeadOffice getInstance (MutableTime time, TimerDurationSeconds duration) {
		HeadOfficeHelper.INSTANCE.initialise(time, duration);
		return HeadOfficeHelper.INSTANCE;
	}

}
