
package dealer_management;

import dao.DatabaseDAO;
import dao.SparkSessionDAO;
import task_scheduler.Manager;
import task_scheduler.TaskManager;
import timer.Timer;
import utils.logger.Log;

/**
 * @author Steve Brown
 *
 *   Objects belonging to a Car Dealership that a member of that
 *   dealership would require access to.
 */
public class DealerObjects implements DealerDAO {

	private DatabaseDAO database;		 
	private SparkSessionDAO spark;		 
	private Timer timer;				 
	private Log log;					 
	private TaskManager taskManager;		
	
	/**
	 * @param database: Dealer's data base.
	 * @param spark: Dealer's Spark session.
	 * @param timer: Application/Dealer timer.
	 * @param log: Application/Dealer log.
	 * @param taskManager: Dealer's TaskManager.
	 */
	public DealerObjects(DatabaseDAO database, SparkSessionDAO spark, Timer timer, Log log, TaskManager taskManager) {
		super();
		if(database != null) {
			this.database = database;
			System.out.println("DB OK");
		}
		
		if(spark != null) {
			this.spark = spark;
			System.out.println("Spark OK");
		}
		if(timer != null) {
			this.timer = timer;
			System.out.println("Timer OK");
		}
		
		if(log != null) {
			this.log = log;
			System.out.println("Log OK");
		}
		
		if(taskManager != null) {
			this.taskManager = taskManager;
			System.out.println("TM OK");
		}
	}

	/* (non-Javadoc)
	 * @see dealer_management.DealerDAO#getDatabase()
	 */
	@Override
	public DatabaseDAO getDatabase() {
		return database;
	}

	/* (non-Javadoc)
	 * @see dealer_management.DealerDAO#getSpark()
	 */
	@Override
	public SparkSessionDAO getSpark() {
		return spark;
	}

	/* (non-Javadoc)
	 * @see dealer_management.DealerDAO#getTimer()
	 */
	@Override
	public Timer getTimer() {
		return timer;
	}

	/* (non-Javadoc)
	 * @see dealer_management.DealerDAO#getLog()
	 */
	@Override
	public Log getLog() {
		return log;
	}

	/* (non-Javadoc)
	 * @see dealer_management.DealerDAO#getTaskManager()
	 */
	@Override
	public Manager getTaskManager() {
		return taskManager;
	}
	
}
