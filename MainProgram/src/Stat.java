import java.util.List;

import javafx.beans.property.SimpleStringProperty;

/*
 * Object to define table values.
 */
public class Stat {

	private final List<String> columnNames;
	private final List<List<Object>> stat;

	public Stat(List<String> columnNames, List<List<Object>> stat) {
		this.columnNames = columnNames;
		this.stat = stat;
	}

	public int getNumColumns() {
		return columnNames.size();
	}

	public String getColumnName(int index) {
		return columnNames.get(index);
	}

	public Object getStat(int column, int row) {
		return stat.get(row).get(column);
	}

	public List<List<Object>> getStat() {
		return stat;
	}


}
