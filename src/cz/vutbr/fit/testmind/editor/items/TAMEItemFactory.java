package cz.vutbr.fit.testmind.editor.items;

import cz.vutbr.fit.testmind.editor.TAMEditor;

public class TAMEItemFactory {
	
	TAMEditor editor;
	
	public TAMEItemFactory(TAMEditor editor) {
		this.editor = editor;
	}
	
	public TAMENode createNode(int x, int y, String title, String body, int type) {
		
		TAMENode node = new TAMENode(editor, x, y, title, body, type);
		editor.getListOfNodes().add(node);
		
		return node;
	}
	
	public TAMENode importNode(int id, int x, int y, String title, String body, int type) {
		
		if(editor.containsNode(id)) {
			System.err.println("IMPORT ERROR: node with id " + id + " already exists!");
			// TODO add dialog
		}
		
		TAMENode node = new TAMENode(editor, x, y, title, body, type, id);
		editor.getListOfNodes().add(node);
		
		return node;
	}
	
	public TAMEConnection createConnection(TAMENode parent, TAMENode child, int type) {
		
		TAMEConnection connection = new TAMEConnection(editor, parent, child, type);
		editor.getListOfConnections().add(connection);
		
		return connection;
	}
	
	public TAMEConnection importConnection(int id, TAMENode parent, TAMENode child, int type) {
		
		if(editor.containsConnection(id)) {
			System.err.println("IMPORT ERROR: connection with id " + id + " already exists!");
			// TODO add dialog
		}
		
		TAMEConnection connection = new TAMEConnection(editor, parent, child, type, id);
		editor.getListOfConnections().add(connection);
		
		return connection;
	}

}
