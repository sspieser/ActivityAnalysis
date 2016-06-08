/**
 * 
 */
package parser;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author sebspi
 *
 */
public class Activity {
	public static enum ACTIVITY_TYPE {
		RUNNING,
		CYCLING,
		SKIMO,
		SWIMMING,
		OTHER
	};
	
	private ArrayList<GPXNode> nodes = null;
	private String type = "Other"; // TODO: liste de types...
	private Date start = null;
	private String name = null;

	public Activity(Date start, String name) {
		this.start = start;
		this.name = name;
	}
	public Activity(Date start, String name, String type) {
		this.start = start;
		this.name = name;
		this.type = type;
	}
	
	public ArrayList<GPXNode> getNodes() {
		return nodes;
	}
	/**
	 * Retourne le type de l'activite courante (generalement course a pied sinon autre...)
	 * 
	 * @return ACTIVITY_TYPE
	 */
	public ACTIVITY_TYPE getType() {
		return (defineType(this.type));
	}
	public Date getStart() {
		return this.start;
	}
	public String getName() {
		return this.name;
	}
	public void setNodes(ArrayList<GPXNode> nodes) {
		this.nodes = nodes;
	}
	
	/**
	 * Defini le type de l'activite a partir de chaines de caractere
	 * peut etre recuperees des TCX ou GPX...
	 * 
	 * @param type
	 * @return
	 */
	public static ACTIVITY_TYPE defineType(String type) {
		if (type != null) {
			switch (type) {
				case "Running": case "running": case "Course à pied": case "Course a pied": case "run": case "Run": 
					return ACTIVITY_TYPE.RUNNING;
				case "Biking": case "ride": case "Ride": case "biking":
					return ACTIVITY_TYPE.CYCLING;
				case "Swim": case "swim": case "Swimming":
					return ACTIVITY_TYPE.SWIMMING;
				case "backcountryski": case "Ski de randonnées": case "skimo": case "SKI DE RANDONNÉE NORDIQUE/SURF DES NEIGES":
					return ACTIVITY_TYPE.SKIMO;
				case "Other": case "other":
					return ACTIVITY_TYPE.OTHER;
				default:
					break;
			}
		}
		return ACTIVITY_TYPE.OTHER;
	}
	
	
}
