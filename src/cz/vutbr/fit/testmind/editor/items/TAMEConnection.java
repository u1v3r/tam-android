package cz.vutbr.fit.testmind.editor.items;

import cz.vutbr.fit.testmind.editor.TAMEditor;
import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.graphics.ITAMGConnection;

public class TAMEConnection {
	
	private int id;
	private ITAMEditor editor;
	private ITAMGConnection core;
	private TAMENode parent;
	private TAMENode child;
	private static int counter = 0;
	private static int defaultType = ITAMGConnection.CONNECTION_TYPE_DEFAULT;
	
	public TAMEConnection(TAMEditor editor, TAMENode parent, TAMENode child, int type) {
		this(editor, parent, child, type, getnewSequenceNumber());
	}
	
	public TAMEConnection(TAMEditor editor, TAMENode parent, TAMENode child, int type, int id) {
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
	
	public ITAMGConnection getCore() {
		return core;
	}
	
	public TAMENode getParent() {
		return parent;
	}
	
	public TAMENode getChild() {
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
		TAMEConnection.defaultType = defaultConnectionType;
	}
	
	

}
