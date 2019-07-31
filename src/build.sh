echo "Compiling"
echo "===================="
echo "1. PostgresDbConn"
javac PostgresDbConn.java
echo "2. CarDb"
javac CarDb.java
echo "3. Cars"
javac --module-path /opt/javafx/javafx-sdk-11.0.2/lib/ --add-modules javafx.controls Cars.java
echo "4. CarOwnerTbl"
javac --module-path /opt/javafx/javafx-sdk-11.0.2/lib/ --add-modules javafx.controls CarOwnerTbl.java
echo "5. CarOwners"
javac --module-path /opt/javafx/javafx-sdk-11.0.2/lib/ --add-modules javafx.controls CarOwners.java
echo "6. SparesInventoryTbl"
javac --module-path /opt/javafx/javafx-sdk-11.0.2/lib/ --add-modules javafx.controls SparesInventory.java
echo "7. SparesInventory"
javac --module-path /opt/javafx/javafx-sdk-11.0.2/lib/ --add-modules javafx.controls SparesInventory.java
echo "8. ToolsInventoryTbl"
javac --module-path /opt/javafx/javafx-sdk-11.0.2/lib/ --add-modules javafx.controls ToolsInventory.java
echo "9. ToolsInventory"
javac --module-path /opt/javafx/javafx-sdk-11.0.2/lib/ --add-modules javafx.controls ToolsInventory.java
echo "10. EmployeeTbl"
javac --module-path /opt/javafx/javafx-sdk-11.0.2/lib/ --add-modules javafx.controls EmployeeTbl.java
echo "11. Employees"
javac --module-path /opt/javafx/javafx-sdk-11.0.2/lib/ --add-modules javafx.controls Employees.java
echo "12. SpecializationsTbl"
javac --module-path /opt/javafx/javafx-sdk-11.0.2/lib/ --add-modules javafx.controls SpecializationsTbl.java
echo "13. Specializations"
javac --module-path /opt/javafx/javafx-sdk-11.0.2/lib/ --add-modules javafx.controls Specializations.java
# echo "12. Car Summary"
# javac --module-path /opt/javafx/javafx-sdk-11.0.2/lib/ --add-modules javafx.controls, javafx.web CarsSummary.java
echo "14. Main"
javac --module-path /opt/javafx/javafx-sdk-11.0.2/lib/ --add-modules javafx.controls Main.java
echo "===================="
echo "Done"
