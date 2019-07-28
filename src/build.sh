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
echo "6. Main"
javac --module-path /opt/javafx/javafx-sdk-11.0.2/lib/ --add-modules javafx.controls Main.java
echo "===================="
echo "Done"
