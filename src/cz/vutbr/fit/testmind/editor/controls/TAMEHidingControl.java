package cz.vutbr.fit.testmind.editor.controls;

import com.touchmenotapps.widget.radialmenu.menu.v1.RadialMenuItem;
import com.touchmenotapps.widget.radialmenu.menu.v1.RadialMenuItem.RadialMenuItemClickListener;

import cz.vutbr.fit.testmind.R;
import cz.vutbr.fit.testmind.MainActivity.MenuItems;
import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.editor.ITAMRadialMenu;
import cz.vutbr.fit.testmind.graphics.ITAMGNode;
import cz.vutbr.fit.testmind.graphics.TAMGraph.ITAMItemGestureListener;
import android.content.res.Resources;
import android.view.MotionEvent;

public class TAMEHidingControl extends TAMEAbstractControl implements ITAMRadialMenu, ITAMItemGestureListener {
	
	private static final String TAG = "TAMEditorHidingControl";
		
	public interface ITAMHidingControlNode {
		public void setChildsVisible(boolean visible, boolean oneLevel);
		public boolean hasVisibleChilds();
	}

	public TAMEHidingControl(ITAMEditor editor) {
		super(editor);
		editor.getListOfItemGestureControls().add(this);
		editor.getListOfRadialMenuListeners().add(this);
	}

	
	public void onItemLongSelectEvent(MotionEvent e, ITAMGNode node) {
		setBranchVisible(node, false);		
	}

	public void onItemLongReleaseEvent(MotionEvent e, ITAMGNode node) {
		// do nothing //
	}

	public void onItemDoubleTapEvent(MotionEvent e, ITAMGNode node) {
		// nepouzivat, pouziva NodeControl na zobrazenie radial menu
		
	}
	
		
	private void setBranchVisible(ITAMGNode node, boolean oneLevel) {
		
		//if(getEditor().getMode() == MenuItems.view_mode) {
			// this control supports only instances of node which implements ITAMHidingControlNode interface //
			// (supports child nodes hiding functionality) //
			
			if(node != null && (node.getHelpObject()) instanceof ITAMHidingControlNode) {
				
				ITAMHidingControlNode selectedNode = (ITAMHidingControlNode) (node.getHelpObject());
				selectedNode.setChildsVisible(!selectedNode.hasVisibleChilds(), oneLevel);
				getEditor().invalidate();
			}
		//}
	}

	public void initRadialMenuItems() {
		
		Resources res = activity.getResources();
		
		RadialMenuItem hideItem = new RadialMenuItem(res.getString(R.string.hide), res.getString(R.string.hide));
		hideItem.setDisplayIcon(R.drawable.radial_hide_one);
		hideItem.setOnMenuItemPressed(new RadialMenuItemClickListener() {

			public void execute() {
				toggleBranchVisible();
				editor.dissmisRadialMenu();
			}
		});
		
		editor.addRadialMenuItem(hideItem);
	}

	protected void toggleBranchVisible() {		
		setBranchVisible(editor.getLastSelectedNode(), true);
	}

}
