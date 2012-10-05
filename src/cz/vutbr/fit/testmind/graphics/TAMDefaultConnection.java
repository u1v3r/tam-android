package cz.vutbr.fit.testmind.graphics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.shapes.PathShape;

public class TAMDefaultConnection implements ITAMConnection {
	
	private final int type = CONNECTION_TYPE_DEFAULT;
	private int background;
	private int highlightColor;
	private boolean isHighlighted;
	private ITAMNode parent;
	private ITAMNode child;
	private TAMGraph graph;

	public TAMDefaultConnection(TAMGraph graph, ITAMNode parent, ITAMNode child) {
		this.graph = graph;
		this.parent = parent;
		this.child = child;
		this.background = Color.RED;
		this.highlightColor = Color.YELLOW;
		this.isHighlighted = false;
	}
	
	public int getType() {
		return type;
	}

	public void draw(Canvas canvas, Paint paint) {
		

		
		paint.setAntiAlias(true);
		if(isHighlighted) {
			paint.setColor(highlightColor);
		} else {
			paint.setColor(background);
		}
		
		
		canvas.drawLine(parent.getPosition().x, parent.getPosition().y,
				child.getPosition().x, child.getPosition().y, paint);

	}

	public boolean hit(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	public void setHighlight(boolean enable) {
		isHighlighted = enable;
	}

	public void setBackgroud(int background) {
		this.background = background;
	}

	public int getBackground() {
		return background;
	}

	public void setHighlightColor(int highlightColor) {
		this.highlightColor = highlightColor;
	}

	public int getHighlightColor() {
		return highlightColor;
	}

	public boolean isHighlighted() {
		return isHighlighted;
	}

	public void move(int dx, int dy) {
		// not supported - connections are fixed to nodes //
	}

}
