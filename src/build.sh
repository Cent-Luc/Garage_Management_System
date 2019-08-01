echo "Compiling"
echo "===================="
echo "01. PostgresDbConn"
javac PostgresDbConn.java
echo "02. CarDb"
javac CarDb.java
echo "03. Cars"
javac --module-path /opt/javafx/javafx-sdk-11.0.2/lib/ --add-modules javafx.controls Cars.java
echo "04. CarOwnerTbl"
javac --module-path /opt/javafx/javafx-sdk-11.0.2/lib/ --add-modules javafx.controls CarOwnerTbl.java
echo "05. CarOwners"
javac --module-path /opt/javafx/javafx-sdk-11.0.2/lib/ --add-modules javafx.controls CarOwners.java
echo "06. SparesInventoryTbl"
javac --module-path /opt/javafx/javafx-sdk-11.0.2/lib/ --add-modules javafx.controls SparesInventory.java
echo "07. SparesInventory"
javac --module-path /opt/javafx/javafx-sdk-11.0.2/lib/ --add-modules javafx.controls SparesInventory.java
echo "08. ToolsInventoryTbl"
javac --module-path /opt/javafx/javafx-sdk-11.0.2/lib/ --add-modules javafx.controls ToolsInventory.java
echo "09. ToolsInventory"
javac --module-path /opt/javafx/javafx-sdk-11.0.2/lib/ --add-modules javafx.controls ToolsInventory.java
echo "10. EmployeeTbl"
javac --module-path /opt/javafx/javafx-sdk-11.0.2/lib/ --add-modules javafx.controls EmployeeTbl.java
echo "11. Employees"
javac --module-path /opt/javafx/javafx-sdk-11.0.2/lib/ --add-modules javafx.controls Employees.java
echo "12. SpecializationsTbl"
javac --module-path /opt/javafx/javafx-sdk-11.0.2/lib/ --add-modules javafx.controls SpecializationsTbl.java
echo "13. Specializations"
javac --module-path /opt/javafx/javafx-sdk-11.0.2/lib/ --add-modules javafx.controls Specializations.java
echo "14. EmployeeSpecializationsTbl"
javac --module-path /opt/javafx/javafx-sdk-11.0.2/lib/ --add-modules javafx.controls EmployeeSpecializationsTbl.java
echo "15. EmployeeSpecializations"
javac --module-path /opt/javafx/javafx-sdk-11.0.2/lib/ --add-modules javafx.controls EmployeeSpecializations.java
echo "16. Main"
javac --module-path /opt/javafx/javafx-sdk-11.0.2/lib/ --add-modules javafx.controls Main.java
echo "===================="
echo "Done"
