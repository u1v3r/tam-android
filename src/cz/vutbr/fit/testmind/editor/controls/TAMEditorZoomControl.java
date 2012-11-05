package cz.vutbr.fit.testmind.editor.controls;

import android.view.View;
import cz.vutbr.fit.testmind.MainActivity.EventObjects;
import cz.vutbr.fit.testmind.editor.TAMEditor;
import cz.vutbr.fit.testmind.graphics.TAMGraph;

public class TAMEditorZoomControl extends TAMEditorAbstractControl implements ITAMButtonListener {

	public TAMEditorZoomControl(TAMEditor editor) {
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
