package dealer;

import java.util.List;

import dealer_management.DealerDAO;
import dealer_working_day.DealerWorkingDay;
import dealer_working_day.OpeningHours;
import departments.department.Department;
import franchise.FranchiseDealer;
import main_dealer.MainDealer;

/*
 *  @author Steve Brown.
 *  
 *  
 *  Represents a car dealership. 
 *  Derived objects are Main Dealer (more functionality) and Franchise (less functionality).
 *  
 *  Contains a helper class to access an instance of a dealership.
 */
public class CarDealer {//implements Dealership{
		
	private String name = "";
	private DealerWorkingDay workingDay = null;
//	private OpeningHours openingHours = null;					// TODO - Do we need>??????????
	private List<Department> departments = null;
	private CarDealer dealership = null;						// TODO - Remove>?????????????????
	private DealerDAO dealerDAO = null;
	

	protected CarDealer(DealershipBuilder dealershipBuilder) {
		this.name = dealershipBuilder.name;
		this.workingDay = dealershipBuilder.workingDay;
		this.departments = dealershipBuilder.departments;
		this.dealership = dealershipBuilder.dealership;
		this.dealerDAO = dealershipBuilder.dealerDAO;	
	}
	
	public CarDealer getDealership() {
		return dealership;
	}
	
	public DealerDAO getDealerDAO() {
		return dealerDAO;
	}
	
	public void setDepartments(List<Department> departments) {
		this.departments = departments;
	}

	public String getName() {
		return name;
	}

	public DealerWorkingDay getWorkingDay() {
		return workingDay;
	}

	public List<Department> getDepartments() {
		return departments;
	}
	

	/*
	 * Builder class for the Dealership object
	 */
	public static class DealershipBuilder {
		private String name = "";
		private DealerWorkingDay workingDay = null;
		private OpeningHours openingHours = null;					// TODO - Do we need>??????????
		private List<Department> departments = null;
		private CarDealer dealership = null;						// TODO - Remove>?????????????????
		private DealerDAO dealerDAO = null;
		
		public DealershipBuilder(CarDealer dealership) {
			this.dealership = dealership;
		}
	
		public DealershipBuilder setDealerDAO(DealerDAO dao) {
			this.dealerDAO = dao;
			return this;
		}
		
		public DealershipBuilder setName(String name) {
			this.name = name;
			return this;
		}
			
		public DealershipBuilder setWorkingDay(DealerWorkingDay workingDay) {
			this.workingDay = workingDay;
			return this;
		}
		
		public DealershipBuilder setOpeningHours(OpeningHours openingHours) {
			this.openingHours = openingHours;
			return this;
		}
		
		public DealershipBuilder setDepartments(List<Department> departments) {
			this.departments = departments;
			return this;
		}
		
		public MainDealer buildMainDealer() {	
			return new MainDealer(this);
		}
		
		public FranchiseDealer buildFranchiseDealer() {	
			return new FranchiseDealer(this);
		}
	}

	/*
	 *  TODO
	 */
	public static class MainDealerBuilder extends DealershipBuilder {

		public MainDealerBuilder(MainDealer dealership, DealerWorkingDay workingDay, DealerDAO dealerDAO) {
			super(dealership);
			this.setWorkingDay(workingDay);
			this.setDealerDAO(dealerDAO);
		}
	}

	/*
	 *  TODO
	 */
	public static class FranchiseDealerBuilder extends DealershipBuilder {

		public FranchiseDealerBuilder(FranchiseDealer dealership, DealerWorkingDay workingDay, DealerDAO dealerDAO) {
			super(dealership);
			this.setWorkingDay(workingDay);
			this.setDealerDAO(dealerDAO);
		}
	}

//	@Override
//	public void startTrading() {
//		msg(); // TODO - R

		// Start the clock
//		dealershipTimer = new HeartBeat(workingDay.getLengthOfWorkingDay());
//		
//		dealershipRunner.scheduleAtFixedRate(() -> {
//			System.out.println(name + " " + dealershipTimer.countdownTheClock() + "s until home time");
//			if(dealershipTimer.timeToClose()) {
//				closeDealership();
//			}
//		}
//		, 1, 1, TimeUnit.SECONDS);
		
//	}

//	@Override
//	public void open() {
//		System.out.println(this.getName() + "'s opening time: " );
//		
//	}
//
//	@Override
//	public void close() {
//		System.out.println(this.getName() + "'s closing time: " 
//				);
//	}		
}
