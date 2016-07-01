package parser;

import java.util.ArrayList;

/**
 * @author sebspi
 *
 */
public abstract class ParserHelper {

	/**
	 * Factory parser
	 * @param activity
	 * @return
	 */
	public static GenericParser getParser(String activity) {
		String ext = getFileExtension(activity);
		switch (ext) {
			case "gpx":
			case "GPX":
				return new GPXParser(activity);
		case "tcx":
		case "TCX":
				return new TCXParser(activity);
		default :
				return null;
		}
	}
	/**
	 * 
	 * @return
	 * @throws GPXParserException 
	 */
	public static Activity getActivity(GenericParser parser) throws GPXParserException {		
		Activity activity = parser.readHeader();
		if (activity != null) {
			ArrayList<GPXNode>nodes = parser.runParse();
			activity.setNodes(nodes);
		}
		return activity;
	}
	
	private static String getFileExtension(String file) {
	    try {
	        return file.substring(file.lastIndexOf(".") + 1);
	    } catch (Exception e) {
	        return "";
	    }
	}	
}
