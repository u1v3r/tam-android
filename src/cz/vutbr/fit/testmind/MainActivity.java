package cz.vutbr.fit.testmind;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
		public static final int edit_mode = R.id.menu_edit_mode;
		public static final int view_mode = R.id.menu_view_mode;
		public static final int file_mode = R.id.menu_file_mode;
	}
	
	public static class EventObjects{
		public static ZoomControls zoomControls;
		public static TAMEditor editor;
	}
		
	private static final String TAG = "MainActivity";
	
	private static TAMProfile profile;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {    	
    	super.onCreate(savedInstanceState);
    	
    	//if(profile != null) {
    		profile = new TAMProfile();
    	//}
    	
    	setContentView(R.layout.activity_main);
    	
    	EventObjects.editor = (TAMEditor) findViewById(R.id.acitity_main_tam_editor);
    	EventObjects.zoomControls = (ZoomControls) findViewById(R.id.acitity_main_zoom_controls);
    	
    	EventObjects.editor.initialize(profile);
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		EventObjects.editor.onActivityResult(requestCode, resultCode, data);		
	}

	public static TAMProfile getProfile() {
		return profile;
	}

}