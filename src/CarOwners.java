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

public class CarOwners {
    private TextField txtIdNumber;
    private TextField txtFirstName;
    private TextField txtSurname;
    private TextField txtPhoneNumber;
    private TextField txtEmail;
    private int currentIndex;
    private CarOwnerTbl carOwnerTbl;
    private ArrayList<CarOwner> carOwners;

    private boolean newRecordState;

    CarOwners() {
	txtIdNumber = null;
	txtFirstName = null;
	txtSurname = null;
	txtPhoneNumber = null;
	txtEmail = null;
	currentIndex = 0;
	carOwnerTbl = null;
	carOwners = null;
	newRecordState = false;
    }

    public VBox getContent() {
	PostgresDbConn garageDb = new PostgresDbConn();
	carOwnerTbl = new CarOwnerTbl(garageDb.connect());
	carOwnerTbl.getAllCarOwners();
	carOwners = carOwnerTbl.carOwners;
	CarOwner carOwner = carOwners.get(0);

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

	txtIdNumber.setText(String.valueOf(carOwner.idNumber));
	txtFirstName.setText(carOwner.firstName);
	txtSurname.setText(carOwner.surname);
	txtPhoneNumber.setText(String.valueOf(carOwner.phoneNumber));
	txtEmail.setText(carOwner.email);

	// Add the controls to the Grid Pane
	grdFields.addRow(0, lblIdNumber, txtIdNumber);
	grdFields.addRow(1, lblFirstName, txtFirstName);
	grdFields.addRow(2, lblSurname, txtSurname);
	grdFields.addRow(3, lblPhoneNumber, txtPhoneNumber);
	grdFields.addRow(4, lblEmail, txtEmail);

	// Align the labels to the right
	grdFields.setHalignment(lblIdNumber, HPos.RIGHT);
	grdFields.setHalignment(lblFirstName, HPos.RIGHT);
	grdFields.setHalignment(lblSurname, HPos.RIGHT);
	grdFields.setHalignment(lblPhoneNumber, HPos.RIGHT);
	grdFields.setHalignment(lblEmail, HPos.RIGHT);

	// I want the text fields to expand to fit the left spacing
	// interestingly, doing this for the first element affects the rest
	GridPane.setHgrow(txtIdNumber, Priority.ALWAYS);

	// Set the labels to their appropriate css classes
	lblIdNumber.getStyleClass().setAll("text-color", "lbl");
	lblFirstName.getStyleClass().setAll("text-color", "lbl");
	lblSurname.getStyleClass().setAll("text-color", "lbl");
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

	CarOwner carOwner = carOwners.get(0);
	txtIdNumber.setText(String.valueOf(carOwner.idNumber));
	txtFirstName.setText(carOwner.firstName);
	txtSurname.setText(carOwner.surname);
	txtPhoneNumber.setText(String.valueOf(carOwner.phoneNumber));
	txtEmail.setText(carOwner.email);
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
	    CarOwner carOwner = carOwners.get(0);
	    txtIdNumber.setText(String.valueOf(carOwner.idNumber));
	    txtFirstName.setText(carOwner.firstName);
	    txtSurname.setText(carOwner.surname);
	    txtPhoneNumber.setText(String.valueOf(carOwner.phoneNumber));
	    txtEmail.setText(carOwner.email);
	    currentIndex = 0;
	    newRecordState = false;
	    return;
	} else {
	    currentIndex--;
	    CarOwner carOwner = carOwners.get(currentIndex);
	    txtIdNumber.setText(String.valueOf(carOwner.idNumber));
	    txtFirstName.setText(carOwner.firstName);
	    txtSurname.setText(carOwner.surname);
	    txtPhoneNumber.setText(String.valueOf(carOwner.phoneNumber));
	    txtEmail.setText(carOwner.email);
	    newRecordState = false;
	}
    }

    private void goNext() {
	if(!leaveNewRecordState()) {
	   return;
	}

	if(currentIndex == (carOwners.size() - 1)) {
	    // I have explicitly stated goLast so as to cater for when a new record is
	    // inserted but not saved to get rid of the blanks
	    // TODO: Abstract this appropriately
	    currentIndex = carOwners.size() - 1;
	    CarOwner carOwner = carOwners.get(currentIndex);
	    txtIdNumber.setText(String.valueOf(carOwner.idNumber));
	    txtFirstName.setText(carOwner.firstName);
	    txtSurname.setText(carOwner.surname);
	    txtPhoneNumber.setText(String.valueOf(carOwner.phoneNumber));
	    txtEmail.setText(carOwner.email);
	    newRecordState = false;
	    return;
	} else {
	    currentIndex++;
	    CarOwner carOwner = carOwners.get(currentIndex);
	    txtIdNumber.setText(String.valueOf(carOwner.idNumber));
	    txtFirstName.setText(carOwner.firstName);
	    txtSurname.setText(carOwner.surname);
	    txtPhoneNumber.setText(String.valueOf(carOwner.phoneNumber));
	    txtEmail.setText(carOwner.email);
	    newRecordState = false;
	}
    }

    private void goLast() {
	if(!leaveNewRecordState()) {
	   return;
	}

	currentIndex = carOwners.size() - 1;
	CarOwner carOwner = carOwners.get(currentIndex);
	txtIdNumber.setText(String.valueOf(carOwner.idNumber));
	txtFirstName.setText(carOwner.firstName);
	txtSurname.setText(carOwner.surname);
	txtPhoneNumber.setText(String.valueOf(carOwner.phoneNumber));
	txtEmail.setText(carOwner.email);
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
	CarOwner carOwner;
	if(result.isPresent()) {
	    carOwner = carOwnerTbl.getCarOwner(Integer.parseInt(result.get()));
	    if(carOwner != null) {
		for(int i = 0; i < carOwners.size(); i++) {
		    if(carOwners.get(i).idNumber == carOwner.idNumber) {
			currentIndex = i;
			carOwner = carOwners.get(currentIndex);
			txtIdNumber.setText(String.valueOf(carOwner.idNumber));
			txtFirstName.setText(carOwner.firstName);
			txtSurname.setText(carOwner.surname);
			txtPhoneNumber.setText(String.valueOf(carOwner.phoneNumber));
			txtEmail.setText(carOwner.email);
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
	txtPhoneNumber.setText("");
	txtEmail.setText("");

	newRecordState = true;
    }

    private void saveRecord() {
	// Include some functionality like in delete to cater for new records
	// After a new record has been added, renavigate appropriately
	// TODO: I think i have implemented this. Investiigate

	CarOwner carOwner = new CarOwner();
	carOwner.firstName = txtFirstName.getText();
	carOwner.surname = txtSurname.getText();
	carOwner.email = txtEmail.getText();
	if(txtIdNumber.getText() == null || txtIdNumber.getText().trim().isEmpty()) {
	    Alert alert = new Alert(AlertType.WARNING);
	    alert.setTitle("Warning");
	    alert.setHeaderText(null);
	    alert.setContentText("The ID Number field should not be left blank");
	    alert.showAndWait();
	    return;
	} else if(carOwner.firstName == null || carOwner.firstName.trim().isEmpty()) {
	    Alert alert = new Alert(AlertType.WARNING);
	    alert.setTitle("Warning");
	    alert.setHeaderText(null);
	    alert.setContentText("The First Name field should not be left blank");
	    alert.showAndWait();
	    return;
	} else if(carOwner.surname == null || carOwner.surname.trim().isEmpty()) {
	    Alert alert = new Alert(AlertType.WARNING);
	    alert.setTitle("Warning");
	    alert.setHeaderText(null);
	    alert.setContentText("The Surname field should not be left blank");
	    alert.showAndWait();
	    return;
	} else if(txtPhoneNumber.getText() == null || txtPhoneNumber.getText().trim().isEmpty()) {
	    Alert alert = new Alert(AlertType.WARNING);
	    alert.setTitle("Warning");
	    alert.setHeaderText(null);
	    alert.setContentText("The Phone Number field should not be left blank");
	    alert.showAndWait();
	    return;
	} else if(carOwner.email == null || carOwner.email.trim().isEmpty()) {
	    // The email field can be left blank
	    carOwner.email = "";
	}

	// I am posting this here so as to avoid errors when
	// no values have been entered. 
	carOwner.idNumber = Integer.parseInt(txtIdNumber.getText());
	carOwner.phoneNumber = Integer.parseInt(txtPhoneNumber.getText());
	
	if(newRecordState) {
	    if(carOwnerTbl.insertCarOwner(carOwner)) {
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
	    if(carOwnerTbl.updateCarOwner(carOwner)) {
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

	// Update the local storage of the carOwners
	carOwnerTbl.getAllCarOwners();
	carOwners = carOwnerTbl.carOwners;
	newRecordState = false;
    }

    private void deleteRecord() {
	// If the user wants to leave the new record state simply set newRecordState to false and go prev
	if(!leaveNewRecordState()) {
	    return;
	}

	CarOwner carOwner = new CarOwner();
	carOwner.idNumber = Integer.parseInt(txtIdNumber.getText());
	carOwner.firstName = txtFirstName.getText();
	carOwner.surname = txtSurname.getText();
	carOwner.phoneNumber = Integer.parseInt(txtPhoneNumber.getText());
	carOwner.email = txtEmail.getText();

	// Confirm if the user wants to delete
	Alert alert = new Alert(AlertType.CONFIRMATION);
	alert.setTitle("Confirmation");
	alert.setHeaderText(null);
	alert.setContentText("Are you sure you want to delete the record?");
	Optional<ButtonType> result = alert.showAndWait();
	if(result.get() == ButtonType.OK) {
	    if(carOwnerTbl.deleteCarOwner(carOwner)) {
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
	// Update the local storage of the carOwners
	carOwnerTbl.getAllCarOwners();
	carOwners = carOwnerTbl.carOwners;
	newRecordState = false;
	// Navigate accordingly
	if(currentIndex > (carOwners.size() - 2)) { // If the record deleted was the last record
	    goLast();
	} else {
	    goPrev();
	}

    }
}
