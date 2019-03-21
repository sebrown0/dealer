/**
 * 
 */
package main_dealer;

import dealer.CarDealer;

/**
 * @author Steve Brown
 *
 *  Implements a main dealer.
 */
public class MainDealer extends CarDealer implements MainDealerFacilities{

	public MainDealer(DealershipBuilder dealershipBuilder) {
		super(dealershipBuilder);
	}

	@Override
	public void warranty() {
		// TODO
		System.out.println("Offer waranty");
	}

	@Override
	public void sale() {
		// TODO
		System.out.println("Main dealer sale");
	}

}
