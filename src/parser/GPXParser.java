package parser;
import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GPXParser implements GenericParser {
	private String XMLTAG_TIME = "time";
	private String XMLTAG_HR = "gpxtpx:hr";
	/**
	 * filename to parse
	 */
	private String activity;

	/**
	 * constructor
	 */
	public GPXParser(String activity) {
		this.activity = activity;
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<GPXNode> runParse() {
		ArrayList<GPXNode> gNodeList = new ArrayList<GPXNode>();
		try {
			File xml = new File(this.activity);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xml);
		
			NodeList hrList = doc.getElementsByTagName(XMLTAG_HR);
			NodeList timeList = doc.getElementsByTagName(XMLTAG_TIME);
	        SimpleDateFormat sdf = new SimpleDateFormat(DATEPATTERN);
			
			for (int i = 0; i < hrList.getLength(); i++) {
				NodeList hList = hrList.item(i).getChildNodes();
				NodeList tList = timeList.item(i+1).getChildNodes();
				for (int j = 0; j < hList.getLength(); j++) {
					String t = parse(tList.item(j).toString());
					int h = Integer.parseInt(parse(hList.item(j).toString()));
					gNodeList.add(new GPXNode(sdf.parse(t), h));
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return gNodeList;
	}	

	@Override
	public Activity readHeader() {
		SimpleDateFormat sdf = new SimpleDateFormat(DATEPATTERN);
		String name = null;
		Date start;
		
		try {
			File xml = new File(this.activity);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xml);
		
			NodeList hrList = doc.getElementsByTagName("metadata");
			NodeList nodes = hrList.item(0).getChildNodes();
			Node node = nodes.item(0);
			String time = node.getNextSibling().getTextContent();
			start = (Date) sdf.parse(time);
			
			hrList = doc.getElementsByTagName("trk");
			nodes = hrList.item(0).getChildNodes();
			node = nodes.item(0);
			name = node.getNextSibling().getTextContent();
			return new Activity(start, name);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param t
	 * @return
	 */
	private String parse(String t){
		boolean read = false;
		String temp = "";
		for (char c : t.toCharArray()){
			if(c == ' ')
				read = true;
			else if (c == ']')
				read = false;
			else if (read)
				temp = temp + c;
		}
		return temp;
	}

		
}
