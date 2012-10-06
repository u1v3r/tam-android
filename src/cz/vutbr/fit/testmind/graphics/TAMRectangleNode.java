package cz.vutbr.fit.testmind.graphics;

import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.shapes.RectShape;

/**
 * Default rectangle node.
 * 
 * @author jurij
 *
 */
public class TAMRectangleNode extends TAMAbstractNode implements ITAMNode {
	
	public static final int TEXT_SIZE = 25;
	private static final int type = NODE_TYPE_RECTANGLE;
	public static final int OFFSET_X = 40;
	public static final int OFFSET_Y = 40;
	
	public TAMRectangleNode(TAMGraph graph, int x, int y, String text) {
		super(graph, x, y, OFFSET_X, OFFSET_Y, text, new RectShape(), type);
	}
	
	public boolean hit(int x, int y) {
		
		TAMGraph graph = getGraph();
		Rect rect = this.getBounds();
		/*
		float left = rect.left*graph.getScaleX()+(graph.getPivotX()-graph.getPivotX()*graph.getScaleX());
		float top = rect.top*graph.getScaleY()+(graph.getPivotY()-graph.getPivotY()*graph.getScaleY());
		float right = rect.right*graph.getScaleX()+(graph.getPivotX()-graph.getPivotX()*graph.getScaleX());
		float bottom = rect.bottom*graph.getScaleY()+(graph.getPivotY()-graph.getPivotY()*graph.getScaleY());
		*/
		
		float left = rect.left*graph.sx+(graph.px-graph.px*graph.sx);
		float top = rect.top*graph.sy+(graph.py-graph.py*graph.sy);
		float right = rect.right*graph.sx+(graph.px-graph.px*graph.sx);
		float bottom = rect.bottom*graph.sy+(graph.py-graph.py*graph.sy);
		
		Rect newRect = new Rect(rect.left, rect.top, rect.right, rect.bottom);
		
		if(x >= left && x <= right && y >= top && y <= bottom) {
			return true;
		} else {
			return false;
		}
		
		// if point is situated in rectangle area then return true, otherwise return false //
		/*if(newRect.contains(x,y)) {
			return true;
		} else {
			return false;
		}*/
	}

	public void move(int dx, int dy) {
		
		setPosition(getPosition().x+dx, getPosition().y+dy);
		
		Rect rect = this.getBounds();
		this.setBounds(rect.left+dx, rect.top+dy, rect.right+dx, rect.bottom+dy);
	}
	
	public void actualizePosition(int x, int y) {
		
		// change position //
		setPosition(x, y);
		
		// count new position for rectangle //
		Rect rect = this.getBounds();
		int width = rect.right - rect.left;
		int height = rect.top - rect.bottom;
		int left = x - width/2;
		int top = y - height/2;
		
		// actualize rectangle //
		this.setBounds(left, top, left+width, top+height);
	}
	
	public void actualizeSize() {
		this.getPaint().setTextSize(TEXT_SIZE);
		int width = (int) this.getPaint().measureText(getText())/2;
		
		Point p = this.getPosition();
		
		// real size of node //
<<<<<<< HEAD
		this.setBounds(p.x-width-40, p.y-32, p.x+width+40, p.y+32);
=======
		this.setBounds(p.x-width-20, p.y-45, p.x+width+20, p.y+45);
>>>>>>> 4329e626a0a1d5911b77d4c65f831014568673fd
	}

}
