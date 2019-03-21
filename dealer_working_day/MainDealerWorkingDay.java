/**
 * 
 */
package dealer_working_day;

import time.ImmutableTime;

/**
 * @author Steve Brown
 * 
 *  A main dealer's opening times are fixed.
 * 
 */
public class MainDealerWorkingDay extends DealerWorkingDay {

	public MainDealerWorkingDay(ImmutableTime openingTime, ImmutableTime closingTime) {
		super(openingTime, closingTime);
	}
}
