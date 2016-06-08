import parser.Activity;
import parser.GenericParser;
import parser.ParserHelper;

public class Main {

	public static void main(String[] args) {
		
		String[] files = {
				"deblocage_caniculaire_avant_la_pluie_du_lendemain.gpx", 
				"activity_1192243211.tcx", 
				"2016-06-04T09-31-29.tcx"
		};

		for (String file: files) {
			
			GenericParser g = ParserHelper.getParser(file);		
			Activity activity = ParserHelper.getActivity(g);
			
			Zones zonesmgr = new Zones();
			zonesmgr.init();
			
			Analyze a = new Analyze(zonesmgr, activity);
			
			a.doAnalyze();
			System.out.println(a.toCSV());
		}
		
	    /*JFrame window = new JFrame();
	    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    window.setBounds(30, 30, 600, 600);
	    window.setTitle("Heart Rate Data");
	    window.getContentPane().add(new Swing(a.routine()));
	    window.setVisible(true);*/
	}

}
