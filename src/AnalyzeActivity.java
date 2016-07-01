import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import parser.Activity;
import parser.GPXNode;

public class AnalyzeActivity {
	private HashMap<Integer, Zone>  defzones = null;
	private ArrayList<GPXNode> nodes;
	//private HashMap<Integer, Integer> results = new HashMap<Integer,Integer>();
	private Result result;
	
	class Result {
		private HashMap<Integer, Integer> results = new HashMap<Integer,Integer>();
		private Activity activity;
		
		Result(Activity activity) {
			this.activity = activity;
		}
		Date getStart() {
			return this.activity.getStart();
		}
		Date getDay() {
			final GregorianCalendar gc = new GregorianCalendar();
		    gc.setTime(this.activity.getStart());
		    gc.set(Calendar.HOUR_OF_DAY, 0);
		    gc.set(Calendar.MINUTE, 0);
		    gc.set(Calendar.SECOND, 0);
		    gc.set(Calendar.MILLISECOND, 0);
		    return gc.getTime();
		}
		String getDayString() {
			DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
			String formattedDate = df.format(getDay());
			return formattedDate;
		}
		HashMap<Integer, Integer> getResults() {
			return results;
		}
		void setResults(HashMap<Integer, Integer> results) {
			this.results = results;
		}
		void init(int count) {
			for (int i = 0; i < count; i++)
				this.results.put(i, 0);
		}
		/**
		 * retourne le total de temps en secondes de l'activite
		 * @return
		 */
		int getTotalTime() {
			int total = 0;
			for (int i = 0; i < this.results.size(); i++) {
				total += this.results.get(i);
			}
			return total;
		}
		// TODO
		public String toCSV() {
			StringBuffer sb = new StringBuffer();
	        
			sb.append(getDayString());
			for (int zidx = 0; zidx < this.results.size(); zidx++) {
				int timeInZone = this.results.get(zidx);
				int h = timeInZone / 60 / 60;
				int min = (timeInZone - h * 60 * 60) / 60;
				int sec = timeInZone - (min * 60 + h * 60 * 60);
				//double total = getTotalTime();
				sb.append(";\"" + h + ":" + min + ":" 
							+ sec + "\"");
			}
			sb.append("");
			
			return (sb.toString());			
		}
	}

	public AnalyzeActivity(Zones defzones, Activity activity) {
		if (activity != null) {
			this.nodes = activity.getNodes();			
			this.defzones = defzones.getZones(activity.getType()); // recupere les zones pour un type d'activite	
			this.result = new Result(activity);
			this.result.init(getZoneCount());
		}
	}

	/**
	 * Lance l'analyse de l'activite
	 */
	public void doAnalyze() {
		HashMap<Integer, ArrayList<Integer>> z = createZones();
		normalize(z, 10);
		tally(z);
	}
	
	public String toCSV() {
		return this.result.toCSV();
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
        DecimalFormat df = new DecimalFormat("##.#");
        
		for (int zidx = 0; zidx < getZoneCount(); zidx++) {
			int timeInZone = getHRZone(zidx);
			int min = timeInZone / 60;
			int sec = timeInZone - (min * 60);
			double total = this.result.getTotalTime();
			sb.append("Zone " + getZoneName(zidx) + ": " + min + "m, " 
						+ sec + "s. " + df.format(timeInZone / total * 100) + "%\n");
		}
		return (sb.toString());
	}

	/**
	 * Creates the zones that the data will fit in
	 * Should take in array of crucial points,
	 * where hr zone changes to next
	 */
	private HashMap<Integer, ArrayList<Integer>> createZones() {
		HashMap<Integer, ArrayList<Integer>> zx = new HashMap<Integer, ArrayList<Integer>>();
		GPXNode prev = null;
		
		// init des tableaux par zones, maintenant que l'on connait le nombre de zones
		for (int i = 0; i < getZoneCount(); i++) {
			zx.put(i, new ArrayList<Integer>());
			//results.put(i, 0);
		}
		
		for (int i = 0; i < nodes.size(); i ++) {
			GPXNode g = nodes.get(i);
			if (prev != null) {
				int target = findZone(g.getHR());
				if (dateDifference(g, prev) < 0) {
					// TODO: impossible !? a analyser!!
				} else {
					zx.get(target).add(dateDifference(g, prev));
				}
			}
			prev = g;
		}
		return zx;
	}
	/**
	 * Removes any data points that are greater than the threshold;
	 * threshold = length of time in between data points;
	 * if it is beyond this threshold, it is likely the function
	 * is not continuous i.e. paused Garmin
	 * @param zx
	 * @param threshold in secs
	 */
	private HashMap<Integer, ArrayList<Integer>> normalize(HashMap<Integer, ArrayList<Integer>> zx, int threshold) {
		//int removed = 0;
		for (int zidx = 0; zidx < getZoneCount(); zidx++) {
			for (int i = 0; i < zx.get(zidx).size(); i++) {
				if (zx.get(zidx).get(i) > threshold) {
					zx.get(zidx).remove(i);
					//removed++;
				}
			}
		}
		//System.err.println("** removed " + removed + " point(s)");
		return zx;
	}
	/*
	 * Adds the seconds collected differences by differences to each zone
	 */
	private void tally(HashMap<Integer, ArrayList<Integer>> zx) {
		for (int thezone = 0; thezone < getZoneCount(); thezone++) {
			for(int i = 0; i < zx.get(thezone).size(); i++) {
				int tmp = getHRZone(thezone);
				tmp += zx.get(thezone).get(i);
				setHRResult(thezone,  tmp);
			}
		}
	}
	private int getHRZone(int zone) {
		return this.result.getResults().get(zone);
	}
	private void setHRResult(int zone, int hr) {
		this.result.getResults().put(zone, hr);
	}
	private int dateDifference(GPXNode a, GPXNode b){	
		return (int) ((a.getTime().getTime() - b.getTime().getTime())/1000);
	}
	
	private int findZone(int hr) {
		for (int i = 0; i < getZoneCount(); i++) {
			if (i == 0) {
				if (hr <= getZoneMax(i)) { 
					return i;
				}
			} else if (hr > getZoneMax(i - 1)
					&& hr <= getZoneMax(i)) { 
				return i;				
			}
		}
		return -1; // ne doit pas arriver TODO
	}
	private String getZoneName(int zidx) {
		return this.defzones.get(zidx).getName();
	}
	private int getZoneMax(int zidx) {
		return this.defzones.get(zidx).getMax();
	}
	private int getZoneCount() {
		return this.defzones.size();
	}
}
