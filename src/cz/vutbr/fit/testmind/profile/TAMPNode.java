package cz.vutbr.fit.testmind.profile;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import android.text.SpannableString;

import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.editor.items.ITAMENode;

public class TAMPNode extends TAMPItem {
	
	private int id;
	private String title;
	private SpannableString body;
	protected List<TAMPNode> listOfChildNodes;
	protected TAMPNode parent;
	
	private static int counter = 0;
	
	public TAMPNode(String title, SpannableString body) {
		this(title, body, getNewSequenceNumber());
	}
	
	public TAMPNode(String title, byte[] body) {
		this(title, body, getNewSequenceNumber());
	}
	
	public TAMPNode(String title, SpannableString body, int id) {
		this.id = id;
		setTitle(title);
		setBody(body);
		listOfChildNodes = new ArrayList<TAMPNode>();
	}
	
	public TAMPNode(String title, byte[] body, int id) {
		this.id = id;
		setTitle(title);
		SpannableString bodyImport = new SpannableString(new String(body,Charset.forName("UTF-8")));
		setBody(bodyImport);
		listOfChildNodes = new ArrayList<TAMPNode>();
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public SpannableString getBody() {
		return body;
	}
	
	private void setBody(String body) {		
		this.body = new SpannableString(body);
	}
	
	public void setBody(SpannableString body){
		this.body = body;
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

	@Override
	public String toString() {
		return "TAMPNode [id=" + id + ", title=" + title + ", body=" + body
				+ ", parent=" + parent + "]";
	}
	

}
