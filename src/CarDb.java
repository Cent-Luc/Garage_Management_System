import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;

class Car {
    public String numberPlate;
    public String model;
    public String brand;
    public String yearOfManufacture;
    public String ownerIdNumber;
}

public class CarDb {
    private final Connection conn;
    public ArrayList<Car> cars = null;
    CarDb(Connection conn) {
	this.conn = conn;
    }
    
    // Return all the cars in the car table
    public boolean getAllCars() {
	cars = new ArrayList<Car>();
	String SQL = "SELECT * FROM car";
	Car car;
	
	try(
	    Statement sttmt = conn.createStatement();
	    ResultSet rs = sttmt.executeQuery(SQL)) {
		while(rs.next()) {
		    car = new Car();
		    car.numberPlate = rs.getString("number_plate");
		    car.model = rs.getString("model");
		    car.brand = rs.getString("brand");
		    car.yearOfManufacture = rs.getString("year_of_manufacture");
		    car.ownerIdNumber = rs.getString("owner_id_number");
		    cars.add(car);
		}
		return true;
	    } catch(SQLException e) {
		System.out.println(e.getMessage());
		return false;
	    }
    }

    // Search for a car and return it
    public Car getCar(String numberPlate) {
	String SQL = "SELECT * FROM car WHERE number_plate = '" + numberPlate.toUpperCase() + "'";
	Car car;
	
	try(
	    Statement sttmt = conn.createStatement();
	    ResultSet rs = sttmt.executeQuery(SQL)) {
		if(rs.next()) { // If the record was found
		    car = new Car();
		    car.numberPlate = rs.getString("number_plate");
		    car.model = rs.getString("model");
		    car.brand = rs.getString("brand");
		    car.yearOfManufacture = rs.getString("year_of_manufacture");
		    car.ownerIdNumber = rs.getString("owner_id_number");
		    return car;
		} else {
		    return null;
		}
	    } catch(SQLException e) {
		System.out.println(e.getMessage());
		// TODO: When error handling properly, return null plus the error
		return null;
	Employees    }
    }

    // Save a new car
    public boolean insertCar(Car car) {
	String SQL = "INSERT INTO car(number_plate, model, brand, year_of_manufacture, owner_id_number) " +
	    " VALUES(?, ?, ?, ?, ?)";

	try(PreparedStatement psttmt = conn.prepareStatement(SQL)) {
	    psttmt.setString(1, car.numberPlate);
	    psttmt.setString(2, car.model);
	    psttmt.setString(3, car.brand);
	    psttmt.setInt(4, Integer.parseInt(car.yearOfManufacture));
	    psttmt.setInt(5, Integer.parseInt(car.ownerIdNumber));

	    psttmt.executeUpdate();
	    return true;
	} catch(SQLException e) {
	    System.out.println(e.getMessage());
	    return false;
	}
    }

    // Alter a car details
    public boolean updateCar(Car car) {
    String SQL = "UPDATE car SET " +
	"model = ? , " +
	"brand = ? , " +
	"year_of_manufacture = ?, " +
	"owner_id_number = ? " +
	"WHERE number_plate = ?";

	try(PreparedStatement psttmt = conn.prepareStatement(SQL)) {
	    psttmt.setString(1, car.model);
	    psttmt.setString(2, car.brand);
	    psttmt.setInt(3, Integer.parseInt(car.yearOfManufacture));
	    psttmt.setInt(4, Integer.parseInt(car.ownerIdNumber));
	    psttmt.setString(5, car.numberPlate);

	    psttmt.executeUpdate();
	    return true;
	} catch(SQLException e) {
	    System.out.println(e.getMessage());
	    return false;
	}
    }

    // Delete a car
    public boolean deleteCar(Car car) {
	String SQL = "DELETE FROM car WHERE number_plate = ?";

	try(PreparedStatement pstmt = conn.prepareStatement(SQL)) {
	    pstmt.setString(1, car.numberPlate);
	    pstmt.executeUpdate();
	    return true;
	} catch (SQLException ex) {
	    System.out.println(ex.getMessage());
	    return false;
	}
    }
}

