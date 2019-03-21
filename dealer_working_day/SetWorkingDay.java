package dealer_working_day;

import time.ImmutableTime;

/*
 *  Only franchises are allowed to set the opening hours.
 *  Main dealers' opening hours are set by head office. 
 */
public interface SetWorkingDay {
	/*
	 *  Give a dealer new opening and closing hours.
	 */
	void setWorkingDay(ImmutableTime openingTime, ImmutableTime closingTime);
	
}
