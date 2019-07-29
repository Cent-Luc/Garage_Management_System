import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;

class Spare {
    public int spareId;
    public String spareName;
    public int price;
    public int quantity;
}

public class SparesInventoryTbl {
    private final Connection conn;
    public ArrayList<Spare> spares = null;
    SparesInventoryTbl(Connection conn) {
	this.conn = conn;
    }
    
    // Return all the spares in the spares_inventory table
    public boolean getAllSpares() {
	spares = new ArrayList<Spare>();
	String SQL = "SELECT * FROM spares_inventory";
	Spare spare;
	
	try(
	    Statement sttmt = conn.createStatement();
	    ResultSet rs = sttmt.executeQuery(SQL)) {
		while(rs.next()) {
		    spare = new Spare();
		    spare.spareId = rs.getInt("spare_id");
		    spare.spareName = rs.getString("spare_name");
		    spare.price = rs.getInt("price");
		    spare.quantity = rs.getInt("quantity");
		    spares.add(spare);
		}
		return true;
	    } catch(SQLException e) {
		System.out.println(e.getMessage());
		return false;
	    }
    }

    // Search for a spare and return its details
    public Spare getSpare(int spareId) {
	String SQL = "SELECT * FROM spares_inventory WHERE spare_id = " + spareId;
	Spare spare;
	
	try(
	    Statement sttmt = conn.createStatement();
	    ResultSet rs = sttmt.executeQuery(SQL)) {
		if(rs.next()) { // If the record was found
		    spare = new Spare();
		    spare.spareId = rs.getInt("spare_id");
		    spare.spareName = rs.getString("spare_name");
		    spare.price = rs.getInt("price");
		    spare.quantity = rs.getInt("quantity");
		    return spare;
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
    public boolean insertSpare(Spare spare) {
	String SQL = "INSERT INTO spares_inventory(spare_name, price, quantity) " +
	    " VALUES(?, ?, ?)";

	try(PreparedStatement psttmt = conn.prepareStatement(SQL)) {
	    psttmt.setString(1, spare.spareName);
	    psttmt.setInt(2, spare.price);
	    psttmt.setInt(3, spare.quantity);

	    psttmt.executeUpdate();
	    return true;
	} catch(SQLException e) {
	    System.out.println(e.getMessage());
	    return false;
	}
    }

    // Alter a spare's details
    public boolean updateSpare(Spare spare) {
    String SQL = "UPDATE spares_inventory SET " +
	"spare_name = ? , " +
	"price = ?, " +
	"quantity = ? " +
	"WHERE spare_id = ?";

	try(PreparedStatement psttmt = conn.prepareStatement(SQL)) {
	    psttmt.setString(1, spare.spareName);
	    psttmt.setInt(2, spare.price);
	    psttmt.setInt(3, spare.quantity);
	    psttmt.setInt(4, spare.spareId);

	    psttmt.executeUpdate();
	    return true;
	} catch(SQLException e) {
	    System.out.println(e.getMessage());
	    return false;
	}
    }

    // Delete a spare
    public boolean deleteSpare(Spare spare) {
	String SQL = "DELETE FROM spares_inventory WHERE spare_id = ?";

	try(PreparedStatement pstmt = conn.prepareStatement(SQL)) {
	    pstmt.setInt(1, spare.spareId);
	    pstmt.executeUpdate();
	    return true;
	} catch (SQLException ex) {
	    System.out.println(ex.getMessage());
	    return false;
	}
    }
}

