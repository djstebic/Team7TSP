import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Statistics {
	public String getTotalPop() {
		String query = "select distinct POP100 from Migeo2010 where NAME =";
		return query;
	}

	public String getWhitePop() {
		return "select distinct P0030002 from Migeo2010 INNER JOIN  SF1_00003 ON (Migeo2010.LOGRECNO = SF1_00003.LOGRECNO ) where NAME =";

	}
	// public String getAfricanPop(){
	// String query = "select distinct P0050004 from Migeo2010 INNER JOIN
	// SF1_00003 ON (Migeo2010.LOGRECNO = SF1_00003.LOGRECNO ) where NAME =";
	// return query;
	// }
	public String getAfricanPop() {
		return "select distinct P0030003 from Migeo2010 INNER JOIN  SF1_00003 ON (Migeo2010.LOGRECNO = SF1_00003.LOGRECNO ) where NAME =";
	}

	public String getAsianPop() {
		return "select distinct P0030005 from Migeo2010 INNER JOIN  SF1_00003 ON (Migeo2010.LOGRECNO = SF1_00003.LOGRECNO ) where NAME =";
	}

	public String getAmericanIndianPop() {
		return "select distinct P0030004 from Migeo2010 INNER JOIN  SF1_00003 ON (Migeo2010.LOGRECNO = SF1_00003.LOGRECNO ) where NAME =";
	}

	public String getNativeHawaiinPop() {
		return "select distinct P0030006 from Migeo2010 INNER JOIN  SF1_00003 ON (Migeo2010.LOGRECNO = SF1_00003.LOGRECNO ) where NAME =";
	}

	public String getotherRacePop() {
		return "select distinct P0030007 from Migeo2010 INNER JOIN  SF1_00003 ON (Migeo2010.LOGRECNO = SF1_00003.LOGRECNO ) where NAME =";
	}

	public String get2orMoreRacePop() {
		return "select distinct P0030008 from Migeo2010 INNER JOIN  SF1_00003 ON (Migeo2010.LOGRECNO = SF1_00003.LOGRECNO ) where NAME =";
	}

	public String getMedianHouseholdIncome() {
		return "select distinct P053001 as [Median Household \nIncome in 1999] from Migeo inner join Mi00006 on (Migeo.LOGRECNO = Mi00006.LOGRECNO) where NAME =";
	}

	public String getAllPop() {
		return "select distinct NAME, P0030002 as [White \nPopulation], P0030003 as [Black \nPopulation], P0030004 as [Indian \nPopulation], P0030005 as [Asian \nPopulation], P0030006 as [Hawaiian \nPopulation],P0030007 as [Other Race \nPopulation], P0030008 as [Multi-Race \nPopulation] from Migeo2010 INNER JOIN  SF1_00003 ON (Migeo2010.LOGRECNO = SF1_00003.LOGRECNO ) where NAME =";
	}

	public String getPopandMedAge() {
		return "SELECT distinct NAME, P0130001 as [Median Age], P0130002 as [Median Age \nMale], P0130003 as [Median Age \nFemale], POP100 as [Total \nPopulation], P0030002 as [White \nPopulation], P0030003 as [Black \nPopulation], P0030004 as [Indian \nPopulation], P0030005 as [Asian \nPopulation], P0030006 as [Hawaiian \nPopulation], P0030007  as [Other Race \nPopulation], P0030008 as [Multi-Race \nPopulation] from (Migeo2010 inner join SF1_00003 on (Migeo2010.LOGRECNO = SF1_00003.LOGRECNO)) inner join SF1_00004 on (Migeo2010.LOGRECNO = SF1_00004.LOGRECNO ) where NAME =";
	}

	public String getMedianAgebySex() {
		return "SELECT distinct NAME, P0130001 as [Median Age], P0130002 as [Median Age \nMale], P0130003 as [Median Age \nFemale] from Migeo2010 inner join SF1_00004 on (Migeo2010.LOGRECNO = SF1_00004.LOGRECNO ) where NAME =";
	}
	
	public String getAggregateHouseholdIncome(){
		return "select distinct P054001 as [Aggregate Household \nIncome in 1999] from Migeo inner join Mi00006 on (Migeo.LOGRECNO = Mi00006.LOGRECNO) where NAME =";
	}
	
	public String getTotalHouseholds(){
		return "select distinct P0280001 as [Total \nHouseholds] from Migeo2010 inner join SF1_00005 on (Migeo2010.LOGRECNO = SF1_00005.LOGRECNO) where NAME =";
	}
	public String getAll(){
		return "SELECT distinct Migeo2010.NAME, P0130001 as [Median Age], P0130002 as [Median Age \nMale], P0130003 as [Median Age \nFemale],P053001 as [Median Income], POP100 as [Total \nPopulation], P0030002 as [White \nPopulation], P0030003 as [Black \nPopulation], P0030004 as [Indian \nPopulation], P0030005 as [Asian \nPopulation], P0030006 as [Hawaiian \nPopulation], P0030007  as [Other Race \nPopulation], P0030008 as [Multi-Race \nPopulation], P0280001 as [Total \nHouseholds] from ((((Migeo2010 inner join SF1_00004 on (Migeo2010.LOGRECNO = SF1_00004.LOGRECNO)) inner join Migeo on (Migeo2010.NAME = Migeo.NAME))inner join Mi00006 on (Migeo.LOGRECNO = Mi00006.LOGRECNO)) inner join SF1_00003 on (Migeo2010.LOGRECNO = SF1_00003.LOGRECNO)) inner join SF1_00005 on (Migeo2010.LOGRECNO = SF1_00005.LOGRECNO) where Migeo2010.NAME =";
	}
	

	public ResultSet getTownships() {
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

	public ResultSet runQuery(String query, String location) {
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
	
	public Stat getAllData(String query, String location) throws SQLException {
		Connection con = InteractWithDatabase.getConnection();

		List<List<Object>> stats = new ArrayList<>();
		List<String> columnNames = new ArrayList<>();

		query = query + " \"" + location + "\"";

		try (Statement state = con.createStatement(); ResultSet result = state.executeQuery(query)) {

			int columnCount = result.getMetaData().getColumnCount();

			for (int i = 1; i <= columnCount; i++) {
				columnNames.add(result.getMetaData().getColumnLabel(i));
			}

			while (result.next()) {
				List<Object> row = new ArrayList<>();
				for (int i = 1; i <= columnCount; i++) {
					row.add(result.getObject(i));
				}
				stats.add(row);
			}

		}

		return new Stat(columnNames, stats);
	}

}
