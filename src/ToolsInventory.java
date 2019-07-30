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

public class ToolsInventory {
    private TextField txtToolId;
    private TextField txtToolName;
    private TextField txtPrice;
    private TextField txtQuantity;
    private int currentIndex;
    private ToolsInventoryTbl toolsInventoryTbl;
    private ArrayList<Tool> tools;

    private boolean newRecordState;

    ToolsInventory() {
	txtToolId = null;
	txtToolName = null;
	txtPrice = null;
	txtQuantity = null;
	currentIndex = 0;
	toolsInventoryTbl = null;
	tools = null;
	newRecordState = false;
    }

    public VBox getContent() {
	PostgresDbConn garageDb = new PostgresDbConn();
	toolsInventoryTbl = new ToolsInventoryTbl(garageDb.connect());
	toolsInventoryTbl.getAllTools();
	tools = toolsInventoryTbl.tools;
	Tool tool = tools.get(0);

	txtToolId = new TextField();
	txtToolId.setDisable(true);
	Label lblToolId = new Label("Tool _Id:");
	lblToolId.setLabelFor(txtToolId);
	lblToolId.setMnemonicParsing(true);

	txtToolName = new TextField();
	Label lblToolName = new Label("_Tool Name:");
	lblToolName.setLabelFor(txtToolName);
	lblToolName.setMnemonicParsing(true);

	txtPrice = new TextField();
	Label lblPrice = new Label("_Price:");
	lblPrice.setLabelFor(txtPrice);
	lblPrice.setMnemonicParsing(true);

	txtQuantity = new TextField();
	Label lblQuantity = new Label("_Quantity:");
	lblQuantity.setLabelFor(txtQuantity);
	lblQuantity.setMnemonicParsing(true);

	GridPane grdFields = new GridPane();
	grdFields.setVgap(25);
	grdFields.setHgap(10);
	grdFields.setMaxWidth(Double.MAX_VALUE);
	// grdFields.setGridLinesVisible(true);
	grdFields.getStyleClass().setAll("brd");

	txtToolId.setText(String.valueOf(tool.toolId));
	txtToolName.setText(tool.toolName);
	txtPrice.setText(String.valueOf(tool.price));
	txtQuantity.setText(String.valueOf(tool.quantity));

	// Add the controls to the Grid Pane
	grdFields.addRow(0, lblToolId, txtToolId);
	grdFields.addRow(1, lblToolName, txtToolName);
	grdFields.addRow(2, lblPrice, txtPrice);
	grdFields.addRow(3, lblQuantity, txtQuantity);

	// Align the labels to the right
	grdFields.setHalignment(lblToolId, HPos.RIGHT);
	grdFields.setHalignment(lblToolName, HPos.RIGHT);
	grdFields.setHalignment(lblPrice, HPos.RIGHT);
	grdFields.setHalignment(lblQuantity, HPos.RIGHT);

	// I want the text fields to expand to fit the left spacing
	// interestingly, doing this for the first element affects the rest
	GridPane.setHgrow(txtToolId, Priority.ALWAYS);

	// Set the labels to their appropriate css classes
	lblToolId.getStyleClass().setAll("text-color", "lbl");
	lblToolName.getStyleClass().setAll("text-color", "lbl");
	lblPrice.getStyleClass().setAll("text-color", "lbl");
	lblQuantity.getStyleClass().setAll("text-color", "lbl");

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

	Tool tool = tools.get(0);
	txtToolId.setText(String.valueOf(tool.toolId));
	txtToolName.setText(tool.toolName);
	txtPrice.setText(String.valueOf(tool.price));
	txtQuantity.setText(String.valueOf(tool.quantity));
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
	    Tool tool = tools.get(0);
	    txtToolId.setText(String.valueOf(tool.toolId));
	    txtToolName.setText(tool.toolName);
	    txtPrice.setText(String.valueOf(tool.price));
	    txtQuantity.setText(String.valueOf(tool.quantity));
	    currentIndex = 0;
	    newRecordState = false;
	    return;
	} else {
	    currentIndex--;
	    Tool tool = tools.get(currentIndex);
	    txtToolId.setText(String.valueOf(tool.toolId));
	    txtToolName.setText(tool.toolName);
	    txtPrice.setText(String.valueOf(tool.price));
	    txtQuantity.setText(String.valueOf(tool.quantity));
	    newRecordState = false;
	}
    }

    private void goNext() {
	if(!leaveNewRecordState()) {
	   return;
	}

	if(currentIndex == (tools.size() - 1)) {
	    // I have explicitly stated goLast so as to cater for when a new record is
	    // inserted but not saved to get rid of the blanks
	    // TODO: Abstract this appropriately
	    currentIndex = tools.size() - 1;
	    Tool tool = tools.get(currentIndex);
	    txtToolId.setText(String.valueOf(tool.toolId));
	    txtToolName.setText(tool.toolName);
	    txtPrice.setText(String.valueOf(tool.price));
	    txtQuantity.setText(String.valueOf(tool.quantity));
	    newRecordState = false;
	    return;
	} else {
	    currentIndex++;
	    Tool tool = tools.get(currentIndex);
	    txtToolId.setText(String.valueOf(tool.toolId));
	    txtToolName.setText(tool.toolName);
	    txtPrice.setText(String.valueOf(tool.price));
	    txtQuantity.setText(String.valueOf(tool.quantity));
	    newRecordState = false;
	}
    }

    private void goLast() {
	if(!leaveNewRecordState()) {
	   return;
	}

	currentIndex = tools.size() - 1;
	Tool tool = tools.get(currentIndex);
	txtToolId.setText(String.valueOf(tool.toolId));
	txtToolName.setText(tool.toolName);
	txtPrice.setText(String.valueOf(tool.price));
	txtQuantity.setText(String.valueOf(tool.quantity));
	newRecordState = false;
    }

    private void searchRecord() {
	if(!leaveNewRecordState()) {
	   return;
	}

	TextInputDialog dialog = new TextInputDialog();
	dialog.setTitle("Search");
	dialog.setHeaderText("Enter the Tool ID to search");
	dialog.setContentText("Tool ID:");
	Optional<String> result = dialog.showAndWait();
	Tool tool;
	if(result.isPresent()) {
	    tool = toolsInventoryTbl.getTool(Integer.parseInt(result.get()));
	    if(tool != null) {
		for(int i = 0; i < tools.size(); i++) {
		    if(tools.get(i).toolId == tool.toolId) {
			currentIndex = i;
			tool = tools.get(currentIndex);
			txtToolId.setText(String.valueOf(tool.toolId));
			txtToolName.setText(tool.toolName);
			txtPrice.setText(String.valueOf(tool.price));
			txtQuantity.setText(String.valueOf(tool.quantity));
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

	txtToolId.setText("");
	txtToolName.setText("");
	txtPrice.setText("");
	txtQuantity.setText("");

	newRecordState = true;
    }

    private void saveRecord() {
	// Include some functionality like in delete to cater for new records
	// After a new record has been added, renavigate appropriately
	// TODO: I think i have implemented this. Investiigate

	Tool tool = new Tool();
	tool.toolName = txtToolName.getText();
	if(!newRecordState && (txtToolId.getText() != null || !txtToolId.getText().trim().isEmpty())) {
	    tool.toolId = Integer.parseInt(txtToolId.getText());
	} else if(tool.toolName == null || tool.toolName.trim().isEmpty()) {
	    Alert alert = new Alert(AlertType.WARNING);
	    alert.setTitle("Warning");
	    alert.setHeaderText(null);
	    alert.setContentText("The Tool Name field should not be left blank");
	    alert.showAndWait();
	    return;
	} else if(txtPrice.getText() == null || txtPrice.getText().trim().isEmpty()) {
	    Alert alert = new Alert(AlertType.WARNING);
	    alert.setTitle("Warning");
	    alert.setHeaderText(null);
	    alert.setContentText("The Price field should not be left blank");
	    alert.showAndWait();
	    return;
	} else if(txtQuantity.getText() == null || txtQuantity.getText().trim().isEmpty()) {
	    Alert alert = new Alert(AlertType.WARNING);
	    alert.setTitle("Warning");
	    alert.setHeaderText(null);
	    alert.setContentText("The quantity field should not be left blank");
	    alert.showAndWait();
	    return;
	} 

	// I am posting this here so as to avoid errors when
	// no values have been entered. 
	tool.quantity = Integer.parseInt(txtQuantity.getText());
	tool.price = Integer.parseInt(txtPrice.getText());
	
	if(newRecordState) {
	    if(toolsInventoryTbl.insertTool(tool)) {
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
	    if(toolsInventoryTbl.updateTool(tool)) {
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

	// Update the local storage of the tools
	toolsInventoryTbl.getAllTools();
	tools = toolsInventoryTbl.tools;
	newRecordState = false;
    }

    private void deleteRecord() {
	// If the user wants to leave the new record state simply set newRecordState to false and go prev
	if(!leaveNewRecordState()) {
	    return;
	}

	Tool tool = new Tool();
	tool.toolId = Integer.parseInt(txtToolId.getText());
	tool.toolName = txtToolName.getText();
	tool.price = Integer.parseInt(txtPrice.getText());
	tool.quantity = Integer.parseInt(txtQuantity.getText());

	// Confirm if the user wants to delete
	Alert alert = new Alert(AlertType.CONFIRMATION);
	alert.setTitle("Confirmation");
	alert.setHeaderText(null);
	alert.setContentText("Are you sure you want to delete the record?");
	Optional<ButtonType> result = alert.showAndWait();
	if(result.get() == ButtonType.OK) {
	    if(toolsInventoryTbl.deleteTool(tool)) {
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
	// Update the local storage of the tools
	toolsInventoryTbl.getAllTools();
	tools = toolsInventoryTbl.tools;
	newRecordState = false;
	// Navigate accordingly
	if(currentIndex > (tools.size() - 2)) { // If the record deleted was the last record
	    goLast();
	} else {
	    goPrev();
	}

    }
}
