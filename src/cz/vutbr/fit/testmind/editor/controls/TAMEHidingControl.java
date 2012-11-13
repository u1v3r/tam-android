package cz.vutbr.fit.testmind.editor.controls;

import cz.vutbr.fit.testmind.MainActivity.MenuItems;
import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.graphics.ITAMGNode;
import cz.vutbr.fit.testmind.graphics.TAMGraph.ITAMItemGestureListener;
import android.view.MotionEvent;

public class TAMEHidingControl extends TAMEAbstractControl implements ITAMItemGestureListener {
	
	private static final String TAG = "TAMEditorHidingControl";
	
	public interface ITAMHidingControlNode {
		public void setChildsVisible(boolean visible, boolean oneLevel);
		public boolean hasVisibleChilds();
	}

	public TAMEHidingControl(ITAMEditor editor) {
		super(editor);
		editor.getListOfItemGestureControls().add(this);
	}

	public void onItemLongSelectEvent(MotionEvent e, ITAMGNode node) {
		
		setBranchVisible(node, false);
		
	}

	public void onItemLongReleaseEvent(MotionEvent e, ITAMGNode node) {
		// do nothing //
	}

	public void onItemDoubleTapEvent(MotionEvent e, ITAMGNode node) {
		
		setBranchVisible(node, true);
	}
	
	private void setBranchVisible(ITAMGNode node, boolean oneLevel) {
		
		if(getEditor().getMode() == MenuItems.view_mode) {
			// this control supports only instances of node which implements ITAMHidingControlNode interface //
			// (supports child nodes hiding functionality) //
			
			if(node != null && (node.getHelpObject()) instanceof ITAMHidingControlNode) {
				
				ITAMHidingControlNode selectedNode = (ITAMHidingControlNode) (node.getHelpObject());
				selectedNode.setChildsVisible(!selectedNode.hasVisibleChilds(), oneLevel);
				getEditor().invalidate();
			}
		}
	}

}
