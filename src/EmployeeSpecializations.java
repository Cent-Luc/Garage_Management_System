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
import javafx.scene.control.ComboBox;

import java.util.ArrayList;
import java.util.Optional;

import java.sql.Connection;

public class EmployeeSpecializations {
    private TextField txtId;
    private TextField txtSpecializationId;
	ComboBox<String> cmbSpecialization;
	ComboBox<String> cmbEmployee;
	private VBox vbxRoot;
    private int currentIndex;
	private EmployeeSpecializationsTbl employeeSpecializationsTbl;
	private ArrayList<EmployeeSpecialization> employeeSpecializations;
	private SpecializationsTbl specializationsTbl;
	private ArrayList<Specialization> specializations;
	private EmployeeTbl employeeTbl;
	private ArrayList<Employee> employees;

    private boolean newRecordState;

    EmployeeSpecializations() {
	txtId = null;

	cmbSpecialization = null;

	currentIndex = 0;

	employeeSpecializationsTbl = null;
	employeeSpecializations = null;

	specializationsTbl = null;
	specializations = null;

	employeeTbl = null;
	employees = null;

	newRecordState = false;
    }

    public VBox getContent() {
	PostgresDbConn garageDb = new PostgresDbConn();
	Connection conn = garageDb.connect();

	employeeSpecializationsTbl = new EmployeeSpecializationsTbl(conn);
	employeeSpecializationsTbl.getAllEmployeeSpecializations();
	employeeSpecializations = employeeSpecializationsTbl.employeeSpecializations;
	EmployeeSpecialization employeeSpecialization = employeeSpecializations.get(0);

	employeeTbl = new EmployeeTbl(conn);
	employeeTbl.getAllEmployees();
	employees = employeeTbl.employees;

	specializationsTbl = new SpecializationsTbl(conn);
	specializationsTbl.getAllSpecializations();
	specializations = specializationsTbl.specializations;

	txtId = new TextField();
	txtId.setDisable(true);
	Label lblId = new Label("_Id:");
	lblId.setLabelFor(txtId);
	lblId.setMnemonicParsing(true);

	Label lblEmployee = new Label("_Employee:");
	cmbEmployee = new ComboBox<>();
	for(Employee emp: employees) {
		String row = String.valueOf(emp.idNumber) + " : " + emp.firstName + " " + emp.surname;
		cmbEmployee.getItems().add(row);
	}
	lblEmployee.setLabelFor(cmbEmployee);
	lblEmployee.setMnemonicParsing(true);

	Label lblSpecialization = new Label("_Specialization:");
	cmbSpecialization = new ComboBox<>();
	for(Specialization varSp: specializations) {
		cmbSpecialization.getItems().add(String.valueOf(varSp.specializationId) + ": " + varSp.specializationName);
	}
	lblSpecialization.setLabelFor(cmbSpecialization);
	lblSpecialization.setMnemonicParsing(true);

	GridPane grdFields = new GridPane();
	grdFields.setVgap(25);
	grdFields.setHgap(10);
	grdFields.setMaxWidth(Double.MAX_VALUE);
	// grdFields.setGridLinesVisible(true);
	grdFields.getStyleClass().setAll("brd");

	txtId.setText(String.valueOf(employeeSpecialization.id));
	String row = String.valueOf(employeeSpecialization.idNumber) 
				+ " : " + employeeSpecialization.firstName + " " + employeeSpecialization.surname;
	cmbEmployee.setValue(row);
	cmbSpecialization.setValue(String.valueOf(employeeSpecialization.specializationId) + ": " + employeeSpecialization.specializationName);

	// Add the controls to the Grid Pane
	grdFields.addRow(0, lblId, txtId);
	grdFields.addRow(1, lblEmployee, cmbEmployee);
	grdFields.addRow(2, lblSpecialization, cmbSpecialization);

	// Align the labels to the right
	grdFields.setHalignment(lblId, HPos.RIGHT);
	grdFields.setHalignment(lblEmployee, HPos.RIGHT);
	grdFields.setHalignment(lblSpecialization, HPos.RIGHT);

	// I want the text fields to expand to fit the left spacing
	// interestingly, doing this for the first element affects the rest
	GridPane.setHgrow(txtId, Priority.ALWAYS);
	cmbEmployee.setMaxWidth(Double.MAX_VALUE);
	cmbSpecialization.setMaxWidth(Double.MAX_VALUE);

	// Set the labels to their appropriate css classes
	lblId.getStyleClass().setAll("text-color", "lbl");
	lblEmployee.getStyleClass().setAll("text-color", "lbl");
	lblSpecialization.getStyleClass().setAll("text-color", "lbl");

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
	btnSearch.getStyleClass().setAll("btn","text-color");
	btnNew.getStyleClass().setAll("btn","text-color");
	btnSave.getStyleClass().setAll("btn","text-color");
	btnDel.getStyleClass().setAll("btn","text-color");
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
	btnLast.getStyleClass().setAll("btn","text-color");
	btnNext.getStyleClass().setAll("btn","text-color");
	btnPrev.getStyleClass().setAll("btn","text-color");
	btnFirst.getStyleClass().setAll("btn","text-color");
	hbxNav.getChildren().addAll(btnFirst, btnPrev, btnNext, btnLast);
	hbxNav.setSpacing(5);
	// Add a border all around the button group
	hbxNav.getStyleClass().setAll("brd");

	// Create a redundant HBox to be able to center the nav buttons
	// without filling the entire card
	HBox hbxNavRow = new HBox(hbxNav);
	HBox.setHgrow(hbxNavRow, Priority.NEVER);
	hbxNavRow.setAlignment(Pos.CENTER);

	vbxRoot = new VBox(10);
	vbxRoot.getChildren().addAll(hbxCenterRow, hbxNavRow);
	vbxRoot.getStylesheets().add("styles.css");
	vbxRoot.setMaxWidth(Double.MAX_VALUE);
	vbxRoot.getStyleClass().setAll("brd");
	// vbxRoot.setStyle("-fx-background-color: black;");

	return vbxRoot;
	}

    // A utility function to Confirm if the user wants to navigate while still there is a new record
    private boolean leaveNewRecordState() {
	if(newRecordState) {
	    Alert alert = new Alert(AlertType.CONFIRMATION);
	    alert.setTitle("Warning");
	    alert.setHeaderText(null);
	    alert.setContentText("New record not saved. Continue still?");
	    Optional<ButtonType> result = alert.showAndWait();
	    if(result.get() == ButtonType.OK) {
		return true;
	    } else {
		return false;
	    }
	} else {
	    return true;
	}
    }

    private void goFirst() {
	if(!leaveNewRecordState()) {
	    return;
	}  

	EmployeeSpecialization employeeSpecialization = employeeSpecializations.get(0);
	txtId.setText(String.valueOf(employeeSpecialization.id));
	String row = String.valueOf(employeeSpecialization.idNumber) 
					+ " : " + employeeSpecialization.firstName + " " + employeeSpecialization.surname;
	cmbEmployee.setValue(row);
	cmbSpecialization.setValue(String.valueOf(employeeSpecialization.specializationId) + ": " + employeeSpecialization.specializationName);
	currentIndex = 0;
	newRecordState = false;
    }



    private void goPrev() {
	if(!leaveNewRecordState()) {
	    return;
	}

	if(currentIndex == 0) {
	    // I have explicitly stated goFirst so as to cater for when a new record is
	    // inserted but not saved to get rid of the blanks
	    // TODO: Abstract this appropriately
	    EmployeeSpecialization employeeSpecialization = employeeSpecializations.get(0);
		txtId.setText(String.valueOf(employeeSpecialization.id));
		String row = String.valueOf(employeeSpecialization.idNumber) 
					+ " : " + employeeSpecialization.firstName + " " + employeeSpecialization.surname;
		cmbEmployee.setValue(row);
		cmbSpecialization.setValue(String.valueOf(employeeSpecialization.specializationId) + ": " + employeeSpecialization.specializationName);
	    currentIndex = 0;
	    newRecordState = false;
	    return;
	} else {
	    currentIndex--;
	    EmployeeSpecialization employeeSpecialization = employeeSpecializations.get(currentIndex);
		txtId.setText(String.valueOf(employeeSpecialization.id));
		String row = String.valueOf(employeeSpecialization.idNumber) 
					+ " : " + employeeSpecialization.firstName + " " + employeeSpecialization.surname;
		cmbEmployee.setValue(row);
		cmbSpecialization.setValue(String.valueOf(employeeSpecialization.specializationId) + ": " + employeeSpecialization.specializationName);
	    newRecordState = false;
	}
    }

    private void goNext() {
	if(!leaveNewRecordState()) {
	    return;
	}

	if(currentIndex == (employeeSpecializations.size() - 1)) {
	    // I have explicitly stated goLast so as to cater for when a new record is
	    // inserted but not saved to get rid of the blanks
	    // TODO: Abstract this appropriately
	    currentIndex = employeeSpecializations.size() - 1;
	    EmployeeSpecialization employeeSpecialization = employeeSpecializations.get(currentIndex);
		txtId.setText(String.valueOf(employeeSpecialization.id));
		String row = String.valueOf(employeeSpecialization.idNumber) 
					+ " : " + employeeSpecialization.firstName + " " + employeeSpecialization.surname;
		cmbEmployee.setValue(row);
		cmbSpecialization.setValue(String.valueOf(employeeSpecialization.specializationId) + ": " + employeeSpecialization.specializationName);
	    newRecordState = false;
	    return;
	} else {
	    currentIndex++;
	    EmployeeSpecialization employeeSpecialization = employeeSpecializations.get(currentIndex);
		txtId.setText(String.valueOf(employeeSpecialization.id));
		String row = String.valueOf(employeeSpecialization.idNumber) 
					+ " : " + employeeSpecialization.firstName + " " + employeeSpecialization.surname;
		cmbEmployee.setValue(row);
		cmbSpecialization.setValue(String.valueOf(employeeSpecialization.specializationId) + ": " + employeeSpecialization.specializationName);
	    newRecordState = false;
	}
    }

    private void goLast() {
	if(!leaveNewRecordState()) {
	    return;
	}

	currentIndex = employeeSpecializations.size() - 1;
	EmployeeSpecialization employeeSpecialization = employeeSpecializations.get(currentIndex);
	txtId.setText(String.valueOf(employeeSpecialization.id));
	String row = String.valueOf(employeeSpecialization.idNumber) 
					+ " : " + employeeSpecialization.firstName + " " + employeeSpecialization.surname;
	cmbEmployee.setValue(row);
	cmbSpecialization.setValue(String.valueOf(employeeSpecialization.specializationId) + ": " + employeeSpecialization.specializationName);
	newRecordState = false;
    }

    private void searchRecord() {
	if(!leaveNewRecordState()) {
	    return;
	}

	TextInputDialog dialog = new TextInputDialog();
	dialog.setTitle("Search");
	dialog.setHeaderText("Enter the ID number to search");
	dialog.setContentText("ID Number:");
	Optional<String> result = dialog.showAndWait();
	EmployeeSpecialization employeeSpecialization;
	if(result.isPresent()) {
	    employeeSpecialization = employeeSpecializationsTbl.getEmployeeSpecialization(
		    Integer.parseInt(result.get()));
	    if(employeeSpecialization != null) {
		for(int i = 0; i < employeeSpecializations.size(); i++) {
		    if(employeeSpecializations.get(i).idNumber == employeeSpecialization.idNumber) {
			currentIndex = i;
			employeeSpecialization = employeeSpecializations.get(currentIndex);
			txtId.setText(String.valueOf(employeeSpecialization.id));
			String row = String.valueOf(employeeSpecialization.idNumber) 
			+ " : " + employeeSpecialization.firstName + " " + employeeSpecialization.surname;
			cmbEmployee.setValue(row);
			cmbSpecialization.setValue(String.valueOf(employeeSpecialization.specializationId) + ": " + employeeSpecialization.specializationName);
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
	if(!leaveNewRecordState()) {
	    return;
	}

	txtId.setText("");
	cmbEmployee.setValue(null);
	cmbSpecialization.setValue(null);

	newRecordState = true;
    }

    private void saveRecord() {
	// Include some functionality like in delete to cater for new records
	// After a new record has been added, renavigate appropriately
	// TODO: I think i have implemented this. Investiigate

	EmployeeSpecialization employeeSpecialization = new EmployeeSpecialization();
	if(!newRecordState && (txtId.getText() != null || !txtId.getText().trim().isEmpty())) {
	    employeeSpecialization.id = Integer.parseInt(txtId.getText());
	} else if(cmbEmployee.getValue() == null) {
	    Alert alert = new Alert(AlertType.WARNING);
	    alert.setTitle("Warning");
	    alert.setHeaderText(null);
	    alert.setContentText("The Employee field should not be left blank");
	    alert.showAndWait();
	    return;
	} else if(cmbSpecialization.getValue() == null) {
	    Alert alert = new Alert(AlertType.WARNING);
	    alert.setTitle("Warning");
	    alert.setHeaderText(null);
	    alert.setContentText("The Specialization field should not be left blank");
	    alert.showAndWait();
	    return;
	}  

	// I am posting this here so as to avoid errors when
	// no values have been entered. 
	employeeSpecialization.idNumber = Integer.parseInt(cmbEmployee.getValue().split(":")[0].trim());
	employeeSpecialization.specializationId = Integer.parseInt(cmbSpecialization.getValue().split(":")[0].trim());

	if(newRecordState) {
	    if(employeeSpecializationsTbl.insertEmployeeSpecialization(employeeSpecialization)) {
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
	    if(employeeSpecializationsTbl.updateEmployeeSpecialization(employeeSpecialization)) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Success");
		alert.setHeaderText(null);
		alert.setContentText("The record has been updated.");
		alert.showAndWait();
	    } else {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error Encountered");
		alert.setHeaderText(null);
		alert.setContentText("An error was encountered while updating the record." +
			"Please check you input values to ensure they are valid then retry saving");
		alert.showAndWait();
		return;
	    }
	}

	// Update the local storage of the employeeSpecializations
	employeeSpecializationsTbl.getAllEmployeeSpecializations();
	employeeSpecializations = employeeSpecializationsTbl.employeeSpecializations;
	newRecordState = false;
    }

    private void deleteRecord() {
	// If the user wants to leave the new record state simply set newRecordState to false and go prev
	if(!leaveNewRecordState()) {
	    return;
	}

	// When deleting, only the id of the record is required
	// TODO: Make the delete fn in the corresponding table require only the record id
	EmployeeSpecialization employeeSpecialization = new EmployeeSpecialization();
	employeeSpecialization.id = Integer.parseInt(txtId.getText());

	// Confirm if the user wants to delete
	Alert alert = new Alert(AlertType.CONFIRMATION);
	alert.setTitle("Confirmation");
	alert.setHeaderText(null);
	alert.setContentText("Are you sure you want to delete the record?");
	Optional<ButtonType> result = alert.showAndWait();
	if(result.get() == ButtonType.OK) {
	    if(employeeSpecializationsTbl.deleteEmployeeSpecialization(employeeSpecialization)) {
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
	// Update the local storage of the employeeSpecializations
	employeeSpecializationsTbl.getAllEmployeeSpecializations();
	employeeSpecializations = employeeSpecializationsTbl.employeeSpecializations;
	newRecordState = false;
	// Navigate accordingly
	if(currentIndex > (employeeSpecializations.size() - 2)) { // If the record deleted was the last record
	    goLast();
	} else {
	    goPrev();
	}

    }
}
