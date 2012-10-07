package cz.vutbr.fit.testmind.graphics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class TAMDefaultConnection implements ITAMConnection {
	
	private final int type = CONNECTION_TYPE_DEFAULT;
	private int background;
	private int highlightColor;
	private boolean isHighlighted;
	private ITAMNode parent;
	private ITAMNode child;
	private TAMGraph graph;
	private boolean isEnabled;

	public TAMDefaultConnection(TAMGraph graph, ITAMNode parent, ITAMNode child) {
		this.graph = graph;
		this.parent = parent;
		this.child = child;
		this.background = Color.RED;
		this.highlightColor = Color.YELLOW;
		this.isHighlighted = false;
		this.isEnabled = true;
	}
	
	public int getType() {
		return type;
	}
	
	public ITAMNode getParentNode() {
		return parent;
	}

	public ITAMNode getChildNode() {
		return child;
	}

	public TAMGraph getGraph() {
		return graph;
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
	
	/*private boolean inRange(int value, int from, int to) {
		
		if(from > to) {
			if(value >= to && value <= from) {
				return true;
			} else {
				return false;
			}
		} else {
			if(value >= from && value <= to) {
				return true;
			} else {
				return false;
			}
		}
	}*/

	public boolean hit(int x, int y) {
		// not supported //
		
		//Point pos1 = parent.getPosition();
		//Point pos2 = child.getPosition();
		
		/*if(inRange(x,pos1.x,pos2.x) && inRange(y,pos1.y,pos2.y)) {
			//TODO
			return true;
		}*/
		return false;
	}

	public void highlight(boolean enable) {
		isHighlighted = enable;
		//graph.invalidate();
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

	public void setForeground(int foreground) {
		// TODO Auto-generated method stub
		
	}

	public int getForeground() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setEnabled(boolean enable) {
		isEnabled = enable;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

}
