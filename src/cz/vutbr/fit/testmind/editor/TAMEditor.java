package cz.vutbr.fit.testmind.editor;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import cz.vutbr.fit.testmind.editor.items.TAMEditorConnection;
import cz.vutbr.fit.testmind.editor.items.TAMEditorFactory;
import cz.vutbr.fit.testmind.editor.items.TAMEditorNode;
import cz.vutbr.fit.testmind.graphics.TAMGraph;

public class TAMEditor extends View {
	
	private TAMGraph graph;
	private TAMEditorNode root;
	private List<TAMEditorNode> listOfEditorNodes;
	private List<TAMEditorConnection> listOfEditorConnections;
	private TAMEditorFactory factory;
	
	public TAMEditor(Context context) {
		super(context);
		
		// TODO buttons (initialization from XML ?), etc...
		
		listOfEditorNodes = new ArrayList<TAMEditorNode>();
		listOfEditorConnections = new ArrayList<TAMEditorConnection>();
		
		factory = new TAMEditorFactory(this);
	}
	
	public boolean containsNode(int id) {
		for(TAMEditorNode node : listOfEditorNodes) {
			if(id == node.getId()) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean containsConnection(int id) {
		for(TAMEditorConnection connection : listOfEditorConnections) {
			if(id == connection.getId()) {
				return true;
			}
		}
		
		return false;
	}

	public TAMGraph getGraph() {
		return graph;
	}

	public TAMEditorNode getRoot() {
		return root;
	}

	public TAMEditorFactory getFactory() {
		return factory;
	}

	protected void setRoot(TAMEditorNode root) {
		this.root = root;
	}

	public List<TAMEditorNode> getListOfNodes() {
		return listOfEditorNodes;
	}

	public List<TAMEditorConnection> getListOfConnections() {
		return listOfEditorConnections;
	}
	
	

}
