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

public class Cars {
    private TextField txtNumberPlate;
    private TextField txtModel;
    private TextField txtBrand;
    private TextField txtYOM;
    private TextField txtOwnerIdNumber;
    private int currentIndex;
    private CarDb carDb;
    private ArrayList<Car> cars;

    private boolean newRecordState;

    Cars() {
	txtNumberPlate = null;
	txtModel = null;
	txtBrand = null;
	txtYOM = null;
	txtOwnerIdNumber = null;
	currentIndex = 0;
	carDb = null;
	cars = null;
	newRecordState = false;
    }

    public VBox getContent() {
	PostgresDbConn garageDb = new PostgresDbConn();
	carDb = new CarDb(garageDb.connect());
	carDb.getAllCars();
	cars = carDb.cars;
	Car car = carDb.cars.get(0);

	txtNumberPlate = new TextField();
	Label lblNumberPlate = new Label("_Number Plate:");
	lblNumberPlate.setLabelFor(txtNumberPlate);
	lblNumberPlate.setMnemonicParsing(true);

	txtModel = new TextField();
	Label lblModel = new Label("_Model:");
	lblModel.setLabelFor(txtModel);
	lblModel.setMnemonicParsing(true);

	txtBrand = new TextField();
	Label lblBrand = new Label("_Brand:");
	lblBrand.setLabelFor(txtBrand);
	lblBrand.setMnemonicParsing(true);

	// Year of manufacture
	txtYOM = new TextField();
	Label lblYOM = new Label("_Year Of Manufacture:");
	lblYOM.setLabelFor(txtYOM);
	lblYOM.setMnemonicParsing(true);

	txtOwnerIdNumber = new TextField();
	Label lblOwnerIdNumber = new Label("_Owner Id Number:");
	lblOwnerIdNumber.setLabelFor(txtOwnerIdNumber);
	lblOwnerIdNumber.setMnemonicParsing(true);

	GridPane grdFields = new GridPane();
	grdFields.setVgap(20);
	grdFields.setHgap(10);
	grdFields.setMaxWidth(Double.MAX_VALUE);
	// grdFields.setGridLinesVisible(true);
	grdFields.getStyleClass().setAll("brd");

	txtNumberPlate.setText(car.numberPlate);
	txtModel.setText(car.model);
	txtBrand.setText(car.brand);
	txtYOM.setText(car.yearOfManufacture);
	txtOwnerIdNumber.setText(car.ownerIdNumber);


	// Add the controls to the Grid Pane
	grdFields.addRow(0, lblNumberPlate, txtNumberPlate);
	grdFields.addRow(1, lblModel, txtModel);
	grdFields.addRow(2, lblBrand, txtBrand);
	grdFields.addRow(3, lblYOM, txtYOM);
	grdFields.addRow(4, lblOwnerIdNumber, txtOwnerIdNumber);

	// Align the labels to the right
	grdFields.setHalignment(lblNumberPlate, HPos.RIGHT);
	grdFields.setHalignment(lblModel, HPos.RIGHT);
	grdFields.setHalignment(lblBrand, HPos.RIGHT);
	grdFields.setHalignment(lblYOM, HPos.RIGHT);
	grdFields.setHalignment(lblOwnerIdNumber, HPos.RIGHT);

	// I want the text fields to expand to fit the left spacing
	// interestingly, doing this for the first element affects the rest
	GridPane.setHgrow(txtNumberPlate, Priority.ALWAYS);

	// Set the labels to their appropriate css classes
	lblNumberPlate.getStyleClass().setAll("text-color", "lbl");
	lblModel.getStyleClass().setAll("text-color", "lbl");
	lblBrand.getStyleClass().setAll("text-color", "lbl");
	lblYOM.getStyleClass().setAll("text-color", "lbl");
	lblOwnerIdNumber.getStyleClass().setAll("text-color", "lbl");

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

	Car car = cars.get(0);
	txtNumberPlate.setText(car.numberPlate);
	txtModel.setText(car.model);
	txtBrand.setText(car.brand);
	txtYOM.setText(car.yearOfManufacture);
	txtOwnerIdNumber.setText(car.ownerIdNumber);
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
	    Car car = cars.get(0);
	    txtNumberPlate.setText(car.numberPlate);
	    txtModel.setText(car.model);
	    txtBrand.setText(car.brand);
	    txtYOM.setText(car.yearOfManufacture);
	    txtOwnerIdNumber.setText(car.ownerIdNumber);
	    currentIndex = 0;
	    newRecordState = false;
	    return;
	} else {
	    currentIndex--;
	    Car car = cars.get(currentIndex);
	    txtNumberPlate.setText(car.numberPlate);
	    txtModel.setText(car.model);
	    txtBrand.setText(car.brand);
	    txtYOM.setText(car.yearOfManufacture);
	    txtOwnerIdNumber.setText(car.ownerIdNumber);
	    newRecordState = false;
	}
    }

    private void goNext() {
	if(!leaveNewRecordState()) {
	   return;
	}

	if(currentIndex == (cars.size() - 1)) {
	    // I have explicitly stated goLast so as to cater for when a new record is
	    // inserted but not saved to get rid of the blanks
	    // TODO: Abstract this appropriately
	    currentIndex = cars.size() - 1;
	    Car car = cars.get(currentIndex);
	    txtNumberPlate.setText(car.numberPlate);
	    txtModel.setText(car.model);
	    txtBrand.setText(car.brand);
	    txtYOM.setText(car.yearOfManufacture);
	    txtOwnerIdNumber.setText(car.ownerIdNumber);
	    newRecordState = false;
	    return;
	} else {
	    currentIndex++;
	    Car car = cars.get(currentIndex);
	    txtNumberPlate.setText(car.numberPlate);
	    txtModel.setText(car.model);
	    txtBrand.setText(car.brand);
	    txtYOM.setText(car.yearOfManufacture);
	    txtOwnerIdNumber.setText(car.ownerIdNumber);
	    newRecordState = false;
	}
    }

    private void goLast() {
	if(!leaveNewRecordState()) {
	   return;
	}

	currentIndex = cars.size() - 1;
	Car car = cars.get(currentIndex);
	txtNumberPlate.setText(car.numberPlate);
	txtModel.setText(car.model);
	txtBrand.setText(car.brand);
	txtYOM.setText(car.yearOfManufacture);
	txtOwnerIdNumber.setText(car.ownerIdNumber);
	newRecordState = false;
    }

    private void searchRecord() {
	if(!leaveNewRecordState()) {
	   return;
	}

	TextInputDialog dialog = new TextInputDialog();
	dialog.setTitle("Search");
	dialog.setHeaderText("Enter the number plate to search");
	dialog.setContentText("Number Plate:");
	Optional<String> result = dialog.showAndWait();
	Car car;
	if(result.isPresent()) {
	    car = carDb.getCar(result.get());
	    if(car != null) {
		for(int i = 0; i < cars.size(); i++) {
		    if(cars.get(i).numberPlate.equalsIgnoreCase(car.numberPlate)) {
			currentIndex = i;
			car = cars.get(currentIndex);
			txtNumberPlate.setText(car.numberPlate);
			txtModel.setText(car.model);
			txtBrand.setText(car.brand);
			txtYOM.setText(car.yearOfManufacture);
			txtOwnerIdNumber.setText(car.ownerIdNumber);
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

	txtNumberPlate.setText("");
	txtModel.setText("");
	txtBrand.setText("");
	txtYOM.setText("");
	txtOwnerIdNumber.setText("");

	newRecordState = true;
    }

    private void saveRecord() {
	// Include some functionality like in delete to cater for new records
	// After a new record has been added, renavigate appropriately
	// TODO: I think i have implemented this. Investiigate

	Car car = new Car();
	car.numberPlate = txtNumberPlate.getText();
	car.model = txtModel.getText();
	car.brand = txtBrand.getText();
	car.yearOfManufacture = txtYOM.getText();
	car.ownerIdNumber = txtOwnerIdNumber.getText();
	if(car.numberPlate == null || car.numberPlate.trim().isEmpty()) {
	    Alert alert = new Alert(AlertType.WARNING);
	    alert.setTitle("Warning");
	    alert.setHeaderText(null);
	    alert.setContentText("The number plate field should not be left blank");
	    alert.showAndWait();
	    return;
	} else if(car.model == null || car.model.trim().isEmpty()) {
	    Alert alert = new Alert(AlertType.WARNING);
	    alert.setTitle("Warning");
	    alert.setHeaderText(null);
	    alert.setContentText("The model field should not be left blank");
	    alert.showAndWait();
	    return;
	} else if(car.brand == null || car.brand.trim().isEmpty()) {
	    Alert alert = new Alert(AlertType.WARNING);
	    alert.setTitle("Warning");
	    alert.setHeaderText(null);
	    alert.setContentText("The brand field should not be left blank");
	    alert.showAndWait();
	    return;
	} else if(car.yearOfManufacture == null || car.yearOfManufacture.trim().isEmpty()) {
	    Alert alert = new Alert(AlertType.WARNING);
	    alert.setTitle("Warning");
	    alert.setHeaderText(null);
	    alert.setContentText("The Year Of Manufacture field should not be left blank");
	    alert.showAndWait();
	    return;
	} else if(car.ownerIdNumber == null || car.ownerIdNumber.trim().isEmpty()) {
	    Alert alert = new Alert(AlertType.WARNING);
	    alert.setTitle("Warning");
	    alert.setHeaderText(null);
	    alert.setContentText("The Owner Id Number field should not be left blank");
	    alert.showAndWait();
	    return;
	}
	
	if(newRecordState) {
	    if(carDb.insertCar(car)) {
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
	    if(carDb.updateCar(car)) {
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

	// Update the local storage of the cars
	carDb.getAllCars();
	cars = carDb.cars;
	newRecordState = false;
    }

    private void deleteRecord() {
	// If the user wants to leave the new record state simply set newRecordState to false and go prev
	if(leaveNewRecordState()) {
	    newRecordState = false;
	    if(currentIndex == 0) {
		goFirst();
	    } else {
		goPrev();
	    }
	    
	    return;
	}

	Car car = new Car();
	car.numberPlate = txtNumberPlate.getText();
	car.model = txtModel.getText();
	car.brand = txtBrand.getText();
	car.yearOfManufacture = txtYOM.getText();
	car.ownerIdNumber = txtOwnerIdNumber.getText();

	// Confirm if the user wants to delete
	Alert alert = new Alert(AlertType.CONFIRMATION);
	alert.setTitle("Confirmation");
	alert.setHeaderText(null);
	alert.setContentText("Are you sure you want to delete the record?");
	Optional<ButtonType> result = alert.showAndWait();
	if(result.get() == ButtonType.OK) {
	    if(carDb.deleteCar(car)) {
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
	// Update the local storage of the cars
	carDb.getAllCars();
	cars = carDb.cars;
	// Navigate accordingly
	if(currentIndex > (cars.size() - 2)) { // If the record deleted was the last record
	    goLast();
	} else {
	    goPrev();
	}

    }
}
