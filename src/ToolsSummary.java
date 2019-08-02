import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import java.util.ArrayList;
import javafx.stage.Stage;

public class ToolsSummary {
    Stage primaryStage = new Stage();


    public Stage getStage() {
        primaryStage.setTitle("Tools Summary");

        PostgresDbConn garageDb = new PostgresDbConn();
        ToolsInventoryTbl toolsInventoryTbl = new ToolsInventoryTbl(garageDb.connect());
        toolsInventoryTbl.getAllTools();
        ArrayList<Tool> tools = toolsInventoryTbl.tools;

        String html = "<!DOCTYPE html>"
            + "<html><head>"
            + "<link rel='stylesheet' href='file:///home/timo/Documents/Projects/Class_Projects/Garage_Management_System/src/media/materialize.min.css'>"
            + "<script src='file:///home/timo/Documents/Projects/Class_Projects/Garage_Management_System/src/media/materialize.min.js'></script>"
            + "<title>Employee Summary</title></head><body class='container-fluid'>"
            + "<h2>Tools List</h2>"
            + "<div class='row'>"
            + "<table class='striped highlight'>"
            + "<thead>"
            + " <tr class='white-text teal darken-4'> "
            + "    <th>#</th>"
            + "    <th>Tool Id</th>"
            + "    <th>Tool Name</th>"
            + "    <th>Price</th>"
            + "    <th>Quantity</th>"
            + "  </tr>"
            + "</thead>"
            + "<tbody>";

        for(int i = 0; i < tools.size(); i++) {
            html += "<tr>"
                + "<th scope='row'>" + i + "</th>"
                + "    <td>" + tools.get(i).toolId + "</td>"
                + "    <td>" + tools.get(i).toolName + "</td>"
                + "    <td>" + tools.get(i).price + "</td>"
                + "    <td>" + tools.get(i).quantity + "</td>"
                + " </tr>";
        }

        html += "<tr>"
                + "<th scope='row'></th>"
                + "    <td></td>"
                + "    <td><b>Total Cost:</b> </td>";

        int totalCost = 0;
        for(Tool tool: tools) {
            totalCost += tool.price;
        }
        html += "    <td><b>" + totalCost + "</b></td><td></td>"
                + " </tr>";

        html += "</tbody></table></div>";
        
        html += "<div class='row'><p class='flow-text'>Total Number of Tools: " + tools.size() + "</p></div>";
        html += "<body></html>";
        WebView webView = new WebView();

        webView.getEngine().loadContent(html);

        VBox vBox = new VBox(webView);
        Scene scene = new Scene(vBox, 960, 600);

        primaryStage.setScene(scene);
        return primaryStage;

    }
}