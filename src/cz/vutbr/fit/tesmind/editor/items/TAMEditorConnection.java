package cz.vutbr.fit.tesmind.editor.items;

import cz.vutbr.fit.tesmind.editor.TAMEditor;
import cz.vutbr.fit.testmind.graphics.ITAMConnection;

public class TAMEditorConnection {
	
	private int id;
	private TAMEditor editor;
	private ITAMConnection core;
	private TAMEditorNode parent;
	private TAMEditorNode child;
	private static int counter = 0;
	private static int defaultType = ITAMConnection.CONNECTION_TYPE_DEFAULT;
	
	public TAMEditorConnection(TAMEditor editor, TAMEditorNode parent, TAMEditorNode child, int type) {
		this(editor, parent, child, type, getnewSequenceNumber());
	}
	
	public TAMEditorConnection(TAMEditor editor, TAMEditorNode parent, TAMEditorNode child, int type, int id) {
		this.id = id;
		this.editor = editor;
		this.parent = parent;
		this.child = child;
		this.core = editor.getGraph().getItemFactory().createConnection(editor.getGraph(), parent.getCore(), child.getCore(), type);
	}
	
	public int getId() {
		return id;
	}
	
	public ITAMConnection getCore() {
		return core;
	}
	
	public TAMEditorNode getParent() {
		return parent;
	}
	
	public TAMEditorNode getChild() {
		return child;
	}

	public TAMEditor getEditor() {
		return editor;
	}

	public static int getnewSequenceNumber() {
		
		counter++;
		
		return counter;
	}

	public static int getDefaultType() {
		return defaultType;
	}

	public static void setDefaultType(int defaultConnectionType) {
		TAMEditorConnection.defaultType = defaultConnectionType;
	}
	
	

}
