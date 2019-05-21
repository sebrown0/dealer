package dealer_management;

import dao.DatabaseDAO;
import dao.SparkSessionDAO;
import timer.Timer;
import utils.logger.Log;

public interface DealerDAO {

	DatabaseDAO getDatabase();

	SparkSessionDAO getSpark();

	Timer getTimer();

	Log getLog();
}