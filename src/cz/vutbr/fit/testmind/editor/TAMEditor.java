package cz.vutbr.fit.testmind.editor;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ZoomControls;
import cz.vutbr.fit.testmind.R;
import cz.vutbr.fit.testmind.editor.items.TAMEditorConnection;
import cz.vutbr.fit.testmind.editor.items.TAMEditorFactory;
import cz.vutbr.fit.testmind.editor.items.TAMEditorNode;
import cz.vutbr.fit.testmind.graphics.ITAMNode;
import cz.vutbr.fit.testmind.graphics.TAMGraph;


/**
 * Obsahuje zakladne funkcie na pracu s grafom 
 *
 */
public class TAMEditor extends View implements TAMIEditor{
	
	/**
	 * Zabezpecuje jednoduchy pristup k jednotlivym polozkam menu
	 * 
	 */
	public final static class MenuItems{
		public static final int add = R.id.menu_add;
		public static final int edit = R.id.menu_edit;
		public static final int delete = R.id.menu_delete;
		public static final int save = R.id.menu_save;
		public static final int settings = R.id.menu_settings;
		public static final int importFile = R.id.menu_import; 
	}
	
	private TAMGraph graph;
	private TAMEditorNode root;
	
	private ZoomControls zoomControls;
	
	private List<TAMEditorNode> listOfEditorNodes;
	private List<TAMEditorConnection> listOfEditorConnections;
	private TAMEditorFactory factory;
	
		
	public TAMEditor(Context context) {
		super(context);
		
		zoomControls = (ZoomControls)findViewById(R.id.zoom_controls);
		
		
		listOfEditorNodes = new ArrayList<TAMEditorNode>();
		listOfEditorConnections = new ArrayList<TAMEditorConnection>();
		
		factory = new TAMEditorFactory(this);
	}
	
	public boolean containsNode(int id) {
		for(TAMEditorNode node : listOfEditorNodes) {
			if(id == node.getId()) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean containsConnection(int id) {
		for(TAMEditorConnection connection : listOfEditorConnections) {
			if(id == connection.getId()) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Vytvori root uzol, je obmedzene len na vytvorenie jedneho root uzlu
	 */
	public void addNode(){
		
	}
	
	
	/**
	 * Prida uzol, ktory obsahuje titulok
	 * 
	 * @param parent Rodicovsky uzol
	 * @param title Titulok uzlu
	 */
	public void addNode(ITAMNode parent, String title){
		
	}
	/*
	/**
	 * Prida uzol, ktory obsahue titulok a telo
	 * 
	 * @param parent
	 * @param title
	 * @param body
	 *
	public void addNode(ITAMNode parent, String title, String body){
		
	}
*/
	public TAMGraph getGraph() {
		return graph;
	}

	public TAMEditorNode getRoot() {
		return root;
	}

	public TAMEditorFactory getFactory() {
		return factory;
	}

	protected void setRoot(TAMEditorNode root) {
		this.root = root;
	}

	public List<TAMEditorNode> getListOfNodes() {
		return listOfEditorNodes;
	}

	public List<TAMEditorConnection> getListOfConnections() {
		return listOfEditorConnections;
	}

	public int getActionBar() {
		return R.menu.activity_main;
	}

	public int getLayout() {
		return R.layout.activity_main;
	}

}
