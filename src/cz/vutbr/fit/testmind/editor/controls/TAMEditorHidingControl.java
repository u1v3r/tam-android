package cz.vutbr.fit.testmind.editor.controls;

import cz.vutbr.fit.testmind.graphics.ITAMNode;
import android.graphics.Canvas;
import android.view.MenuItem;
import android.view.MotionEvent;

public class TAMEditorHidingControl extends TAMEditorAbstractControl {

	private static final String TAG = "TAMEditorHidingControl";

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onDraw(Canvas canvas) {
		// do nothing //
	}

	@Override
	public void onTouchEvent(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void onSelectNodeEvent(ITAMNode node) {
		// TODO Auto-generated method stub
		
	}

	public void onUnselectNodeEvent(ITAMNode node) {
		// TODO Auto-generated method stub
		
	}

	public void onMoveNodeEvent(ITAMNode node) {
		// TODO Auto-generated method stub
		
	}

}
