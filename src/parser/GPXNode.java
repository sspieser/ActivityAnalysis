package parser;
import java.util.Date;


public class GPXNode {

	
	private Date time;
	private int hr;

	public GPXNode() {
		time = null;
	}
	public GPXNode(Date time, int hr){
		this.time = time;
		this.hr = hr;
	}
	
	public void printNode(){
		System.out.println("time: "+ time+" hr: "+ hr);
	}
		
	public Date getTime(){ return time; }
	public int getHR(){	return hr;	}
}
