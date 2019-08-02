import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.sql.Connection;
import javafx.stage.Stage;

public class EmployeeSummary {
    Stage primaryStage = new Stage();


    public Stage getStage() {
        primaryStage.setTitle("Employee Summary");

        PostgresDbConn garageDb = new PostgresDbConn();
        Connection conn = garageDb.connect();
        EmployeeTbl employeeTbl = new EmployeeTbl(conn);
        employeeTbl.getAllEmployees();
        EmployeeSpecializationsTbl empSpTbl = new EmployeeSpecializationsTbl(conn);
        empSpTbl.getAllEmployeeSpecializations();
        ArrayList<EmployeeSpecialization> employeeSpecializations = empSpTbl.employeeSpecializations;
        ArrayList<Employee> employees = employeeTbl.employees;
        String html = "<!DOCTYPE html>"
            + "<html><head>"
            + "<link rel='stylesheet' href='file:///home/timo/Documents/Projects/Class_Projects/Garage_Management_System/src/media/materialize.min.css'>"
            + "<script src='file:///home/timo/Documents/Projects/Class_Projects/Garage_Management_System/src/media/materialize.min.js'></script>"
            + "<title>Employee Summary</title></head><body class='container-fluid'>"
            + "<h2>Employees List</h2>"
            + "<div class='row'>"
            + "<table class='striped highlight'>"
            + "<thead>"
            + " <tr class='white-text teal darken-4'> "
            + "    <th>#</th>"
            + "    <th>Id Number</th>"
            + "    <th>First Name</th>"
            + "    <th>Surname</th>"
            + "    <th>Gender</th>"
            + "    <th>Kra Pin</th>"
            + "    <th>Phone Number</th>"
            + "    <th>Email</th>"
            + "    <th>Specialization Name</th>"
            + "  </tr>"
            + "</thead>"
            + "<tbody>";

        for(int i = 0; i < employees.size(); i++) {
            html += "<tr>"
                + "<th scope='row'>" + i + "</th>"
                + "    <td>" + employees.get(i).idNumber + "</td>"
                + "    <td>" + employees.get(i).firstName + "</td>"
                + "    <td>" + employees.get(i).surname + "</td>"
                + "    <td>" + employees.get(i).gender + "</td>"
                + "    <td>" + employees.get(i).kraPin + "</td>"
                + "    <td>" + employees.get(i).phoneNumber + "</td>"
                + "    <td>" + employees.get(i).email + "</td>";
                for(EmployeeSpecialization emp: employeeSpecializations) {
                    if(emp.idNumber == employees.get(i).idNumber) {
                        html += "    <td>" + emp.specializationName + "</td>";
                        break;
                    }
                }
            html += "</tr>";
        }

        html += "</tbody></table></div>";
        
        html += "<div class='row'><p class='flow-text'>Total Number of Employees: " + employees.size() + "</p></div>";
        html += "<body></html>";
        WebView webView = new WebView();

        webView.getEngine().loadContent(html);

        VBox vBox = new VBox(webView);
        Scene scene = new Scene(vBox, 960, 600);

        primaryStage.setScene(scene);
        return primaryStage;

    }
}