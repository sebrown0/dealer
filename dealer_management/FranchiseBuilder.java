package dealer_management;

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
	default CarDealer build( 
			String name, 
			DealerWorkingDay openingHours,
			DealerDAO dealerDAO) {

				FranchiseDealer franchiseDealer = null;
				franchiseDealer =  new CarDealer.FranchiseDealerBuilder(franchiseDealer, openingHours, dealerDAO)
						.setName(name)
						.setWorkingDay(openingHours)
						.setDealerDAO(dealerDAO)
						.buildFranchiseDealer();

				dealerDAO.getLog().logEntry("TODO - FranchiseBuilder", "Created franchise Dealer (" + name + ")");
				return franchiseDealer;
			}
}
