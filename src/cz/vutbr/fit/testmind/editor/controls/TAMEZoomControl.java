package cz.vutbr.fit.testmind.editor.controls;

import android.util.FloatMath;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import cz.vutbr.fit.testmind.MainActivity.EventObjects;
import cz.vutbr.fit.testmind.editor.TAMEditorMain;
import cz.vutbr.fit.testmind.graphics.ITAMGNode;
import cz.vutbr.fit.testmind.graphics.TAMGraph;
import cz.vutbr.fit.testmind.graphics.TAMGraph.ITAMItemGestureListener;
import cz.vutbr.fit.testmind.graphics.TAMGraph.ITAMTouchListener;
import cz.vutbr.fit.testmind.graphics.TAMGraph.TAMGMotionEvent;

public class TAMEZoomControl extends TAMEAbstractControl implements ITAMButtonListener,ITAMTouchListener {

	private static final String TAG = "TAMEZoomControl";
	public static final int PINCH = 0;
	public static final int IDLE = 1;
	public static final int TOUCH = 2;
	
	private int touchState;
	
	private float dist0;
	private float distCurrent;
	
	public TAMEZoomControl(TAMEditorMain editor) {
		super(editor);
		editor.getListOfButtonControls().add(this);
		editor.getListOfTouchControls().add(this);
		touchState = IDLE;
	}
	/*
	public void zoom(float sx, float sy, float px, float py) {
		getEditor().zoom(sx, sy, px, py);
	}
	*/

	public void onButtonSelected(View item) {
		
		if(item == EventObjects.btn_zoom_in) {
			((TAMGraph)editor).onZoomIn();
		} else if(item == EventObjects.btn_zoom_out) {
			((TAMGraph)editor).onZoomOut();
		}
	}	
	public void onTouchEvent(MotionEvent e, float dx, float dy) {
		
		float distx, disty;
		//Log.d(TAG, "posX:" + e.getX() + ",posY:" + e.getY());
		
		switch(e.getAction() & MotionEvent.ACTION_MASK){
			case MotionEvent.ACTION_DOWN:
				//A pressed gesture has started, the motion contains the initial starting location.
				touchState = TOUCH;
				Log.d(TAG, "ACTION_DOWN");
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				//A non-primary pointer has gone down.
				Log.d(TAG, "ACTION_POINTER_DOWN");
				touchState = PINCH;				
	
				//Get the distance when the second pointer touch
				distx = e.getX(0) - e.getX(1);
				disty = e.getY(0) - e.getY(1);
				dist0 = FloatMath.sqrt(distx * distx + disty * disty);
	
				break;
			case MotionEvent.ACTION_MOVE:
				//A change has happened during a press gesture (between ACTION_DOWN and ACTION_UP).
				Log.d(TAG, "ACTION_MOVE");
				
				if(touchState == PINCH){
					Log.d(TAG, "zoooming");
					//Log.d(TAG, "posX1:" + e.getX(0) + ",posX2:" + e.getX(1));
					
					
					//vzdialenost dvoch dotykovych bodov v ramci osy 
					distx = e.getX(0) - e.getX(1);
					disty = e.getY(0) - e.getY(1);
										
					// vzdialenost obidvoch dotykov
					distCurrent = FloatMath.sqrt(distx * distx + disty * disty);
					
					float pivotX = (e.getX(0) + e.getX(1))/2f; //pivot je v strede
					float pivotY = (e.getY(0) + e.getY(1))/2f; //pivot je v strede
					
					float scale = distCurrent/dist0;
					Log.d(TAG, "scale" + scale);
					
					//Log.d(TAG,"distx:" + distx + ",disty:" + disty + ",dist0:" + dist0 + ",distCurrent:" + distCurrent);
					editor.zoom(editor.getZoom().sx*scale, editor.getZoom().sy*scale, pivotX, pivotY);
				}
	
				break;
			case MotionEvent.ACTION_UP:
				//A pressed gesture has finished.
				Log.d(TAG, "ACTION_UP");
				
				touchState = IDLE;
				break;
			case MotionEvent.ACTION_POINTER_UP:
				//A non-primary pointer has gone up.
				Log.d(TAG, "ACTION_POINTER_UP");
				
				touchState = TOUCH;
				break;
		}		
	}
	
	public void onHitEvent(MotionEvent e, TAMGMotionEvent ge) {
		// TODO Auto-generated method stub
	}	

	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

}
