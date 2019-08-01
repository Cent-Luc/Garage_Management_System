import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;

class EmployeeSpecialization {
    public int id;
    public int idNumber;
    public String firstName;
    public String surname;
    public int specializationId;
    public String specializationName;
}

public class EmployeeSpecializationsTbl {
    private final Connection conn;
    public ArrayList<EmployeeSpecialization> employeeSpecializations = null;

    EmployeeSpecializationsTbl(Connection conn) {
	this.conn = conn;
    }

    // Return all the Employee Specializations in the table
    public boolean getAllEmployeeSpecializations() {
	employeeSpecializations = new ArrayList<EmployeeSpecialization>();
	String SQL = "SELECT employee_specializations.id, "
	    + "employee.id_number, employee.first_name, employee.surname, "
	    + "specializations.specialization_id, specializations.specialization_name "
	    + "FROM employee_specializations "
	    + "INNER JOIN employee ON employee.id_number=employee_specializations.employee_id_number "
	    + "INNER JOIN specializations "
	    + "ON employee_specializations.specialization_id=specializations.specialization_id;";
	EmployeeSpecialization employeeSpecialization;

	try (Statement sttmt = conn.createStatement(); ResultSet rs = sttmt.executeQuery(SQL)) {
	    while (rs.next()) {
		employeeSpecialization = new EmployeeSpecialization();
		employeeSpecialization.id = rs.getInt("id");
		employeeSpecialization.idNumber = rs.getInt("id_number");
		employeeSpecialization.firstName = rs.getString("first_name");
		employeeSpecialization.surname = rs.getString("surname");
		employeeSpecialization.specializationId = rs.getInt("specialization_id");
		employeeSpecialization.specializationName = rs.getString("specialization_name");
		employeeSpecializations.add(employeeSpecialization);
	    }
	    return true;
	} catch (SQLException e) {
	    System.out.println(e.getMessage());
	    return false;
	}
    }

    // Search for a employeeSpecialization and return him/her
    public EmployeeSpecialization getEmployeeSpecialization(int idNumber) {
	String SQL = "SELECT employee_specializations.id, "
	    + "employee.id_number, employee.first_name, employee.surname, "
	    + "specializations.specialization_id, specializations.specialization_name "
	    + "FROM employee_specializations"
	    + "INNER JOIN employee ON employee.id_number=employee_specializations.employee_id_number"
	    + "INNER JOIN specializations ON "
	    + "employee_specializations.specialization_id=specializations.specialization_id"
	    + "WHERE employee_specializations.employee_id_number = " + idNumber;
	EmployeeSpecialization employeeSpecialization;

	try (Statement sttmt = conn.createStatement(); ResultSet rs = sttmt.executeQuery(SQL)) {
	    if (rs.next()) { // If the record was found
		employeeSpecialization = new EmployeeSpecialization();
		employeeSpecialization.id = rs.getInt("employee_specializations.id");
		employeeSpecialization.idNumber = rs.getInt("employee.id_number");
		employeeSpecialization.firstName = rs.getString("employee.first_name");
		employeeSpecialization.surname = rs.getString("employee.surname");
		employeeSpecialization.specializationId = rs.getInt("specializations.specializations_id");
		employeeSpecialization.specializationName = rs.getString("specializations.specializations_name");
		employeeSpecializations.add(employeeSpecialization);
		return employeeSpecialization;
	    } else {
		return null;
	    }
	} catch (SQLException e) {
	    System.out.println(e.getMessage());
	    // TODO: When error handling properly, return null plus the error
	    return null;
	}
    }

    // Save a new employeeSpecialization
    public boolean insertEmployeeSpecialization(EmployeeSpecialization employeeSpecialization) {
	String SQL = "INSERT INTO employee_specializations(employee_id_number, specialization_id) "
	    + " VALUES(?, ?)";

	try (PreparedStatement psttmt = conn.prepareStatement(SQL)) {
	    psttmt.setInt(1, employeeSpecialization.idNumber);
	    psttmt.setInt(2, employeeSpecialization.specializationId);

	    psttmt.executeUpdate();
	    return true;
	} catch (SQLException e) {
	    System.out.println(e.getMessage());
	    return false;
	}
    }

    // Alter an employeeSpecialization's details
    public boolean updateEmployeeSpecialization(EmployeeSpecialization employeeSpecialization) {
	String SQL = "UPDATE employee_specializations SET " 
	    + "employee_id_number = ? , " 
	    + "specialization_id = ?, "
	    + "WHERE id = ?";

	try (PreparedStatement psttmt = conn.prepareStatement(SQL)) {
	    psttmt.setInt(1, employeeSpecialization.idNumber);
	    psttmt.setInt(2, employeeSpecialization.specializationId);
	    psttmt.setInt(3, employeeSpecialization.id);

	    psttmt.executeUpdate();
	    return true;
	} catch (SQLException e) {
	    System.out.println(e.getMessage());
	    return false;
	}
    }

    // Delete an employeeSpecialization
    public boolean deleteEmployeeSpecialization(EmployeeSpecialization employeeSpecialization) {
	String SQL = "DELETE FROM employeeSpecialization WHERE id = ?";

	try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
	    pstmt.setInt(1, employeeSpecialization.id);
	    pstmt.executeUpdate();
	    return true;
	} catch (SQLException ex) {
	    System.out.println(ex.getMessage());
	    return false;
	}
    }
}
