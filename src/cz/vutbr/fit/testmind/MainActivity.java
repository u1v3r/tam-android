package cz.vutbr.fit.testmind;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import android.widget.ZoomControls;
import cz.vutbr.fit.testmind.editor.TAMEditor;
import cz.vutbr.fit.testmind.profile.TAMProfile;

public class MainActivity extends FragmentActivity {
	
	/**
	 * Zabezpecuje jednoduchy pristup k jednotlivym polozkam menu
	 * 
	 */
	public final static class MenuItems{
		public static final int add = R.id.menu_add;
		public static final int edit = R.id.menu_edit;
		public static final int delete = R.id.menu_delete;
		public static final int open = R.id.menu_open;
		public static final int save = R.id.menu_save;		
		public static final int settings = R.id.menu_settings;
		public static final int importFile = R.id.menu_import;
		public static final int exportFile = R.id.menu_export;
		public static final int create_mode = R.id.menu_create_mode;
		public static final int view_mode = R.id.menu_view_mode;
		public static final int testing = R.id.menu_testing;
	}
	
	/**
	 * Zabezpecuje jednoduchy pristup k jednotlivym polozkam toolbaru
	 * 
	 */
	public final static class ButtonItems{
		public static final int add = R.id.button_add;
		public static final int delete = R.id.button_delete;
		public static final int edit = R.id.button_edit;
		public static final int hide_one = R.id.button_hide_one;
		public static final int hide_all = R.id.button_hide_all;
		public static final int view = R.id.button_view;
		public static final int zoom_in = R.id.button_zoom_in;
		public static final int zoom_out = R.id.button_zoom_out;
	}
	
	public static class EventObjects{
		public static ZoomControls zoomControls;
		public static TAMEditor editor;
		
		public static View btn_add;
		public static View btn_delete;
		public static View btn_edit;
		public static View btn_hide_one;
		public static View btn_hide_all;
		public static View btn_view;
		public static View btn_zoom_in;
		public static View btn_zoom_out;
		
		//public static MenuItem menu_create;
		//public static MenuItem menu_view;
		
		public static Animation animAlpha;
	}
		
	private static final String TAG = "MainActivity";
	
	private static TAMProfile profile;
	private static MainActivity mainActivityInstance;	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {    	
    	super.onCreate(savedInstanceState);
    	
    	//if(profile != null) {
    		profile = new TAMProfile();
    	//}
    	
    	setContentView(R.layout.activity_main);
    	
    	EventObjects.editor = (TAMEditor) findViewById(R.id.acitity_main_tam_editor);
    	
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
    	
    	EventObjects.editor.initialize(profile);
    	
    	mainActivityInstance = this;
    }
        
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	getMenuInflater().inflate(R.menu.activity_main, menu);    	
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {    	
    	EventObjects.editor.onOptionsItemSelected(item);
		return super.onOptionsItemSelected(item);
		    	
    }
    
    public void buttonPressed(View view) {
    	EventObjects.editor.onButtonSelected(view);
    }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		EventObjects.editor.onActivityResult(requestCode, resultCode, data);		
	}

	public static TAMProfile getProfile() {
		return profile;
	}
	
    public static MainActivity getMainActivityInstance() {
        return mainActivityInstance;
    }
}