package cz.vutbr.fit.testmind.editor;

import java.util.ArrayList;
import java.util.List;

import com.touchmenotapps.widget.radialmenu.menu.v1.RadialMenuItem;
import com.touchmenotapps.widget.radialmenu.menu.v1.RadialMenuWidget;
import com.touchmenotapps.widget.radialmenu.menu.v1.RadialMenuItem.RadialMenuItemClickListener;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.preference.PreferenceManager.OnActivityResultListener;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;

import cz.vutbr.fit.testmind.MainActivity;
import cz.vutbr.fit.testmind.R;
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
	protected List<ITAMRadialMenu> listOfRadialMenuListeners;
	
	private static final int OUTER_RING_ALPHA = 255;	
	private static final int INNER_RING_ALPHA = 255;	
	private static final int OUTLINE_ALPHA = 255;	
	private static final int DISABLED_ALPHA = 255;
	private static final int SELECTED_ALPHA = 255;
	private static final int TEXT_ALPHA = 255;
	
	private static final int TEXT_SIZE = 16;		
	private static final int ICON_MIN_SIZE = 30;
	private static final int ICON_MAX_SIZE = 40;
	private static final int BORDER_SIZE = 10;
	
	private static final long ANIMATION_SPEED = 0L;
	
	private static final int OUTER_RADIUS = 120;
	private static final int INNER_RADIUS = 30;	
	
	/**
	 * Konstanta sluzi na vycentrovanie riadial menu na stred dotyku
	 * 
	 * pozn. v buducnosti by bolo dobre keby sa pocitalo automaticky
	 */
	private static final int RIADIAL_MENU_Y_OFFSET = 100;
	
	private RadialMenuWidget radialMenu;
	
	
	public TAMAbstractEditor(Context context, AttributeSet attrs){		
		super(context,attrs,0);
		
		this.listOfENodes = new ArrayList<ITAMENode>();
		this.listOfEConnections = new ArrayList<ITAMEConnection>();
		this.listOfMenuControls = new ArrayList<ITAMMenuListener>();
		this.listOfButtonControls = new ArrayList<ITAMButtonListener>();
		this.listOfOnActivityResultControls = new ArrayList<OnActivityResultListener>();		
		this.listOfRadialMenuListeners = new ArrayList<ITAMRadialMenu>();
		
		radialMenu = new RadialMenuWidget(getContext());
	}
	
	public void initialize(TAMProfile profile) {
		this.profile = profile;
		super.initialize();
				
		initializeControls();
		initializeRadialMenu();
		
		this.profile.getListOfEditors().add(this);
	}
	
	protected abstract void initializeControls();
	
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
	
	public List<ITAMRadialMenu> getListOfRadialMenuListeners(){
		return listOfRadialMenuListeners;
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
	
	private void initializeRadialMenu() {
		
		Resources res = getResources();
				
		radialMenu.setSelectedColor(res.getColor(R.color.selected_color), SELECTED_ALPHA);
		//radialMenu.setDisabledColor(res.getColor(R.color.disabled_color), DISABLED_ALPHA);
		radialMenu.setInnerRingRadius(INNER_RADIUS, OUTER_RADIUS);		
		radialMenu.setAnimationSpeed(ANIMATION_SPEED);		
		radialMenu.setIconSize(ICON_MIN_SIZE, ICON_MAX_SIZE);
		radialMenu.setTextSize(TEXT_SIZE);
		radialMenu.setTextColor(res.getColor(R.color.text_color), TEXT_ALPHA);
		radialMenu.setOutlineColor(res.getColor(R.color.outline_color), OUTLINE_ALPHA);
		radialMenu.setInnerRingColor(res.getColor(R.color.inner_ring_color), INNER_RING_ALPHA);
		radialMenu.setOuterRingColor(res.getColor(R.color.outer_ring_color), OUTER_RING_ALPHA);	
		radialMenu.setColoredBorderSize(BORDER_SIZE);
		
		//pieMenu.setHeader("Test Menu", 20);	
		
		initRadialMenuItems();
	}
	
	private void initRadialMenuItems() {
		
		for (ITAMRadialMenu menu : getListOfRadialMenuListeners()) {
			menu.initRadialMenuItems();
		}
		
	}

	public void addRadialMenuItem(RadialMenuItem item){
		radialMenu.addMenuEntry(item);
	}
	
	public void showRadialMenu(int posX, int posY, View anchor){
		
		/* pri otvorenom slidingmenu dochadza k nespravnej interpretacii pozicie,
		 * preto treba posunut dolava
		*/
		if(MainActivity.slidingMenu.isMenuShowing()){
			posX = posX - (int)getContext().getResources().getDimension(R.dimen.slidingmenu_behindwidth);
		}
		
		radialMenu.setCenterLocation(posX, posY+RIADIAL_MENU_Y_OFFSET);
		radialMenu.show(anchor);
	}
	
	public void dissmisRadialMenu(){		
		radialMenu.dismiss();
	}
		
	protected abstract void actualizeMenus(int visibility);
}
