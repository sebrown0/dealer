/**
 * 
 */
package dealer_management;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import database.StoredProcedure;
import departments.accounts_department.AccountsDeptartment;
import departments.department.Department;
import departments.department.ObjectDetails;
import departments.garage_services_department.GarageServicesDeptartment;
import departments.hr_department.HRDeptartment;
import departments.it_department.ITDepartment;
import departments.order_department.OrderDepartment;
import departments.sales_department.SalesDepartment;
import departments.stock_department.StockDeptartment;
import enums.ErrorCodes;
import enums.HRDeptSP;
import tasks.task_helper.CreateEmployee;
import tasks.task_helper.CreateEmployeeHelper;
import utils.logger.Log;
import utils.logger.Loggable;

/**
 * @author Steve Brown
 *	TODO
 *	Creates a list of departments from the Stored Procedure HRDeptSP.DEPARTMENT_IDS.
 *	Departments are the only objects that can assign tasks. 
 *	So they have to be created before any other tasks can be run.
 *	Should be run at the start of each new day.
 */
public class DepartmentFactory implements Loggable {
	
	private List<Department> departmentList = new ArrayList<>();
	private DealerDAO dealerDAO;
			
	public DepartmentFactory(DealerDAO dealerDAO) {
		this.dealerDAO = dealerDAO;
	}
	
	/*
	 * Create all departments that are in the TableNames.DEPT table (except dept == none).
	 */
	public ErrorCodes createDepartments() {
		ErrorCodes errorCode = ErrorCodes.NONE;
		Log log = dealerDAO.getLog(); 
	
		dealerDAO.getDatabase().dbConnect(); // TODO - Drop DB connection when finished.

		// Use a stored procedure to get the departments.
		StoredProcedure sp = new StoredProcedure(HRDeptSP.GET_DEPARTMENTS.value(), dealerDAO.getDatabase().dbConnection(), log);
		sp.execute();

		if (sp.errorCode() == ErrorCodes.NONE) {
			ConcurrentHashMap<String, String> departments 
				= sp.getMapOfValues("dept_id", "dept_name"); 

			if (!departments.isEmpty()) {
				for (String id : departments.keySet()) {
					log.logEntry(this, "Creating " + departments.get(id) + " department");
					ObjectDetails deptDetails = createDepartmentDetails(id, departments);
					Department aDepartment = null;
					switch (id) {			
					case "1":						
						aDepartment = new HRDeptartment();
						break;

					case "2":
						aDepartment = new SalesDepartment();
						break;
						
					case "3":
						aDepartment = new ITDepartment();
						break;
						
					case "4":
						aDepartment = new AccountsDeptartment();
						break;
						
					case "5":
						aDepartment = new GarageServicesDeptartment();
						break;
					
					case "6":
						aDepartment = new StockDeptartment();
						break;
					
					case "7":
						aDepartment = new OrderDepartment();
						break;
						
					default:
						break;
					}
					
					if(aDepartment != null) 	
						departmentList.add(buildDepartment(aDepartment, deptDetails, dealerDAO));	
				}
			} else {
				errorCode = ErrorCodes.UNKNOWN_ERROR; // TODO - Error code
				log.logEntry(this, errorCode.eCode());
			}
		}

		return errorCode;
	}

	private ObjectDetails createDepartmentDetails(String id, ConcurrentHashMap<String, String> departments) {
		ObjectDetails details = new ObjectDetails();
		details.setDeptID(id);
		details.setDeptName(departments.get(id));
		return details;
	}
	
	private Department buildDepartment(Department aDepartment, ObjectDetails deptDetails, DealerDAO dealerDAO) {
		aDepartment.setDepartmentDetails(deptDetails);
		aDepartment.setDepartmentDAO(dealerDAO);
		assignDeptManager(aDepartment);
		return aDepartment;
	}

	private void assignDeptManager(Department aDepartment) {
		CreateEmployeeHelper helper = new CreateEmployee(aDepartment);
		ResultSet empRS = helper.getEmployeesFromDB(aDepartment.getDeptID(), HRDeptSP.DEPARTMENT_MANAGER); 
		if(empRS != null) 
			helper.updateTeam(empRS);		
	}
	
	public List<Department> getDepartments() {
		return departmentList;
	}
}
