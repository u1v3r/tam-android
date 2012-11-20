package cz.vutbr.fit.testmind.editor.controls;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.view.MotionEvent;
import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.editor.items.ITAMENode;
import cz.vutbr.fit.testmind.editor.items.TAMENode;
import cz.vutbr.fit.testmind.graphics.ITAMGItem;
import cz.vutbr.fit.testmind.graphics.ITAMGNode;
import cz.vutbr.fit.testmind.graphics.TAMGraph;
import cz.vutbr.fit.testmind.graphics.TAMGraph.ITAMBlankAreaGestureListener;
import cz.vutbr.fit.testmind.graphics.TAMGraph.ITAMItemGestureListener;
import cz.vutbr.fit.testmind.graphics.TAMGraph.ITAMItemListener;
import cz.vutbr.fit.testmind.graphics.TAMGraph.ITAMPreDrawListener;
import cz.vutbr.fit.testmind.graphics.TAMGraph.ITAMTouchListener;
import cz.vutbr.fit.testmind.graphics.TAMGraph.TAMGMotionEvent;
import cz.vutbr.fit.testmind.profile.TAMPConnection;
import cz.vutbr.fit.testmind.profile.TAMPConnectionFactory;

public class TAMEConnectionControl extends TAMEAbstractControl implements ITAMItemGestureListener, ITAMPreDrawListener, ITAMBlankAreaGestureListener, ITAMTouchListener {
	
	private boolean active = false;
	private ITAMGNode fromNode;
	private ITAMGNode toNode;
	private PointF to;

	public TAMEConnectionControl(ITAMEditor editor) {
		super(editor);
		editor.getListOfItemGestureControls().add(this);
		//editor.getListOfItemControls().add(this);
		editor.getListOfMoveGestureControls().add(this);
		editor.getListOfPreDrawControls().add(this);
		editor.getListOfTouchControls().add(this);
		to = new PointF();
	}

	public void onItemLongSelectEvent(MotionEvent e, ITAMGNode node) {
		
		if(node != null) {
			
			fromNode = node;
			
			((TAMGraph) getEditor()).setMoveItemsOrGraphEnabled(false);
			
			node.setSelected(false);
			((ITAMENode) fromNode.getHelpObject()).setBackgroundStyle(ITAMENode.RED);
			
			active = true;
		}
	}

	public void onItemLongReleaseEvent(MotionEvent e, ITAMGNode node) {
		
		System.out.println("release");
		
		if(active) {
			((TAMGraph) getEditor()).setMoveItemsOrGraphEnabled(true);
			
			active = false;
			
			if(toNode != null) {
				
				TAMPConnection connection = editor.getProfile().getConnection(((TAMENode) fromNode.getHelpObject()).getProfile(), ((TAMENode) toNode.getHelpObject()).getProfile());
				
				if(connection != null && !connection.hasReference(editor)) {
					TAMPConnectionFactory.addEReference(connection, editor);
				}
				
				
				((ITAMENode) toNode.getHelpObject()).setBackgroundStyle(ITAMENode.BLUE);
			}
			
			toNode = null;
			
			((ITAMENode) fromNode.getHelpObject()).setBackgroundStyle(ITAMENode.BLUE);
		}
	}

	public void onItemDoubleTapEvent(MotionEvent e, ITAMGNode node) {
		// do nothing //
	}

	public void onPreDraw(Canvas canvas, Paint paint) {
		
		//System.out.println("draw");
		
		if(active) {
			
			Point from = fromNode.getPosition();
			//System.out.println(from.x + " " + from.y + " " + to.x + " " + to.y);
			paint.setStrokeWidth(6f);
			paint.setColor(Color.BLACK);
			if(toNode == null) {
				canvas.drawLine(from.x, from.y, to.x, to.y, paint);
			} else {
				Point to = toNode.getPosition();
				canvas.drawLine(from.x, from.y, to.x, to.y, paint);
			}
		}
	}

	public void onBlankMoveEvent(MotionEvent e, float dx, float dy) {
		to.x += dx;
		to.y += dy;
		
		ITAMGNode selected = ((TAMGraph) editor).isNodeHit(to.x, to.y);
		
		if(selected != fromNode) {
			if(toNode != selected) {
				if(toNode != null) {
					((ITAMENode) toNode.getHelpObject()).setBackgroundStyle(ITAMENode.BLUE);
				}
				if(selected != null) {
					((ITAMENode) selected.getHelpObject()).setBackgroundStyle(ITAMENode.RED);
				}
				
				toNode = selected;
			}
		}
	}

	public void onBlankLongPressEvent(MotionEvent e, float dx, float dy) {
		// TODO Auto-generated method stub
		
	}

	public void onBlankDoubleTapEvent(MotionEvent e, float dx, float dy) {
		// TODO Auto-generated method stub
		
	}

	public void onHitEvent(MotionEvent e, TAMGMotionEvent ge) {
		to.x = ge.dx;
		to.y = ge.dy;
	}

}
