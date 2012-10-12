package cz.vutbr.fit.testmind;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ZoomControls;
import cz.vutbr.fit.testmind.dialogs.AddNodeDialog.AddNodeDialogListener;
import cz.vutbr.fit.testmind.editor.TAMEditor;
import cz.vutbr.fit.testmind.editor.controls.TAMEditorNodesControl;
import cz.vutbr.fit.testmind.graphics.TAMGraph;

public class MainActivity extends FragmentActivity implements AddNodeDialogListener{
	
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
		public static TAMGraph graph;
	}
	
	private static final String TAG = "MainActivity";	
	private TAMEditor editor;
	private TAMEditorNodesControl controller;
	

	//protected int currentZoomLevel = 0;
	//protected int maxZoomLovel = 0;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {    	
    	super.onCreate(savedInstanceState);    	
    	setContentView(R.layout.activity_main);
    	  
    	EventObjects.graph = (TAMGraph)findViewById(R.id.tam_graph);
    	EventObjects.zoomControls = (ZoomControls)findViewById(R.id.zoom_controls);
    	
    	this.editor = new TAMEditor(this);
    	this.controller = new TAMEditorNodesControl(this.editor);
		   
        controller.createDefaultRootNode();
        
        /*
        graph = (TAMGraph)findViewById(R.id.tam_graph);
        zoomControls = (ZoomControls)findViewById(R.id.zoom_controls);
        zoomControls.setOnZoomInClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				graph.zoom(graph.sx + TAMGraph.ZOOM_STEP, graph.sx + TAMGraph.ZOOM_STEP, 
						graph.getWidth()/2, graph.getHeight()/2);
			}
		});
        zoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				graph.zoom(graph.sx - TAMGraph.ZOOM_STEP, graph.sy - TAMGraph.ZOOM_STEP, 
						graph.getWidth()/2, graph.getHeight()/2);
			}
		});
        
		ITAMNode node1 = graph.addRoot(ITAMNode.NODE_TYPE_RECTANGLE, 10, 10, "jedna");
		
		node1.addChild(10, 40, "dva");
		
		ITAMNode node2 = graph.addRoot(ITAMNode.NODE_TYPE_RECTANGLE, 60, 60, "tri");
		
		node2.addChild(100, 100, "ctyri");
		*/
    }
        
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	getMenuInflater().inflate(R.menu.activity_main, menu);    	
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	switch (item.getItemId()) {
		case MenuItems.add:		
			controller.showAddChildNodeDialog();
			break;
		case MenuItems.edit:
			editNode();
			break;
		case MenuItems.delete:
			deleteNode();			
			break;
		case MenuItems.save:
			saveMap();
			break;
		case MenuItems.importFile:
			importFile();
			break;
		case MenuItems.settings:
			
			break;
		default: 
			return super.onOptionsItemSelected(item);
		
    	
    	} 	
    	
    	return true;    	
    }

	private void importFile() {
		// TODO Auto-generated method stub
		
	}

	private void saveMap() {
		// TODO Auto-generated method stub
		
	}

	private void deleteNode() {
		// TODO Auto-generated method stub
		
	}

	private void editNode() {
		// TODO Auto-generated method stub
		
	}

	public void onFinishAddChildNodeDialog(String title) {
		controller.addChildNode(title, editor.getLastSelectedNode());
	}
}