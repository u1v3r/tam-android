package cz.vutbr.fit.testmind.editor.controls;

import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.editor.items.TAMENode;
import cz.vutbr.fit.testmind.graphics.ITAMGNode;
import android.graphics.Canvas;
import android.view.MenuItem;
import android.view.MotionEvent;

public class TAMEditorHidingControl extends TAMEditorAbstractControl {

	public TAMEditorHidingControl(ITAMEditor editor) {
		super(editor);
	}

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

	public void onSelectNodeEvent(ITAMGNode node) {
		// TODO Auto-generated method stub
		
	}

	public void onUnselectNodeEvent(ITAMGNode node) {
		// TODO Auto-generated method stub
		
	}

	public void onMoveNodeEvent(ITAMGNode node) {
		// TODO Auto-generated method stub
		
	}

}
