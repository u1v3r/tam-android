package cz.vutbr.fit.testmind.graphics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class TAMDefaultConnection implements ITAMConnection {
	
	private final int type = CONNECTION_TYPE_DEFAULT;
	private int background;
	private int highlightColor;
	private ITAMNode parent;
	private ITAMNode child;
	private TAMGraph graph;
	
	private boolean isHighlighted;
	private boolean isEnabled;
	private boolean isSelected;

	public TAMDefaultConnection(TAMGraph graph, ITAMNode parent, ITAMNode child) {
		this.graph = graph;
		this.parent = parent;
		this.child = child;
		this.background = Color.RED;
		this.highlightColor = Color.YELLOW;
		
		this.isHighlighted = false;
		this.isEnabled = true;
		this.isSelected = false;
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
		
		paint.setStrokeWidth(6);
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
	
	public void setHighlighted(boolean enable) {
		isHighlighted = enable;
		//graph.invalidate();
	}

	public boolean isHighlighted() {
		return isHighlighted;
	}
	
	public void setSelected(boolean enable) {
		
		if(isSelected != enable) {
			
			if(enable) {
				graph.listOfSelectedItems.add(this);
				graph.moveOnTop(this);
			} else {
				graph.listOfSelectedItems.remove(this);
			}
			
			setHighlighted(enable);
			this.isSelected = enable;
		}
	}

	public boolean isSelected() {
		return isSelected;
	}
	
	public void setEnabled(boolean enable) {
		
		if(isEnabled != enable) {
			
			if(enable) {
				graph.listOfDrawableItems.add(this);
			} else {
				graph.listOfDrawableItems.remove(this);
			}
			
			parent.setEnabled(enable);
			child.setEnabled(enable);
		}
		
		isEnabled = enable;
	}

	public boolean isEnabled() {
		return isEnabled;
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

}
