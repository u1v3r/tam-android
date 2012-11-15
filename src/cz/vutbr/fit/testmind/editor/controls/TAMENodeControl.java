package cz.vutbr.fit.testmind.editor.controls;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.preference.PreferenceManager.OnActivityResultListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import cz.vutbr.fit.testmind.EditNodeActivity;
import cz.vutbr.fit.testmind.MainActivity.EventObjects;
import cz.vutbr.fit.testmind.MainActivity.MenuItems;
import cz.vutbr.fit.testmind.R;
import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.editor.items.ITAMENode;
import cz.vutbr.fit.testmind.editor.items.TAMENode;
import cz.vutbr.fit.testmind.graphics.ITAMGItem;
import cz.vutbr.fit.testmind.graphics.ITAMGNode;
import cz.vutbr.fit.testmind.graphics.TAMGraph.ITAMItemGestureListener;
import cz.vutbr.fit.testmind.graphics.TAMGraph.ITAMItemListener;
import cz.vutbr.fit.testmind.graphics.TAMGraph.ITAMTouchListener;
import cz.vutbr.fit.testmind.graphics.TAMGraph.TAMGMotionEvent;

/**
 * Stara sa o zakladne operacie s uzlom (pridanie, odstranenie, uprava)
 */
public class TAMENodeControl extends TAMEAbstractControl  implements ITAMItemGestureListener, ITAMButtonListener,
                                                                     ITAMTouchListener, OnActivityResultListener,
                                                                     ITAMItemListener {
	
	private static final String TAG = "TAMEditorNodes";
	private static final long VIBRATE_DURATION = 100;
	
	public static final String NODE_TITLE = "title";
	public static final String NODE_BODY = "body";
	public static final String NODE_COLOR = "color";
	
	private ITAMENode selectedNode = null;
	
	public static enum BackgroundStyle {
		GREEN,BLUE,RED,PURPLE;
	}
	
	public interface ITAMNodeControlListener {
		public ITAMENode createNodeWithProfileAndConnection(String title, String body, ITAMENode parent, int posX, int posY);
	}
	
	private boolean creatingByGesture = false;
	private boolean waitingForClick = false;
	
	private float x,y;
	
	public TAMENodeControl(ITAMNodeControlListener editor) {
		super((ITAMEditor) editor);
		initializeListeners((ITAMEditor) editor);
	}
	
	private void initializeListeners(ITAMEditor editor) {
		editor.getListOfItemGestureControls().add(this);
		editor.getListOfOnActivityResultControls().add(this);
		editor.getListOfTouchControls().add(this);
		editor.getListOfItemControls().add(this);		
		editor.getListOfButtonControls().add(this);
	}
	
	/**
	 * Color generator
	 * 
	 * @param background
	 * @return
	 */
	private BackgroundStyle genrateColorId(int background) {
        
		BackgroundStyle color = BackgroundStyle.BLUE;
		
		int blue = editor.getResources().getColor(R.color.node_background_1);
		int green = editor.getResources().getColor(R.color.node_background_2);
		int red = editor.getResources().getColor(R.color.node_background_3);
		int purple = editor.getResources().getColor(R.color.node_background_4);
		
    	if(background == blue){
    		color = BackgroundStyle.BLUE;
    	}else if(background == green){
    		color = BackgroundStyle.GREEN;
    	}else if(background == red){
    		color = BackgroundStyle.RED;
    	}else if(background == purple){
    		color = BackgroundStyle.PURPLE;
    	}
    	
    	return color;
	}

	/**
	 * Cez dialog vytvori child pre vybrany parrent uzol
	 */
	public void addChild() {
		
		if(editor.getLastSelectedNode() == null) {
			Toast.makeText(editor.getContext(), R.string.parent_node_not_selected, Toast.LENGTH_LONG).show();
		} else {
			selectedNode = (ITAMENode) editor.getLastSelectedNode().getHelpObject();
			Toast.makeText(editor.getContext(), R.string.click, Toast.LENGTH_LONG).show();
			waitingForClick = true;
		}
	}

	/**
	 * Shows edit node dialog for actual selected node.
	 */
	public void showEditNodeDialog() {
		ITAMGNode gNode = editor.getLastSelectedNode();
		
		if(gNode != null) {
			showEditNodeDialog((ITAMENode) (gNode.getHelpObject()));
		} else {
			Toast.makeText(editor.getContext(), R.string.node_not_selected, Toast.LENGTH_LONG).show();
		}
	}
	
	/**
	 * Shows edit node dialog for selected node.
	 * 
	 * @param selectedNode
	 */
	public void showEditNodeDialog(ITAMENode selectedNode) {
		
		if(selectedNode == null) return;
		
		this.selectedNode = selectedNode;
		
		Intent intent = new Intent(editor.getContext(), EditNodeActivity.class);	
				
		intent.putExtra(NODE_TITLE, selectedNode.getProfile().getTitle());
		intent.putExtra(NODE_BODY, selectedNode.getProfile().getBody());				
		intent.putExtra(NODE_COLOR, genrateColorId(selectedNode.getGui().getColorBackground()));						
		
		activity.startActivityForResult(intent, EDIT_NODE_RESULT_CODE);
		
	}

	/**
	 * Some item is pressed for long time. As result of this action is created new child of pressed node.
	 */
	public void onItemLongSelectEvent(MotionEvent e, ITAMGNode node) {
		
		if(editor.getMode() == MenuItems.create_mode) {
			Vibrator vibrator = (Vibrator)editor.getContext().getSystemService(Context.VIBRATOR_SERVICE);
			if(vibrator.hasVibrator()){
				vibrator.vibrate(VIBRATE_DURATION);
			}
			
			creatingByGesture = true;
			
			// vytvori prazdny uzol
			TAMENode selectedNode = (TAMENode) node.getHelpObject();			
			ITAMENode eNode = ((ITAMNodeControlListener) editor).createNodeWithProfileAndConnection("","",selectedNode,(int) x, (int) y);
			
			editor.unselectAll();
			eNode.getGui().setSelected(true);
		}
	}

	/**
	 * When created node is released, dialog with name selection is opened.
	 */
	public void onItemLongReleaseEvent(MotionEvent e, ITAMGNode node) {
		if(creatingByGesture) {
				  
			creatingByGesture = false;
			
			showEditNodeDialog((TAMENode)node.getHelpObject());
		}
	}
	
	/**
	 * When user double clicks on node, edit dialog is opened.
	 */
	public void onItemDoubleTapEvent(MotionEvent e, ITAMGNode node) {
		
		int mode = editor.getMode();
		
		if(mode == MenuItems.create_mode) {
			showEditNodeDialog((TAMENode)node.getHelpObject());
		}
	}
	
	/**
	 * This method is called as result of edit node dialog.
	 */
	public boolean onActivityResult(int requestCode, int resultCode, Intent data) {

		//Log.d(TAG,getListOfSelectedNodes().toString() + "");
		
		if(resultCode == EDIT_NODE_RESULT_CODE){

			String nodeTitle = data.getStringExtra(NODE_TITLE);
			String nodeBody = data.getStringExtra(NODE_BODY);
			BackgroundStyle nodeColor = (BackgroundStyle)data.getSerializableExtra(NODE_COLOR);
			
			// musi byt vybrany jeden uzol
			if(selectedNode == null) return true; 
					
			
			if(nodeTitle != null){				
				selectedNode.getGui().setText(nodeTitle);
				selectedNode.getProfile().setTitle(nodeTitle);				
			}
			
			if(nodeBody != null){								
				selectedNode.getProfile().setBody(nodeBody);				
			}
						
			selectedNode.getGui().setBackgroundStyle(nodeColor);
				
			editor.invalidate();
		}

		return true;
	}
	
	/**
	 * This method is called as result of add node dialog.
	 */
	/*public void onFinishAddChildNodeDialog(String title) {
		
		// treba urcit, ci sa bude vytvarat child alebo len upravovat uz vytvoreny uzol
		if(creatingNewChild){
			
			addChildNode(title, (TAMENode) editor.getLastSelectedNode().getHelpObject());
			creatingNewChild = false;
			
		} else{// na vytvaranie bolo pouzite gesto, takze uzol uz existuje
			selectedNode.getGui().setText(title);
			selectedNode.getProfile().setTitle(title);
		}
			
		editor.invalidate();
	}*/

	public void onHitEvent(MotionEvent e, TAMGMotionEvent ge) {
		x = ge.dx;
		y = ge.dy;
		
		if(waitingForClick) {
			//if(e.getAction() == MotionEvent.ACTION_DOWN) {
				
				waitingForClick = false;
				
				ITAMENode node = ((ITAMNodeControlListener) editor).createNodeWithProfileAndConnection("", "", selectedNode, (int) ge.dx, (int) ge.dy);
				
				showEditNodeDialog(node);
			//}
		}
	}

	public void onItemSelectEvent(ITAMGItem item, boolean selection) {
		if(selection){
			selectedNode = (TAMENode)item.getHelpObject();
		}
	}

	public void onButtonSelected(View item) {
		
		if(item == EventObjects.btn_add) {
			addChild();
		} else if(item == EventObjects.btn_edit) {
			showEditNodeDialog();
		} else if(item == EventObjects.btn_delete) {
			
		}
	}

	public void onItemHitEvent(MotionEvent e, TAMGMotionEvent ge) {
		// TODO Auto-generated method stub
		
	}

	public void onItemMoveEvent(MotionEvent e, TAMGMotionEvent ge) {
		// TODO Auto-generated method stub
		
	}
}
