import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Statistics {
	public ResultSet getTotalPop(){
		Connection con = InteractWithDatabase.getConnection();
		Statement state = null;
		ResultSet result = null;
		try {
			state = con.createStatement();
			result = state.executeQuery("select distinct POP100 from Migeo2010 where NAME = \"Crystal Falls township\"");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
