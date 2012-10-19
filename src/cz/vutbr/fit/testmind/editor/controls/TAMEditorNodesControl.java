package cz.vutbr.fit.testmind.editor.controls;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Environment;
import android.os.Vibrator;
import android.util.Log;
import android.view.MenuItem;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.widget.Toast;
import cz.vutbr.fit.testmind.MainActivity;
import cz.vutbr.fit.testmind.MainActivity.MenuItems;
import cz.vutbr.fit.testmind.R;
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
import cz.vutbr.fit.testmind.io.Serializer;
import cz.vutbr.fit.testmind.profile.TAMPConnection;
import cz.vutbr.fit.testmind.profile.TAMPNode;



/**
 * Stara sa o zakladne operacie s uzlom (pridanie, odstranenie, uprava)
 * 
 * @author Radovan Dvorsky
 *
 */
public class TAMEditorNodesControl extends TAMEditorAbstractControl  implements AddNodeDialogListener,
	ITAMMenuListener,OnGestureListener,OnDoubleTapListener, 
	ITAMTouchListener,ITAMDrawListener,ITAMItemListener {	
	
	private static final String DEFAULT_ROOT_TITLE = "root";
	private static final String DEFAULT_ROOT_BODY = "root body";
	private static final String TAG = "TAMEditorNodes";
	private static final String INTENT_MIME_TYPE = "text/xml";
		
	
	private static final long VIBRATE_DRUATION = 100;	
	private List<TAMENode> selectedNodesList;	
	private boolean creatingByGesture = false;
	private boolean creatingNewChild = false;
	
	public TAMEditorNodesControl(ITAMEditor editor) {
		super(editor);
		createDefaultRootNode();
		setOnGestureListner(this);
		selectedNodesList = new ArrayList<TAMENode>();
		editor.getListOfMenuControls().add(this);
				
		editor.getListOfTouchControls().add(this);
		editor.getListOfDrawControls().add(this);
		editor.getListOfItemControls().add(this);
	}
	

	/**
	 * Vytvori root node do stredu kresliacej plochy
	 */
	public void createDefaultRootNode(){
		
		// ak uz je jeden root uzol vytvoreny nesmie sa vytvorit druhy
		//if(editor.hasRootNode()) return;
		
		System.out.println("profile: " + MainActivity.getProfile());
		
		Point position = new Point(editor.getWidth()/2, editor.getHeight()/2);
		TAMPNode pNode = MainActivity.getProfile().createRoot(DEFAULT_ROOT_TITLE, DEFAULT_ROOT_BODY);
		pNode.addEReference(editor, position.x, position.y);
		//editor.createRoot(TAMGRectangleNode.NODE_TYPE_RECTANGLE, position.x, position.y, 
		//		DEFAULT_ROOT_TITLE, DEFAULT_ROOT_BODY);	
		
	}
		
	
	/**
	 * Cez dialog vytvori child pre vybrany parrent uzol
	 */
	public void showAddChildNodeDialog() {		
		//showAddChildNodeDialog((TAMEditorNode) editor.getLastSelectedNode().getHelpObject());
		
		creatingNewChild = true;
		
		if(editor.getLastSelectedNode() == null) {
			//System.out.println("context " + editor.getContext());
			Toast.makeText(editor.getContext(), 
					R.string.node_not_selected, Toast.LENGTH_LONG).show();
		} else {
			TAMENode parent = (TAMENode) editor.getLastSelectedNode().getHelpObject();
			showAddNodeDialog(parent);
		}
	}


	/**
	 * Prida child uzol k parent uzlu s automatickym umiestnenim
	 * 
	 * @param title Titulok uzlu
	 * @param parent Rodicovsky uzol 
	 */
	public void addChildNode(String title, TAMENode parent) {
			
		/*
		 * TODO: Treba vytovorit triedu, ktora bude rozmiestnovat nove uzly po ploche
		 */
		addChildNode(title, "", parent, 
				new Point(parent.getGui().getPosition().x, parent.getGui().getPosition().y));
	}
	
	/**
	 * Prida child uzol k parent uzlu s automatickym umiestnenim
	 * 
	 * @param title Titulok uzlu
	 * @param body Telo uzlu
	 * @param parent Rodicovsky uzol 
	 */
	public void addChildNode(String title, String body, TAMENode parent) {
			
		/*
		 * TODO: Treba vytovorit triedu, ktora bude rozmiestnovat nove uzly po ploche
		 */
		addChildNode(title, body, parent, 
				new Point(parent.getGui().getPosition().x, parent.getGui().getPosition().y));
	}
	
	/**
	 * Vytovori child uzol k parent uzlu a umiestni ho na zadanu poziciu
	 * 
	 * @param title Titulok uzlu
	 * @param body Telo uzlu
	 * @param parent Rodicovsky uzol 
	 * @param position Pozicia child uzlu
	 */
	public void addChildNode(String title, String body, TAMENode parent, Point position){		
		
		
		createNode(title, body, parent.getProfile(), position.x, position.y);
		
		/*
		TAMPNode pNode = MainActivity.getProfile().createNode(title, body);
		pNode.addEReference(editor, posX, posY);
		TAMPConnection pConnection = MainActivity.getProfile().createConnection(parent.getProfile(), pNode);
		pConnection.addEReference(editor);
		*/
		
		Log.d(TAG,MainActivity.getProfile().getListOfPNodes().toString());
	}

	/**
	 * @deprecated Nepouzivat bude sa presuvat do noveho control
	 * 
	 *  Zobrazi file manager s moznostou vyberu suboru
	 */
	public void importFile() {
		
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT); 
        intent.setType(INTENT_MIME_TYPE); 
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            activity.startActivityForResult(
            		
                    Intent.createChooser(intent, 
                    		editor.getResources().getString(R.string.select_file_to_upload)),
                    		MainActivity.PICK_FILE_RESULT_CODE);
            
        } catch (ActivityNotFoundException e) {
            
        	// vyzve uzivatela na instalaciu file managera
            Toast.makeText(editor.getContext(), 
            		editor.getResources().getString(R.string.install_file_manager), 
                    Toast.LENGTH_SHORT).show();
        }
	}
	
	public void onFinishAddChildNodeDialog(String title) {
		
		if(creatingNewChild){
			
			addChildNode(title, (TAMENode) editor.getLastSelectedNode().getHelpObject());
			creatingNewChild = false;
			
		} else{
			
			// ak vytvara cez gesto, tak len prepise text uzlu		
			if(selectedNodesList.size() == 1){
				selectedNodesList.get(0).getGui().setText(title);
				selectedNodesList.get(0).getProfile().setTitle(title);
			}
			
			editor.invalidate();		
		}
	}

	public boolean onOptionsItemSelected(MenuItem item) {

/*		
		if(item.getItemId() == MenuItems.add) {
			showAddChildNodeDialog();
			return true;
		} else if(item.getItemId() == MenuItems.importFile) {
			FreeMind freeMind = new FreeMind();
			try {
				freeMind.getMindMap();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		} else {
			return false;
		}
*/		

		/**
		 * TODO: open, save, import, export menu treba presunut do vlastnych control
		 */
		
		switch (item.getItemId()) {
			case MenuItems.add:		
				showAddChildNodeDialog();
				return true;
			case MenuItems.edit:
				
				break;
			case MenuItems.delete:
							
				break;
			case MenuItems.save:
			    File cardDirectory = Environment.getExternalStorageDirectory();
				Serializer serializer = new Serializer(cardDirectory.getPath()+"/TestMind.db");
				serializer.serialize(MainActivity.getProfile());
			    return true;
			case MenuItems.importFile:
				importFile();
				return true;
			case MenuItems.settings:
	
				break;
			default: 
				return false;    	
    	} 	
    	
    	return true;		
	}


	private void onSelectNodeEvent(TAMENode node) {		
		
		Log.d(TAG,"select node: " + node.getGui().getText());
		
		synchronized (selectedNodesList) {
			selectedNodesList.add(node);
		};
	}

	private void onUnselectNodeEvent(TAMENode node) {
		
		Log.d(TAG,"unselect node: " + node.getGui().getText());
		
		synchronized (selectedNodesList) {
			selectedNodesList.remove(node);
		};
	}


	public void onMoveNodeEvent(ITAMGNode node) {
		// TODO Auto-generated method stub
		
	}


	public void onItemHitEvent(MotionEvent e, ITAMGItem item, float ax, float ay) {
		// TODO Auto-generated method stub
		
	}


	public void onItemMoveEvent(MotionEvent e, ITAMGItem item, int dx, int dy) {
		// TODO Auto-generated method stub
		
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

	public void onDraw(Canvas canvas) {
		
		if(creatingByGesture){
			
			if(((TAMGraph)editor).isDragging) return;
			
			Log.d(TAG,"drop");
				
			if(selectedNodesList.size() == 1){
				  
				creatingByGesture = false;
				
				showAddNodeDialog(selectedNodesList.get(0));					
			}
			
		}
	}


	
	public boolean onTouchEvent(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean onSingleTapConfirmed(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean onDoubleTap(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean onDoubleTapEvent(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
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
		Log.d(TAG,"onLongPress" + selectedNodesList.size());

		
		// musi byt vybrany prave jeden uzol
		if(selectedNodesList.size() == 1){
			
			
			Vibrator vibrator = (Vibrator)editor.getContext().getSystemService(Context.VIBRATOR_SERVICE);
			if(vibrator.hasVibrator()){
				vibrator.vibrate(VIBRATE_DRUATION);
			}
			
			creatingByGesture = true;
			
			// vytvori prazdny uzol
			TAMENode selectedNode = selectedNodesList.get(0);			
			ITAMENode eNode = createNode("","",selectedNode.getProfile(),(int)e.getX(),(int)e.getY());
						
			selectedNode.getGui().setSelected(false);
			eNode.getGui().setSelected(true);
			
			editor.invalidate();					
		}		
	}


	private ITAMENode createNode(String title, String body, TAMPNode parent, int posX, int posY) {
		
		TAMPNode newProfileNode = MainActivity.getProfile().createNode(title, body);
		ITAMENode newEditorNode = newProfileNode.addEReference(editor, posX, posY);
		TAMPConnection pConnection = MainActivity.getProfile().createConnection(parent, newProfileNode);
		pConnection.addEReference(editor);
		//TAMENode newNode = selectedNode.addChild((int)e.getX(), (int)e.getY(), "","");
		
		return newEditorNode;		
	}


	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}	
}
