package cz.vutbr.fit.testmind.editor;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager.OnActivityResultListener;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;

import cz.vutbr.fit.testmind.MainActivity;
import cz.vutbr.fit.testmind.MainActivity.MenuItems;
import cz.vutbr.fit.testmind.editor.controls.ITAMButtonListener;
import cz.vutbr.fit.testmind.editor.controls.ITAMMenuListener;
import cz.vutbr.fit.testmind.editor.items.ITAMEConnection;
import cz.vutbr.fit.testmind.editor.items.ITAMENode;
import cz.vutbr.fit.testmind.editor.items.TAMEConnection;
import cz.vutbr.fit.testmind.editor.items.TAMENode;
import cz.vutbr.fit.testmind.graphics.TAMGraph;
import cz.vutbr.fit.testmind.profile.TAMPConnection;
import cz.vutbr.fit.testmind.profile.TAMPNode;
import cz.vutbr.fit.testmind.profile.TAMProfile;

public abstract class TAMAbstractEditor extends TAMGraph implements ITAMEditor {
	
	protected List<ITAMENode> listOfENodes;
	protected List<ITAMEConnection> listOfEConnections;
	protected TAMProfile profile;
	protected List<ITAMMenuListener> listOfMenuControls;
	protected List<ITAMButtonListener> listOfButtonControls;
	protected List<OnActivityResultListener> listOfOnActivityResultControls;
	
	public TAMAbstractEditor(Context context, AttributeSet attrs){		
		super(context,attrs,0);
		
		this.listOfENodes = new ArrayList<ITAMENode>();
		this.listOfEConnections = new ArrayList<ITAMEConnection>();
		this.listOfMenuControls = new ArrayList<ITAMMenuListener>();
		this.listOfButtonControls = new ArrayList<ITAMButtonListener>();
		this.listOfOnActivityResultControls = new ArrayList<OnActivityResultListener>();
	}
	
	public void initialize(TAMProfile profile, MainActivity mainActivity) {
		this.profile = profile;
		super.initialize();
		
		initializeControls(mainActivity);
		
		this.profile.getListOfEditors().add(this);
	}
	
	protected abstract void initializeControls(MainActivity mainActivity);
	
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
	
	public List<OnActivityResultListener> getListOfOnActivityResultControls() {
		return listOfOnActivityResultControls;
	}
	
	public TAMProfile getProfile() {
		return profile;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		
		boolean selected = false;
		
		int id = item.getItemId();
		
		switch (id) {
			case MenuItems.create_mode:
			case MenuItems.view_mode:
			case MenuItems.show_result:
			case MenuItems.next_question:
				modeChanged(item);
				break;
			default:
				for(ITAMMenuListener control : listOfMenuControls) {
					selected = control.onOptionsItemSelected(item);
				}
				break;
		}
		
		return selected;

	}

	protected abstract void modeChanged(MenuItem item);
	
	public void onButtonSelected(View item) {
		
		for(ITAMButtonListener control : listOfButtonControls) {
			control.onButtonSelected(item);
		}
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		for (OnActivityResultListener control : getListOfOnActivityResultControls()) {
			control.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	public void setEditorVisibility(int visibility) {
		super.setVisibility(visibility);
		actualizeMenus(visibility);
	}

	protected abstract void actualizeMenus(int visibility);

}
