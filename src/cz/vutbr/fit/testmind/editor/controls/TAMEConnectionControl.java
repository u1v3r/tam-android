package cz.vutbr.fit.testmind.editor.controls;

import android.graphics.Canvas;
import android.view.MotionEvent;
import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.graphics.ITAMGNode;
import cz.vutbr.fit.testmind.graphics.TAMGraph.ITAMItemGestureListener;
import cz.vutbr.fit.testmind.graphics.TAMGraph.ITAMPreDrawListener;

public class TAMEConnectionControl extends TAMEAbstractControl implements ITAMItemGestureListener, ITAMPreDrawListener {
	
	

	public TAMEConnectionControl(ITAMEditor editor) {
		super(editor);
		editor.getListOfItemGestureControls().add(this);
	}

	public void onItemLongSelectEvent(MotionEvent e, ITAMGNode node) {
		// TODO Auto-generated method stub
		
	}

	public void onItemLongReleaseEvent(MotionEvent e, ITAMGNode node) {
		// TODO Auto-generated method stub
		
	}

	public void onItemDoubleTapEvent(MotionEvent e, ITAMGNode node) {
		// do nothing //
	}

	public void onPreDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		
	}

}
