package cz.vutbr.fit.tesmind.editor.items;

import java.util.ArrayList;
import java.util.List;

import cz.vutbr.fit.tesmind.editor.TAMEditor;
import cz.vutbr.fit.testmind.graphics.ITAMConnection;
import cz.vutbr.fit.testmind.graphics.ITAMNode;

public class TAMEditorNode {
	
	private int id;
	private TAMEditor editor;
	private ITAMNode core;
	private String body;
	private List<TAMEditorNode> listOfChildNodes;
	private static int counter = 0;
	private static int defaultType = ITAMConnection.CONNECTION_TYPE_DEFAULT;
	
	public TAMEditorNode(TAMEditor editor, int x, int y, String title, String body, int type) {
		this(editor, x, type, title, body, type, getNewSequenceNumber());
	}
	
	public TAMEditorNode(TAMEditor editor, int x, int y, String title, String body, int type, int id) {
		this.id = id;
		this.editor = editor;
		this.body = body;
		this.listOfChildNodes = new ArrayList<TAMEditorNode>();
		this.core = editor.getGraph().addRoot(type, x, y, title);
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public ITAMNode getCore() {
		return core;
	}

	public int getId() {
		return id;
	}

	public TAMEditor getEditor() {
		return editor;
	}

	public static int getNewSequenceNumber() {
		
		counter++;
		
		return counter;
	}

	public List<TAMEditorNode> getListOfChildNodes() {
		return listOfChildNodes;
	}
	
	public TAMEditorNode addChild(int x, int y, String title, String body) {
		
		return addChild(x, y, title, body, getDefaultType(), TAMEditorConnection.getDefaultType());
	}
	
	public TAMEditorNode addChild(int x, int y, String title, String body, int nodeType, int connectionType) {
		
		TAMEditorNode node = editor.getFactory().createNode(x, y, title, body, nodeType);
		editor.getFactory().createConnection(this, node, connectionType);
		listOfChildNodes.add(node);
		
		return node;
	}

	public static int getDefaultType() {
		return defaultType;
	}

	public static void setDefaultType(int defaultType) {
		TAMEditorNode.defaultType = defaultType;
	}

}
