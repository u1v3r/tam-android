package cz.vutbr.fit.testmind.profile;

import java.io.Serializable;

import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.editor.items.ITAMEConnection;

public class TAMPConnection extends TAMPItem implements Serializable {
	
	private static final long serialVersionUID = 7589667439972717917L;
	
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
		if(parent != null) {
			parent.listOfChildNodes.add(child);
		}
		child.parent = parent;
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
	
	protected static void resetSequenceNumber(int counter) {
		TAMPConnection.counter = counter;
	}

	public static int getCounter() {
		return counter;
	}

	public void dispose() {
		super.dispose();
		
		parent = null;
		child = null;
	}
}
