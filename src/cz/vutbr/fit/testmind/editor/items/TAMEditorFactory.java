package cz.vutbr.fit.testmind.editor.items;

import cz.vutbr.fit.testmind.editor.TAMEditor;

public class TAMEditorFactory {
	
	TAMEditor editor;
	
	public TAMEditorFactory(TAMEditor editor) {
		this.editor = editor;
	}
	
	public TAMEditorNode createNode(int x, int y, String title, String body, int type) {
		
		TAMEditorNode node = new TAMEditorNode(editor, x, y, title, body, type);
		editor.getListOfNodes().add(node);
		
		return node;
	}
	
	public TAMEditorNode importNode(int id, int x, int y, String title, String body, int type) {
		
		if(editor.containsNode(id)) {
			System.err.println("IMPORT ERROR: node with id " + id + " already exists!");
			// TODO add dialog
		}
		
		TAMEditorNode node = new TAMEditorNode(editor, x, y, title, body, type, id);
		editor.getListOfNodes().add(node);
		
		return node;
	}
	
	public TAMEditorConnection createConnection(TAMEditorNode parent, TAMEditorNode child, int type) {
		
		TAMEditorConnection connection = new TAMEditorConnection(editor, parent, child, type);
		editor.getListOfConnections().add(connection);
		
		return connection;
	}
	
	public TAMEditorConnection importConnection(int id, TAMEditorNode parent, TAMEditorNode child, int type) {
		
		if(editor.containsConnection(id)) {
			System.err.println("IMPORT ERROR: connection with id " + id + " already exists!");
			// TODO add dialog
		}
		
		TAMEditorConnection connection = new TAMEditorConnection(editor, parent, child, type, id);
		editor.getListOfConnections().add(connection);
		
		return connection;
	}

}
