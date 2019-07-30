import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;

class Tool {
    public int toolId;
    public String toolName;
    public int price;
    public int quantity;
}

public class ToolsInventoryTbl {
    private final Connection conn;
    public ArrayList<Tool> tools = null;
    ToolsInventoryTbl(Connection conn) {
	this.conn = conn;
    }
    
    // Return all the tools in the tools_inventory table
    public boolean getAllTools() {
	tools = new ArrayList<Tool>();
	String SQL = "SELECT * FROM tools_inventory";
	Tool tool;
	
	try(
	    Statement sttmt = conn.createStatement();
	    ResultSet rs = sttmt.executeQuery(SQL)) {
		while(rs.next()) {
		    tool = new Tool();
		    tool.toolId = rs.getInt("tool_id");
		    tool.toolName = rs.getString("tool_name");
		    tool.price = rs.getInt("price");
		    tool.quantity = rs.getInt("quantity");
		    tools.add(tool);
		}
		return true;
	    } catch(SQLException e) {
		System.out.println(e.getMessage());
		return false;
	    }
    }

    // Search for a tool and return its details
    public Tool getTool(int toolId) {
	String SQL = "SELECT * FROM tools_inventory WHERE tool_id = " + toolId;
	Tool tool;
	
	try(
	    Statement sttmt = conn.createStatement();
	    ResultSet rs = sttmt.executeQuery(SQL)) {
		if(rs.next()) { // If the record was found
		    tool = new Tool();
		    tool.toolId = rs.getInt("tool_id");
		    tool.toolName = rs.getString("tool_name");
		    tool.price = rs.getInt("price");
		    tool.quantity = rs.getInt("quantity");
		    return tool;
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
    public boolean insertTool(Tool tool) {
	String SQL = "INSERT INTO tools_inventory(tool_name, price, quantity) " +
	    " VALUES(?, ?, ?)";

	try(PreparedStatement psttmt = conn.prepareStatement(SQL)) {
	    psttmt.setString(1, tool.toolName);
	    psttmt.setInt(2, tool.price);
	    psttmt.setInt(3, tool.quantity);

	    psttmt.executeUpdate();
	    return true;
	} catch(SQLException e) {
	    System.out.println(e.getMessage());
	    return false;
	}
    }

    // Alter a tool's details
    public boolean updateTool(Tool tool) {
    String SQL = "UPDATE tools_inventory SET " +
	"tool_name = ? , " +
	"price = ?, " +
	"quantity = ? " +
	"WHERE tool_id = ?";

	try(PreparedStatement psttmt = conn.prepareStatement(SQL)) {
	    psttmt.setString(1, tool.toolName);
	    psttmt.setInt(2, tool.price);
	    psttmt.setInt(3, tool.quantity);
	    psttmt.setInt(4, tool.toolId);

	    psttmt.executeUpdate();
	    return true;
	} catch(SQLException e) {
	    System.out.println(e.getMessage());
	    return false;
	}
    }

    // Delete a tool
    public boolean deleteTool(Tool tool) {
	String SQL = "DELETE FROM tools_inventory WHERE tool_id = ?";

	try(PreparedStatement pstmt = conn.prepareStatement(SQL)) {
	    pstmt.setInt(1, tool.toolId);
	    pstmt.executeUpdate();
	    return true;
	} catch (SQLException ex) {
	    System.out.println(ex.getMessage());
	    return false;
	}
    }
}

