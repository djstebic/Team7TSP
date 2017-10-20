import javafx.beans.property.SimpleStringProperty;

/*
 * Object to define table values.
 */
public class Stat {
	
	private final SimpleStringProperty stat;
	
	Stat(String s) {
		this.stat = new SimpleStringProperty(s);
	}
	
	public String getStat() {
		return stat.get();
	}
	
	public void setStat(String s) {
		stat.set(s);
	}
	
	
}
