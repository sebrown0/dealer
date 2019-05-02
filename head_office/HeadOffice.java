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
import timer.Timer;
import timer.TimerDurationSeconds;
import utils.Log;
import utils.Logger.LogHelper;
import utils.Simulator;

/**
 * @author Steve Brown
 *
 *  
 */
public class HeadOffice implements Observer {		
	private Timer timer; 
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
				
		timer = new SlowTimer(
				time,								// Starting time of the timer. 
				new SlowHeartbeat("HeadOffice"), 	// Use a slow heart beat.
				duration, 							// Timer will run for this duration.
				"HeadOfficeTimer",					// Owner of the timer.
				this);								// Register us as an observer of the timer.
		 
		timer.startTimer();
		log = LogHelper.logInstance(true, timer); 	// A new log to begin the day.

		taskManager = new TaskManager(timer, new FastHeartbeat("Head Office Task Manager"), log);		
		dealershipManagement = new DealerManagement(timer, taskManager, log);
		headOffice.registerObserver(taskManager);
		headOffice.registerObserver(dealershipManagement); 

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
	public Timer timer() {
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
