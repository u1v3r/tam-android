package cz.vutbr.fit.testmind.editor.controls;

import android.view.MenuItem;
import android.view.MotionEvent;
import cz.vutbr.fit.testmind.MainActivity.MenuItems;
import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.graphics.ITAMGItem;
import cz.vutbr.fit.testmind.graphics.ITAMGNode;
import cz.vutbr.fit.testmind.graphics.TAMGraph.ITAMTouchListener;

public class TAMEditorToolbarContol extends TAMEditorAbstractControl implements ITAMMenuListener, ITAMTouchListener {

	public TAMEditorToolbarContol(ITAMEditor editor) {
		super(editor);
		editor.getListOfMenuControls().add(this);
		editor.getListOfTouchControls().add(this);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		
		int id = item.getItemId();
		
		switch (id) {
			case MenuItems.create_mode:
			case MenuItems.view_mode:
				editor.hideMenu();
				editor.showMenu(item.getItemId());
				//Toast.makeText(editor.getContext(), item.getTitle().toString() + " " + getEditor().getResources().getText(R.string.mode_active), Toast.LENGTH_SHORT).show();
				return true;
			default:
				return false;
		}
	}

	public void onTouchEvent(MotionEvent e) {
		// do nothing //
	}

	public void onHitEvent(MotionEvent e, ITAMGItem item) {
		
		if(item != null && item instanceof ITAMGNode) {
			editor.showMenu();
		} else {
			editor.hideMenu();
		}
	}

}
