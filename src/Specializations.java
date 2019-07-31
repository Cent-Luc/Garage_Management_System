import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

import javafx.geometry.Pos;
import javafx.geometry.HPos;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

import java.util.ArrayList;
import java.util.Optional;

public class Specializations {
	private TextField txtSpecializationId;
	private TextField txtSpecializationName;
	private TextField txtRate;
	private int currentIndex;
	private SpecializationsTbl specializationsTbl;
	private ArrayList<Specialization> specializations;

	private boolean newRecordState;

	Specializations() {
		txtSpecializationId = null;
		txtSpecializationName = null;
		txtRate = null;
		currentIndex = 0;
		specializationsTbl = null;
		specializations = null;
		newRecordState = false;
	}

	public VBox getContent() {
		PostgresDbConn garageDb = new PostgresDbConn();
		specializationsTbl = new SpecializationsTbl(garageDb.connect());
		specializationsTbl.getAllSpecializations();
		specializations = specializationsTbl.specializations;
		Specialization specialization = specializations.get(0);

		txtSpecializationId = new TextField();
		txtSpecializationId.setDisable(true);
		Label lblSpecializationId = new Label("Specialization _Id:");
		lblSpecializationId.setLabelFor(txtSpecializationId);
		lblSpecializationId.setMnemonicParsing(true);

		txtSpecializationName = new TextField();
		Label lblSpecializationName = new Label("_Specialization Name:");
		lblSpecializationName.setLabelFor(txtSpecializationName);
		lblSpecializationName.setMnemonicParsing(true);

		txtRate = new TextField();
		Label lblRate = new Label("_Rate:");
		lblRate.setLabelFor(txtRate);
		lblRate.setMnemonicParsing(true);

		GridPane grdFields = new GridPane();
		grdFields.setVgap(25);
		grdFields.setHgap(10);
		grdFields.setMaxWidth(Double.MAX_VALUE);
		// grdFields.setGridLinesVisible(true);
		grdFields.getStyleClass().setAll("brd");

		txtSpecializationId.setText(String.valueOf(specialization.specializationId));
		txtSpecializationName.setText(specialization.specializationName);
		txtRate.setText(String.valueOf(specialization.rate));

		// Add the controls to the Grid Pane
		grdFields.addRow(0, lblSpecializationId, txtSpecializationId);
		grdFields.addRow(1, lblSpecializationName, txtSpecializationName);
		grdFields.addRow(2, lblRate, txtRate);

		// Align the labels to the right
		grdFields.setHalignment(lblSpecializationId, HPos.RIGHT);
		grdFields.setHalignment(lblSpecializationName, HPos.RIGHT);
		grdFields.setHalignment(lblRate, HPos.RIGHT);

		// I want the text fields to expand to fit the left spacing
		// interestingly, doing this for the first element affects the rest
		GridPane.setHgrow(txtSpecializationId, Priority.ALWAYS);

		// Set the labels to their appropriate css classes
		lblSpecializationId.getStyleClass().setAll("text-color", "lbl");
		lblSpecializationName.getStyleClass().setAll("text-color", "lbl");
		lblRate.getStyleClass().setAll("text-color", "lbl");

		// The utility buttons
		VBox vbxUtility = new VBox(5);
		Button btnSearch = new Button("Search");
		Button btnNew = new Button("New");
		Button btnSave = new Button("Save");
		Button btnDel = new Button("Delete");
		btnSearch.setOnAction(e -> searchRecord());
		btnNew.setOnAction(e -> newRecord());
		btnSave.setOnAction(e -> saveRecord());
		btnDel.setOnAction(e -> deleteRecord());
		btnSearch.getStyleClass().setAll("btn", "text-color");
		btnNew.getStyleClass().setAll("btn", "text-color");
		btnSave.getStyleClass().setAll("btn", "text-color");
		btnDel.getStyleClass().setAll("btn", "text-color");
		// set their max widths to max value so they can grow horizontally
		btnSearch.setMaxWidth(Double.MAX_VALUE);
		btnNew.setMaxWidth(Double.MAX_VALUE);
		btnSave.setMaxWidth(Double.MAX_VALUE);
		btnDel.setMaxWidth(Double.MAX_VALUE);
		vbxUtility.getChildren().addAll(btnSearch, btnNew, btnSave, btnDel);
		vbxUtility.getStyleClass().setAll("brd");
		// Create a redundant VBox to style the border perfectly
		VBox vbxUtilityCol = new VBox(vbxUtility);
		VBox.setVgrow(vbxUtility, Priority.NEVER);

		// Add the grid pane and buttons to a single hbox
		HBox hbxCenterRow = new HBox(grdFields, vbxUtilityCol);
		hbxCenterRow.setSpacing(10);
		// Make the gridpane expand and fill up remaining spaces
		HBox.setHgrow(grdFields, Priority.ALWAYS);

		HBox hbxNav = new HBox();
		Button btnFirst = new Button("<<");
		Button btnPrev = new Button("<");
		Button btnNext = new Button(">");
		Button btnLast = new Button(">>");
		btnFirst.setOnAction(e -> goFirst());
		btnPrev.setOnAction(e -> goPrev());
		btnNext.setOnAction(e -> goNext());
		btnLast.setOnAction(e -> goLast());
		btnLast.getStyleClass().setAll("btn", "text-color");
		btnNext.getStyleClass().setAll("btn", "text-color");
		btnPrev.getStyleClass().setAll("btn", "text-color");
		btnFirst.getStyleClass().setAll("btn", "text-color");
		hbxNav.getChildren().addAll(btnFirst, btnPrev, btnNext, btnLast);
		hbxNav.setSpacing(5);
		// Add a border all around the button group
		hbxNav.getStyleClass().setAll("brd");

		// Create a redundant HBox to be able to center the nav buttons
		// without filling the entire card
		HBox hbxNavRow = new HBox(hbxNav);
		HBox.setHgrow(hbxNavRow, Priority.NEVER);
		hbxNavRow.setAlignment(Pos.CENTER);

		VBox vbxRoot = new VBox(10);
		vbxRoot.getChildren().addAll(hbxCenterRow, hbxNavRow);
		vbxRoot.getStylesheets().add("styles.css");
		vbxRoot.setMaxWidth(Double.MAX_VALUE);
		vbxRoot.getStyleClass().setAll("brd");
		// vbxRoot.setStyle("-fx-background-color: black;");

		return vbxRoot;
	}

	// A utility function to Confirm if the user wants to navigate while still there
	// is a new record
	private boolean leaveNewRecordState() {
		if (newRecordState) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Warning");
			alert.setHeaderText(null);
			alert.setContentText("New record not saved. Continue still?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	private void goFirst() {
		if (!leaveNewRecordState()) {
			return;
		}

		Specialization specialization = specializations.get(0);
		txtSpecializationId.setText(String.valueOf(specialization.specializationId));
		txtSpecializationName.setText(specialization.specializationName);
		txtRate.setText(String.valueOf(specialization.rate));
		currentIndex = 0;
		newRecordState = false;
	}

	private void goPrev() {
		if (!leaveNewRecordState()) {
			return;
		}

		if (currentIndex == 0) {
			// I have explicitly stated goFirst so as to cater for when a new record is
			// inserted but not saved to get rid of the blanks
			// TODO: Abstract this appropriately
			Specialization specialization = specializations.get(0);
			txtSpecializationId.setText(String.valueOf(specialization.specializationId));
			txtSpecializationName.setText(specialization.specializationName);
			txtRate.setText(String.valueOf(specialization.rate));
			currentIndex = 0;
			newRecordState = false;
			return;
		} else {
			currentIndex--;
			Specialization specialization = specializations.get(currentIndex);
			txtSpecializationId.setText(String.valueOf(specialization.specializationId));
			txtSpecializationName.setText(specialization.specializationName);
			txtRate.setText(String.valueOf(specialization.rate));
			newRecordState = false;
		}
	}

	private void goNext() {
		if (!leaveNewRecordState()) {
			return;
		}

		if (currentIndex == (specializations.size() - 1)) {
			// I have explicitly stated goLast so as to cater for when a new record is
			// inserted but not saved to get rid of the blanks
			// TODO: Abstract this appropriately
			currentIndex = specializations.size() - 1;
			Specialization specialization = specializations.get(currentIndex);
			txtSpecializationId.setText(String.valueOf(specialization.specializationId));
			txtSpecializationName.setText(specialization.specializationName);
			txtRate.setText(String.valueOf(specialization.rate));

			newRecordState = false;
			return;
		} else {
			currentIndex++;
			Specialization specialization = specializations.get(currentIndex);
			txtSpecializationId.setText(String.valueOf(specialization.specializationId));
			txtSpecializationName.setText(specialization.specializationName);
			txtRate.setText(String.valueOf(specialization.rate));

			newRecordState = false;
		}
	}

	private void goLast() {
		if (!leaveNewRecordState()) {
			return;
		}

		currentIndex = specializations.size() - 1;
		Specialization specialization = specializations.get(currentIndex);
		txtSpecializationId.setText(String.valueOf(specialization.specializationId));
		txtSpecializationName.setText(specialization.specializationName);
		txtRate.setText(String.valueOf(specialization.rate));
		newRecordState = false;
	}

	private void searchRecord() {
		if (!leaveNewRecordState()) {
			return;
		}

		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Search");
		dialog.setHeaderText("Enter the Specialization Name to search");
		dialog.setContentText("Specialization Name:");
		Optional<String> result = dialog.showAndWait();
		Specialization specialization;
		if (result.isPresent()) {
			specialization = specializationsTbl.getSpecialization(result.get().trim());
			if (specialization != null) {
				for (int i = 0; i < specializations.size(); i++) {
					if (specializations.get(i).specializationId == specialization.specializationId) {
						currentIndex = i;
						specialization = specializations.get(currentIndex);
						txtSpecializationId.setText(String.valueOf(specialization.specializationId));
						txtSpecializationName.setText(specialization.specializationName);
						txtRate.setText(String.valueOf(specialization.rate));

						newRecordState = false;
					}
				}
			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Failure");
				alert.setHeaderText(null);
				alert.setContentText("Record not found");
				alert.showAndWait();
			}
		}
	}

	private void newRecord() {
		if (!leaveNewRecordState()) {
			return;
		}

		txtSpecializationId.setText("");
		txtSpecializationName.setText("");
		txtRate.setText("");

		newRecordState = true;
	}

	private void saveRecord() {
		// Include some functionality like in delete to cater for new records
		// After a new record has been added, renavigate appropriately
		// TODO: I think i have implemented this. Investiigate

		Specialization specialization = new Specialization();
		specialization.specializationName = txtSpecializationName.getText();
		if (!newRecordState
				&& (txtSpecializationId.getText() != null || !txtSpecializationId.getText().trim().isEmpty())) {
			specialization.specializationId = Integer.parseInt(txtSpecializationId.getText());
		} else if (specialization.specializationName == null || specialization.specializationName.trim().isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning");
			alert.setHeaderText(null);
			alert.setContentText("The Specialization Name field should not be left blank");
			alert.showAndWait();
			return;
		} else if (txtRate.getText() == null || txtRate.getText().trim().isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning");
			alert.setHeaderText(null);
			alert.setContentText("The Rate field should not be left blank");
			alert.showAndWait();
			return;
		}

		// I am posting this here so as to avoid errors when
		// no values have been entered.
		specialization.rate = Integer.parseInt(txtRate.getText());

		if (newRecordState) {
			if (specializationsTbl.insertSpecialization(specialization)) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Success");
				alert.setHeaderText(null);
				alert.setContentText("The new record has been saved.");
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Encountered");
				alert.setHeaderText("An error was encountered while saving the new record.");
				alert.setContentText("Please check you input values to ensure they are valid then retry saving");
				alert.showAndWait();
				return;
			}
		} else {
			if (specializationsTbl.updateSpecialization(specialization)) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Success");
				alert.setHeaderText(null);
				alert.setContentText("The record has been updated.");
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Encountered");
				alert.setHeaderText(null);
				alert.setContentText("An error was encountered while updating the record."
						+ "Please check you input values to ensure they are valid then retry saving");
				alert.showAndWait();
				return;
			}
		}

		// Update the local storage of the specializations
		specializationsTbl.getAllSpecializations();
		specializations = specializationsTbl.specializations;
		newRecordState = false;
	}

	private void deleteRecord() {
		// If the user wants to leave the new record state simply set newRecordState to
		// false and go prev
		if (!leaveNewRecordState()) {
			return;
		}

		Specialization specialization = new Specialization();
		specialization.specializationId = Integer.parseInt(txtSpecializationId.getText());
		specialization.specializationName = txtSpecializationName.getText();
		specialization.rate = Integer.parseInt(txtRate.getText());

		// Confirm if the user wants to delete
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to delete the record?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			if (specializationsTbl.deleteSpecialization(specialization)) {
				alert.setAlertType(AlertType.INFORMATION);
				alert.setTitle("Success");
				alert.setHeaderText(null);
				alert.setContentText("The record has been deleted.");
				alert.showAndWait();
			} else {
				alert.setAlertType(AlertType.ERROR);
				alert.setTitle("Error Encountered");
				alert.setHeaderText(null);
				alert.setContentText("An error was encountered while deleting the record.");
				alert.showAndWait();
				return;
			}
		} else {
			alert.setAlertType(AlertType.INFORMATION);
			alert.setTitle("Information");
			alert.setHeaderText(null);
			alert.setContentText("The operation has been cancelled.");
			alert.showAndWait();
			return;
		}

		// If control reaches here, the record has been deleted
		// Update the local storage of the specializations
		specializationsTbl.getAllSpecializations();
		specializations = specializationsTbl.specializations;
		newRecordState = false;
		// Navigate accordingly
		if (currentIndex > (specializations.size() - 2)) { // If the record deleted was the last record
			goLast();
		} else {
			goPrev();
		}

	}
}
