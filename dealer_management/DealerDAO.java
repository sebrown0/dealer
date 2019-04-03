package dealer_management;

import dao.DatabaseDAO;
import dao.SparkSessionDAO;
import task_scheduler.Manager;
import timer.Timers;
import utils.Log;

public interface DealerDAO {

	DatabaseDAO getDatabase();

	SparkSessionDAO getSpark();

	Timers getTimer();

	Log getLog();

	Manager getTaskManager();

}