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
	
	public static final int TEXT_SIZE = 64;
	private static final int type = NODE_TYPE_RECTANGLE;
	public static final int OFFSET_X = 80;
	public static final int OFFSET_Y = 80;
	
	public TAMRectangleNode(TAMGraph graph, int x, int y, String text) {
		super(graph, x, y, OFFSET_X, OFFSET_Y, text, new RectShape(), type);
	}
	
	public boolean hit(float x, float y) {
		
		//TAMGraph graph = getGraph();
		Rect rect = this.getBounds();
		
		/*float left = rect.left*graph.sx+(graph.px-graph.px*graph.sx);
		float top = rect.top*graph.sy+(graph.py-graph.py*graph.sy);
		float right = rect.right*graph.sx+(graph.px-graph.px*graph.sx);
		float bottom = rect.bottom*graph.sy+(graph.py-graph.py*graph.sy);*/
		
		Rect newRect = new Rect(rect.left, rect.top, rect.right, rect.bottom);
		
		/*if(x >= left && x <= right && y >= top && y <= bottom) {
			return true;
		} else {
			return false;
		}*/
		
		// if point is situated in rectangle area then return true, otherwise return false //
		if(newRect.contains((int)x,(int)y)) {
			return true;
		} else {
			return false;
		}
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
		this.setBounds(p.x-width-80, p.y-64, p.x+width+80, p.y+64);
	}

}
