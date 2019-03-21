/**
 * 
 */
package dealer_management;

import java.util.List;

import dealer.CarDealer;
import dealer_working_day.DealerWorkingDay;
import main_dealer.MainDealer;

/**
 * @author Steve Brown
 *
 *  Creates a new dealer of the type MainDealer.
 */
public interface MainDealerBuilder extends DealerBuilder{

	@Override
	default void build(List<CarDealer> dealerList, String name, DealerWorkingDay openingHours) {
//		System.out.println("Created Main Dealer: " + name);		// TODO - Log
		MainDealer mainDealer = null;
		mainDealer =  new CarDealer.MainDealerBuilder(mainDealer, openingHours)
				.setName(name)
				.buildMainDealer();
		// Add the dealer to the list of dealers.
		dealerList.add(mainDealer);
	}
}
