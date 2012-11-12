package cz.vutbr.fit.testmind.editor.controls;

import android.view.MotionEvent;
import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.graphics.TAMGraph.ITAMTouchListener;
import cz.vutbr.fit.testmind.graphics.TAMGraph.TAMGMotionEvent;

public class TAMERootInitializeControl extends TAMEAbstractControl implements ITAMTouchListener {
	
	public interface ITAMRootControlListener {
		public boolean createDefaultRootNode(int x, int y);
	}

	public TAMERootInitializeControl(ITAMRootControlListener editor) {
		super((ITAMEditor) editor);
		((ITAMEditor) editor).getListOfTouchControls().add(this);
	}

	public void onHitEvent(MotionEvent e, TAMGMotionEvent ge) {
		if(((ITAMRootControlListener) getEditor()).createDefaultRootNode((int) ge.dx, (int) ge.dy)) {
			getEditor().getListOfTouchControls().remove(this);
		}
	}

	public void onTouchEvent(MotionEvent e, float dx, float dy) {
		// TODO Auto-generated method stub
		
	}

}
