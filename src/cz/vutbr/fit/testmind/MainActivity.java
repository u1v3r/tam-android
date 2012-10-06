package cz.vutbr.fit.testmind;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.ZoomControls;
import cz.vutbr.fit.testmind.graphics.ITAMNode;
import cz.vutbr.fit.testmind.graphics.TAMGraph;



public class MainActivity extends Activity {
	
	protected TAMGraph graph;
	protected ZoomControls zoomControls;
	
	protected int currentZoomLevel = 0;
	protected int maxZoomLovel = 0;	

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);        

        graph = (TAMGraph)findViewById(R.id.tam_graph);
        zoomControls = (ZoomControls)findViewById(R.id.zoom_controls);
        zoomControls.setOnZoomInClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				graph.zoom(graph.getScaleX() + TAMGraph.ZOOM_STEP, graph.getScaleY() + TAMGraph.ZOOM_STEP, 
						graph.getWidth()/2, graph.getPivotY()/2);
			}
		});
        zoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				graph.zoom(graph.getScaleX() - TAMGraph.ZOOM_STEP, graph.getScaleY() - TAMGraph.ZOOM_STEP, 
						graph.getWidth()/2, graph.getPivotY()/2);
			}
		});
        
		ITAMNode node1 = graph.addRoot(ITAMNode.NODE_TYPE_RECTANGLE, 10, 10, "jedna");
		
		node1.addChild(10, 40, "dva");
		
		ITAMNode node2 = graph.addRoot(ITAMNode.NODE_TYPE_RECTANGLE, 60, 60, "tri");
		
		node2.addChild(100, 100, "ctyri");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	switch (item.getItemId()) {
		case R.id.menu_add:
			addNode();
			break;
		case R.id.menu_edit:
			editNode();
			break;
		case R.id.menu_delete:
			deleteNode();			
			break;
		case R.id.menu_save:
			saveMap();
			break;
		case R.id.menu_settings:
			
			break;
		default: 
			return super.onOptionsItemSelected(item);
		
    	
    	} 	
    	
    	return true;    	
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

	private void addNode() {
		// TODO Auto-generated method stub
		
	}
}