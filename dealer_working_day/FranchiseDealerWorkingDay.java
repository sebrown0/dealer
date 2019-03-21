/**
 * 
 */
package dealer_working_day;

import time.ImmutableTime;

/**
 * @author Steve Brown
 * 
 * The length of the day is set at  instantiation. 
 * To change the times call the setWorkingDay method.
 * 
 */
public class FranchiseDealerWorkingDay extends DealerWorkingDay implements SetWorkingDay{
	
	public FranchiseDealerWorkingDay(ImmutableTime openingTime, ImmutableTime closingTime) {
		super(openingTime, closingTime);
	}

	/*
	 * (non-Javadoc)
	 * @see working_day.SetWorkingDay#setWorkingDay(time.ImmutableTime, time.ImmutableTime)
	 */
	@Override
	public void setWorkingDay(ImmutableTime openingTime, ImmutableTime closingTime) {
		this.openingTime = openingTime;
		this.closingTime = closingTime;
	}

}
