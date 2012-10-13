package cz.vutbr.fit.testmind.editor.items;

import cz.vutbr.fit.testmind.editor.TAMEditor;
import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.graphics.ITAMConnection;

public class TAMEditorConnection {
	
	private int id;
	private ITAMEditor editor;
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
		this.core = editor.getItemFactory().createConnection(editor, parent.getCore(), child.getCore(), type);
		this.core.setHelpObject(this);
		this.parent.getListOfChildNodes().add(child);
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

	public ITAMEditor getEditor() {
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
