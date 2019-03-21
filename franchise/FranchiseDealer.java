/**
 * 
 */
package car_dealership.franchise;

import car_dealership.dealer.CarDealer;

/**
 * @author Steve Brown
 *
 *  Implements a Dealer of type Franchise Dealer.
 */
public class FranchiseDealer extends CarDealer implements FranchiseFacilities {

	public FranchiseDealer(DealershipBuilder dealershipBuilder) {
		super(dealershipBuilder);
	}

	@Override
	public void sale() {
		System.out.println("Franchise dealer sale");
	}

}
