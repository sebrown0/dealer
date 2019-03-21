/**
 * 
 */
package dealer_working_day;

import time.MutableTime;

/**
 * @author Steve Brown
 *
 */
public interface OpeningHours {
	static final boolean OPEN = true;			// Within dealership's opening hours.
	static final boolean CLOSED = false;		// Outside dealership's opening hours.
	
	/*
	 * Get the openingTimeSeconds.
	 */
	int openingTimeSeconds();
	
	/*
	 * Get the closingTimeSeconds.
	 */
	int closingTimeSeconds();
	
	/*
	 * Get the Length Of Working Day in seconds.
	 */
	int getLengthOfWorkingDay();
	
	/* 
	 * Opening time in the format hh:mm:ss
	 */
	String openingTimeFormatted();
	
	/* 
	 * Closing time in the format hh:mm:ss
	 */
	String closingTimeFormatted();
	
	/*
	 *  Check to see if the dealership should be open or closed for the given time.
	 */
	boolean checkOpeningHours(MutableTime currentTime);
}
