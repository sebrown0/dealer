
package dealer_management;

import dao.DatabaseDAO;
import dao.SparkSessionDAO;
import task_scheduler.Manager;
import task_scheduler.TaskManager;
import timer.Timers;
import utils.Log;

/**
 * @author Steve Brown
 *
 *   Objects belonging to a Car Dealership that a member of that
 *   dealership would require access to.
 */
public class DealerObjects implements DealerDAO {

	private DatabaseDAO database;		 
	private SparkSessionDAO spark;		 
	private Timers timer;				 
	private Log log;					 
	private TaskManager taskManager;		
	
	/**
	 * @param database: Dealer's data base.
	 * @param spark: Dealer's Spark session.
	 * @param timer: Application/Dealer timer.
	 * @param log: Application/Dealer log.
	 * @param taskManager: Dealer's TaskManager.
	 */
	public DealerObjects(DatabaseDAO database, SparkSessionDAO spark, Timers timer, Log log, TaskManager taskManager) {
		super();
		this.database = database;
		this.spark = spark;
		this.timer = timer;
		this.log = log;
		this.taskManager = taskManager;
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
	public Timers getTimer() {
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
