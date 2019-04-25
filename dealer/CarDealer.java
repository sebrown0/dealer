package dealer;

import java.util.List;

import dealer_management.DealerDAO;
import dealer_working_day.DealerWorkingDay;
import dealer_working_day.OpeningHours;
import departments.department.Department;
import franchise.FranchiseDealer;
import main_dealer.MainDealer;
import tasks.task_objects.OpenDealership;
import tasks.task_status.Taskable;
import tasks.task_status.TasksStatus;

/*
 *  @author Steve Brown.
 *  
 *  
 *  Represents a car dealership. 
 *  Derived objects are Main Dealer (more functionality) and Franchise (less functionality).
 *  
 *  Contains a helper class to access an instance of a dealership.
 */
public class CarDealer implements Taskable {
		
	private String name = "";
	private DealerWorkingDay workingDay = null;
	private List<Department> departments = null;
	private CarDealer dealership = null;						
	private DealerDAO dealerDAO = null;
	

	protected CarDealer(DealershipBuilder dealershipBuilder) {
		this.name = dealershipBuilder.name;
		this.workingDay = dealershipBuilder.workingDay;
		this.departments = dealershipBuilder.departments;
		this.dealership = dealershipBuilder.dealership;
		this.dealerDAO = dealershipBuilder.dealerDAO;	
	}
	
	@Override
	public void taskUpdate(TasksStatus s) {
		dealerDAO.getLog().logEntry("<<TASK UPDATE>>", "Completed Task = " + s.taskStatus() + " - Task ID = " + s.taskStatus());
	}
	
	public Department getDepartmentByName(String deptName) {
		for (Department d : departments) 
			if(d.deptName().equals(deptName))
				return d;
		return null;
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

	public static class MainDealerBuilder extends DealershipBuilder {

		public MainDealerBuilder(MainDealer dealership, DealerWorkingDay workingDay, DealerDAO dealerDAO) {
			super(dealership);
			this.setWorkingDay(workingDay);
			this.setDealerDAO(dealerDAO);
		}
	}

	public static class FranchiseDealerBuilder extends DealershipBuilder {

		public FranchiseDealerBuilder(FranchiseDealer dealership, DealerWorkingDay workingDay, DealerDAO dealerDAO) {
			super(dealership);
			this.setWorkingDay(workingDay);
			this.setDealerDAO(dealerDAO);
		}
	}
}