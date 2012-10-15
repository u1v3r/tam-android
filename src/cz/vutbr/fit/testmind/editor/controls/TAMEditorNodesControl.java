package cz.vutbr.fit.testmind.editor.controls;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Point;
import android.view.MenuItem;
import android.widget.Toast;
import cz.vutbr.fit.testmind.MainActivity;
import cz.vutbr.fit.testmind.MainActivity.MenuItems;
import cz.vutbr.fit.testmind.R;
import cz.vutbr.fit.testmind.dialogs.AddNodeDialog.AddNodeDialogListener;
import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.editor.items.ITAMENode;
import cz.vutbr.fit.testmind.editor.items.TAMENode;
import cz.vutbr.fit.testmind.graphics.ITAMGNode;
import cz.vutbr.fit.testmind.profile.TAMPConnection;
import cz.vutbr.fit.testmind.profile.TAMPNode;


/**
 * Stara sa o zakladne operacie s uzlom (pridanie, odstranenie, uprava)
 * 
 * @author Radovan Dvorsky
 *
 */
public class TAMEditorNodesControl extends TAMEditorAbstractControl  implements AddNodeDialogListener, ITAMMenuListener {
		
	private static final String DEFAULT_ROOT_TITLE = "root";
	private static final String DEFAULT_ROOT_BODY = "root body";
	private static final String TAG = "TAMEditorNodes";
	private static final String INTENT_MIME_TYPE = "text/xml";
		

	public TAMEditorNodesControl(ITAMEditor editor) {
		super(editor);
		createDefaultRootNode();
	}
	

	/**
	 * Vytvori root node do stredu kresliacej plochy
	 */
	public void createDefaultRootNode(){
		
		// ak uz je jeden root uzol vytvoreny nesmie sa vytvorit druhy
		//if(editor.hasRootNode()) return;
		
		System.out.println("profile: " + MainActivity.getProfile());
		
		Point position = new Point(this.editor.getWidth()/2, this.editor.getHeight()/2);
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
		
		int posX = position.x;
		int posY = position.y;
		
		TAMPNode pNode = MainActivity.getProfile().createNode("", "");
		pNode.addEReference(editor, posX, posY);
		TAMPConnection pConnection = MainActivity.getProfile().createConnection(parent.getProfile(), pNode);
		pConnection.addEReference(editor);
	}

	/**
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
		addChildNode(title, (TAMENode) editor.getLastSelectedNode().getHelpObject());
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		
		
		switch (item.getItemId()) {
			case MenuItems.add:		
				showAddChildNodeDialog();
				return true;
			case MenuItems.edit:
				
				break;
			case MenuItems.delete:
							
				break;
			case MenuItems.save:
				
				break;
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
