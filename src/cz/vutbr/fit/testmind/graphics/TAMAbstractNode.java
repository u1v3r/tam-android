package cz.vutbr.fit.testmind.graphics;

import cz.vutbr.fit.testmind.R;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.util.Log;

/**
 * Abstract rectangle class providing basic functionality of drawable nodes.
 * 
 * @author jurij
 *
 */
public abstract class TAMAbstractNode extends ShapeDrawable implements ITAMNode {
	
	private TAMGraph graph;
	private String text;
	
	private int type;
	
	private Point position;
	private int offsetX;
	private int offsetY;
	
	private int background;
	private int foreground;
	private int highlightColor;
	private int highlightColorStroke;
	private int backgroundStroke;
	
	private boolean isHighlited;
	
	
	public TAMAbstractNode(TAMGraph graph, int x, int y, int offsetX, int offsetY, String text, Shape shape, int type) {
		super(shape);
		
		this.graph = graph;
		this.text = text;

		this.type = type;
		this.background = graph.getResources().getColor(R.color.node_background);
		this.backgroundStroke = graph.getResources().getColor(R.color.node_background_stroke);
		this.foreground = graph.getResources().getColor(R.color.node_text);
		this.highlightColor = graph.getResources().getColor(R.color.node_highlight_background);
		this.highlightColorStroke = graph.getResources().getColor(R.color.node_highlight_background_stroke);
		
		this.isHighlited = false;
		
		this.position = new Point(x,y);
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		
		position = new Point(x, y);
		
		actualizeSize();
	}
	
	public abstract boolean hit(int x, int y);
	
	public abstract void move(int dx, int dy);
	
	public abstract void actualizePosition(int x, int y);

	public abstract void actualizeSize();
	
	public Point getPosition() {
		return position;
	}
	
	protected void setPosition(int x, int y) {
		this.position.x = x;
		this.position.y = y;
	}

	public int getType() {
		return type;
	}

	public TAMGraph getGraph() {
		return graph;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		actualizeSize();
	}

	public int getBackground() {
		return background;
	}
	
	public void setBackgroud(int background) {
		this.background = background;
	}
	
	public int getForeground() {
		return foreground;
	}

	public void setForeground(int foreground) {
		this.foreground = foreground;
	}
	
	public int getHighlightColor() {
		return highlightColor;
	}
	
	public void setHighlightColor(int highlightColor) {
		this.highlightColor = highlightColor;
	}

	public void highlight(boolean enable) {
		isHighlited = enable;
	}
	
	public boolean isHighlighted() {
		return isHighlited;
	}

	public ITAMItem addChild(int x, int y, String text) {
		return addChild(type, x, y, text);
	}

	public ITAMItem addChild(int type, int x, int y, String text) {
		
		// create new node from item factory //
		ITAMNode node = graph.getItemFactory().createNode(graph, type, x, y, text);
		
		// create new connection between these nodes from item factory //
		graph.getItemFactory().createConnection(graph, this, node, ITAMConnection.CONNECTION_TYPE_DEFAULT);
		
		return node;
	}

	public float getWidth() {
		return getShape().getWidth();
	}
	
	public float getHeight() {
		return getShape().getHeight();
	}
	
	@Override
	protected void onDraw(Shape shape, Canvas canvas, Paint paint) {
		Paint strokePaint = new Paint();
		strokePaint.setStrokeWidth(2f);
		strokePaint.setStyle(Paint.Style.STROKE);
		
		if(isHighlited) {
			paint.setColor(highlightColor);
			strokePaint.setColor(highlightColorStroke);
		} else {
			paint.setColor(background);
			strokePaint.setColor(backgroundStroke);
		}
					
		super.onDraw(shape, canvas, paint);
		super.onDraw(shape, canvas, strokePaint);
		
		paint.setColor(foreground);
		canvas.drawText(text, offsetX, offsetY, paint);
	}
}