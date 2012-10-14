package cz.vutbr.fit.testmind.editor;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MenuItem;
import android.view.MotionEvent;
import cz.vutbr.fit.testmind.R;
import cz.vutbr.fit.testmind.editor.controls.TAMEditorAbstractControl;
import cz.vutbr.fit.testmind.editor.controls.TAMEditorGesturesControl;
import cz.vutbr.fit.testmind.editor.controls.TAMEditorNodesControl;
import cz.vutbr.fit.testmind.editor.controls.TAMEditorZoomControl;
import cz.vutbr.fit.testmind.editor.items.TAMEConnection;
import cz.vutbr.fit.testmind.editor.items.TAMEItemFactory;
import cz.vutbr.fit.testmind.editor.items.TAMENode;
import cz.vutbr.fit.testmind.graphics.ITAMGNode;
import cz.vutbr.fit.testmind.graphics.ITAMGNode.OnNodeSelectListener;
import cz.vutbr.fit.testmind.graphics.TAMGAbstractNode;
import cz.vutbr.fit.testmind.graphics.TAMGraph;


/**
 * Obsahuje zakladne funkcie na pracu s grafom 
 *
 */
public class TAMEditor extends TAMGraph implements ITAMEditor{
	
	private static final String TAG = "TAMEditor";
		
	private TAMENode root;
	
	private List<TAMENode> listOfNodes;
	private List<TAMEConnection> listOfConnections;
	private List<TAMEditorAbstractControl> listOfControls;

	private TAMEItemFactory factory;

	
			
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
	}
	
	public void initialize() {
		this.listOfNodes = new ArrayList<TAMENode>();
		this.listOfConnections = new ArrayList<TAMEConnection>();
		this.listOfControls = new ArrayList<TAMEditorAbstractControl>();
		listOfControls.add(new TAMEditorNodesControl(this));
		listOfControls.add(new TAMEditorZoomControl(this,R.id.zoom_controls));
		listOfControls.add(new TAMEditorGesturesControl(this));
		
		this.factory = new TAMEItemFactory(this);		
	}
	

	public TAMENode createRoot(int type, int x, int y, String title, String body) {
		
		if(root != null) return root;
		
		TAMENode node = new TAMENode(this, x, y, title, body, type);
		listOfNodes.add(node);
		
		setRoot(node);
		
		return node;
	}
	
	/* (non-Javadoc)
	 * @see cz.vutbr.fit.testmind.editor.TAMIEditor#containsNode(int)
	 */
	public boolean containsNode(int id) {
		for(TAMENode node : listOfNodes) {
			if(id == node.getId()) {
				return true;
			}
		}
		
		return false;
	}
	
	/* (non-Javadoc)
	 * @see cz.vutbr.fit.testmind.editor.TAMIEditor#getNode(int)
	 */
	public TAMENode getNode(int id) {
		for(TAMENode node : listOfNodes) {
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
		for(TAMEConnection connection : listOfConnections) {
			if(id == connection.getId()) {
				return true;
			}
		}
		
		return false;
	}
	

	/* (non-Javadoc)
	 * @see cz.vutbr.fit.testmind.editor.TAMIEditor#getConnection(int)
	 */
	public TAMEConnection getConnection(int id) {
		for(TAMEConnection connection : listOfConnections) {
			if(id == connection.getId()) {
				return connection;
			}
		}
		
		return null;
	}

	/* (non-Javadoc)
	 * @see cz.vutbr.fit.testmind.editor.TAMIEditor#getRoot()
	 */
	public TAMENode getRoot() {
		return root;
	}

	/* (non-Javadoc)
	 * @see cz.vutbr.fit.testmind.editor.TAMIEditor#getFactory()
	 */
	public TAMEItemFactory getFactory() {
		return factory;
	}

	private void setRoot(TAMENode root) {
		
		if(this.root == null){
			this.root = root;
		}
	}

	/* (non-Javadoc)
	 * @see cz.vutbr.fit.testmind.editor.TAMIEditor#getListOfNodes()
	 */
	public List<TAMENode> getListOfNodes() {
		return listOfNodes;
	}

	/* (non-Javadoc)
	 * @see cz.vutbr.fit.testmind.editor.TAMIEditor#getListOfConnections()
	 */
	public List<TAMEConnection> getListOfConnections() {
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
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for(TAMEditorAbstractControl control : listOfControls) {
			control.onDraw(canvas);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		
		for(TAMEditorAbstractControl control : listOfControls) {
			control.onTouchEvent(e);
		}
				
		return super.onTouchEvent(e);
	}
	
	public void onSelectEvent(ITAMGNode node){
		for(TAMEditorAbstractControl control : listOfControls) {			 
			control.onSelectNodeEvent(node);
		}
	}

	public void onUnselectEvent(ITAMGNode node) {
		for(TAMEditorAbstractControl control : listOfControls) {			 
			control.onUnselectNodeEvent(node);
		}
	}
	
}
