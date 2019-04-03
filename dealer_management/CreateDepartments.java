/**
 * 
 */
package dealer_management;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import database.StoredProcedure;
import departments.department.Department;
import departments.hr_department.HRDept;
import departments.order_department.OrderDepartment;
import departments.sales_department.SalesDepartment;
import departments.stock_department.StockDept;
import enums.ErrorCodes;
import enums.HRDeptSP;
import utils.Log;

/**
 * @author Steve Brown
 *	TODO
 *	Creates a list of departments from the Stored Procedure HRDeptSP.DEPARTMENT_IDS.
 *	Departments are the only objects that can assign tasks. 
 *	So they have to be created before any other tasks can be run.
 *	Should be run at the start of each new day.
 */
public class CreateDepartments {
	
	private final String objId;
	private List<Department> departmentList = new ArrayList<>(); // TODO - Number of depts
	private DealerDAO dealerDAO;
	
	
	public CreateDepartments(DealerDAO dealerDAO) {
		this.objId = "<" + "dept.deptName()" + ">" + " <" + this.getClass().getSimpleName() + ">";
		this.dealerDAO = dealerDAO;
	}
	
	/*
	 * Create all departments that are in the TableNames.DEPT table (except dept == none).
	 */
	public ErrorCodes createDepartments() {
		ErrorCodes errorCode = ErrorCodes.NONE;
		Log log = dealerDAO.getLog(); 
	
		log.logEntry(objId, "Creating Departments");

		dealerDAO.getDatabase().dbConnect(); // TODO - Drop DB connection when finished.

		
		// Use a stored procedure to get the departments.
		StoredProcedure sp = new StoredProcedure(HRDeptSP.GET_DEPARTMENTS.value(), dealerDAO.getDatabase().dbConnection(), log);
		sp.execute();

		if (sp.errorCode() == ErrorCodes.NONE) {

			ConcurrentHashMap<String, String> departments 
				= sp.getMapOfValues("dept_id", "dept_name"); // TODO - Use enum or map fields?

			if (!departments.isEmpty()) {
				for (String id : departments.keySet()) {
					log.logEntry(objId, "Creating " + departments.get(id) + " department");
					
					Department aDepartment = null;
					switch (id) {			// TODO - Use enum for case statements
					case "1":						
						HRDept hr = new HRDept(id, departments.get(id), dealerDAO);
						aDepartment = hr;
						break;

					case "2":
						SalesDepartment sales = new SalesDepartment(id, departments.get(id), dealerDAO);
						aDepartment = sales;
						break;
					
					case "6":
						StockDept stock = new StockDept(id, departments.get(id), dealerDAO);
						aDepartment = stock;
						break;
					
					case "7":
						OrderDepartment order = new OrderDepartment(id, departments.get(id), dealerDAO);
						aDepartment = order;
						break;
						
					default:
						break;
					}
					
					if(aDepartment != null) {	
						departmentList.add(aDepartment);	// Add the department to the list of departments.
					}
				}
			} else {
				errorCode = ErrorCodes.UNKNOWN_ERROR; // TODO - Error code
				log.logEntry(objId, errorCode.eCode());
			}
		}

		return errorCode;
	}

	public List<Department> getDepartments() {
//		public BlockingQueue<Department> getDepartments() {
		// Will have to cast the receiving object to TaskCreateDepartments if instantiated as TaskRunner.
		return departmentList;
	}
}
