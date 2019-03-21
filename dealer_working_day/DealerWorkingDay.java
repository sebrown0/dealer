/**
 * 
 */
package car_dealership.dealer_working_day;

import time.ImmutableTime;
import time.MutableTime;

/**
 * @author Steve Brown
 *
 *  The opening and closing time of a dealer.
 */
public abstract class DealerWorkingDay implements OpeningHours {

	protected ImmutableTime openingTime;			// Opening time as a non-changeble time.
	protected ImmutableTime closingTime;			// Closing time as a non-changeble time.
	private int openingTimeInSeconds;				// Opening time in seconds.
	private int closingTimeInSeconds;				// Closing time in seconds.
	private int lengthOfDayInSeconds;				// Length of day in seconds.
	private boolean withinOpeningHours = false;		// Currently we should be open.
	private boolean openForBusiness = CLOSED;		// Are we open or closed.
	
	/*
	 *  A new working day with an opening and closing time.
	 */
	public DealerWorkingDay(ImmutableTime openingTime, ImmutableTime closingTime) {
		this.openingTime = openingTime;
		this.closingTime = closingTime;
		this.openingTimeInSeconds = openingTime.givenTimeSeconds();
		this.closingTimeInSeconds = closingTime.givenTimeSeconds();
		this.lengthOfDayInSeconds = setLengthOfWorkingDay();
	}

	/**
	 *  Set the length of the working day for a dealership.
	 *  If it's more than 86400 (24hrs) default to 28800 (8hrs).
	 */
	private int setLengthOfWorkingDay() {
		int lengthOfDay =  closingTimeInSeconds - openingTimeInSeconds; 
		return (lengthOfDay > 86400) ? 28800 : lengthOfDay;
	}
	
	/*
	 * (non-Javadoc)
	 * @see working_day.OpeningHours#open(time.MutableTime)
	 */
	@Override
	public boolean checkOpeningHours(MutableTime currentTime) {
		int time = currentTime.currentTime();

		if((openingTime.givenTimeSeconds() <= time) && (closingTime.givenTimeSeconds() >= time)) {
			withinOpeningHours = OpeningHours.OPEN;
		}else {
			withinOpeningHours = OpeningHours.CLOSED;
		}
		return withinOpeningHours;
	}
	
	/*
	 * (non-Javadoc)
	 * @see working_day.OpeningHours#openingTimeSeconds()
	 */
	@Override
	public int openingTimeSeconds() {
		// The time in seconds
		return openingTime.givenTimeSeconds();
	}

	/*
	 * (non-Javadoc)
	 * @see working_day.OpeningHours#closingTimeSeconds()
	 */
	@Override
	public int closingTimeSeconds() {
		return closingTime.givenTimeSeconds();
	}

	/*
	 * (non-Javadoc)
	 * @see working_day.OpeningHours#openingTimeFormatted()
	 */
	@Override
	public String openingTimeFormatted() {
		return openingTime.formattedTime();
	}

	/*
	 * (non-Javadoc)
	 * @see working_day.OpeningHours#closingTimeFormatted()
	 */
	@Override
	public String closingTimeFormatted() {
		return closingTime.formattedTime();
	}

	/*
	 * (non-Javadoc)
	 * @see working_day.OpeningHours#getLengthOfWorkingDay()
	 */
	@Override
	public int getLengthOfWorkingDay() {
		return lengthOfDayInSeconds;
	}

	/*
	 *  Getters and Setters below
	 */
	public int getOpeningTimeInSeconds() {
		return openingTimeInSeconds;
	}

	public int getClosingTimeInSeconds() {
		return closingTimeInSeconds;
	}
	
	/*
	 *  Set if the dealership is open for business.
	 */
	public void openForBusiness(boolean openOrClosed) {
		openForBusiness = openOrClosed;
	}
	
	/*
	 *  Is the dealership open for business.
	 */
	public boolean openForBusiness() {
		return openForBusiness;
	}
}
