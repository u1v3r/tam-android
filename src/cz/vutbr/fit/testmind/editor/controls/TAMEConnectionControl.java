package cz.vutbr.fit.testmind.editor.controls;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.view.MotionEvent;
import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.graphics.ITAMGItem;
import cz.vutbr.fit.testmind.graphics.ITAMGNode;
import cz.vutbr.fit.testmind.graphics.TAMGraph;
import cz.vutbr.fit.testmind.graphics.TAMGraph.ITAMItemGestureListener;
import cz.vutbr.fit.testmind.graphics.TAMGraph.ITAMItemListener;
import cz.vutbr.fit.testmind.graphics.TAMGraph.ITAMPreDrawListener;
import cz.vutbr.fit.testmind.graphics.TAMGraph.TAMGMotionEvent;

public class TAMEConnectionControl extends TAMEAbstractControl implements ITAMItemGestureListener, ITAMPreDrawListener, ITAMItemListener {
	
	private boolean active = false;
	private Point from;
	private PointF to;

	public TAMEConnectionControl(ITAMEditor editor) {
		super(editor);
		editor.getListOfItemGestureControls().add(this);
		to = new PointF();
	}

	public void onItemLongSelectEvent(MotionEvent e, ITAMGNode node) {
		
		if(node != null) {
			
			from = node.getPosition();
			to.x = from.x;
			to.y = from.y;
			
			((TAMGraph) getEditor()).setMoveItemsOrGraphEnabled(false);
			
			active = true;
		}
	}

	public void onItemLongReleaseEvent(MotionEvent e, ITAMGNode node) {
		
		if(active) {
			((TAMGraph) getEditor()).setMoveItemsOrGraphEnabled(true);
			
			active = true;
		}
	}

	public void onItemDoubleTapEvent(MotionEvent e, ITAMGNode node) {
		// do nothing //
	}

	public void onPreDraw(Canvas canvas, Paint paint) {
		
		if(active) {
			paint.setStrokeWidth(6f);
			paint.setColor(Color.BLACK);
			canvas.drawLine(from.x, from.y, to.x, to.y, paint);
		}
	}

	public void onItemHitEvent(MotionEvent e, TAMGMotionEvent ge) {
		// do nothing //
	}

	public void onItemMoveEvent(MotionEvent e, TAMGMotionEvent ge) {
		to.x += ge.dx;
		to.y += ge.dy;
	}

	public void onItemSelectEvent(ITAMGItem item, boolean selection) {
		// do nothing //
	}

}
