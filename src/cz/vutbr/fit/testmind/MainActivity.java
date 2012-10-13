package cz.vutbr.fit.testmind;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.ZoomControls;
import cz.vutbr.fit.testmind.dialogs.AddNodeDialog.AddNodeDialogListener;
import cz.vutbr.fit.testmind.editor.TAMEditor;
import cz.vutbr.fit.testmind.editor.controls.TAMEditorNodesControl;
import cz.vutbr.fit.testmind.editor.items.TAMEditorNode;

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
	private TAMEditor editor;
	private TAMEditorNodesControl controller;
	

	//protected int currentZoomLevel = 0;
	//protected int maxZoomLovel = 0;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {    	
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_main);
    	  
    	editor = (TAMEditor)findViewById(R.id.tam_editor);
    	//EventObjects.zoomControls = (ZoomControls)findViewById(R.id.zoom_controls);
    	
    	//this.editor = new TAMEditor(this);
    	//this.controller = new TAMEditorNodesControl(this.editor);
		   
        /*controller.createDefaultRootNode();
               
        EventObjects.zoomControls.setOnZoomInClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				controller.zoomIn(EventObjects.editor);
				/*graph.zoom(graph.sx + TAMGraph.ZOOM_STEP, graph.sx + TAMGraph.ZOOM_STEP, 
						graph.getWidth()/2, graph.getHeight()/2);
				*/
			/*}
		});
        EventObjects.zoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				controller.zoomOut(EventObjects.editor);
				/*
				graph.zoom(graph.sx - TAMGraph.ZOOM_STEP, graph.sy - TAMGraph.ZOOM_STEP, 
						graph.getWidth()/2, graph.getHeight()/2);
				*/
			/*}
		});*/
    }
        
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	getMenuInflater().inflate(R.menu.activity_main, menu);    	
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	editor.onOptionsItemSelected(item);
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

}