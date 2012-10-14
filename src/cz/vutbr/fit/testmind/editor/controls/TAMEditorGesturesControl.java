package cz.vutbr.fit.testmind.editor.controls;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MenuItem;
import android.view.MotionEvent;
import cz.vutbr.fit.testmind.dialogs.AddNodeDialog.AddNodeDialogListener;
import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.editor.items.TAMENode;
import cz.vutbr.fit.testmind.graphics.ITAMGNode;
import cz.vutbr.fit.testmind.graphics.TAMGraph;
import cz.vutbr.fit.testmind.graphics.TAMGraph.ITAMDrawListener;
import cz.vutbr.fit.testmind.graphics.TAMGraph.ITAMTouchListener;

public class TAMEditorGesturesControl extends TAMEditorAbstractControl 
	implements OnGestureListener,OnDoubleTapListener, AddNodeDialogListener, 
	ITAMTouchListener,ITAMDrawListener{
		
	
	private static final String TAG = "TAMEditorGesturesControl";
	
	private List<TAMENode> selectedNodesList;	
	
	private GestureDetector gDetector;	
	
	private boolean creatingNewNode = false;
	
	public TAMEditorGesturesControl(ITAMEditor editor) {
		super(editor);
		gDetector = new GestureDetector(editor.getContext(),this);
		selectedNodesList = new ArrayList<TAMENode>();
	}
		
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean onTouchEvent(MotionEvent e) {
		gDetector.onTouchEvent(e);		
		return true;
	}

	public void onSelectNodeEvent(ITAMGNode node) {		
		TAMENode selectedNode = (TAMENode)node.getHelpObject();
		
		Log.d(TAG,"select node: " + selectedNode.getCore().getText());
		
		synchronized (selectedNodesList) {
			selectedNodesList.add(selectedNode);
		};
	}

	public void onUnselectNodeEvent(ITAMGNode node) {
		
		TAMENode selectedNode = (TAMENode)node.getHelpObject();
		
		Log.d(TAG,"unselect node: " + selectedNode.getCore().getText());
		
		synchronized (selectedNodesList) {
			selectedNodesList.remove(selectedNode);
		};
	}

	public void onMoveNodeEvent(ITAMGNode node) {
		Log.d(TAG,"onMoveNodeEvent");
		
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

		
		// musi byt vybrany prave jeden uzol
		if(selectedNodesList.size() == 1){
			
			creatingNewNode = true;
			
			TAMENode selectedNode = selectedNodesList.get(0);
			/*
			for (ITAMNode node : selectedNodesList) {
				selectedNode = node;
			}*/
			
					
			TAMENode newNode = selectedNode.addChild((int)e.getX(), (int)e.getY(), "","");
						
			selectedNode.getCore().setSelected(false);
			newNode.getCore().setSelected(true);
			
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
			selectedNodesList.get(0).getCore().setText(title);			
		}
				
		
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
		Log.d(TAG,"draw");
				
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
}
