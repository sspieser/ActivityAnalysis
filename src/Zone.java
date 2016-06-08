import parser.Activity;
import parser.Activity.ACTIVITY_TYPE;

public class Zone {
	public ACTIVITY_TYPE getType() {
		return type;
	}
	private String name;
	private int max;
	private ACTIVITY_TYPE type;

	Zone (String type, String name, int max) {
		this.type = Activity.defineType(type);
		this.name = name;
		this.max = max;
	}		
	Zone (ACTIVITY_TYPE type, String name, int max) {
		this.type = type;
		this.name = name;
		this.max = max;
	}
	public int getMax() {
		return this.max;
	}
	public String getName() {
		return this.name;
	}
}
