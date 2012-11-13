package cz.vutbr.fit.testmind.profile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TAMPNode extends TAMPItem implements Serializable{
	
	private static final long serialVersionUID = 8313169869686450190L;
	
	private int id;
	private String title;
	private String body;
	protected List<TAMPNode> listOfChildNodes;
	protected TAMPNode parent;
	
	private static int counter = 0;
	
	public TAMPNode(String title, String htmlBody) {
		this(title, htmlBody, getNewSequenceNumber());
	}
	
	public TAMPNode(String title, String htmlBody, int id) {		
		this.id = id;
		setTitle(title);
		setBody(htmlBody);
		listOfChildNodes = new ArrayList<TAMPNode>();
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
		
	public void setBody(String htmlBody){
		this.body = htmlBody;
	}
	
	public int getId() {
		return id;
	}
	
	public List<TAMPNode> getListOfChildNodes() {
		return listOfChildNodes;
	}	
	
	public TAMPNode getParent() {
		return parent;
	}

	protected void setParent(TAMPNode parent) {
		this.parent = parent;
	}

	public static int getNewSequenceNumber() {
		
		counter++;
		
		return counter;
	}
	
	public static int getCounter() {
		return counter;
	}

	protected static void resetSequenceNumber(int counter) {
		TAMPNode.counter = counter;
	}

	public void dispose() {
		super.dispose();
		
		if(parent != null) {
			parent.listOfChildNodes.remove(this);
		}
		
		listOfChildNodes.clear();
		parent = null;
	}
/*	
	public ITAMENode addEReference(ITAMEditor editor, int x, int y) {
		if(!editorReferences.containsKey(editor)) {
			ITAMENode eNode = editor.createNode(this, x, y);
			editorReferences.put(editor, eNode);
			return eNode;
		} else {
			return null;
		}
	}
	
	public ITAMENode addEReference(ITAMEditor editor, int x, int y, int type) {
		if(!editorReferences.containsKey(editor)) {
			ITAMENode eNode = editor.createNode(this, x, y, type);
			editorReferences.put(editor, eNode);
			return eNode;
		} else {
			return null;
		}
	}
*/
	@Override
	public String toString() {
		return "TAMPNode [id=" + id + ", title=" + title + ", body=" + body
				+ ", parent=" + parent + "]";
	}
	

}
