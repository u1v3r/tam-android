package cz.vutbr.fit.testmind.graphics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.Shape;

public class TAMRactangleNode extends ShapeDrawable implements ITAMNode {
	
	public final int TEXT_SIZE = 14;
	private Point position;
	private Point size;
	private final int type = NODE_TYPE_RECTANGLE;
	private TAMGraph graph;
	private int background;
	private int foreground;
	private int highlightColor;
	private boolean isHighlited;
	private String text;
	
	public TAMRactangleNode(TAMGraph graph, int x, int y, String text) {
		super(new RectShape());
		
		this.getPaint().setTextSize(TEXT_SIZE);
		
		this.graph = graph;
		this.position = new Point(x,y);
		this.text = text;
		int width = (int) this.getPaint().measureText(text)/2;
		/*Rect rect = new Rect();
		this.getPaint().getTextBounds(text, 0, 0, rect);
		System.out.println("rect: " + rect);*/
		System.out.println(width);
		this.size = new Point((width*2+40), 20); // TODO count new size due to text
		this.background = 0xffffdab9;
		this.foreground = Color.BLACK;
		this.highlightColor = Color.YELLOW;
		this.isHighlited = false;
	    this.setBounds(x-width-20, y-16, x+width+20, y+16);
	    
	    this.getPaint().setColor(background);
	}
	
	public ITAMItem addChild(int x, int y, String text) {
		return addChild(type, x, y, text);
	}

	public ITAMItem addChild(int type, int x, int y, String text) {
		
		ITAMNode node = graph.getItemFactory().createNode(graph, type, x, y, text);
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
		
		if(rect.contains(x,y)) {
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
		
		//graph.invalidate();
	}
	
	public boolean isHighlighted() {
		return isHighlited;
	}

	public void move(int dx, int dy) {
		
		position.x += dx;
		position.y += dy;
		
		Rect rect = this.getBounds();
		this.setBounds(rect.left+dx, rect.top+dy, rect.right+dx, rect.bottom+dy);
		
		//graph.invalidate();
	}
	
	@Override
	protected void onDraw(Shape shape, Canvas canvas, Paint paint) {
		
		if(isHighlited) {
			paint.setColor(highlightColor);
		} else {
			paint.setColor(background);
		}
		
		super.onDraw(shape, canvas, paint);
		
		paint.setColor(foreground);
		canvas.drawText(text, 20, 20, paint);
	}

}
