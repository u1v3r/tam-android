package cz.vutbr.fit.testmind;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;
import android.widget.ZoomControls;
import cz.vutbr.fit.testmind.dialogs.AddNodeDialog;
import cz.vutbr.fit.testmind.dialogs.AddNodeDialog.AddNodeDialogListener;
import cz.vutbr.fit.testmind.editor.TAMEditor;
import cz.vutbr.fit.testmind.graphics.ITAMNode;
import cz.vutbr.fit.testmind.graphics.TAMGraph;

public class MainActivity extends FragmentActivity implements AddNodeDialogListener{
	
	private static final String TAG = "MainActivity";
	
	protected TAMEditor editor = new TAMEditor(this);
	
	protected TAMGraph graph;
	//protected ZoomControls zoomControls;
	
	//protected int currentZoomLevel = 0;
	//protected int maxZoomLovel = 0;
	
	private ITAMNode selectedNode;	

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(editor.getLayout());        
        
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
    	// vlozi action menu
        getMenuInflater().inflate(editor.getActionBar(), menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	switch (item.getItemId()) {
		case TAMEditor.MenuItems.add:
			addNode();
			break;
		case TAMEditor.MenuItems.edit:
			editNode();
			break;
		case TAMEditor.MenuItems.delete:
			deleteNode();			
			break;
		case TAMEditor.MenuItems.save:
			saveMap();
			break;
		case TAMEditor.MenuItems.importFile:
			importFile();
			break;
		case TAMEditor.MenuItems.settings:
			
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
	
	/**
	 * Vytvorí child pre vybrany parrent uzol
	 */
	protected void addNode() {
		
		selectedNode = graph.getLastSelectedNode();
		
		if(selectedNode != null){
			showAddNodeDialog();
		}else{
			Toast.makeText(this, R.string.node_not_selected, Toast.LENGTH_LONG).show();			
		}
	}
	
	/**
	 * Zobrazí dialog na pridanie uzlu
	 */
	private void showAddNodeDialog() {
		
		//FragmentActivity fm = getSupportFragmentManager();		
		AddNodeDialog dialog = new AddNodeDialog();
		dialog.show(getSupportFragmentManager(), "fragment_add_node");
		
	}
	
	/**
	 * Vykoná sa po uzavretí dialogu
	 */
	public void onFinishNodeAddDialog(String title) {
		/*
		 * TODO: Treba vytovorit triedu, ktora bude rozmiestnovat nove uzly po ploche
		 */
		selectedNode.addChild(selectedNode.getPosition().x + 50, 
				selectedNode.getPosition().y + 50, title);
	}
}