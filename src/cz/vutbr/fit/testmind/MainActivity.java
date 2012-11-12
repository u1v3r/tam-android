package cz.vutbr.fit.testmind;

import java.io.Serializable;
import java.util.List;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ZoomControls;
import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.editor.TAMEditorMain;
import cz.vutbr.fit.testmind.editor.TAMEditorTest;
import cz.vutbr.fit.testmind.profile.TAMPConnection;
import cz.vutbr.fit.testmind.profile.TAMPNode;
import cz.vutbr.fit.testmind.profile.TAMProfile;

public class MainActivity extends FragmentActivity {
	
	/**
	 * Zabezpecuje jednoduchy pristup k jednotlivym polozkam menu
	 * 
	 */
	public final static class MenuItems {
		public static final int open = R.id.menu_open;
		public static final int save = R.id.menu_save;		
		public static final int settings = R.id.menu_settings;
		public static final int importFile = R.id.menu_import;
		public static final int exportFile = R.id.menu_export;
		public static final int create_mode = R.id.menu_create_mode;
		public static final int view_mode = R.id.menu_view_mode;
		public static final int test_structure = R.id.menu_test_structure;
		public static final int edit_structure = R.id.menu_edit_structure;
		public static final int test_content = R.id.menu_test_content;
	}
	
	/**
	 * Zabezpecuje jednoduchy pristup k jednotlivym polozkam toolbaru
	 * 
	 */
	public final static class ButtonItems {
		public static final int add = R.id.button_add;
		public static final int delete = R.id.button_delete;
		public static final int edit = R.id.button_edit;
		public static final int hide_one = R.id.button_hide_one;
		public static final int hide_all = R.id.button_hide_all;
		public static final int view = R.id.button_view;
		public static final int zoom_in = R.id.button_zoom_in;
		public static final int zoom_out = R.id.button_zoom_out;
	}
	
	public static class EventObjects {
		public static ZoomControls zoomControls;
		public static TAMEditorMain editor_main;
		public static TAMEditorTest editor_test;
		
		public static View btn_add;
		public static View btn_delete;
		public static View btn_edit;
		public static View btn_hide_one;
		public static View btn_hide_all;
		public static View btn_view;
		public static View btn_zoom_in;
		public static View btn_zoom_out;
		
		public static Menu menu;
		
		public static MenuItem menu_create;
		public static MenuItem menu_view;
		
		public static Animation animAlpha;
	}
		
	private static final String TAG = "MainActivity";
	private static final String CONNECTION_LIST = "connections";
	private static final String NODES_LIST = "nodes";
	
	private ITAMEditor actualEditor;
	private List<TAMPNode> listOfNodes;
	private List<TAMPConnection> listOfConnections;
	
	private static TAMProfile profile;
	private static MainActivity mainActivityInstance;	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {    	
    	super.onCreate(savedInstanceState);	
    	   	
    	
    	//if(profile != null) {
    		profile = new TAMProfile();
    	//}		
    	  	
    	
    	setContentView(R.layout.activity_main);
    	
    	EventObjects.editor_main = (TAMEditorMain) findViewById(R.id.acitity_main_tam_editor);
    	EventObjects.editor_test = (TAMEditorTest) findViewById(R.id.acitity_test_tam_editor);
    	
    	actualEditor = EventObjects.editor_main;
    	
    	EventObjects.btn_add = findViewById(R.id.button_add);
		EventObjects.btn_delete = findViewById(R.id.button_delete);
		EventObjects.btn_edit = findViewById(R.id.button_edit);
		EventObjects.btn_hide_one = findViewById(R.id.button_hide_one);
		EventObjects.btn_hide_all = findViewById(R.id.button_hide_all);
		EventObjects.btn_view = findViewById(R.id.button_view);
		EventObjects.btn_zoom_in = findViewById(R.id.button_zoom_in);
		EventObjects.btn_zoom_out = findViewById(R.id.button_zoom_out);
		
		//EventObjects.menu_create = (MenuItem) findViewById(R.id.menu_create_mode);
		//EventObjects.menu_view = (MenuItem) findViewById(R.id.menu_view_mode);
		
		EventObjects.animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
    	
		// initialize editors //
    	EventObjects.editor_main.initialize(profile);
    	EventObjects.editor_test.initialize(profile);
    	
    	mainActivityInstance = this;
    }
	
	/**
	 * Po otoceni zariadenia nevymaze obsah
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {		
		
		super.onRestoreInstanceState(savedInstanceState);
		
		Log.d(TAG,"velkost  pred: " + profile.getListOfPNodes().size());
		
		listOfNodes = (List<TAMPNode>) savedInstanceState.getSerializable(NODES_LIST);
    	listOfConnections = (List<TAMPConnection>) savedInstanceState.getSerializable(CONNECTION_LIST);
    	
    	
    	Log.d(TAG, "saved instance initialization:" + listOfNodes.size());
    	
    	if(listOfNodes.size() < 1) return;
    	
    	TAMPNode rootNode = listOfNodes.get(0);
    	      
    	/*
    	profile.importRoot(rootNode.getTitle(), rootNode.getBody(), rootNode.getId());
    	
    	for (int i = 1; i < listOfNodes.size(); i++) {
    		TAMPNode node = listOfNodes.get(0);
			profile.importNode(node.getTitle(), node.getBody(), node.getId());
			
		}        	
    	
    	for (TAMPConnection conn : listOfConnections) {
			profile.importConnection(conn.getParent(), conn.getChild(), conn.getId());
		}
    	*/
    	EventObjects.editor_main.invalidate();
		
	}
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	
    	outState.putSerializable(NODES_LIST, (Serializable)profile.getListOfPNodes());
    	outState.putSerializable(CONNECTION_LIST, (Serializable)profile.getListOfPConnections());
    	
    	super.onSaveInstanceState(outState);
    }
        
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	getMenuInflater().inflate(R.menu.activity_main, menu);    
    	
    	EventObjects.menu = menu;
    	EventObjects.menu_create = menu.findItem(MenuItems.create_mode).setVisible(true);
		EventObjects.menu_view = menu.findItem(MenuItems.view_mode).setVisible(true);
		
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	int id = item.getItemId();
    	
    	if(MenuItems.edit_structure == id){
			if(actualEditor != EventObjects.editor_main) {
				actualEditor.setVisibility(View.GONE);
				actualEditor = EventObjects.editor_main;
				actualEditor.setVisibility(View.VISIBLE);
			}
		} else if(MenuItems.test_structure == id){
			if(actualEditor != EventObjects.editor_test) {
				actualEditor.setVisibility(View.GONE);
				actualEditor = EventObjects.editor_test;
				actualEditor.setVisibility(View.VISIBLE);
			}
		} else if(MenuItems.test_content == id){
			// TODO: open another activity //
		} else {
			EventObjects.editor_main.onOptionsItemSelected(item);
			return super.onOptionsItemSelected(item);
		}
    	return true;
    }
    
    public void buttonPressed(View view) {
    	EventObjects.editor_main.onButtonSelected(view);
    }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		EventObjects.editor_main.onActivityResult(requestCode, resultCode, data);		
	}

	public static TAMProfile getProfile() {
		return profile;
	}
	
    public static MainActivity getMainActivityInstance() {
        return mainActivityInstance;
    }
}