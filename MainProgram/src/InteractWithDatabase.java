import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class InteractWithDatabase {

	    public static void main(String[] args) {

	        // variables
	        Connection connection = null;
	        Statement statement = null;
	        ResultSet resultSet = null;

	        // Step 1: Loading or registering Oracle JDBC driver class
	        try {
	            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
	        }
	        catch(ClassNotFoundException cnfex) {
	            System.out.println("Problem in loading or "
	                    + "registering MS Access JDBC driver");
	            cnfex.printStackTrace();
	        }

	        // Step 2: Opening database connection
	        try {
	            String msAccDB = "D:\\Documents\\Michigan Tech\\Michigan Tech 2017-2018\\Semester 1\\Team Software Project\\Database\\SF1_Access2007.accdb";
	            String dbURL = "jdbc:ucanaccess://" + msAccDB; 

	            // Step 2.A: Create and get connection using DriverManager class
	            connection = DriverManager.getConnection(dbURL); 

	            // Step 2.B: Creating JDBC Statement 
	            statement = connection.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
	                    java.sql.ResultSet.CONCUR_READ_ONLY);
	            statement.setFetchSize(Integer.MIN_VALUE);

	            // Step 2.C: Executing SQL & retrieve data into ResultSet
	            resultSet = statement.executeQuery("SELECT POP100 FROM Migeo2010 WHERE NAME = \"Crystal Falls township\"");

//	            System.out.println("ID\tName\t\t\tAge\tMatches");
	            System.out.println("==\t================\t===\t=======");

	            // processing returned data and printing into console
	            ResultSetMetaData rsmd = resultSet.getMetaData();
	            int columnsNumber = rsmd.getColumnCount();
	            while (resultSet.next()) {
	                for (int i = 1; i <= columnsNumber; i++) {
	                    if (i > 1) System.out.print(",  ");
	                    String columnValue = resultSet.getString(i);
	                    System.out.print(columnValue + " " + rsmd.getColumnName(i));
	                }
	                System.out.println("");
	            }
	        }
	        catch(SQLException sqlex){
	            sqlex.printStackTrace();
	        }
	    }
}

