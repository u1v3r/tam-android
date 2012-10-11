package cz.vutbr.fit.testmind.editor.items;

import cz.vutbr.fit.testmind.editor.TAMEditor;

public class TAMEditorFactory {
	
	TAMEditor editor;
	
	public TAMEditorFactory(TAMEditor editor) {
		this.editor = editor;
	}
	
	public TAMEditorNode createRoot(int type, int x, int y, String title, String body) {
		
		if(editor.getRoot() != null) return editor.getRoot();
		
		TAMEditorNode node = new TAMEditorNode(editor, x, y, title, body, type);
		editor.getListOfNodes().add(node);
		
		return node;
	}
	
	public TAMEditorNode createNode(int x, int y, String title, String body, int type) {
		
		TAMEditorNode node = new TAMEditorNode(editor, x, y, title, body, type);
		editor.getListOfNodes().add(node);
		
		return node;
	}
	
	public TAMEditorConnection createConnection(TAMEditorNode parent, TAMEditorNode child, int type) {
		
		TAMEditorConnection connection = new TAMEditorConnection(editor, parent, child, type);
		editor.getListOfConnections().add(connection);
		
		return connection;
	}

}
