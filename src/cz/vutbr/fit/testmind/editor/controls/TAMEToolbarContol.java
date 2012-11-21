package cz.vutbr.fit.testmind.editor.controls;

import android.view.MenuItem;
import android.view.MotionEvent;
import cz.vutbr.fit.testmind.MainActivity.MenuItems;
import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.graphics.ITAMGItem;
import cz.vutbr.fit.testmind.graphics.ITAMGNode;
import cz.vutbr.fit.testmind.graphics.TAMGraph.ITAMTouchListener;
import cz.vutbr.fit.testmind.graphics.TAMGraph.TAMGMotionEvent;

public class TAMEToolbarContol extends TAMEAbstractControl implements ITAMTouchListener {
	
	public interface ITAMToolbarControlItem {
		public void showToolbar();
		public void hideToolbar();
	}

	public TAMEToolbarContol(ITAMToolbarControlItem editor) {
		super((ITAMEditor) editor);
		((ITAMEditor)editor).getListOfTouchControls().add(this);
	}

	public void onHitEvent(MotionEvent e, TAMGMotionEvent ge) {
		
		if(ge.item != null && ge.item instanceof ITAMGNode) {
			((ITAMToolbarControlItem)editor).showToolbar();
		} else {
			((ITAMToolbarControlItem)editor).hideToolbar();
		}
	}

	public void onTouchEvent(MotionEvent e, float dx, float dy) {
		// TODO Auto-generated method stub
		
	}

}
