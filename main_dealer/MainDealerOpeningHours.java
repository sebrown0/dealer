/**
 * 
 */
package main_dealer;

import java.util.concurrent.TimeUnit;

import dealer_working_day.OpeningHours;

/**
 * @author Steve Brown
 *
 *  TODO
 */
public interface MainDealerOpeningHours extends OpeningHours{
	void setLengthOfWorkingDay(TimeUnit timeUnit);
}
