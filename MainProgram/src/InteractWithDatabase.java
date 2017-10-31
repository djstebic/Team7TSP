import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class InteractWithDatabase {
	public static Connection connection = null;
	
	public static Connection getConnection(){
		if(connection != null){	

			return connection;
		}
		else{
			try {
				Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
//				String msAccDB = "D:\\Census Database\\SF1_Access2007.accdb";
				String msAccDB = "data/SF1_Access2007_1.accdb";
				String dbURL = "jdbc:ucanaccess://" + msAccDB + ";keepMirror=" + getPathToMirror() + ",singleconnection=true";
				connection = DriverManager.getConnection(dbURL);
			} catch (ClassNotFoundException cnfex) {
				System.out.println("Problem in loading or " + "registering MS Access JDBC driver");
				cnfex.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return connection;
		}
	}
	
	public static Path getPathToMirror() {
	    try {
	        Path temp = Paths.get(System.getProperty("java.io.tmpdir"));
	        Path multum = Paths.get( temp + "/multum");
	        if (!Files.exists(multum)) {
	            multum = Files.createDirectory(multum);
	        }
	        Path hsqldb = Paths.get(multum + "/hsqldb");
	        if (!Files.exists(hsqldb)) {
	            hsqldb = Files.createDirectory(hsqldb);
	        }
	        return hsqldb;
	    } catch (IOException e) {
	        System.out.println("Unable to create Hsqldb directory");
	    }
	    return null;
	}
	
	public void printData(ResultSet toPrint) throws SQLException{
		ResultSetMetaData rsmd = toPrint.getMetaData();
		int columnsNumber = rsmd.getColumnCount();
		for (int i = 1; i <= columnsNumber; i++) {
			System.out.printf("%-12.10s",rsmd.getColumnName(i));
		}
		System.out.println();
		while (toPrint.next()) {
			for (int i = 1; i <= columnsNumber; i++) {
				System.out.printf("%-12.10s",  toPrint.getString(i));
			}
			System.out.println("");
		}
	}
	public static long folderSize(File directory) {
	    long length = 0;
	    for (File file : directory.listFiles()) {
	        if (file.isFile())
	            length += file.length();
	        else
	            length += folderSize(file);
	    }
	    return length;
	}

	public static void main(String[] args) {
		Statistics test = new Statistics();
		InteractWithDatabase prt = new InteractWithDatabase();
		long size = 0;
		Path temp = Paths.get(System.getProperty("java.io.tmpdir"));
		Path multum = Paths.get(temp + "/multum");
		File f = multum.toFile();
		if (folderSize(f) < 8439947254L)
			try {
				System.out.println( Paths.get(temp + "/multum"));
				Runtime.getRuntime().exec("cmd /C erase /Q" + " " + Paths.get(temp + "/multum"));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		String query = test.getMedianHouseholdIncome();
		try {
		prt.printData(test.runQuery(query, "Crystal Falls township"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		}
	}
