package cz.vutbr.fit.testmind.editor;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import cz.vutbr.fit.testmind.MainActivity;
import cz.vutbr.fit.testmind.editor.items.TAMEditorConnection;
import cz.vutbr.fit.testmind.editor.items.TAMEditorFactory;
import cz.vutbr.fit.testmind.editor.items.TAMEditorNode;
import cz.vutbr.fit.testmind.graphics.ITAMNode;
import cz.vutbr.fit.testmind.graphics.TAMGraph;


/**
 * Obsahuje zakladne funkcie na pracu s grafom 
 *
 */
public class TAMEditor extends View implements ITAMEditor{
	
	private static final String TAG = "TAMEditor";
		
	private TAMEditorNode root;
	
	private List<TAMEditorNode> listOfNodes;
	private List<TAMEditorConnection> listOfConnections;

	private TAMEditorFactory factory;
			
	public TAMEditor(Context context) {
		super(context);
		
		/*
		View inflater = View.inflate(context, R.layout.activity_main, null);
		
		//LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);		
		this.addView(inflater);
		
		
		this.graph = (TAMGraph)findViewById(R.id.tam_graph);		
		this.zoomControls = (ZoomControls)findViewById(R.id.zoom_controls);
		*/	
		//Log.d(TAG, this.zoomControls.toString());
		
		this.listOfNodes = new ArrayList<TAMEditorNode>();
		this.listOfConnections = new ArrayList<TAMEditorConnection>();
		
		this.factory = new TAMEditorFactory(this);
	}
	

	public TAMEditorNode createRoot(int type, int x, int y, String title, String body, ITAMNode core) {
		
		if(root != null) return root;
		
		TAMEditorNode node = new TAMEditorNode(this, x, y, title, body, type);
		listOfNodes.add(node);
		
		return node;
	}
	
	/* (non-Javadoc)
	 * @see cz.vutbr.fit.testmind.editor.TAMIEditor#containsNode(int)
	 */
	public boolean containsNode(int id) {
		for(TAMEditorNode node : listOfNodes) {
			if(id == node.getId()) {
				return true;
			}
		}
		
		return false;
	}
	
	/* (non-Javadoc)
	 * @see cz.vutbr.fit.testmind.editor.TAMIEditor#getNode(int)
	 */
	public TAMEditorNode getNode(int id) {
		for(TAMEditorNode node : listOfNodes) {
			if(id == node.getId()) {
				return node;
			}
		}
		
		return null;
	}
	
	/* (non-Javadoc)
	 * @see cz.vutbr.fit.testmind.editor.TAMIEditor#containsConnection(int)
	 */
	public boolean containsConnection(int id) {
		for(TAMEditorConnection connection : listOfConnections) {
			if(id == connection.getId()) {
				return true;
			}
		}
		
		return false;
	}
	

	/* (non-Javadoc)
	 * @see cz.vutbr.fit.testmind.editor.TAMIEditor#getConnection(int)
	 */
	public TAMEditorConnection getConnection(int id) {
		for(TAMEditorConnection connection : listOfConnections) {
			if(id == connection.getId()) {
				return connection;
			}
		}
		
		return null;
	}


	/* (non-Javadoc)
	 * @see cz.vutbr.fit.testmind.editor.TAMIEditor#getGraph()
	 */
	public TAMGraph getGraph() {
		return MainActivity.EventObjects.graph;
	}

	/* (non-Javadoc)
	 * @see cz.vutbr.fit.testmind.editor.TAMIEditor#getRoot()
	 */
	public TAMEditorNode getRoot() {
		return root;
	}

	/* (non-Javadoc)
	 * @see cz.vutbr.fit.testmind.editor.TAMIEditor#getFactory()
	 */
	public TAMEditorFactory getFactory() {
		return factory;
	}

	public void setRoot(TAMEditorNode root) {
		this.root = root;
	}

	/* (non-Javadoc)
	 * @see cz.vutbr.fit.testmind.editor.TAMIEditor#getListOfNodes()
	 */
	public List<TAMEditorNode> getListOfNodes() {
		return listOfNodes;
	}

	/* (non-Javadoc)
	 * @see cz.vutbr.fit.testmind.editor.TAMIEditor#getListOfConnections()
	 */
	public List<TAMEditorConnection> getListOfConnections() {
		return listOfConnections;
	}
	
	/**
	 * Vyhlada prave jeden uzol, ktory je vybrany
	 * 
	 * @return {@link TAMEditorNode}|null - ak nie je ziadny vybrany
	 */
	public TAMEditorNode getLastSelectedNode(){
		
		if(getGraph().getLastSelectedNode() != null) {
			return (TAMEditorNode) getGraph().getLastSelectedNode().getHelpObject();
		}
		
		return null;
	}

	public View getView() {
		return this;
	}

	public boolean hasRootNode() {
		
		if(root != null) return true;
		
		return false;
	}

}
