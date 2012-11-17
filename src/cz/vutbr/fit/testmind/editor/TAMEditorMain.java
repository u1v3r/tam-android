package cz.vutbr.fit.testmind.editor;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import cz.vutbr.fit.testmind.MainActivity;
import cz.vutbr.fit.testmind.MainActivity.EventObjects;
import cz.vutbr.fit.testmind.MainActivity.MenuItems;
import cz.vutbr.fit.testmind.editor.controls.TAMEHidingControl;
import cz.vutbr.fit.testmind.editor.controls.TAMEIOControl;
import cz.vutbr.fit.testmind.editor.controls.TAMENodeControl;
import cz.vutbr.fit.testmind.editor.controls.TAMENodeControl.ITAMNodeControlListener;
import cz.vutbr.fit.testmind.editor.controls.TAMEOpenSaveControl;
import cz.vutbr.fit.testmind.editor.controls.TAMERootInitializeControl;
import cz.vutbr.fit.testmind.editor.controls.TAMERootInitializeControl.ITAMRootControlListener;
import cz.vutbr.fit.testmind.editor.controls.TAMEToolbarContol;
import cz.vutbr.fit.testmind.editor.controls.TAMEToolbarContol.ITAMToolbarConstrolItem;
import cz.vutbr.fit.testmind.editor.controls.TAMEZoomControl;
import cz.vutbr.fit.testmind.editor.items.ITAMENode;
import cz.vutbr.fit.testmind.profile.TAMPConnection;
import cz.vutbr.fit.testmind.profile.TAMPConnectionFactory;
import cz.vutbr.fit.testmind.profile.TAMPNode;

/**
 * Obsahuje zakladne funkcie na pracu s grafom 
 *
 */
public class TAMEditorMain extends TAMAbstractEditor implements ITAMEditor, ITAMToolbarConstrolItem, 
	ITAMRootControlListener, ITAMNodeControlListener {
	
	private static final String TAG = "TAMEditor";
	
	private int mode;
	private boolean hasRoot = false;
	private boolean hasVisibleMenu = false;
			
	public TAMEditorMain(Context context) {
		this(context, null);
	}
	
	public TAMEditorMain(Context context, AttributeSet attrs) {		
		super(context,attrs);
		// do not type anything there - use initializeControls method instead //
	}
	
	public boolean createDefaultRootNode(int x, int y) {
		
		if(hasRoot) {
			return false;
		} else {
			TAMPNode pNode = MainActivity.getProfile().createRoot("", "");			
			ITAMENode eNode = TAMPConnectionFactory.addEReference(pNode, this, x, y);
			eNode.getGui().setSelected(true);
			showToolbar();
			
			this.hasRoot = true;
			
			return true;
		}
		
	}

	public int getMode() {
		return mode;
	}

	public ITAMENode createNodeWithProfileAndConnection(String title, String body, ITAMENode parent, int posX, int posY) {
		TAMPNode newProfileNode = profile.createNode(title, body);		
		ITAMENode newEditorNode = TAMPConnectionFactory.addEReference(newProfileNode, this, posX, posY);
		TAMPConnection pConnection = profile.createConnection(parent.getProfile(), newProfileNode);		
		TAMPConnectionFactory.addEReference(pConnection, this);		
		newEditorNode.getGui().setBackgroundStyle(parent.getGui().getBackgroundStyle());
		
		return newEditorNode;
	}
	
	public void showToolbar() {
		showToolbar(mode);
	}
	
	private void showToolbar(int mode) {
		
		hasVisibleMenu = true;
		
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
	
	public void hideToolbar() {
		
		hasVisibleMenu = false;
		
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
	
	@Override
	protected void initializeControls() {
		new TAMEZoomControl(this);
		new TAMENodeControl(this);
		new TAMEOpenSaveControl(this);
		new TAMEIOControl(this);
		new TAMEHidingControl(this);
		new TAMEToolbarContol(this);
		new TAMERootInitializeControl(this);
		
		mode = MenuItems.create_mode;
		//actualItem = EventObjects.menu_create;
	}

	@Override
	protected void modeChanged(MenuItem item) {
		int id = item.getItemId();
		hideToolbar();
		showToolbar(id);
		//Toast.makeText(editor.getContext(), item.getTitle().toString() + " " + getEditor().getResources().getText(R.string.mode_active), Toast.LENGTH_SHORT).show();
		if(item == EventObjects.menu_create) {
			EventObjects.menu.findItem(MenuItems.create_mode).setEnabled(false);
			EventObjects.menu.findItem(MenuItems.view_mode).setEnabled(true);
		} else if(item == EventObjects.menu_view) {
			EventObjects.menu.findItem(MenuItems.create_mode).setEnabled(true);
			EventObjects.menu.findItem(MenuItems.view_mode).setEnabled(false);
		}
		mode = id;
	}

	@Override
	protected void actualizeMenus(int visibility) {
		// menu //
		if(visibility == View.VISIBLE) {
			EventObjects.menu_create.setVisible(true);
			EventObjects.menu_view.setVisible(true);
		} else {
			EventObjects.menu_create.setVisible(false);
			EventObjects.menu_view.setVisible(false);
		}
		
		// toolbar //
		if(hasVisibleMenu) {
			if(visibility == View.VISIBLE) {
				showToolbar();
			} else {
				hideToolbar();
				hasVisibleMenu = true;
			}
		}
	}
	
	@Override
	public void setVisibility(int visibility) {
		super.setVisibility(visibility);
	}
	
}
