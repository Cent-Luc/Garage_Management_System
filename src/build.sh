echo "Compiling"
echo "===================="
echo "1. PostgresDbConn"
javac PostgresDbConn.java
echo "2. CarDb"
javac CarDb.java
echo "2. Cars"
javac --module-path /opt/javafx/javafx-sdk-11.0.2/lib/ --add-modules javafx.controls Cars.java
echo "3. Main"
javac --module-path /opt/javafx/javafx-sdk-11.0.2/lib/ --add-modules javafx.controls Main.java
echo "===================="
echo "Done"
