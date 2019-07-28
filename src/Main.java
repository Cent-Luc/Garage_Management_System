import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.scene.effect.DropShadow;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class Main extends Application {
    private Stage stage;
    private Label lblTitle;
    private HBox hbxContent;
    private double dragOffsetX;
    private double dragOffsetY;

    @Override
    public void start(Stage stage) {
	// Store the stage reference in the instance variable to
	// use it in the mouse pressed event handler later
	this.stage = stage;

	HBox root = new HBox();

	VBox vbxRow1 = new VBox(10);
	vbxRow1.setPrefWidth(200);
	vbxRow1.setStyle(
		"-fx-background-color: #0b3142;");
	HBox hbxLogo = new HBox(5);
	hbxLogo.setPrefSize(200, 55);
	hbxLogo.setStyle(
		"-fx-background-color: #0f5257;");
	Button btnCarOwners = new Button("Car Owners");
	btnCarOwners.setOnAction(e -> setVbxRow2Contents("car_owner"));
	vbxRow1.getChildren().addAll(hbxLogo, btnCarOwners);


	VBox vbxRow2 = new VBox(25);
	vbxRow2.setPrefWidth(650);
	vbxRow2.setStyle(
		"-fx-background-color: #9c92a3;");

	HBox hbxNavBar = new HBox(5);
	Button btnClose = new Button();
	// Set it to close the stage when pressed
	btnClose.setOnAction(e -> stage.close());
	// Load an image and set its height while preserving the aspect ratio
	Image imgCloseCircle = new Image(getClass().getResourceAsStream("media/times-circle.png"));
	Image imgClosePlain = new Image(getClass().getResourceAsStream("media/times.png"));
	ImageView imgCloseIconPlain = new ImageView(imgClosePlain);
	ImageView imgCloseIconCircle = new ImageView(imgCloseCircle);
	imgCloseIconCircle.setFitHeight(15);
	imgCloseIconPlain.setFitHeight(10);
	imgCloseIconCircle.setPreserveRatio(true);
	imgCloseIconPlain.setPreserveRatio(true);
	// Add the image to the button and set the background of the button to transparent
	btnClose.setGraphic(imgCloseIconPlain);
	btnClose.setStyle("-fx-background-color:transparent");
	btnClose.setPadding(new Insets(10, 13, 10, 12));
	// Add an event listener for when it is hovered on
	btnClose.addEventHandler(MouseEvent.MOUSE_ENTERED,
		new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent e) {
			btnClose.setGraphic(imgCloseIconCircle);
			btnClose.setPadding(new Insets(10, 10, 10, 10));
		    }
	});

	btnClose.addEventHandler(MouseEvent.MOUSE_EXITED,
		new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent e) {
			btnClose.setGraphic(imgCloseIconPlain);
			btnClose.setPadding(new Insets(10, 13, 10, 12));
		    }
	});
	
	HBox hbxTitle = new HBox(10);
	hbxTitle.setPrefSize(250, 30);
	// Set the max width so that the hbx does not scale up to fill the vbx
	hbxTitle.setMaxWidth(250);
	hbxTitle.setStyle("-fx-background-radius: 2.0;");
	// Add a label to the title
	lblTitle = new Label("Cars");
	lblTitle.setStyle(
		"-fx-text-fill: #0b3142;" +
		"-fx-font-size: 23px;" +
		"-fx-font-weight: bolder;");
	// Center the label in the hbox
	hbxTitle.setAlignment(Pos.CENTER);
	// Add the label to the hbxTitle
	hbxTitle.getChildren().addAll(lblTitle);
	// Add the title to the navbar
	// Add the button to the hbxNavBar
	hbxNavBar.getChildren().addAll(hbxTitle, btnClose);
	hbxNavBar.setAlignment(Pos.TOP_RIGHT);

	// Offset the title from the right to make it appear centered
	hbxNavBar.setMargin(hbxTitle, new Insets(10, 160, 0, 0));
	
	hbxContent = new HBox(10);
	hbxContent.setMaxWidth(600);
	// Set the bg color
	hbxContent.setStyle("-fx-background-radius: 2.0;");

	// Add the car ui's root element
	Cars cars = new Cars();
	VBox rtContent = cars.getContent();
	hbxContent.getChildren().addAll(rtContent);
	hbxContent.setHgrow(rtContent, Priority.ALWAYS);

	// Add the shadow effect
	// hbxContent.setEffect(dsEffect);
	// Add the navbar and content to row 2
	vbxRow2.getChildren().addAll(hbxNavBar, hbxContent);
	// Offset the margin of the content from the left
	vbxRow2.setMargin(hbxContent, new Insets(0, 0, 25, 25));

	root.getChildren().addAll(vbxRow1, vbxRow2);

	Scene scene = new Scene(root);

	// Set mouse pressed and dragged event handlers for the scene
	// to support window moving
	scene.setOnMousePressed(e -> handleMousePressed(e));
	scene.setOnMouseDragged(e -> handleMouseDragged(e));

	stage.setScene(scene);
	stage.setTitle("CSS Titles");
	stage.initStyle(StageStyle.UNDECORATED);
	stage.show();
    }
    
    protected void handleMousePressed(MouseEvent e) {
	// Store the mouse x and y coordinates with respect to the
	// stage in the reference variables to use them in the drag event
	this.dragOffsetX = e.getScreenX() - stage.getX();
	this.dragOffsetY = e.getScreenY() - stage.getY();
    }

    protected void handleMouseDragged(MouseEvent e) {
	// Move the stage by the drag amount
	stage.setX(e.getScreenX() - this.dragOffsetX);
	stage.setY(e.getScreenY() - this.dragOffsetY);
    }

    private void setVbxRow2Contents(String UIName) {
	VBox uiContent;
	switch(UIName) {
	    case "car_owner":
		CarOwners carOwnersUI = new CarOwners();
		lblTitle.setText("Car Owners");
		uiContent = carOwnersUI.getContent();
		hbxContent.getChildren().set(0, uiContent);
		hbxContent.setHgrow(uiContent, Priority.ALWAYS);
		break;
	    default:
		break;
	}
    }
}
