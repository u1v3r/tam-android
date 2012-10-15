package cz.vutbr.fit.testmind;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
		public static final int save = R.id.menu_save;
		public static final int settings = R.id.menu_settings;
		public static final int importFile = R.id.menu_import; 
	}
	
	public static class EventObjects{
		public static ZoomControls zoomControls;
		public static TAMEditor editor;
	}
	
	public static final int PICK_FILE_RESULT_CODE = 0;
	
	private static final String TAG = "MainActivity";
	
	private static TAMProfile profile;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {    	
    	super.onCreate(savedInstanceState);
    	
    	//if(profile != null) {
    		profile = new TAMProfile();
    	//}
    	
    	setContentView(R.layout.activity_main);
    	
    	EventObjects.editor = (TAMEditor) findViewById(R.id.tam_editor);
    	EventObjects.zoomControls = (ZoomControls) findViewById(R.id.zoom_controls);
    	
    	EventObjects.editor.initialize();
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

		if(PICK_FILE_RESULT_CODE == requestCode && RESULT_OK == resultCode){
			
			Uri uri = data.getData();
			Log.d(TAG,"File selected: " + uri.toString());
			
			// TODO: implementovat import suboru
			
			

		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}

	public static TAMProfile getProfile() {
		return profile;
	}

}