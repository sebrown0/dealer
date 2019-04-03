/**
 * 
 */
package dealer_management;

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
	default CarDealer build(
			String name, 
			DealerWorkingDay openingHours,
			DealerDAO dealerDAO) {
	
				MainDealer mainDealer = null;
				mainDealer =  new CarDealer.MainDealerBuilder(mainDealer, openingHours, dealerDAO)
						.setName(name)
						.setWorkingDay(openingHours)
						.setDealerDAO(dealerDAO)
						.buildMainDealer();

				dealerDAO.getLog().logEntry("TODO - MainDealerBuilder", "Created Main Dealer (" + name + ")");
				return mainDealer;
			}
}
