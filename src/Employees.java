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

public class Employees {
    private TextField txtIdNumber;
    private TextField txtFirstName;
    private TextField txtSurname;
    private TextField txtGender;
    private TextField txtKraPin;
    private TextField txtPhoneNumber;
    private TextField txtEmail;
    private int currentIndex;
    private EmployeeTbl employeeTbl;
    private ArrayList<Employee> employees;

    private boolean newRecordState;

    Employees() {
	txtIdNumber = null;
	txtFirstName = null;
	txtSurname = null;
	txtGender = null;
	txtKraPin = null;
	txtPhoneNumber = null;
	txtEmail = null;
	currentIndex = 0;
	employeeTbl = null;
	employees = null;
	newRecordState = false;
    }

    public VBox getContent() {
	PostgresDbConn garageDb = new PostgresDbConn();
	employeeTbl = new EmployeeTbl(garageDb.connect());
	employeeTbl.getAllEmployees();
	employees = employeeTbl.employees;
	Employee employee = employees.get(0);

	txtIdNumber = new TextField();
	Label lblIdNumber = new Label("_Id Number:");
	lblIdNumber.setLabelFor(txtIdNumber);
	lblIdNumber.setMnemonicParsing(true);

	txtFirstName = new TextField();
	Label lblFirstName = new Label("_First Name:");
	lblFirstName.setLabelFor(txtFirstName);
	lblFirstName.setMnemonicParsing(true);

	txtSurname = new TextField();
	Label lblSurname = new Label("_Surname:");
	lblSurname.setLabelFor(txtSurname);
	lblSurname.setMnemonicParsing(true);

	txtGender = new TextField();
	Label lblGender = new Label("_Gender:");
	lblGender.setLabelFor(txtGender);
	lblGender.setMnemonicParsing(true);
	
	txtKraPin = new TextField();
	Label lblKraPin = new Label("_Kra Pin:");
	lblSurname.setLabelFor(txtKraPin);
	lblKraPin.setMnemonicParsing(true);

	txtPhoneNumber = new TextField();
	Label lblPhoneNumber = new Label("_Phone Number:");
	lblPhoneNumber.setLabelFor(txtPhoneNumber);
	lblPhoneNumber.setMnemonicParsing(true);

	txtEmail = new TextField();
	Label lblEmail = new Label("_Email:");
	lblEmail.setLabelFor(txtEmail);
	lblEmail.setMnemonicParsing(true);

	GridPane grdFields = new GridPane();
	grdFields.setVgap(25);
	grdFields.setHgap(10);
	grdFields.setMaxWidth(Double.MAX_VALUE);
	// grdFields.setGridLinesVisible(true);
	grdFields.getStyleClass().setAll("brd");

	txtIdNumber.setText(String.valueOf(employee.idNumber));
	txtFirstName.setText(employee.firstName);
	txtSurname.setText(employee.surname);
	txtGender.setText(String.valueOf(employee.gender));
	txtKraPin.setText(employee.kraPin);
	txtPhoneNumber.setText(String.valueOf(employee.phoneNumber));
	txtEmail.setText(employee.email);

	// Add the controls to the Grid Pane
	grdFields.addRow(0, lblIdNumber, txtIdNumber);
	grdFields.addRow(1, lblFirstName, txtFirstName);
	grdFields.addRow(2, lblSurname, txtSurname);
	grdFields.addRow(3, lblGender, txtGender);
	grdFields.addRow(4, lblKraPin, txtKraPin);
	grdFields.addRow(5, lblPhoneNumber, txtPhoneNumber);
	grdFields.addRow(6, lblEmail, txtEmail);

	// Align the labels to the right
	grdFields.setHalignment(lblIdNumber, HPos.RIGHT);
	grdFields.setHalignment(lblFirstName, HPos.RIGHT);
	grdFields.setHalignment(lblSurname, HPos.RIGHT);
	grdFields.setHalignment(lblGender, HPos.RIGHT);
	grdFields.setHalignment(lblKraPin, HPos.RIGHT);
	grdFields.setHalignment(lblPhoneNumber, HPos.RIGHT);
	grdFields.setHalignment(lblEmail, HPos.RIGHT);

	// I want the text fields to expand to fit the left spacing
	// interestingly, doing this for the first element affects the rest
	GridPane.setHgrow(txtIdNumber, Priority.ALWAYS);

	// Set the labels to their appropriate css classes
	lblIdNumber.getStyleClass().setAll("text-color", "lbl");
	lblFirstName.getStyleClass().setAll("text-color", "lbl");
	lblSurname.getStyleClass().setAll("text-color", "lbl");
	lblGender.getStyleClass().setAll("text-color", "lbl");
	lblKraPin.getStyleClass().setAll("text-color", "lbl");
	lblPhoneNumber.getStyleClass().setAll("text-color", "lbl");
	lblEmail.getStyleClass().setAll("text-color", "lbl");

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
	
	VBox vbxRoot = new VBox(10);
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

	Employee employee = employees.get(0);
	txtIdNumber.setText(String.valueOf(employee.idNumber));
	txtFirstName.setText(employee.firstName);
	txtSurname.setText(employee.surname);
	txtGender.setText(String.valueOf(employee.gender));
	txtKraPin.setText(employee.kraPin);
	txtPhoneNumber.setText(String.valueOf(employee.phoneNumber));
	txtEmail.setText(employee.email);
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
	    Employee employee = employees.get(0);
		txtIdNumber.setText(String.valueOf(employee.idNumber));
		txtFirstName.setText(employee.firstName);
		txtSurname.setText(employee.surname);
		txtGender.setText(String.valueOf(employee.gender));
		txtKraPin.setText(employee.kraPin);
		txtPhoneNumber.setText(String.valueOf(employee.phoneNumber));
		txtEmail.setText(employee.email);
	    currentIndex = 0;
	    newRecordState = false;
	    return;
	} else {
	    currentIndex--;
	    Employee employee = employees.get(0);
		txtIdNumber.setText(String.valueOf(employee.idNumber));
		txtFirstName.setText(employee.firstName);
		txtSurname.setText(employee.surname);
		txtGender.setText(String.valueOf(employee.gender));
		txtKraPin.setText(employee.kraPin);
		txtPhoneNumber.setText(String.valueOf(employee.phoneNumber));
		txtEmail.setText(employee.email);
	    newRecordState = false;
	}
    }

    private void goNext() {
	if(!leaveNewRecordState()) {
	   return;
	}

	if(currentIndex == (employees.size() - 1)) {
	    // I have explicitly stated goLast so as to cater for when a new record is
	    // inserted but not saved to get rid of the blanks
	    // TODO: Abstract this appropriately
	    currentIndex = employees.size() - 1;
	    Employee employee = employees.get(0);
		txtIdNumber.setText(String.valueOf(employee.idNumber));
		txtFirstName.setText(employee.firstName);
		txtSurname.setText(employee.surname);
		txtGender.setText(String.valueOf(employee.gender));
		txtKraPin.setText(employee.kraPin);
		txtPhoneNumber.setText(String.valueOf(employee.phoneNumber));
		txtEmail.setText(employee.email);
	    newRecordState = false;
	    return;
	} else {
	    currentIndex++;
	    Employee employee = employees.get(0);
		txtIdNumber.setText(String.valueOf(employee.idNumber));
		txtFirstName.setText(employee.firstName);
		txtSurname.setText(employee.surname);
		txtGender.setText(String.valueOf(employee.gender));
		txtKraPin.setText(employee.kraPin);
		txtPhoneNumber.setText(String.valueOf(employee.phoneNumber));
		txtEmail.setText(employee.email);
	    newRecordState = false;
	}
    }

    private void goLast() {
	if(!leaveNewRecordState()) {
	   return;
	}

	currentIndex = employees.size() - 1;
	Employee employee = employees.get(0);
	txtIdNumber.setText(String.valueOf(employee.idNumber));
	txtFirstName.setText(employee.firstName);
	txtSurname.setText(employee.surname);
	txtGender.setText(String.valueOf(employee.gender));
	txtKraPin.setText(employee.kraPin);
	txtPhoneNumber.setText(String.valueOf(employee.phoneNumber));
	txtEmail.setText(employee.email);
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
	Employee employee;
	if(result.isPresent()) {
	    employee = employeeTbl.getEmployee(Integer.parseInt(result.get()));
	    if(employee != null) {
		for(int i = 0; i < employees.size(); i++) {
		    if(employees.get(i).idNumber == employee.idNumber) {
			currentIndex = i;
			employee = employees.get(currentIndex);
			txtIdNumber.setText(String.valueOf(employee.idNumber));
			txtFirstName.setText(employee.firstName);
			txtSurname.setText(employee.surname);
			txtGender.setText(String.valueOf(employee.gender));
			txtKraPin.setText(employee.kraPin);
			txtPhoneNumber.setText(String.valueOf(employee.phoneNumber));
			txtEmail.setText(employee.email);
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

	txtIdNumber.setText("");
	txtFirstName.setText("");
	txtSurname.setText("");
	txtGender.setText("");
	txtKraPin.setText("");
	txtPhoneNumber.setText("");
	txtEmail.setText("");

	newRecordState = true;
    }

    private void saveRecord() {
	// Include some functionality like in delete to cater for new records
	// After a new record has been added, renavigate appropriately
	// TODO: I think i have implemented this. Investiigate

	Employee employee = new Employee();
	employee.firstName = txtFirstName.getText();
	employee.surname = txtSurname.getText();
	employee.gender = txtGender.getText().charAt(0);
	// TODO: GET ONLY THE FIRST CHARACTER
	employee.kraPin = txtKraPin.getText();
	employee.email = txtEmail.getText();
	if(txtIdNumber.getText() == null || txtIdNumber.getText().trim().isEmpty()) {
	    Alert alert = new Alert(AlertType.WARNING);
	    alert.setTitle("Warning");
	    alert.setHeaderText(null);
	    alert.setContentText("The ID Number field should not be left blank");
	    alert.showAndWait();
	    return;
	} else if(employee.firstName == null || employee.firstName.trim().isEmpty()) {
	    Alert alert = new Alert(AlertType.WARNING);
	    alert.setTitle("Warning");
	    alert.setHeaderText(null);
	    alert.setContentText("The First Name field should not be left blank");
	    alert.showAndWait();
	    return;
	} else if(employee.surname == null || employee.surname.trim().isEmpty()) {
	    Alert alert = new Alert(AlertType.WARNING);
	    alert.setTitle("Warning");
	    alert.setHeaderText(null);
	    alert.setContentText("The Surname field should not be left blank");
	    alert.showAndWait();
	    return;
	} else if(String.valueOf(employee.gender) == null || String.valueOf(employee.gender).trim().isEmpty()) {
	    Alert alert = new Alert(AlertType.WARNING);
	    alert.setTitle("Warning");
	    alert.setHeaderText(null);
	    alert.setContentText("The Gender field should not be left blank");
	    alert.showAndWait();
	    return;
	} else if(employee.kraPin == null || employee.kraPin.trim().isEmpty()) {
	    Alert alert = new Alert(AlertType.WARNING);
	    alert.setTitle("Warning");
	    alert.setHeaderText(null);
	    alert.setContentText("The Kra Pin field should not be left blank");
	    alert.showAndWait();
	    return;
	} else if(txtPhoneNumber.getText() == null || txtPhoneNumber.getText().trim().isEmpty()) {
	    Alert alert = new Alert(AlertType.WARNING);
	    alert.setTitle("Warning");
	    alert.setHeaderText(null);
	    alert.setContentText("The Phone Number field should not be left blank");
	    alert.showAndWait();
	    return;
	} else if(employee.email == null || employee.email.trim().isEmpty()) {
	    // The email field can be left blank
	    employee.email = "";
	}

	// I am posting this here so as to avoid errors when
	// no values have been entered. 
	employee.idNumber = Integer.parseInt(txtIdNumber.getText());
	employee.phoneNumber = Integer.parseInt(txtPhoneNumber.getText());
	
	if(newRecordState) {
	    if(employeeTbl.insertEmployee(employee)) {
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
	    if(employeeTbl.updateEmployee(employee)) {
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

	// Update the local storage of the employees
	employeeTbl.getAllEmployees();
	employees = employeeTbl.employees;
	newRecordState = false;
    }

    private void deleteRecord() {
	// If the user wants to leave the new record state simply set newRecordState to false and go prev
	if(!leaveNewRecordState()) {
	    return;
	}

	Employee employee = new Employee();
	employee.idNumber = Integer.parseInt(txtIdNumber.getText());
	employee.firstName = txtFirstName.getText();
	employee.surname = txtSurname.getText();
	employee.gender = txtGender.getText().charAt(0);
	// TODO: GET ONLY THE FIRST CHARACTER
	employee.kraPin = txtKraPin.getText();
	employee.phoneNumber = Integer.parseInt(txtPhoneNumber.getText());
	employee.email = txtEmail.getText();

	// Confirm if the user wants to delete
	Alert alert = new Alert(AlertType.CONFIRMATION);
	alert.setTitle("Confirmation");
	alert.setHeaderText(null);
	alert.setContentText("Are you sure you want to delete the record?");
	Optional<ButtonType> result = alert.showAndWait();
	if(result.get() == ButtonType.OK) {
	    if(employeeTbl.deleteEmployee(employee)) {
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
	// Update the local storage of the employees
	employeeTbl.getAllEmployees();
	employees = employeeTbl.employees;
	newRecordState = false;
	// Navigate accordingly
	if(currentIndex > (employees.size() - 2)) { // If the record deleted was the last record
	    goLast();
	} else {
	    goPrev();
	}

    }
}
