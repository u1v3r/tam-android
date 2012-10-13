package cz.vutbr.fit.testmind.editor;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import cz.vutbr.fit.testmind.MainActivity;
import cz.vutbr.fit.testmind.editor.controls.TAMEditorAbstractControl;
import cz.vutbr.fit.testmind.editor.controls.TAMEditorNodesControl;
import cz.vutbr.fit.testmind.editor.items.TAMEditorConnection;
import cz.vutbr.fit.testmind.editor.items.TAMEditorFactory;
import cz.vutbr.fit.testmind.editor.items.TAMEditorNode;
import cz.vutbr.fit.testmind.graphics.TAMGraph;


/**
 * Obsahuje zakladne funkcie na pracu s grafom 
 *
 */
public class TAMEditor extends TAMGraph implements ITAMEditor{
	
	private static final String TAG = "TAMEditor";
		
	private TAMEditorNode root;
	
	private List<TAMEditorNode> listOfNodes;
	private List<TAMEditorConnection> listOfConnections;
	private List<TAMEditorAbstractControl> listOfControls;

	private TAMEditorFactory factory;
			
	public TAMEditor(Context context) {
		this(context, null);
	}
	
	public TAMEditor(Context context, AttributeSet attrs){		
		super(context,attrs,0);
		
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
		this.listOfControls = new ArrayList<TAMEditorAbstractControl>();
		listOfControls.add(new TAMEditorNodesControl(this));
		
		this.factory = new TAMEditorFactory(this);
	}
	

	public TAMEditorNode createRoot(int type, int x, int y, String title, String body) {
		
		if(root != null) return root;
		
		TAMEditorNode node = new TAMEditorNode(this, x, y, title, body, type);
		listOfNodes.add(node);
		
		setRoot(node);
		
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

	private void setRoot(TAMEditorNode root) {
		
		if(this.root == null){
			this.root = root;
		}
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
	
	public boolean hasRootNode() {
		
		if(root != null) return true;
		
		return false;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		
		boolean selected = false;
		
		for(TAMEditorAbstractControl control : listOfControls) {
			selected = control.onOptionsItemSelected(item);
		}
		
		return selected;
	}

}
