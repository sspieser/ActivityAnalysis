package parser;

import java.util.ArrayList;

/**
 * 
 * @author sebspi
 *
 */
public abstract interface GenericParser {
	//protected static String XMLTAG_TIME = "time";
	//protected static String XMLTAG_HR = "gpxtpx:hr"; // default to GPX
	final static String DATEPATTERN = "yyyy-MM-dd'T'hh:mm:ss";
		
	/**
	 * Parse le fichier et construit la liste de noeuds
	 * @param t
	 * @return
	 */
	abstract ArrayList<GPXNode> runParse();

	/**
	 * Lecture des entetes de l'activite
	 * 
	 * @return
	 * @throws GPXParserException 
	 */
	abstract Activity readHeader() throws GPXParserException;
	
}
