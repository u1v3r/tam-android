package cz.vutbr.fit.testmind.editor;

import java.util.Random;

import cz.vutbr.fit.testmind.MainActivity;
import cz.vutbr.fit.testmind.MainActivity.EventObjects;
import cz.vutbr.fit.testmind.editor.controls.TAMEToolbarContol;
import cz.vutbr.fit.testmind.editor.controls.TAMEToolbarContol.ITAMToolbarConstrolItem;
import cz.vutbr.fit.testmind.editor.controls.TAMEZoomControl;
import cz.vutbr.fit.testmind.editor.items.ITAMENode;
import cz.vutbr.fit.testmind.profile.TAMPConnectionFactory;
import cz.vutbr.fit.testmind.profile.TAMPNode;
import cz.vutbr.fit.testmind.profile.TAMProfile;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;

public class TAMEditorTest extends TAMAbstractEditor implements ITAMEditor, ITAMToolbarConstrolItem  {
	
	private boolean hasVisibleMenu = false;
	private Random random;
	private int size;
	
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
		new TAMEToolbarContol(this);
		
		random = new Random();
	}

	@Override
	protected void modeChanged(MenuItem item) {
		if(item == EventObjects.menu_show) {
			// TODO
		} else if(item == EventObjects.menu_next) {
			System.out.println("next");
			generateNextQuestion();
			EventObjects.menu_show.setEnabled(true);
		}
	}
	
	public int getMode() {
		// this editor has no modes //
		return 0;
	}

	@Override
	protected void actualizeMenus(int visibility) {
		
		if(visibility == View.VISIBLE) {
			actualizeVariables();
			EventObjects.menu_show.setVisible(true);
			EventObjects.menu_show.setEnabled(false);
			EventObjects.menu_next.setVisible(true);
		} else {
			disposeAllItems();
			EventObjects.menu_show.setVisible(false);
			EventObjects.menu_next.setVisible(false);
		}
		
		if(hasVisibleMenu) {
			if(visibility == View.VISIBLE) {
				showToolbar();
			} else {
				hideToolbar();
				hasVisibleMenu = true;
			}
		}
	}
	
	@Override
	public void setVisibility(int visibility) {
		super.setVisibility(visibility);
	}

	private void actualizeVariables() {
		// optimizations //
		size = profile.getListOfPNodes().size();
	}

	public void showToolbar() {
		
		hasVisibleMenu = true;
		
		EventObjects.btn_connect.setVisibility(VISIBLE);		
		
		EventObjects.btn_zoom_in.setVisibility(GONE);
		EventObjects.btn_zoom_out.setVisibility(GONE);
	}

	public void hideToolbar() {
		
		hasVisibleMenu = false;
		
		EventObjects.btn_connect.setVisibility(GONE);		
		
		EventObjects.btn_zoom_in.setVisibility(VISIBLE);
		EventObjects.btn_zoom_out.setVisibility(VISIBLE);
	}
	
	public void generateNextQuestion() {
		
		TAMPNode node = findNewBaseNode();
		
		disposeAllItems();
		
		deployNodes(node);
		
		invalidate();
	}

	private TAMPNode findNewBaseNode() {
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
		
		int width = (int) ((getWidth()/getZoom().sx));
		int height = (int) ((getHeight()/getZoom().sy));
		
		TAMPConnectionFactory.addEReference(node, this, random.nextInt(width), random.nextInt(height));
		
		for(TAMPNode child : node.getListOfChildNodes()) {
			TAMPConnectionFactory.addEReference(child, this, random.nextInt(width), random.nextInt(height));
		}
		
	}

}
