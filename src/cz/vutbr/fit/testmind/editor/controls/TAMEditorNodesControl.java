package cz.vutbr.fit.testmind.editor.controls;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Point;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Toast;
import cz.vutbr.fit.testmind.MainActivity;
import cz.vutbr.fit.testmind.R;
import cz.vutbr.fit.testmind.dialogs.AddNodeDialog;
import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.editor.items.TAMEditorNode;
import cz.vutbr.fit.testmind.graphics.TAMGraph;
import cz.vutbr.fit.testmind.graphics.TAMRectangleNode;


/**
 * Stara sa o zakladne operacie s uzlom (pridanie, odstranenie, uprava)
 * 
 * @author Radovan Dvorsky
 *
 */
public class TAMEditorNodesControl extends TAMEditorAbstractControl{
		
	private static final String DEFAULT_ROOT_TITLE = "root";
	private static final String DEFAULT_ROOT_BODY = "root body";
	private static final String TAG = "TAMEditorNodes";
	private static final String INTENT_MIME_TYPE = "text/xml";
		
	private ITAMEditor editor;
	private TAMGraph graph;
	private FragmentActivity activity;
	private View view;
	
	public TAMEditorNodesControl(ITAMEditor editor) {
		this.editor = editor;
		this.graph = editor.getGraph();
		this.view = editor.getView();
		this.activity = (FragmentActivity)this.view.getContext();
	}
	

	/**
	 * Vytvori root node do stredu kresliacej plochy
	 */
	public void createDefaultRootNode(){
		
		// ak uz je jeden root uzol vytvoreny nesmie sa vytvorit druhy
		if(editor.hasRootNode()) return;
		
		Point position = new Point(this.graph.getWidth()/2, this.graph.getHeight()/2);
		
		editor.createRoot(TAMRectangleNode.NODE_TYPE_RECTANGLE, position.x, position.y, 
				DEFAULT_ROOT_TITLE, DEFAULT_ROOT_BODY);	
		
	}
		
	
	/**
	 * Cez dialog vytvori child pre vybrany parrent uzol
	 */
	public void showAddChildNodeDialog() {		
		showAddChildNodeDialog(editor.getLastSelectedNode());
	}
	
	/**
	 * Cez dialog vytvori child pre vybrany parrent uzol 
	 * 
	 * @param parent Rodicovsky uzol ku ktoremu bude vytvoreny child uzol
	 */
	public void showAddChildNodeDialog(TAMEditorNode parent) {
			
		if(parent != null){
			showAddNodeDialog(parent);
		}else{
			Toast.makeText(editor.getView().getContext(), 
					R.string.node_not_selected, Toast.LENGTH_LONG).show();			
		}
		
	}
	
	/**
	 * Zobrazí dialog na pridanie uzlu
	 * @param parent 
	 */
	private void showAddNodeDialog(TAMEditorNode parent) {
			
		FragmentManager fm = activity.getSupportFragmentManager();		
		AddNodeDialog dialog = new AddNodeDialog();
		dialog.show(fm, "fragment_add_node");
		
	}
	
	/**
	 * Prida child uzol k parent uzlu s automatickym umiestnenim
	 * 
	 * @param title Titulok uzlu
	 * @param parent Rodicovsky uzol 
	 */
	public void addChildNode(String title, TAMEditorNode parent) {
			
		/*
		 * TODO: Treba vytovorit triedu, ktora bude rozmiestnovat nove uzly po ploche
		 */
		addChildNode(title, "", parent, 
				new Point(parent.getCore().getPosition().x, parent.getCore().getPosition().y));
	}
	
	/**
	 * Prida child uzol k parent uzlu s automatickym umiestnenim
	 * 
	 * @param title Titulok uzlu
	 * @param body Telo uzlu
	 * @param parent Rodicovsky uzol 
	 */
	public void addChildNode(String title, String body, TAMEditorNode parent) {
			
		/*
		 * TODO: Treba vytovorit triedu, ktora bude rozmiestnovat nove uzly po ploche
		 */
		addChildNode(title, body, parent, 
				new Point(parent.getCore().getPosition().x, parent.getCore().getPosition().y));
	}
	
	/**
	 * Vytovori child uzol k parent uzlu a umiestni ho na zadanu poziciu
	 * 
	 * @param title Titulok uzlu
	 * @param body Telo uzlu
	 * @param parent Rodicovsky uzol 
	 * @param position Pozicia child uzlu
	 */
	public void addChildNode(String title, String body, TAMEditorNode parent, Point position){		
		
		int posX = position.x;
		int posY = position.y;
		
		parent.addChild(posX, posY, title, body);
	}


	public void zoomIn(ZoomEventListener graph) {
		// TODO Auto-generated method stub
		
	}


	public void zoomOut(ZoomEventListener graph) {
		// TODO Auto-generated method stub
		
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
                    		editor.getView().getResources().getString(R.string.select_file_to_upload)),
                    		MainActivity.PICK_FILE_RESULT_CODE);
            
        } catch (ActivityNotFoundException e) {
            
        	// vyzve uzivatela na instalaciu file managera
            Toast.makeText(editor.getView().getContext(), 
            		editor.getView().getResources().getString(R.string.install_file_manager), 
                    Toast.LENGTH_SHORT).show();
        }
	}
	
	
	
}
