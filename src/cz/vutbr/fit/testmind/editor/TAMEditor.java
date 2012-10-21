package cz.vutbr.fit.testmind.editor;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.preference.PreferenceManager.OnActivityResultListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import cz.vutbr.fit.testmind.MainActivity;
import cz.vutbr.fit.testmind.R;
import cz.vutbr.fit.testmind.editor.controls.ITAMMenuListener;
import cz.vutbr.fit.testmind.editor.controls.TAMEditorIOControl;
import cz.vutbr.fit.testmind.editor.controls.TAMEditorNodesControl;
import cz.vutbr.fit.testmind.editor.controls.TAMEditorOpenSaveControl;
import cz.vutbr.fit.testmind.editor.controls.TAMEditorZoomControl;
import cz.vutbr.fit.testmind.editor.items.ITAMEConnection;
import cz.vutbr.fit.testmind.editor.items.ITAMENode;
import cz.vutbr.fit.testmind.editor.items.TAMEConnection;
import cz.vutbr.fit.testmind.editor.items.TAMENode;
import cz.vutbr.fit.testmind.graphics.TAMGraph;
import cz.vutbr.fit.testmind.profile.TAMPConnection;
import cz.vutbr.fit.testmind.profile.TAMPNode;

/**
 * Obsahuje zakladne funkcie na pracu s grafom 
 *
 */
public class TAMEditor extends TAMGraph implements ITAMEditor{
	
	private static final String TAG = "TAMEditor";
	
	private List<ITAMENode> listOfENodes;
	private List<ITAMEConnection> listOfEConnections;
	//protected List<ITAMMenuListener> listOfMenuListeners;

	private boolean hasRoot = false;
			
	public TAMEditor(Context context) {
		this(context, null);
	}
	
	public TAMEditor(Context context, AttributeSet attrs){		
		super(context,attrs,0);
		
		this.listOfENodes = new ArrayList<ITAMENode>();
		this.listOfEConnections = new ArrayList<ITAMEConnection>();
		//this.listOfMenuListeners = new ArrayList<ITAMMenuListener>();
	}
	
	public void initialize() {
		new TAMEditorZoomControl(this, R.id.acitity_main_zoom_controls);
		new TAMEditorNodesControl(this);
		new TAMEditorOpenSaveControl(this);
		new TAMEditorIOControl(this);
		
		MainActivity.getProfile().getListOfEditors().add(this);
	}
	
	public boolean containsNode(int id) {
		for(ITAMENode node : listOfENodes) {
			if(id == node.getProfile().getId()) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean containsConnection(int id) {
		for(ITAMEConnection connection : listOfEConnections) {
			if(id == connection.getProfile().getId()) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasWindowFocus);
		
		// ak neexistuje root uzol treba ho vytvorit
		if(!hasRoot){
			createDefaultRootNode();
		}
	}
	
	/**
	 * Vytvori root node do stredu kresliacej plochy
	 */
	public void createDefaultRootNode(){
		
		
		Point position = new Point(getWidth()/2, getHeight()/2);
		TAMPNode pNode = MainActivity.getProfile().createRoot("", "");
		pNode.addEReference(this, position.x, position.y);
		//editor.createRoot(TAMGRectangleNode.NODE_TYPE_RECTANGLE, position.x, position.y, 
		//		DEFAULT_ROOT_TITLE, DEFAULT_ROOT_BODY);
		
		this.hasRoot = true;
		
	}

	public List<ITAMENode> getListOfENodes() {
		return listOfENodes;
	}

	public List<ITAMEConnection> getListOfEConnections() {
		return listOfEConnections;
	}

	public ITAMENode createNode(TAMPNode profile, int x, int y) {
		ITAMENode node = new TAMENode(this, profile, x, y);
		listOfENodes.add(node);
		return node;
	}

	public ITAMENode createNode(TAMPNode profile, int x, int y, int type) {
		ITAMENode node = new TAMENode(this, profile, x, y, type);
		listOfENodes.add(node);
		return node;
	}

	public ITAMEConnection createConnection(TAMPConnection profile) {
		ITAMEConnection connection = new TAMEConnection(this, profile);
		listOfEConnections.add(connection);
		return connection;
	}

	public ITAMEConnection createConnection(TAMPConnection profile, int type) {
		ITAMEConnection connection = new TAMEConnection(this, profile, type);
		listOfEConnections.add(connection);
		return connection;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		
		boolean selected = false;
		
		for(ITAMMenuListener control : getListOfMenuControls()) {
			selected = control.onOptionsItemSelected(item);
		}
		
		return selected;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		for (OnActivityResultListener control : getListOfOnActivityResultControls()) {
			control.onActivityResult(requestCode, resultCode, data);
		}
	}

	
	
}
