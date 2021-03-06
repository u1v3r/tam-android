package cz.vutbr.fit.testmind.editor.controls;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
import cz.vutbr.fit.testmind.editor.controls.TAMEToolbarContol.ITAMToolbarControlItem;
import cz.vutbr.fit.testmind.editor.items.ITAMENode;
import cz.vutbr.fit.testmind.editor.items.TAMENode;
import cz.vutbr.fit.testmind.graphics.ITAMGConnection;
import cz.vutbr.fit.testmind.graphics.ITAMGItem;
import cz.vutbr.fit.testmind.graphics.ITAMGNode;
import cz.vutbr.fit.testmind.graphics.TAMGraph.ITAMItemGestureListener;
import cz.vutbr.fit.testmind.graphics.TAMGraph.ITAMItemListener;
import cz.vutbr.fit.testmind.graphics.TAMGraph.ITAMTouchListener;
import cz.vutbr.fit.testmind.graphics.TAMGraph.TAMGMotionEvent;
import cz.vutbr.fit.testmind.profile.TAMPConnection;
import cz.vutbr.fit.testmind.profile.TAMPConnectionFactory;
import cz.vutbr.fit.testmind.profile.TAMPNode;
import cz.vutbr.fit.testmind.profile.Tag;

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
	public static final String NODE_TAGS = "tags";
	
	private ITAMENode selectedNode = null;
	
	public interface ITAMNodeControlListener {
		public ITAMENode createNodeWithProfileAndConnection(String title, String body, ITAMENode parent, int posX, int posY);
	}
	
	private boolean creatingByGesture = false;
	private boolean waitingForClick = false;
	
	private float x,y;
	
	private HashSet<Integer> removedConnections = new HashSet<Integer>();
	
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
		
		// ak existuje len root, tak ho zvol,riesi problem s nenastanevym selectedNode pri prvom spusteni
		if(this.editor.getProfile().getRoot() != null && this.editor.getListOfENodes().size() == 1){
			selectedNode = this.editor.getListOfENodes().get(0);
		}
	}

	/**
	 * Cez dialog vytvori child pre vybrany parrent uzol
	 */
	private void addChildNode() {		
		if(selectedNode == null) {
			Toast.makeText(editor.getContext(), R.string.parent_node_not_selected, Toast.LENGTH_LONG).show();
		} else {			
			Toast.makeText(editor.getContext(), R.string.click, Toast.LENGTH_LONG).show();
			// caka na dalsie kliknutie na platno v metode onHitEvent()
			waitingForClick = true;
		}
	}


	/**
	 * Shows edit node dialog for selected node.
	 */
	private void openEditNodeActivity() {		
		
		if(selectedNode == null){
			Toast.makeText(editor.getContext(), R.string.node_not_selected, Toast.LENGTH_LONG).show();
			return;
		}
		
		openEditNodeActivity(selectedNode);		
	}
	
	/**
	 * Shows edit node dialog for provided node.
	 * 
	 * @param selectedNode
	 */
	private void openEditNodeActivity(ITAMENode node) {		
		
		selectedNode = node;
		
		Intent intent = new Intent(editor.getContext(), EditNodeActivity.class);	
		node.getProfile().setTitle(node.getProfile().getTitle().trim());
		
		intent.putExtra(NODE_TITLE, node.getProfile().getTitle());
		intent.putExtra(NODE_BODY, node.getProfile().getBody());				
		intent.putExtra(NODE_COLOR, node.getBackgroundStyle());		
		intent.putExtra(NODE_TAGS, (Serializable)node.getProfile().getListOfTags());

		activity.startActivityForResult(intent, REQUEST_CODES.EDIT_NODE);
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
			ITAMENode eNode = 
					((ITAMNodeControlListener) editor).createNodeWithProfileAndConnection(
							"             ",
							"",
							selectedNode,
							(int) x, (int) y);
			
			editor.unselectAll();
			eNode.getGui().setSelected(true);
		}
	}

	/**
	 * When created node is released, dialog with name selection is opened.
	 */
	public void onItemLongReleaseEvent(MotionEvent e, ITAMGNode node) {
		if(creatingByGesture) {		
			
			openEditNodeActivity((TAMENode)node.getHelpObject());
			
		}
	}
	
	/**
	 * When user double clicks on node, edit dialog is opened.
	 */
	public void onItemDoubleTapEvent(MotionEvent e, ITAMGNode node) {
		
		int mode = editor.getMode();
		
		if(mode == MenuItems.create_mode) {
			openEditNodeActivity((TAMENode)node.getHelpObject());
		}
	}
	
	/**
	 * This method is called as result of edit node dialog.
	 */
	public boolean onActivityResult(int requestCode, int resultCode, Intent data) {

		////Log.d(TAG,getListOfSelectedNodes().toString() + "");
		
		////Log.d(TAG,"req:" + requestCode + ", result:" + resultCode);
		
		if(requestCode == REQUEST_CODES.EDIT_NODE && resultCode == EDIT_NODE_RESULT_CODE){

			String nodeTitle = data.getStringExtra(NODE_TITLE);
			String nodeBody = data.getStringExtra(NODE_BODY);			
			int nodeColor = data.getIntExtra(NODE_COLOR, ITAMENode.BLUE);
			
			// musi byt vybrany jeden uzol
			if(selectedNode == null) return true; 
						
			selectedNode.getProfile().setListOfTags((List<Tag>) data.getSerializableExtra(NODE_TAGS));
			
			if(nodeTitle != null){				
				selectedNode.getGui().setText(nodeTitle);
				selectedNode.getProfile().setTitle(nodeTitle);				
			}
			
			if(nodeBody != null){								
				selectedNode.getProfile().setBody(nodeBody);				
			}
			
			selectedNode.setBackgroundStyle(nodeColor);			
			
			editor.invalidate();
			
			// pre istotu vzdy false
			creatingByGesture = false;
			
			return true;
		}
		
		////Log.d(TAG,"gesture: " + creatingByGesture);
		
		if(requestCode == REQUEST_CODES.EDIT_NODE && resultCode == Activity.RESULT_CANCELED && creatingByGesture){
			editor.unselectAll();
			deleteTraverse(selectedNode.getProfile());
			editor.invalidate();
			
			// pre istotu vzdy false
			creatingByGesture = false;
			
			return true;
		}
		

		
		return false;
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
								
				ITAMENode node = ((ITAMNodeControlListener) editor).createNodeWithProfileAndConnection(
						"", "", selectedNode, (int) ge.dx, (int) ge.dy);
				
				editor.unselectAll();
				node.getGui().setSelected(true);
				openEditNodeActivity(node);
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
			addChildNode();
		} else if(item == EventObjects.btn_edit) {
			openEditNodeActivity();
		} else if(item == EventObjects.btn_delete) {
			deleteSelectedNodeSubTree();
		}
	}

	private void deleteSelectedNodeSubTree() {
		
		if(selectedNode == null) return;
		
		showDeleteAlertDialogAndDelete();
		
		
	}

	private void showDeleteAlertDialogAndDelete() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setMessage(R.string.main_activity_delete_alert_dialog_content)
			   .setTitle(R.string.main_activity_delete_alert_dialog_title);
		
		builder.setPositiveButton(R.string.ok, new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				editor.unselectAll();	
				deleteTraverse(selectedNode.getProfile());
				editor.invalidate();
				dialog.dismiss();
				((ITAMToolbarControlItem)editor).hideToolbar();
			}
		});
		
		builder.setNegativeButton(R.string.cancel, new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		
		builder.create().show();
	}

	/**
	 * Traverse subtree and delete nodes
	 * @param root
	 */
	private void deleteTraverse(TAMPNode root) {		
		
		// odstrani listy
		if (root.getListOfChildNodes().isEmpty() && root.getId() != 1){			
			////Log.d(TAG, "delete node: " + root.getTitle());			
			removeConnection(root);
			editor.getProfile().deleteNodeTest(root);			
			return;
		}
		
		// treba riesit cez ListIterator inak vyhadzuje ConcurrentModificationException
		ListIterator<TAMPNode> iterator = root.getListOfChildNodes().listIterator();
		while (iterator.hasNext()) {			
			TAMPNode pNode = iterator.next();
			deleteTraverse(pNode);
		}		
		
		if(root.getId() != 1){//root mapy sa nikdy nesmie odstranit
			////Log.d(TAG, "delete node: " + root.getTitle());
			removeConnection(root);
			editor.getProfile().deleteNodeTest(root);			
		}
	}
	
	private void removeConnection(TAMPNode node){

		// TODO: ked bude cas, tak by bolo dobre to skusit spravit aj nejak rozumnejsie
		ListIterator<TAMPConnection> iterator = editor.getProfile().getListOfPConnections().listIterator();		
		while(iterator.hasNext()){
			TAMPConnection conn = iterator.next();
			if(conn.getChild().equals(node)){
				removedConnections.add(conn.getId());			
			}
		}		
		
		deleteConnections();
	}
	
	private void deleteConnections() {
		for (int id : removedConnections) {
			////Log.d(TAG, "mazem conn: " + id);	
			editor.getProfile().deleteConnection(id);
		}	
	}


	public void onItemHitEvent(MotionEvent e, TAMGMotionEvent ge) {
		// TODO Auto-generated method stub
		
	}

	public void onItemMoveEvent(MotionEvent e, TAMGMotionEvent ge) {
		// TODO Auto-generated method stub
		
	}

	public void onTouchEvent(MotionEvent e, float dx, float dy) {
		// TODO Auto-generated method stub
		
	}
}
