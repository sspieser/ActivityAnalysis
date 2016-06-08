package parser;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TCXParser implements GenericParser {
	private String XMLTAG_HRVALUE = "Value";
	private String XMLTAG_TIME = "Time";
	private String XMLTAG_HR = "HeartRateBpm";
	/**
	 * filename to parse
	 */
	private String activity;
	
	public TCXParser(String activity) {
		this.activity = activity;
	}
	/**
	 * 
	 * @return
	 */
	@Override
	public ArrayList<GPXNode> runParse() {
		ArrayList<GPXNode> gNodeList = new ArrayList<GPXNode>();
		
		try {
			File xml = new File(this.activity);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xml);
		
			NodeList hrList = doc.getElementsByTagName(XMLTAG_HR);
	        SimpleDateFormat sdf = new SimpleDateFormat(DATEPATTERN);
			
	        for (int s = 0; s < hrList.getLength() ; s++) {
	        	  Element element = (Element) hrList.item(s);
	        	  NodeList name = element.getElementsByTagName(XMLTAG_HRVALUE);
	        	  Element line = (Element) name.item(0);
	        	  int h = Integer.parseInt(line.getFirstChild().getTextContent());
	        	  
	        	  NodeList sibling = element.getParentNode().getChildNodes();
	        	  for (int i = 0; i < sibling.getLength(); i++) {
	        		  Node element2 = (Node) sibling.item(i);
	        		  if (element2.getNodeName() == XMLTAG_TIME) {
	        			  String t = element2.getTextContent();
	        			  gNodeList.add(new GPXNode(sdf.parse(t), h));
	        		  }
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
		String type = null;
		
		try {
			File xml = new File(this.activity);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xml);
		
			NodeList hrList = doc.getElementsByTagName("Activity");
			for (int s = 0; s < hrList.getLength() ; s++) {
				Element element = (Element) hrList.item(s);
				type = element.getAttribute("Sport");
				NodeList nodes = element.getChildNodes();
				for (int i = 0; i < nodes.getLength(); i++) {
					Node node = (Node) nodes.item(i);
					if (node.getNodeName() == "Id") {
						name = node.getTextContent();
					}
					if (node.getNodeName() == "Lap") {
						start = sdf.parse(node.getAttributes().getNamedItem("StartTime").getTextContent());
						
						return new Activity(start, name, type);
					}					
				}				
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
