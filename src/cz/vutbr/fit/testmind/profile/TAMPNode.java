package cz.vutbr.fit.testmind.profile;

import java.util.List;

public class TAMPNode {
	
	private int id;
	private String title;
	private String body;
	private List<TAMPNode> listOfChildNodes;
	
	public TAMPNode(String title, String body) {
		
	}
	
	public TAMPNode(String title, String body, int id) {
		this.id = id;
		setTitle(title);
		setBody(body);
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getBody() {
		return body;
	}
	
	public void setBody(String body) {
		this.body = body;
	}
	
	public int getId() {
		return id;
	}
	
	public List<TAMPNode> getListOfChildNodes() {
		return listOfChildNodes;
	}
	
	

}
