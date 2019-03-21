/**
 * 
 */
package car_dealership.main_dealer;

import java.util.concurrent.TimeUnit;

import car_dealership.dealer_working_day.*;

/**
 * @author Steve Brown
 *
 *  TODO
 */
public interface MainDealerOpeningHours extends OpeningHours{
	void setLengthOfWorkingDay(TimeUnit timeUnit);
}
