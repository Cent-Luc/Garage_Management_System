import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;

class CarOwner {
    public int idNumber;
    public String firstName;
    public String surname;
    public int phoneNumber;
    public String email;
}

public class CarOwnerTbl {
    private final Connection conn;
    public ArrayList<CarOwner> carOwners = null;
    CarOwnerTbl(Connection conn) {
	this.conn = conn;
    }
    
    // Return all the car owners in the car table
    public boolean getAllCarOwners() {
	carOwners = new ArrayList<CarOwner>();
	String SQL = "SELECT * FROM car_owner";
	CarOwner carOwner;
	
	try(
	    Statement sttmt = conn.createStatement();
	    ResultSet rs = sttmt.executeQuery(SQL)) {
		while(rs.next()) {
		    carOwner = new CarOwner();
		    carOwner.idNumber = rs.getInt("id_number");
		    carOwner.firstName = rs.getString("first_name");
		    carOwner.surname = rs.getString("surname");
		    carOwner.phoneNumber = rs.getInt("phone_number");
		    carOwner.email = rs.getString("email");
		    carOwners.add(carOwner);
		}
		return true;
	    } catch(SQLException e) {
		System.out.println(e.getMessage());
		return false;
	    }
    }

    // Search for a car owner and return him or her
    public CarOwner getCarOwner(int idNumber) {
	String SQL = "SELECT * FROM car_owner WHERE id_number = " + idNumber;
	CarOwner carOwner;
	
	try(
	    Statement sttmt = conn.createStatement();
	    ResultSet rs = sttmt.executeQuery(SQL)) {
		if(rs.next()) { // If the record was found
		    carOwner = new CarOwner();
		    carOwner.idNumber = rs.getInt("id_number");
		    carOwner.firstName = rs.getString("first_name");
		    carOwner.surname = rs.getString("surname");
		    carOwner.phoneNumber = rs.getInt("phone_number");
		    carOwner.email = rs.getString("email");
		    return carOwner;
		} else {
		    return null;
		}
	    } catch(SQLException e) {
		System.out.println(e.getMessage());
		// TODO: When error handling properly, return null plus the error
		return null;
	    }
    }

    // Save a new car owner
    public boolean insertCarOwner(CarOwner carOwner) {
	String SQL = "INSERT INTO car_owner(id_number, first_name, surname, phone_number, email) " +
	    " VALUES(?, ?, ?, ?, ?)";

	try(PreparedStatement psttmt = conn.prepareStatement(SQL)) {
	    psttmt.setInt(1, carOwner.idNumber);
	    psttmt.setString(2, carOwner.firstName);
	    psttmt.setString(3, carOwner.surname);
	    psttmt.setInt(4, carOwner.phoneNumber);
	    psttmt.setString(5, carOwner.email);

	    psttmt.executeUpdate();
	    return true;
	} catch(SQLException e) {
	    System.out.println(e.getMessage());
	    return false;
	}
    }

    // Alter a car owner's details
    public boolean updateCarOwner(CarOwner carOwner) {
    String SQL = "UPDATE car_owner SET " +
	"first_name = ? , " +
	"surname = ?, " +
	"phone_number = ? ," +
	"email = ? " +
	"WHERE id_number = ?";

	try(PreparedStatement psttmt = conn.prepareStatement(SQL)) {
	    psttmt.setString(1, carOwner.firstName);
	    psttmt.setString(2, carOwner.surname);
	    psttmt.setInt(3, carOwner.phoneNumber);
	    psttmt.setString(4, carOwner.email);
	    psttmt.setInt(5, carOwner.idNumber);

	    psttmt.executeUpdate();
	    return true;
	} catch(SQLException e) {
	    System.out.println(e.getMessage());
	    return false;
	}
    }

    // Delete a car owner
    public boolean deleteCarOwner(CarOwner carOwner) {
	String SQL = "DELETE FROM car_owner WHERE id_number = ?";

	try(PreparedStatement pstmt = conn.prepareStatement(SQL)) {
	    pstmt.setInt(1, carOwner.idNumber);
	    pstmt.executeUpdate();
	    return true;
	} catch (SQLException ex) {
	    System.out.println(ex.getMessage());
	    return false;
	}
    }
}

