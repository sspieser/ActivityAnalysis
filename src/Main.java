import parser.Activity;
import parser.GenericParser;
import parser.ParserHelper;

public class Main {

	public static void main(String[] args) {
		
			String[] files = {
					"deblocage_caniculaire_avant_la_pluie_du_lendemain.gpx", 
					"activity_1192243211.tcx", 
					"2016-06-04T09-31-29.tcx",
					"1358090.gpx",
					"visugpx_1409644824.gpx",
					"La Belle Etoile 2016, race.gpx"
			};
			if (args == null || args.length == 0) args = files;
	
			for (String file: args) {
				
				try {
					GenericParser g = ParserHelper.getParser(file);		
					Activity activity = ParserHelper.getActivity(g);
					
					if (activity != null) {
						Zones zonesmgr = new Zones();
						zonesmgr.init();
						
						AnalyzeActivity a = new AnalyzeActivity(zonesmgr, activity);
						
						a.doAnalyze();
						System.out.println(a.toCSV());
						//System.out.println(a.toString());
					} else {
						System.err.println(" * Pas d'activite, file \"" + file.toString());
					}
				} catch (Exception e) {
					System.err.println("** ERROR, file \"" + file.toString() + "\": " + e.getMessage());
				}
			}
		
	    /*JFrame window = new JFrame();
	    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    window.setBounds(30, 30, 600, 600);
	    window.setTitle("Heart Rate Data");
	    window.getContentPane().add(new Swing(a.routine()));
	    window.setVisible(true);*/
	}

}
