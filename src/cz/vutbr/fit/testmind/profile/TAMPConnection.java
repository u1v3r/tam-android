package cz.vutbr.fit.testmind.profile;

import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.editor.items.ITAMEConnection;

public class TAMPConnection extends TAMPItem {
	
	private int id;
	private TAMPNode parent;
	private TAMPNode child;
	
	private static int counter = 0;
	
	public TAMPConnection(TAMPNode parent, TAMPNode child) {
		this(parent, child, getNewSequenceNumber());
	}
	
	public TAMPConnection(TAMPNode parent, TAMPNode child, int id) {
		this.id = id;
		this.parent = parent;
		this.child = child;
	}
	
	public int getId() {
		return id;
	}

	public TAMPNode getParent() {
		return parent;
	}

	public TAMPNode getChild() {
		return child;
	}

	public static int getNewSequenceNumber() {
		
		counter++;
		
		return counter;
	}
	
	protected static void resetSequenceNumber() {
		counter = 0;
	}

	public void dispose() {
		super.dispose();
		
		parent = null;
		child = null;
	}
	
	public boolean connectsSomething(ITAMEditor editor) {
		
		return (parent.getEReference(editor) != null && child.getEReference(editor) != null);
	}
	
	public ITAMEConnection addEReference(ITAMEditor editor) {
		if(connectsSomething(editor) && !editorReferences.containsKey(editor)) {
			ITAMEConnection eConnection = editor.createConnection(this);
			editorReferences.put(editor, eConnection);
			return eConnection;
		} else {
			return null;
		}
	}
	
	public ITAMEConnection addEReference(ITAMEditor editor, int type) {
		if(connectsSomething(editor) && !editorReferences.containsKey(editor)) {
			ITAMEConnection eConnection = editor.createConnection(this, type);
			editorReferences.put(editor, eConnection);
			return eConnection;
		} else {
			return null;
		}
	}

}
