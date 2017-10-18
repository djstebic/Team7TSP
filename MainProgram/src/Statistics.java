import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Statistics {
	public ResultSet getTotalPop(String township){
		Connection con = InteractWithDatabase.getConnection();
		Statement state = null;
		ResultSet result = null;
		try {
			state = con.createStatement();
			String part =  "\""+ township + "\"";
			result = state.executeQuery("select distinct POP100 from Migeo2010 where NAME = " + part);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public ResultSet getWhitePopNotHisp(){
		Connection con = InteractWithDatabase.getConnection();
		Statement state = null;
		ResultSet result = null;
		try {
			state = con.createStatement();
			result = state.executeQuery("select distinct P0050003 from Migeo2010 INNER JOIN  SF1_00003 ON (Migeo2010.LOGRECNO = SF1_00003.LOGRECNO ) where NAME = \"Crystal Falls township\"");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public ResultSet getAfricanPop(){
		Connection con = InteractWithDatabase.getConnection();
		Statement state = null;
		ResultSet result = null;
		try {
			state = con.createStatement();
			result = state.executeQuery("select distinct P0050004 from Migeo2010 INNER JOIN  SF1_00003 ON (Migeo2010.LOGRECNO = SF1_00003.LOGRECNO ) where NAME = \"Crystal Falls township\"");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	public ResultSet getTownships(){
		Connection con = InteractWithDatabase.getConnection();
		Statement state = null;
		ResultSet result = null;
		try {
			state = con.createStatement();
			result = state.executeQuery("select distinct NAME from Migeo2010 where NAME like '%township%'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;		
	}
}
