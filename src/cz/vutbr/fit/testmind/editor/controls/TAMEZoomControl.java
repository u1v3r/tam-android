package cz.vutbr.fit.testmind.editor.controls;

import android.view.View;
import cz.vutbr.fit.testmind.MainActivity.EventObjects;
import cz.vutbr.fit.testmind.editor.TAMEditorMain;
import cz.vutbr.fit.testmind.graphics.TAMGraph;

public class TAMEZoomControl extends TAMEAbstractControl implements ITAMButtonListener {

	private static final String TAG = "TAMEZoomControl";
	
	public TAMEZoomControl(TAMEditorMain editor) {
		super(editor);
		editor.getListOfButtonControls().add(this);
	}
	/*
	public void zoom(float sx, float sy, float px, float py) {
		getEditor().zoom(sx, sy, px, py);
	}
	*/

	public void onButtonSelected(View item) {
		
		if(item == EventObjects.btn_zoom_in) {
			((TAMGraph)editor).onZoomIn();
		} else if(item == EventObjects.btn_zoom_out) {
			((TAMGraph)editor).onZoomOut();
		}
	}	

}
