import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;

import parser.Activity;
import parser.Activity.ACTIVITY_TYPE;

/**
 * 
 */

/**
 * @author sebspi
 *
 */
public class Zones {
	private Hashtable<ACTIVITY_TYPE, HashMap<Integer, Zone>> zones = new Hashtable<ACTIVITY_TYPE, HashMap<Integer, Zone>>();
		
	public Zones() {
	}
	// TODO error mgt!!
	public void init() {
		loadConfig();
	}
	
	public HashMap<Integer, Zone> getZones(ACTIVITY_TYPE type) {
		HashMap<Integer, Zone> zone = this.zones.get(type);
		if (zone == null) {
			return  this.zones.get(ACTIVITY_TYPE.RUNNING); // TODO: exception si pas de zones pour Running!
		} else {
			return zone;
		}
	}
	
	private void loadConfig() {
		InputStream  inputStream = null;
		
		try {
			Properties prop = new Properties();
			 inputStream = getClass().getClassLoader().getResourceAsStream("properties/zones.properties");
			if (inputStream != null) {
				prop.load(inputStream);
				inputStream.close();
			} else {
				throw new FileNotFoundException("property file '" + "properties/zones.properties" + "' not found in the classpath");
			}
			
			// recup des types de zones 
			String zonetypes = prop.getProperty("zone.type");
			zonetypes = zonetypes.trim();
			String[] array = zonetypes.split(",");
			for (int i = 0; i < array.length; i++) {
				HashMap<Integer, Zone> zonesByType = new HashMap<Integer, Zone>();
				String type = array[i].trim();
				String strnumzones = prop.getProperty(type + ".num.zones");
				int numzones = Integer.parseInt(strnumzones);
				for (int j = 0; j < numzones; j++) {
					String name = prop.getProperty(type + ".z" + (j + 1) + ".name");
					String max = prop.getProperty(type + ".z" + (j + 1) + ".max");
					Zone zone = new Zone(type, name, Integer.parseInt(max));
					zonesByType.put(new Integer(j), zone);
				}
				zones.put(Activity.defineType(type), zonesByType);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace(); // TODO
		} finally {
			try { inputStream.close(); } catch (Exception e) {}
		}
		
	}
}

