/**
 * 
 */
package dealer_management;

import dealer.CarDealer;
import dealer_working_day.DealerWorkingDay;

/**
 * @author Steve Brown
 *
 *  Super interface for a car dealership builder.
 */
public interface DealerBuilder {

	default CarDealer buildDealer(
			DealerBuilder dealerBuilder,	// The type of dealer to build.
			String name, 					// The name of the dealer.
			DealerWorkingDay openingHours,	// The opening times of the dealer.
			DealerDAO dealerDAO){			// Objects that a dealer requires, i.e. data base.
				return dealerBuilder.build( 
					name, 
					openingHours, 
					dealerDAO);
			}

	/*
	 *  Child dealer builder implements this method.
	 */
	CarDealer build( 
			String name, 
			DealerWorkingDay openingHours,
			DealerDAO dealerDAO);
}
