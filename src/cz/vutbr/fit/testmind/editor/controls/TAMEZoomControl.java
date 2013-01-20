package cz.vutbr.fit.testmind.editor.controls;

import android.view.View;
import cz.vutbr.fit.testmind.editor.ITAMEditor;

public class TAMEZoomControl extends TAMEAbstractControl{

	public TAMEZoomControl(ITAMEditor editor) {
		super(editor);
	}
	/*
	public void zoom(float sx, float sy, float px, float py) {
		getEditor().zoom(sx, sy, px, py);
	}
	*/

	public void onButtonSelected(View item) {
		
		/*
		if(((View) getEditor()).getVisibility() == View.VISIBLE) {
			if(item == EventObjects.btn_zoom_in) {
				((TAMGraph)editor).onZoomIn();
			} else if(item == EventObjects.btn_zoom_out) {
				((TAMGraph)editor).onZoomOut();
			}
		}
		*/
	}	

}
