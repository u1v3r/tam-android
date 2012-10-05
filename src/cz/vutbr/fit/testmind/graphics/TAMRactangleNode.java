package cz.vutbr.fit.testmind.graphics;

import cz.vutbr.fit.tesmind.graphics.ITAMConnection;
import cz.vutbr.fit.tesmind.graphics.ITAMNode;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;

public class TAMRactangleNode extends ShapeDrawable implements ITAMNode {
	
	private Point position;
	private Point size;
	private final int type = NODE_TYPE_RECTANGLE;
	private TAMGraph graph;
	private int background;
	private int highlightColor;
	private boolean isHighlited;
	
	public TAMRactangleNode(TAMGraph graph, int x, int y) {
		super(new RectShape());
		
		this.graph = graph;
		this.position = new Point(x,y);
		this.size = new Point(20, 20); // TODO count new size due to text
		this.background = Color.BLUE;
		this.highlightColor = Color.YELLOW;
		this.isHighlited = false;
		this.getPaint().setColor(background);
	    this.setBounds(x-10, y-10, x+10, y+10);
	}
	
	public ITAMItem addChild(int x, int y) {
		return addChild(type, x, y);
	}

	public ITAMItem addChild(int type, int x, int y) {
		
		ITAMNode node = graph.getItemFactory().createNode(graph, type, x, y);
		graph.getItemFactory().createConnection(graph, this, node, ITAMConnection.CONNECTION_TYPE_DEFAULT);
		
		return node;
	}

	public int getType() {
		return type;
	}

	public Point getPosition() {
		return position;
	}

	public Point getSize() {
		return size;
	}
	
	public boolean hit(int x, int y) {
		
		Rect rect = this.getBounds();
		
		if(x >= rect.left && x <= rect.right && y >= rect.top && y <= rect.bottom) {
			return true;
		} else {
			return false;
		}
	}
	
	public void setBackgroud(int background) {
		this.background = background;
		if(!isHighlited) {
			this.getPaint().setColor(background);
		}
	}
	
	public int getBackground() {
		return background;
	}
	
	public void setHighlightColor(int highlightColor) {
		this.highlightColor = highlightColor;
		if(isHighlited) {
			this.getPaint().setColor(highlightColor);
		}
	}
	
	public int getHighlightColor() {
		return highlightColor;
	}

	public void setHighlight(boolean enable) {
		
		if(enable) {
			this.getPaint().setColor(highlightColor);
		} else {
			this.getPaint().setColor(background);
		}
		
		isHighlited = enable;
		//graph.refreshDrawableState();
		graph.invalidate();
	}
	
	public boolean isHighlighted() {
		return isHighlited;
	}

	public void move(int dx, int dy) {
		
		position.x += dx;
		position.y += dy;
		
		Rect rect = this.getBounds();
		this.setBounds(rect.left+dx, rect.top+dy, rect.right+dx, rect.bottom+dy);
		
		graph.invalidate();
	}

}
