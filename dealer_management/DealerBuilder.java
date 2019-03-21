/**
 * 
 */
package dealer_management;

import java.util.List;

import dealer.CarDealer;
import dealer_working_day.DealerWorkingDay;

/**
 * @author Steve Brown
 *
 *  Super interface for a car dealership builder.
 */
public interface DealerBuilder {

	/*
	 *  Inputs:
	 *  	1. dealerList: List of all dealers. This is updated with the newly created dealership.
	 *  	2. dealerBuilder: The type of dealer to build.
	 *  	3. name: the name of the dealer.
	 *  	4. openingHours: the opening times of the dealer.
	 */
	default void buildDealer(List<CarDealer> dealerList, DealerBuilder dealerBuilder, String name, DealerWorkingDay openingHours) {
		dealerBuilder.build(dealerList, name, openingHours);
	}
	
	/*
	 *  Child dealer builder implements this method.
	 */
	void build(List<CarDealer> dealerList, String name, DealerWorkingDay openingHours);

}
