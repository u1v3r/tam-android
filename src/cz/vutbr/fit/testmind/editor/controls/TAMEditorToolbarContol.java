package cz.vutbr.fit.testmind.editor.controls;

import android.view.MenuItem;
import android.view.MotionEvent;
import cz.vutbr.fit.testmind.MainActivity.MenuItems;
import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.graphics.ITAMGItem;
import cz.vutbr.fit.testmind.graphics.ITAMGNode;
import cz.vutbr.fit.testmind.graphics.TAMGraph.ITAMTouchListener;

public class TAMEditorToolbarContol extends TAMEditorAbstractControl implements ITAMTouchListener {
	
	public interface ITAMToolbarConstrolItem {
		public void showMenu();
		public void hideMenu();
	}

	public TAMEditorToolbarContol(ITAMToolbarConstrolItem editor) {
		super((ITAMEditor) editor);
		((ITAMEditor)editor).getListOfTouchControls().add(this);
	}

	public void onTouchEvent(MotionEvent e) {
		// do nothing //
	}

	public void onHitEvent(MotionEvent e, ITAMGItem item) {
		
		if(item != null && item instanceof ITAMGNode) {
			((ITAMToolbarConstrolItem)editor).showMenu();
		} else {
			((ITAMToolbarConstrolItem)editor).hideMenu();
		}
	}

}
