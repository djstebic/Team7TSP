import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Statistics {
	public String getTotalPop(){
	String query = "select distinct POP100 from Migeo2010 where NAME =";
	return query;
	}
	
	public String getWhitePop(){
		return  "select distinct P0030002 from Migeo2010 INNER JOIN  SF1_00003 ON (Migeo2010.LOGRECNO = SF1_00003.LOGRECNO ) where NAME =";
	
	}
	
//		public String getAfricanPop(){
//			String query = "select distinct P0050004 from Migeo2010 INNER JOIN SF1_00003 ON (Migeo2010.LOGRECNO = SF1_00003.LOGRECNO ) where NAME =";
//			return query;
//		}
	public String getAfricanPop(){
		return "select distinct P0030003 from Migeo2010 INNER JOIN  SF1_00003 ON (Migeo2010.LOGRECNO = SF1_00003.LOGRECNO ) where NAME =";
	}
	public String getAsianPop(){
		return "select distinct P0030005 from Migeo2010 INNER JOIN  SF1_00003 ON (Migeo2010.LOGRECNO = SF1_00003.LOGRECNO ) where NAME =";
	}
	
	public String getAmericanIndianPop(){
		return "select distinct P0030004 from Migeo2010 INNER JOIN  SF1_00003 ON (Migeo2010.LOGRECNO = SF1_00003.LOGRECNO ) where NAME =";
	}
	
	public String getNativeHawaiinPop(){
		return "select distinct P0030006 from Migeo2010 INNER JOIN  SF1_00003 ON (Migeo2010.LOGRECNO = SF1_00003.LOGRECNO ) where NAME =";
	}
	
	public String getotherRacePop(){
		return "select distinct P0030007 from Migeo2010 INNER JOIN  SF1_00003 ON (Migeo2010.LOGRECNO = SF1_00003.LOGRECNO ) where NAME =";
	}
	
	public String get2orMoreRacePop(){
		return "select distinct P0030008 from Migeo2010 INNER JOIN  SF1_00003 ON (Migeo2010.LOGRECNO = SF1_00003.LOGRECNO ) where NAME =";
	}
	
	public String getAllPop(){
		return "select distinct P0030002, P0030003, P0030004, P0030005, P0030006,P0030007, P0030008 from Migeo2010 INNER JOIN  SF1_00003 ON (Migeo2010.LOGRECNO = SF1_00003.LOGRECNO ) where NAME =";
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
	
	public String getMedianAgebySex(){
		return "SELECT distinct P0130001, P0130002, P0130003 from Migeo2010 inner join SF1_00004 on (Migeo2010.LOGRECNO = SF1_00004.LOGRECNO ) where NAME =";
	}
	
	
	public ResultSet runQuery(String query, String location){
		Connection con = InteractWithDatabase.getConnection();
		Statement state = null;
		ResultSet result = null;
		try {
			state = con.createStatement();
			query = query + " \"" + location + "\"";
			result = state.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
}
