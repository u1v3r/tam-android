package cz.vutbr.fit.testmind.editor.controls;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.graphics.ITAMNode;

public class TAMEditorGesturesControl extends TAMEditorAbstractControl {

	private static final String TAG = "TAMEditorGesturesControl";

	public TAMEditorGesturesControl(ITAMEditor editor) {
		setEditor(editor);
	}
		
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTouchEvent(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	public void onSelectNodeEvent(ITAMNode node) {
		
	}
}
