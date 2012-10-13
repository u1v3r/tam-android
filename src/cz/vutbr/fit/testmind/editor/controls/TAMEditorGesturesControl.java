package cz.vutbr.fit.testmind.editor.controls;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;
import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.graphics.ITAMNode;

public class TAMEditorGesturesControl extends TAMEditorAbstractControl implements OnGestureListener {
	
	private static final String TAG = "TAMEditorGesturesControl";
	
	private List<ITAMNode> selectedNodesList;	
	
	private GestureDetector gDetector;	
	
	public TAMEditorGesturesControl(ITAMEditor editor) {
		setEditor(editor);
		gDetector = new GestureDetector(editor.getContext(),this);
		selectedNodesList = new ArrayList<ITAMNode>();
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
		gDetector.onTouchEvent(e);
	}

	public void onSelectNodeEvent(ITAMNode node) {		
		
		Log.d(TAG,"select node: " + node.getText());
		
		synchronized (selectedNodesList) {
			selectedNodesList.add(node);
		};
	}

	public void onUnselectNodeEvent(ITAMNode node) {
		
		Log.d(TAG,"unselect node: " + node.getText());
		
		synchronized (selectedNodesList) {
			selectedNodesList.remove(node);
		};
	}

	public void onMoveNodeEvent(ITAMNode node) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean onDown(MotionEvent e) {
		Log.d(TAG,"onDown");
		return true;
	}

	public void onShowPress(MotionEvent e) {
		Log.d(TAG,"onShowPress");
		
	}

	public boolean onSingleTapUp(MotionEvent e) {
		Log.d(TAG,"onSingleTapUp");
		return true;
	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		Log.d(TAG,"onScroll");
		return true;
	}

	public void onLongPress(MotionEvent e) {
		Log.d(TAG,"onLongPress");
		
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		Log.d(TAG,"onFling");
		return true;
	}
}
