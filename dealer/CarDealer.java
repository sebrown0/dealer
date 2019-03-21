package dealer;

import java.util.List;
import dealer_working_day.DealerWorkingDay;
import dealer_working_day.OpeningHours;
import franchise.FranchiseDealer;
import main_dealer.MainDealer;
import database.DatabaseDAO;
import depts.Department;

/*
 *  @author Steve Brown.
 *  
 *  Singleton object.
 *  
 *  Represents a car dealership. 
 *  Derived objects are Main Dealer (more functionality) and Franchise (less functionality).
 *  
 *  Contains a helper class to access an instance of a dealership.
 */
public class CarDealer implements Dealership{
	
	private String name = "";
	private String spark = "";
	
//	private ScheduledExecutorService dealershipRunner = Executors.newScheduledThreadPool(2);
	private DatabaseDAO database = null;
	private DealerWorkingDay workingDay = null;
	private OpeningHours openingHours = null;
	private List<Department> departments = null;
//	private HeartBeat dealershipTimer = null;
	private CarDealer dealership = null;

//	private Logger log;

	protected CarDealer(DealershipBuilder dealershipBuilder) {
		this.name = dealershipBuilder.name;
		this.database = dealershipBuilder.database;
		this.spark = dealershipBuilder.spark;
		this.workingDay = dealershipBuilder.workingDay;
		this.openingHours = dealershipBuilder.workingDay;
		this.departments = dealershipBuilder.departments;
		this.dealership = dealershipBuilder.dealership;
	}
	
	public CarDealer getDealership() {
		return dealership;
	}
	
//	private void closeDealership() {
//		System.out.println("Yipee");
//		dealershipRunner.shutdownNow();		
//	}
	
	private void msg() { // TODO - Remove
		System.out.println(this.name + " started trading");
//		System.out.println("  Database: " + database.dbName());
		System.out.println("  Spark: " + spark);
		System.out.println("  Working day: " + openingHours.getLengthOfWorkingDay() + " s");		
	}
	
	/*
	 * Getters for dealership
	 */
	public String getName() {
		return name;
	}

	public DatabaseDAO getDatabase() {
		return database;
	}

	public String getSpark() {
		return spark;
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
		private DatabaseDAO database = null;
		private String spark = "";
		
		private DealerWorkingDay workingDay = null;
		private OpeningHours openingHours = null;
		private List<Department> departments = null;
		private CarDealer dealership = null;
		
		public DealershipBuilder(CarDealer dealership) {
			this.dealership = dealership;
		}
		
		public DealershipBuilder setName(String name) {
			this.name = name;
			return this;
		}
		
		public DealershipBuilder setDatabase(DatabaseDAO databaseDAO) {
			this.database = databaseDAO;
			return this;
		}
		
		public DealershipBuilder setSpark(String spark) {
			this.spark = spark;
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

	public static class MainDealerBuilder extends DealershipBuilder {

		public MainDealerBuilder(MainDealer dealership, DealerWorkingDay workingDay) {
			super(dealership);
			this.setWorkingDay(workingDay);
			
		}
	}
	
	public static class FranchiseDealerBuilder extends DealershipBuilder {

		public FranchiseDealerBuilder(FranchiseDealer dealership, DealerWorkingDay workingDay) {
			super(dealership);
			this.setWorkingDay(workingDay);
		}
	}

	@Override
	public void startTrading() {
		msg(); // TODO - R

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
		
	}

	@Override
	public void open() {
		System.out.println(this.getName() + "'s opening time: " );
		
	}

	@Override
	public void close() {
		System.out.println(this.getName() + "'s closing time: " 
				);
	}		
}
