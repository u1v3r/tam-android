package cz.vutbr.fit.testmind.editor.controls;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Vibrator;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import cz.vutbr.fit.testmind.MainActivity;
import cz.vutbr.fit.testmind.dialogs.AddNodeDialog.AddNodeDialogListener;
import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.editor.items.ITAMENode;
import cz.vutbr.fit.testmind.editor.items.TAMENode;
import cz.vutbr.fit.testmind.graphics.ITAMGItem;
import cz.vutbr.fit.testmind.graphics.ITAMGNode;
import cz.vutbr.fit.testmind.graphics.TAMGraph;
import cz.vutbr.fit.testmind.graphics.TAMGraph.ITAMDrawListener;
import cz.vutbr.fit.testmind.graphics.TAMGraph.ITAMItemListener;
import cz.vutbr.fit.testmind.graphics.TAMGraph.ITAMTouchListener;
import cz.vutbr.fit.testmind.profile.TAMPConnection;
import cz.vutbr.fit.testmind.profile.TAMPNode;

public class TAMEditorGesturesControl extends TAMEditorAbstractControl 
	implements OnGestureListener,OnDoubleTapListener, AddNodeDialogListener, 
	ITAMTouchListener,ITAMDrawListener,ITAMItemListener{
		
	
	private static final String TAG = "TAMEditorGesturesControl";

	private static final long VIBRATE_DRUATION = 100;
	
	private List<TAMENode> selectedNodesList;
	
	private boolean creatingNewNode = false;
	
	public TAMEditorGesturesControl(ITAMEditor editor) {
		super(editor);
		setOnGestureListner(this);
		selectedNodesList = new ArrayList<TAMENode>();		
		editor.getListOfTouchControls().add(this);
		editor.getListOfDrawControls().add(this);
		editor.getListOfItemControls().add(this);
	}

	public void onSelectNodeEvent(TAMENode node) {		
		
		Log.d(TAG,"select node: " + node.getGui().getText());
		
		synchronized (selectedNodesList) {
			selectedNodesList.add(node);
		};
	}

	public void onUnselectNodeEvent(TAMENode node) {
		
		Log.d(TAG,"unselect node: " + node.getGui().getText());
		
		synchronized (selectedNodesList) {
			selectedNodesList.remove(node);
		};
	}
	
	public boolean onDown(MotionEvent e) {
		//Log.d(TAG,"onDown");
				
		return true;
	}

	public void onShowPress(MotionEvent e) {
		//Log.d(TAG,"onShowPress");
		
	}

	public boolean onSingleTapUp(MotionEvent e) {
		//Log.d(TAG,"onSingleTapUp");
		return true;
	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		//Log.d(TAG,"onScroll");
		return true;
	}

	public void onLongPress(MotionEvent e) {
		Log.d(TAG,"onLongPress" + selectedNodesList.size());

		
		// musi byt vybrany prave jeden uzol
		if(selectedNodesList.size() == 1){
			
			
			Vibrator vibrator = (Vibrator)editor.getContext().getSystemService(Context.VIBRATOR_SERVICE);
			if(vibrator.hasVibrator()){
				vibrator.vibrate(VIBRATE_DRUATION);
			}
			
			creatingNewNode = true;
			
			TAMENode selectedNode = selectedNodesList.get(0);

			
			TAMPNode pNode = MainActivity.getProfile().createNode("", "");
			ITAMENode eNode = pNode.addEReference(editor, (int)e.getX(), (int)e.getY());
			TAMPConnection pConnection = MainActivity.getProfile().createConnection(selectedNode.getProfile(), pNode);
			pConnection.addEReference(editor);
			//TAMENode newNode = selectedNode.addChild((int)e.getX(), (int)e.getY(), "","");
						
			selectedNode.getGui().setSelected(false);
			eNode.getGui().setSelected(true);
			
			editor.invalidate();					
		}		
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		Log.d(TAG,"onFling");
		
		
		return true;
	}

	public void onFinishAddChildNodeDialog(String title) {		
		
		if(selectedNodesList.size() == 1){
			selectedNodesList.get(0).getGui().setText(title);			
		}
		editor.invalidate();
				
		
	}
	/*
	public void onItemHitEvent(MotionEvent e, ITAMGItem item, float ax, float ay) {
		Log.d(TAG,"hit");
		
	}

	public void onItemMoveEvent(MotionEvent e, ITAMGItem item, int dx, int dy) {
		Log.d(TAG,"move - motionEvent: " + e.getAction() + 
				", buttonState:" + e.getButtonState());
		
	}
*/
	public void onDraw(Canvas canvas) {
		//Log.d(TAG,"draw");
				
		if(creatingNewNode){
			
			if(((TAMGraph)editor).isDragging) return;
			
			Log.d(TAG,"drop");
				
			if(selectedNodesList.size() == 1){
				creatingNewNode = false;
				
				showAddNodeDialog(selectedNodesList.get(0));					
			}
			
		}
		
		
	}

	public boolean onSingleTapConfirmed(MotionEvent e) {
		Log.d(TAG,"onSingleTapConfirmed");
		return true;
	}

	public boolean onDoubleTap(MotionEvent e) {
		Log.d(TAG,"onDoubleTap");
		return true;
	}

	public boolean onDoubleTapEvent(MotionEvent e) {
		Log.d(TAG,"onDoubleTapEvent");
		
		return true;
	}

	public void onItemHitEvent(MotionEvent e, ITAMGItem item, float ax, float ay) {
		// do nothing //
	}

	public void onItemMoveEvent(MotionEvent e, ITAMGItem item, int dx, int dy) {
		// do nothing //
	}

	public void onItemSelectEvent(ITAMGItem item, boolean selection) {
		if(item instanceof ITAMGNode) {
			TAMENode node = (TAMENode) item.getHelpObject();
			
			if(selection) {
				onSelectNodeEvent(node);
			} else {
				onUnselectNodeEvent(node);
			}
		}
	}

	public boolean onTouchEvent(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
}
