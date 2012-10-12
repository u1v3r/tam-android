package cz.vutbr.fit.testmind.editor.controls;

import android.graphics.Point;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import cz.vutbr.fit.testmind.R;
import cz.vutbr.fit.testmind.dialogs.AddNodeDialog;
import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.editor.TAMEditor;
import cz.vutbr.fit.testmind.editor.items.TAMEditorNode;
import cz.vutbr.fit.testmind.graphics.ITAMNode;
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
		
		
		Point position = new Point(this.view.getWidth()/2, this.view.getHeight()/2);
		
		ITAMNode core = graph.addRoot(TAMRectangleNode.NODE_TYPE_RECTANGLE, 
				position.x, position.y, DEFAULT_ROOT_TITLE);
		
		TAMEditorNode rootEditorNode = editor.createRoot(TAMRectangleNode.NODE_TYPE_RECTANGLE,
				position.x, position.y, 
				DEFAULT_ROOT_TITLE, DEFAULT_ROOT_BODY,
				core);
		
		
		editor.setRoot(rootEditorNode);
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
		addChildNode(title, parent, 
				new Point(parent.getCore().getPosition().x, parent.getCore().getPosition().y));
	}
	
	/**
	 * Vytovori child uzol k parent uzlu a umiestni ho na zadanu poziciu
	 * 
	 * @param title Titulok uzlu
	 * @param parent Rodicovsky uzol
	 * @param position Pozicia child uzlu
	 */
	public void addChildNode(String title, TAMEditorNode parent, Point position){		
		
		int posX = position.x;
		int posY = position.y;
		editor.getFactory().createNode(posX, posY, title, "",TAMRectangleNode.NODE_TYPE_RECTANGLE);
		/*
		ITAMNode core = (ITAMNode) parent.getCore().addChild(posX, posY, title);
		
		TAMEditorNode editorNode = new TAMEditorNode((TAMEditor) editor,posX,posY,title,"",
				TAMRectangleNode.NODE_TYPE_RECTANGLE,core);
		
		editor.getListOfNodes().add(editorNode);		
		parent.getListOfChildNodes().add(editorNode);
		*/
	}


	public void zoomIn(ZoomEventListener graph) {
		// TODO Auto-generated method stub
		
	}


	public void zoomOut(ZoomEventListener graph) {
		// TODO Auto-generated method stub
		
	}
	
}
