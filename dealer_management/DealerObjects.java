
package dealer_management;

import dao.DatabaseDAO;
import dao.SparkSessionDAO;
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
	
	/**
	 * @param database: Dealer's data base.
	 * @param spark: Dealer's Spark session.
	 * @param timer: Application/Dealer timer.
	 * @param log: Application/Dealer log.
	 */
	public DealerObjects(DatabaseDAO database, SparkSessionDAO spark, Timer timer, Log log, TaskManager taskManager) {
		super();
		if(database != null) 
			this.database = database;
		
		if(spark != null) 
			this.spark = spark;
			
		if(timer != null) 
			this.timer = timer;
		
		if(log != null) 
			this.log = log;
	}

	@Override
	public DatabaseDAO getDatabase() {
		return database;
	}

	@Override
	public SparkSessionDAO getSpark() {
		return spark;
	}

	@Override
	public Timer getTimer() {
		return timer;
	}

	@Override
	public Log getLog() {
		return log;
	}
}
