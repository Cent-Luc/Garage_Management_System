import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;

class Employee {
    public int idNumber;
    public String firstName;
	public String surname;
	public char gender;
	public String  kraPin;
    public int phoneNumber;
    public String email;
}

public class EmployeeTbl {
    private final Connection conn;
    public ArrayList<Employee> employees = null;
    EmployeeTbl(Connection conn) {
	this.conn = conn;
    }
    
    // Return all the car owners in the car table
    public boolean getAllEmployees() {
	employees = new ArrayList<Employee>();
	String SQL = "SELECT * FROM employee";
	Employee employee;
	
	try(
	    Statement sttmt = conn.createStatement();
	    ResultSet rs = sttmt.executeQuery(SQL)) {
		while(rs.next()) {
		    employee = new Employee();
		    employee.idNumber = rs.getInt("id_number");
		    employee.firstName = rs.getString("first_name");
			employee.surname = rs.getString("surname");
			employee.gender = rs.getString("gender").charAt(0);
			employee.kraPin = rs.getString("kra_pin");
		    employee.phoneNumber = rs.getInt("phone_number");
		    employee.email = rs.getString("email");
		    employees.add(employee);
		}
		return true;
	    } catch(SQLException e) {
		System.out.println(e.getMessage());
		return false;
	    }
    }

    // Search for a employee and return him or her
    public Employee getEmployee(int idNumber) {
	String SQL = "SELECT * FROM employee WHERE id_number = " + idNumber;
	Employee employee;
	
	try(
	    Statement sttmt = conn.createStatement();
	    ResultSet rs = sttmt.executeQuery(SQL)) {
		if(rs.next()) { // If the record was found
		    employee = new Employee();
		    employee.idNumber = rs.getInt("id_number");
		    employee.firstName = rs.getString("first_name");
			employee.surname = rs.getString("surname");
			employee.gender = rs.getString("gender").charAt(0);
			employee.kraPin = rs.getString("kra_pin");
		    employee.phoneNumber = rs.getInt("phone_number");
		    employee.email = rs.getString("email");
		    return employee;
		} else {
		    return null;
		}
	    } catch(SQLException e) {
		System.out.println(e.getMessage());
		// TODO: When error handling properly, return null plus the error
		return null;
	    }
    }

    // Save a new employee
    public boolean insertEmployee(Employee employee) {
	String SQL = "INSERT INTO employee(id_number, first_name, surname, gender, kra_pin, phone_number, email) " +
	    " VALUES(?, ?, ?, ?, ?, ?, ?)";

	try(PreparedStatement psttmt = conn.prepareStatement(SQL)) {
	    psttmt.setInt(1, employee.idNumber);
	    psttmt.setString(2, employee.firstName);
	    psttmt.setString(3, employee.surname);
	    psttmt.setString(4, String.valueOf(employee.gender));
	    psttmt.setString(5, employee.kraPin);
	    psttmt.setInt(6, employee.phoneNumber);
	    psttmt.setString(7, employee.email);

	    psttmt.executeUpdate();
	    return true;
	} catch(SQLException e) {
	    System.out.println(e.getMessage());
	    return false;
	}
    }

    // Alter an employee's details
    public boolean updateEmployee(Employee employee) {
    String SQL = "UPDATE employee SET " +
	"first_name = ? , " +
	"surname = ?, " +
	"gender = ?, " +
	"kra_pin = ?, " +
	"phone_number = ? ," +
	"email = ? " +
	"WHERE id_number = ?";

	try(PreparedStatement psttmt = conn.prepareStatement(SQL)) {
	    psttmt.setString(1, employee.firstName);
	    psttmt.setString(2, employee.surname);
	    psttmt.setString(3, String.valueOf(employee.gender));
	    psttmt.setString(4, employee.kraPin);
	    psttmt.setInt(5, employee.phoneNumber);
		psttmt.setString(6, employee.email);
		psttmt.setInt(7, employee.idNumber);

	    psttmt.executeUpdate();
	    return true;
	} catch(SQLException e) {
	    System.out.println(e.getMessage());
	    return false;
	}
    }

    // Delete an employee
    public boolean deleteEmployee(Employee employee) {
	String SQL = "DELETE FROM employee WHERE id_number = ?";

	try(PreparedStatement pstmt = conn.prepareStatement(SQL)) {
	    pstmt.setInt(1, employee.idNumber);
	    pstmt.executeUpdate();
	    return true;
	} catch (SQLException ex) {
	    System.out.println(ex.getMessage());
	    return false;
	}
    }
}

