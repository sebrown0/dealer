package dealer_management;

import java.util.List;

import dealer.CarDealer;
import dealer_working_day.DealerWorkingDay;
import franchise.FranchiseDealer;

/**
 * @author Steve Brown
 *
 *  Creates a new dealer of the type FranchiseDealer.
 */
public interface FranchiseBuilder extends DealerBuilder {

	@Override
	default void build(List<CarDealer> dealerList, String name, DealerWorkingDay openingHours) {
//		System.out.println("Created Franchise Dealer: " + name);	// TODO - Log
		FranchiseDealer franchise = null;
		franchise =  new CarDealer.FranchiseDealerBuilder(franchise, openingHours)
				.setName(name)
				.buildFranchiseDealer();
		// Add the dealer to the list of dealers.
		dealerList.add(franchise);
	}
}
