import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;

class Specialization {
	public int specializationId;
	public String specializationName;
	public int rate;
}

public class SpecializationsTbl {
	private final Connection conn;
	public ArrayList<Specialization> specializations = null;

	SpecializationsTbl(Connection conn) {
		this.conn = conn;
	}

	// Return all the specializations in the specializations table
	public boolean getAllSpecializations() {
		specializations = new ArrayList<Specialization>();
		String SQL = "SELECT * FROM specializations";
		Specialization specialization;

		try (Statement sttmt = conn.createStatement(); ResultSet rs = sttmt.executeQuery(SQL)) {
			while (rs.next()) {
				specialization = new Specialization();
				specialization.specializationId = rs.getInt("specialization_id");
				specialization.specializationName = rs.getString("specialization_name");
				specialization.rate = rs.getInt("rate");
				specializations.add(specialization);
			}
			return true;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	// Search for a specialization and return its details
	public Specialization getSpecialization(String specializationName) {
		String SQL = "SELECT * FROM specializations WHERE specialization_name = '" + specializationName + "'";
		Specialization specialization;

		try (Statement sttmt = conn.createStatement(); ResultSet rs = sttmt.executeQuery(SQL)) {
			if (rs.next()) { // If the record was found
				specialization = new Specialization();
				specialization.specializationId = rs.getInt("specialization_id");
				specialization.specializationName = rs.getString("specialization_name");
				specialization.rate = rs.getInt("rate");
				return specialization;
			} else {
				return null;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			// TODO: When error handling properly, return null plus the error
			return null;
		}
	}

	// Save a new specialization
	public boolean insertSpecialization(Specialization specialization) {
		String SQL = "INSERT INTO specializations(specialization_name, rate) " + " VALUES(?, ?)";

		try (PreparedStatement psttmt = conn.prepareStatement(SQL)) {
			psttmt.setString(1, specialization.specializationName.toLowerCase());
			psttmt.setInt(2, specialization.rate);

			psttmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	// Alter a specialization's details
	public boolean updateSpecialization(Specialization specialization) {
		String SQL = "UPDATE specializations SET " + "specialization_name = ? , " + "rate = ? "
				+ "WHERE specialization_id = ?";

		try (PreparedStatement psttmt = conn.prepareStatement(SQL)) {
			psttmt.setString(1, specialization.specializationName.toLowerCase());
			psttmt.setInt(2, specialization.rate);
			psttmt.setInt(3, specialization.specializationId);

			psttmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	// Delete a specialization
	public boolean deleteSpecialization(Specialization specialization) {
		String SQL = "DELETE FROM specializations WHERE specialization_id = ?";

		try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
			pstmt.setInt(1, specialization.specializationId);
			pstmt.executeUpdate();
			return true;
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}
}
