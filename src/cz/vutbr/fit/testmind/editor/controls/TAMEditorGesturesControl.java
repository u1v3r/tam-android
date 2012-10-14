package cz.vutbr.fit.testmind.editor.controls;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MenuItem;
import android.view.MotionEvent;
import cz.vutbr.fit.testmind.dialogs.AddNodeDialog.AddNodeDialogListener;
import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.editor.items.TAMEditorNode;
import cz.vutbr.fit.testmind.graphics.ITAMNode;

public class TAMEditorGesturesControl extends TAMEditorAbstractControl implements OnGestureListener,AddNodeDialogListener {
		
	
	private static final String TAG = "TAMEditorGesturesControl";
	
	private List<TAMEditorNode> selectedNodesList;	
	
	private GestureDetector gDetector;	
	
	private boolean creatingNewNode = false;
	
	public TAMEditorGesturesControl(ITAMEditor editor) {
		super(editor);
		gDetector = new GestureDetector(editor.getContext(),this);
		selectedNodesList = new ArrayList<TAMEditorNode>();
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
		TAMEditorNode selectedNode = (TAMEditorNode)node.getHelpObject();
		
		Log.d(TAG,"select node: " + selectedNode.getCore().getText());
		
		synchronized (selectedNodesList) {
			selectedNodesList.add(selectedNode);
		};
	}

	public void onUnselectNodeEvent(ITAMNode node) {
		
		TAMEditorNode selectedNode = (TAMEditorNode)node.getHelpObject();
		
		Log.d(TAG,"unselect node: " + selectedNode.getCore().getText());
		
		synchronized (selectedNodesList) {
			selectedNodesList.remove(selectedNode);
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
		
		// musi byt vybrany prave jeden uzol
		if(selectedNodesList.size() == 1){
			
			creatingNewNode = true;
			
			TAMEditorNode selectedNode = selectedNodesList.get(0);
			/*
			for (ITAMNode node : selectedNodesList) {
				selectedNode = node;
			}*/
			
					
			TAMEditorNode newNode = selectedNode.addChild((int)e.getX(), (int)e.getY(), "","");
						
			selectedNode.getCore().setSelected(false);
			newNode.getCore().setSelected(true);
			
			editor.invalidate();
			
			
			//showAddNodeDialog(selectedNode);
			
			
		}	
		
		
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		Log.d(TAG,"onFling");
		
		
		return true;
	}

	public void onFinishAddChildNodeDialog(String title) {
		// TODO Auto-generated method stub
		
	}
}
