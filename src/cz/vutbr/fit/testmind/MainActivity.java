package cz.vutbr.fit.testmind;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ZoomControls;
import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.editor.TAMEditorMain;
import cz.vutbr.fit.testmind.editor.TAMEditorTest;
import cz.vutbr.fit.testmind.editor.controls.TAMEOpenSaveControl;
import cz.vutbr.fit.testmind.editor.controls.TAMERootInitializeControl;
import cz.vutbr.fit.testmind.io.Serializer;
import cz.vutbr.fit.testmind.profile.TAMProfile;
import cz.vutbr.fit.testmind.testing.TestingParcelable;

public class MainActivity extends FragmentActivity {
	
	public static final String PREFS_NAME = "TestMindPrefs";
	
	/**
	 * Zabezpecuje jednoduchy pristup k jednotlivym polozkam menu
	 * 
	 */
	public final static class MenuItems {
		public static final int open = R.id.menu_open;
		public static final int save = R.id.menu_save;		
		//public static final int settings = R.id.menu_settings;
		public static final int importFile = R.id.menu_import;
		public static final int shareFile = R.id.menu_share;
		public static final int create_mode = R.id.menu_create_mode;
		public static final int view_mode = R.id.menu_view_mode;
		public static final int test_structure = R.id.menu_test_structure;
		public static final int edit_structure = R.id.menu_edit_structure;
		public static final int test_content = R.id.menu_test_content;
		public static final int show_result = R.id.menu_show_result;
		public static final int next_question = R.id.menu_next_question;
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
		public static final int connect = R.id.button_connect;
	}
	
	public static LinearLayout leftToolbar;
	public static LinearLayout rightToolbar;
	
	
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
		public static View btn_connect;
		
		public static Menu menu;
		
		public static MenuItem menu_create;
		public static MenuItem menu_view;
		public static MenuItem menu_show;
		public static MenuItem menu_next;
		
		public static Animation animAlpha;
	}
		
	private static final String TAG = "MainActivity";
	
	public static final String LAST_OPENED_FILE = "last";

	private ITAMEditor actualEditor;
	
	private static TAMProfile profile;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {    	
    	super.onCreate(savedInstanceState);	
    	//Log.d(TAG, "onCreate");
    	
    	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
    	String lastMindMap = settings.getString(LAST_OPENED_FILE, "");
    	
    	// docasny hack, vid dokumentacia TAMERootInitializeControl.initControl
    	if(!lastMindMap.isEmpty()) TAMERootInitializeControl.initControl = false;
    	
    	//if(profile != null) {
			profile = new TAMProfile();
		//}	
		
		
    	setContentView(R.layout.activity_main);
    	
    	leftToolbar = (LinearLayout) findViewById(R.id.activity_main_left_toolbar);
		rightToolbar = (LinearLayout) findViewById(R.id.activity_main_right_toolbar);
    	
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
		EventObjects.btn_connect = findViewById(R.id.button_connect);
		
		//EventObjects.menu_create = (MenuItem) findViewById(R.id.menu_create_mode);
		//EventObjects.menu_view = (MenuItem) findViewById(R.id.menu_view_mode);
		
		EventObjects.animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
    	
		// initialize editors //
    	EventObjects.editor_main.initialize(profile);
    	EventObjects.editor_test.initialize(profile);   	  	
    	    	
    	if(!lastMindMap.isEmpty()){
    		    		
    		Serializer serializer = new Serializer(
            		String.format("%s/%s." + TAMEOpenSaveControl.TESTMIND_FILE_EXTENSION, TAMProfile.TESTMIND_DIRECTORY.getPath(), lastMindMap));
            
            serializer.deserialize(profile);
            
    	} 	
    }
	
	/**
	 * Po otoceni zariadenia nevymaze obsah
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {		
		super.onRestoreInstanceState(savedInstanceState);
		
		//Log.d(TAG, "onRestoreInstanceState");
		
		String lastMindMap = savedInstanceState.getString(LAST_OPENED_FILE);
		
		Serializer serializer = new Serializer(
        		String.format("%s/%s." + TAMEOpenSaveControl.TESTMIND_FILE_EXTENSION, TAMProfile.TESTMIND_DIRECTORY.getPath(), lastMindMap));
        
        serializer.deserialize(profile);  
		
	}
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	
    	//Log.d(TAG, "onSaveInstanceState");
    	/* nie je vhodne tu serializovat veci, vola sa prilis casto
    	Serializer serializer = new Serializer(
				String.format("%s/%s.db", TAMProfile.TESTMIND_DIRECTORY.getPath(), profile.getFileName()));
		serializer.serialize(profile);
    	
		//Log.d(TAG, "ukladam: " + String.format("%s/%s.db", TAMProfile.TESTMIND_DIRECTORY.getPath(), profile.getFileName()));
		*/
		outState.putString(LAST_OPENED_FILE, profile.getFileName());
		
		
    	super.onSaveInstanceState(outState);
    }
    
        
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	getMenuInflater().inflate(R.menu.activity_main, menu);    
    	
    	EventObjects.menu = menu;
    	EventObjects.menu_create = menu.findItem(MenuItems.create_mode).setVisible(true);
		EventObjects.menu_view = menu.findItem(MenuItems.view_mode).setVisible(true);
		EventObjects.menu_show = menu.findItem(MenuItems.show_result).setVisible(false);
		EventObjects.menu_next = menu.findItem(MenuItems.next_question).setVisible(false);
		
    	return true;
    }
    
    
    @Override
    protected void onDestroy() {
    	// vola sa ked ma byt aktivita uplne odstranena z pamate
    	super.onDestroy();
    	
    	/* ak by mapa nebola ulozena, nech sa uzivatelovi nestrati */
    	Serializer serializer = new Serializer(
				String.format("%s/%s.%s",
						TAMProfile.TESTMIND_DIRECTORY.getPath(), 
						profile.getFileName(),
						TAMEOpenSaveControl.TESTMIND_FILE_EXTENSION));
		serializer.serialize(profile);
    	
		/* ulozi nastavenia, tak aby ich bolo mozne obnovit aj po uplnom zruseni aplikacie */
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(LAST_OPENED_FILE, profile.getFileName());
		editor.commit();
		
		//Log.d(TAG, "onDestroy save: " + String.format(
		//		"%s/%s.%s", TAMProfile.TESTMIND_DIRECTORY.getPath(), 
		//		profile.getFileName(),
		//		TAMEOpenSaveControl.TESTMIND_FILE_EXTENSION));
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	int id = item.getItemId();
    	
    	if(MenuItems.edit_structure == id){
			if(actualEditor != EventObjects.editor_main) {
				actualEditor.setEditorVisibility(View.GONE);
				actualEditor = EventObjects.editor_main;
				actualEditor.setEditorVisibility(View.VISIBLE);
			}
		} else if(MenuItems.test_structure == id){
			if(actualEditor != EventObjects.editor_test) {
				actualEditor.setEditorVisibility(View.GONE);
				actualEditor = EventObjects.editor_test;
				actualEditor.setEditorVisibility(View.VISIBLE);
			}
		} else if(MenuItems.test_content == id){
	        Intent i = new Intent(this, TestingActivity.class);
	        
	        TestingParcelable nodeParcelable = new TestingParcelable(MainActivity.getProfile().getRoot());
	        i.putExtra("cz.vutbr.fit.testmind.testing.TestingParcelable", nodeParcelable);
	        
	        this.startActivity(i);
		} else {
			actualEditor.onOptionsItemSelected(item);
			return super.onOptionsItemSelected(item);
		}
    	return true;
    }
    
    public void buttonPressed(View view) {
    	actualEditor.onButtonSelected(view);
    }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		actualEditor.onActivityResult(requestCode, resultCode, data);		
	}

	public static TAMProfile getProfile() {
		return profile;
	}	
}