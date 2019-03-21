/**
 * 
 */
package car_dealership.franchise;

import java.util.concurrent.TimeUnit;

import car_dealership.dealer_working_day.OpeningHours;

/**
 * @author Steve Brown
 *
 *  Set the opening hours for a franchise.
 */
public interface FranchiseOpeningHours extends OpeningHours {
	void setOpeningHours(int openingTime, int closingTime, TimeUnit timeUnit);
}
