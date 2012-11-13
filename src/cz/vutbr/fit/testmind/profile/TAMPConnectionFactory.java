package cz.vutbr.fit.testmind.profile;

import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.editor.items.ITAMEConnection;
import cz.vutbr.fit.testmind.editor.items.ITAMENode;

public class TAMPConnectionFactory {
	
	private TAMPConnectionFactory(){}
	
	public static final ITAMENode addEReference(TAMPNode node, ITAMEditor editor, int x, int y){
		
		if(!node.getEditorReferences().containsKey(editor)) {
			ITAMENode eNode = editor.createNode(node, x, y);
			node.getEditorReferences().put(editor, eNode);
			return eNode;
		} else {
			return null;
		}
	}
		
	public static final ITAMENode addEReference(TAMPNode node, ITAMEditor editor, int x, int y, int type) {
		
		if(!node.getEditorReferences().containsKey(editor)) {
			ITAMENode eNode = editor.createNode(node, x, y, type);
			node.getEditorReferences().put(editor, eNode);
			return eNode;
		} else {
			return null;
		}
	}	
	
	public static final boolean connectsSomething(TAMPNode parent, TAMPNode child, ITAMEditor editor) {
		
		return (parent.getEReference(editor) != null && child.getEReference(editor) != null);
	}
	
	public static final ITAMEConnection addEReference(TAMPConnection connection, ITAMEditor editor) {
		
		boolean connect = TAMPConnectionFactory.connectsSomething(connection.getParent(), connection.getChild(), editor);
		
		if(connect && !connection.getEditorReferences().containsKey(editor)) {
			ITAMEConnection eConnection = editor.createConnection(connection);
			connection.getEditorReferences().put(editor, eConnection);
			return eConnection;
		} else {
			return null;
		}
	}
	
	public static final ITAMEConnection addEReference(TAMPConnection connection, ITAMEditor editor, int type) {
		
		boolean connect = TAMPConnectionFactory.connectsSomething(connection.getParent(), connection.getChild(), editor);
		
		if(connect && !connection.getEditorReferences().containsKey(editor)) {
			ITAMEConnection eConnection = editor.createConnection(connection,type);
			connection.getEditorReferences().put(editor, eConnection);
			return eConnection;
		} else {
			return null;
		}
	}
}
