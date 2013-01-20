package cz.vutbr.fit.testmind.editor;

import java.util.Random;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import cz.vutbr.fit.testmind.MainActivity;
import cz.vutbr.fit.testmind.MainActivity.EventObjects;
import cz.vutbr.fit.testmind.StructureTestingActivity;
import cz.vutbr.fit.testmind.editor.controls.TAMEConnectionControl;
import cz.vutbr.fit.testmind.editor.controls.TAMEZoomControl;
import cz.vutbr.fit.testmind.graphics.TAMGZoom;
import cz.vutbr.fit.testmind.graphics.TAMGraph;
import cz.vutbr.fit.testmind.profile.TAMPConnectionFactory;
import cz.vutbr.fit.testmind.profile.TAMPNode;
import cz.vutbr.fit.testmind.profile.TAMProfile;

public class TAMEditorTest extends TAMAbstractEditor implements ITAMEditor  {
		
	private Random random;
	private int size;
	public final int OFFSET = 100;
	
	public TAMEditorTest(Context context) {
		this(context, null);
	}

	public TAMEditorTest(Context context, AttributeSet attrs) {
		super(context, attrs);
		// do not type anything there - use initializeControls method instead //
	}

	@Override
	protected void initializeControls() {
		
		new TAMEZoomControl(this);
		new TAMEConnectionControl(this);
		
		random = new Random();
	}
		
	private void actualizeVariables() {
		// optimizations //
		size = profile.getListOfPNodes().size();
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	
		switch (item.getItemId()) {
    	case StructureTestingActivity.menuShow:
    		// TODO
    		break;
    	case StructureTestingActivity.menuNext:
    		//System.out.println("next");
    		generateNextQuestion();
    	default:
    		return super.onOptionsItemSelected(item);
    	}	
    	
    	return true;
	}
	
	public void generateNextQuestion() {
		
		generateNextQuestion(findNewBaseNode(), 1);
		
	}
	
	public void generateNextQuestion(TAMPNode node, int depth) {
			
		
		disposeAllItems();
		
		deployNodes(node);
		
		invalidate();
	}

	private TAMPNode findNewBaseNode() {
		
		actualizeVariables();
		
		TAMProfile profile = MainActivity.getProfile();
		TAMPNode node;
		
		int index = random.nextInt(size);
		
		boolean notFound;
		
		do {
			notFound = false;
			
			node = profile.getListOfPNodes().get(index);
			
			if(node.getListOfChildNodes().isEmpty()) {
				index = (index+191)%size;
				notFound = true;
			} else {
				// TODO
				/*for(ITAMENode actualNode : getListOfENodes()) {
					if(actualNode.getProfile() == node) {
						notFound = true;
						index = (index+191)%size;
						break;
					}
				}*/
			}
			
		} while(notFound);
		
		return node;
	}

	private void disposeAllItems() {
		
		int size = getListOfENodes().size();
		
		for(int i = 0; i < size; i++) {
			getListOfENodes().get(0).getProfile().removeEReference(this);
		}
		
		getListOfENodes().clear();
		
		size = getListOfEConnections().size();
		
		for(int i = 0; i < size; i++) {
			getListOfEConnections().get(0).getProfile().removeEReference(this);
		}
		
		getListOfEConnections().clear();
	}
	
	private void deployNodes(TAMPNode node) {
		
		/*int offsetX = ((int)(50/getZoom().sx));
		int offsetY = ((int)(100/getZoom().sy));
		
		int width = (int) ((getWidth()/getZoom().sx)) - offsetX;
		int height = (int) ((getHeight()/getZoom().sy)) - offsetY;
		
		int px, py;
		
		if(getZoom().sx >= 1) {
			px = (int) ((getZoom().px)-(offsetX/2));
		} else {
			px = (int) (-(getZoom().px)-(offsetX/2));
		}
		if(getZoom().sx >= 1) {
			py = (int) ((getZoom().py)-(offsetY/2));
		} else {
			py = (int) (-(getZoom().py)-(offsetY/2));
		}*/
		
		TAMGZoom zoom = getZoom();
		zoom(TAMGraph.DEFAULT_ZOOM, TAMGraph.DEFAULT_ZOOM, getWidth()/2, getHeight()/2);
		translate(0, 0);
		
		int offsetX = ((int)(OFFSET/zoom.sx));
		int offsetY = ((int)(OFFSET/zoom.sy));
		int halfOffsetX = offsetX/2;
		int halfOffsetY = offsetY/2;
		
		int width = (int) ((getWidth()/zoom.sx)) - offsetX;
		int height = (int) ((getHeight()/zoom.sy)) - offsetY;
		
		//System.out.println(width + " " + height + " " + offsetX + " " + offsetY);
		
		TAMPConnectionFactory.addEReference(node, this, (random.nextInt(width)+halfOffsetX-getWidth()/2), (random.nextInt(height)+halfOffsetY-getHeight()/2));
		
		for(TAMPNode child : node.getListOfChildNodes()) {
			TAMPConnectionFactory.addEReference(child, this, (random.nextInt(width)+halfOffsetX-getWidth()/2), (random.nextInt(height)+halfOffsetY-getHeight()/2));
		}
		
	}
}
