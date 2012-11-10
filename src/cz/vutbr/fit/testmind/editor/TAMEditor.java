package cz.vutbr.fit.testmind.editor;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.preference.PreferenceManager.OnActivityResultListener;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import cz.vutbr.fit.testmind.MainActivity;
import cz.vutbr.fit.testmind.MainActivity.ButtonItems;
import cz.vutbr.fit.testmind.MainActivity.EventObjects;
import cz.vutbr.fit.testmind.R;
import cz.vutbr.fit.testmind.MainActivity.MenuItems;
import cz.vutbr.fit.testmind.editor.controls.ITAMButtonListener;
import cz.vutbr.fit.testmind.editor.controls.ITAMMenuListener;
import cz.vutbr.fit.testmind.editor.controls.TAMEActivityControl;
import cz.vutbr.fit.testmind.editor.controls.TAMEHidingControl;
import cz.vutbr.fit.testmind.editor.controls.TAMEIOControl;
import cz.vutbr.fit.testmind.editor.controls.TAMENodeControl;
import cz.vutbr.fit.testmind.editor.controls.TAMEOpenSaveControl;
import cz.vutbr.fit.testmind.editor.controls.TAMERootInitializeControl;
import cz.vutbr.fit.testmind.editor.controls.TAMEToolbarContol;
import cz.vutbr.fit.testmind.editor.controls.TAMEToolbarContol.ITAMToolbarConstrolItem;
import cz.vutbr.fit.testmind.editor.controls.TAMERootInitializeControl.ITAMRootListener;
import cz.vutbr.fit.testmind.editor.controls.TAMEZoomControl;
import cz.vutbr.fit.testmind.editor.items.ITAMEConnection;
import cz.vutbr.fit.testmind.editor.items.ITAMENode;
import cz.vutbr.fit.testmind.editor.items.TAMEConnection;
import cz.vutbr.fit.testmind.editor.items.TAMENode;
import cz.vutbr.fit.testmind.graphics.ITAMGItem;
import cz.vutbr.fit.testmind.graphics.TAMGraph;
import cz.vutbr.fit.testmind.profile.TAMPConnection;
import cz.vutbr.fit.testmind.profile.TAMPNode;
import cz.vutbr.fit.testmind.profile.TAMProfile;

/**
 * Obsahuje zakladne funkcie na pracu s grafom 
 *
 */
public class TAMEditor extends TAMGraph implements ITAMEditor, ITAMToolbarConstrolItem, ITAMRootListener {
	
	private static final String TAG = "TAMEditor";
	
	private List<ITAMENode> listOfENodes;
	private List<ITAMEConnection> listOfEConnections;
	private TAMProfile profile;
	private int mode;
	protected List<ITAMMenuListener> listOfMenuControls;
	protected List<ITAMButtonListener> listOfButtonControls;
	protected MenuItem actualItem;
	//protected List<ITAMMenuListener> listOfMenuListeners;

	private boolean hasRoot = false;
			
	public TAMEditor(Context context) {
		this(context, null);
	}
	
	public TAMEditor(Context context, AttributeSet attrs){		
		super(context,attrs,0);
		
		this.listOfENodes = new ArrayList<ITAMENode>();
		this.listOfEConnections = new ArrayList<ITAMEConnection>();
		this.listOfMenuControls = new ArrayList<ITAMMenuListener>();
		this.listOfButtonControls = new ArrayList<ITAMButtonListener>();
	}
	
	public void initialize(TAMProfile profile) {
		this.profile = profile;
		super.initialize();
		new TAMEZoomControl(this);
		new TAMENodeControl(this);
		new TAMEOpenSaveControl(this);
		new TAMEIOControl(this);
		new TAMEHidingControl(this);
		new TAMEToolbarContol(this);
		new TAMEActivityControl(this);
		new TAMERootInitializeControl(this);
		
		mode = MenuItems.create_mode;
		//actualItem = EventObjects.menu_create;
		
		this.profile.getListOfEditors().add(this);
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
	
	public boolean createDefaultRootNode(int x, int y) {
		
		if(hasRoot) {
			return false;
		} else {
			TAMPNode pNode = MainActivity.getProfile().createRoot("", "");
			ITAMENode eNode = pNode.addEReference(this, x, y);
			eNode.getGui().setSelected(true);
			
			this.hasRoot = true;
			
			return true;
		}
		
	}

	public int getMode() {
		return mode;
	}

	public List<ITAMENode> getListOfENodes() {
		return listOfENodes;
	}

	public List<ITAMEConnection> getListOfEConnections() {
		return listOfEConnections;
	}
	
	public List<ITAMMenuListener> getListOfMenuControls() {
		return listOfMenuControls;
	}
	
	public List<ITAMButtonListener> getListOfButtonControls() {
		return listOfButtonControls;
	}
	
	public TAMProfile getProfile() {
		return profile;
	}

	public ITAMENode createNodeWithProfileAndConnection(String title, String body, ITAMENode parent, int posX, int posY) {
		TAMPNode newProfileNode = profile.createNode(title, body);
		ITAMENode newEditorNode = newProfileNode.addEReference(this, posX, posY);
		TAMPConnection pConnection = profile.createConnection(parent.getProfile(), newProfileNode);
		pConnection.addEReference(this);		
		newEditorNode.getGui().setBackgroundStyle(parent.getGui().getBackgroundStyle());
		
		return newEditorNode;
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
	
	public void showMenu() {
		showMenu(mode);
	}
	
	public void showMenu(int mode) {
		
		if(mode == MenuItems.create_mode) {
			EventObjects.btn_add.setVisibility(VISIBLE);
			EventObjects.btn_delete.setVisibility(VISIBLE);
			EventObjects.btn_edit.setVisibility(VISIBLE);
		} else if(mode == MenuItems.view_mode) {
			EventObjects.btn_hide_all.setVisibility(VISIBLE);
			EventObjects.btn_hide_one.setVisibility(VISIBLE);
			EventObjects.btn_view.setVisibility(VISIBLE);
		}
		
		EventObjects.btn_zoom_in.setVisibility(GONE);
		EventObjects.btn_zoom_out.setVisibility(GONE);
	}
	
	public void hideMenu() {
		if(mode == MenuItems.create_mode) {
			//EventObjects.btn_add.startAnimation(EventObjects.animAlpha);
			EventObjects.btn_add.setVisibility(GONE);
			EventObjects.btn_delete.setVisibility(GONE);
			EventObjects.btn_edit.setVisibility(GONE);
		} else if(mode == MenuItems.view_mode) {
			EventObjects.btn_hide_all.setVisibility(GONE);
			EventObjects.btn_hide_one.setVisibility(GONE);
			EventObjects.btn_view.setVisibility(GONE);
		}
		
		EventObjects.btn_zoom_in.setVisibility(VISIBLE);
		EventObjects.btn_zoom_out.setVisibility(VISIBLE);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		
		boolean selected = false;
		
		int id = item.getItemId();
		
		switch (id) {
			case MenuItems.create_mode:
			case MenuItems.view_mode:
				hideMenu();
				showMenu(item.getItemId());
				//Toast.makeText(editor.getContext(), item.getTitle().toString() + " " + getEditor().getResources().getText(R.string.mode_active), Toast.LENGTH_SHORT).show();
				mode = id;
				/*if(item != actualItem) {
					if(actualItem != null) {
						actualItem.setChecked(false);
					}
					item.setEnabled(true);
					actualItem = item;
				}*/
				break;
			default:
				for(ITAMMenuListener control : listOfMenuControls) {
					selected = control.onOptionsItemSelected(item);
				}
				break;
		}
		
		return selected;

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		for (OnActivityResultListener control : getListOfOnActivityResultControls()) {
			control.onActivityResult(requestCode, resultCode, data);
		}
	}

	public void onButtonSelected(View item) {
		
		for(ITAMButtonListener control : listOfButtonControls) {
			control.onButtonSelected(item);
		}
	}

	public void reset() {
		super.reset();
	}
	
}
