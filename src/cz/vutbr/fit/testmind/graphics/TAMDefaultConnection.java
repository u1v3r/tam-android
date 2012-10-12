package cz.vutbr.fit.testmind.graphics;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

public class TAMDefaultConnection implements ITAMConnection {
	
	private static final int type = CONNECTION_TYPE_DEFAULT;
	private static final int HIGHLIGHT_COLOR = 0x7f040004;
	private int background;
	private int highlightColor;
	private ITAMNode parent;
	private ITAMNode child;
	private TAMGraph graph;
	
	private boolean isHighlighted;
	private boolean isEnabled;
	private boolean isSelected;
	
	private List<Point> listOfMiddlePoints;
	private Point selectedPoint;
	
	private Point from;
	private Point to;
	
	public static int OFFSET = 10;

	public TAMDefaultConnection(TAMGraph graph, ITAMNode parent, ITAMNode child) {
		this.graph = graph;
		this.parent = parent;
		this.child = child;
		this.background = Color.RED;
		this.highlightColor = graph.getResources().getColor(HIGHLIGHT_COLOR);
		
		this.isHighlighted = false;
		this.isEnabled = true;
		this.isSelected = false;
		
		this.listOfMiddlePoints = new ArrayList<Point>();
		this.selectedPoint = null;
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
		int a = 0;
		Point from = parent.getPosition();
		for(Point to : listOfMiddlePoints) {
			
			canvas.drawLine(from.x, from.y, to.x, to.y, paint);
			
			from = to;
		}
		
		// last or one line type //
		canvas.drawLine(from.x, from.y,
				child.getPosition().x, child.getPosition().y, paint);
		
		if(isHighlighted) {
			paint.setColor(Color.BLACK);
			
			for(Point point : listOfMiddlePoints) {
				canvas.drawRect(point.x-10, point.y-10, point.x+10, point.y+10, paint);
			}
		}

	}
	
	private boolean inRange(float value, float from, float to) {
		
		if(from > to) {
			if(value >= (to-20) && value <= (from+20)) {
				return true;
			} else {
				return false;
			}
		} else {
			if(value >= (from-20) && value <= (to+20)) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	private float getLineParameter(float p, float a, float b) {
		return (p-a)/(b-a);
	}
	
	private boolean checkOneHit(float x, float y, Point pos1, Point pos2) {
		
		if(inRange(x,pos1.x,pos2.x) && inRange(y,pos1.y,pos2.y)) {
			
			int a = (pos2.x-pos1.x);
			int b = (pos2.y-pos1.y);
			float up = (Math.abs(b*x-a*y-(b*pos1.x-a*pos1.y)));
			float down = (float) (a*a+b*b);
			
			float num = up*up/down;
			
			if(graph.sx > 1) {
				num*=graph.sx;
			}
			
			if(num < OFFSET*OFFSET) {
				this.from = pos1;
				this.to = pos2;
				return true;
			}
		}
		
		return false;
	}

	public boolean hit(float x, float y) {
			
		Point from = parent.getPosition();
		
		for(Point to : listOfMiddlePoints) {
			
			if(checkOneHit(x, y, from, to)) {
				return true;
			}
			
			from = to;
		}
		
		if(checkOneHit(x, y, from, child.getPosition())) {
			return true;
		}
		return false;
	}

	public void setBackground(int background) {
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
				selectedPoint = null;
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
	
	private void moveOnePoint(Point point, int dx, int dy) {
		if(point != null) {
			point.x += dx;
			point.y += dy;
		}
	}
	
	public void moveSelectedPoint(int dx, int dy) {
		System.out.println(selectedPoint);
		moveOnePoint(selectedPoint, dx, dy);
	}

	public void move(int dx, int dy) {
		for(Point point : listOfMiddlePoints) {
			moveOnePoint(point, dx, dy);
		}
	}

	public void setForeground(int foreground) {
		// TODO Auto-generated method stub
		
	}

	public int getForeground() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Point getSelectedPoint() {
		return selectedPoint;
	}
	
	private boolean laysNearPoint(int x, int y, Point p) {
		
		if(Math.abs(x-p.x) <= OFFSET && Math.abs(y-p.y) <= OFFSET) {
			return true;
		} else {
			return false;
		}
	}

	public void setSelectedPoint(float x, float y) {
		
		if(laysNearPoint((int) x, (int) y, from)) {
			selectedPoint = from;
		} else if(laysNearPoint((int) x, (int) y, to)) {
			selectedPoint = to;
		} else {
			int newX;
			int newY;
			
			if(from.x > to.x-OFFSET && from.x < to.x+OFFSET) {
				newY = (int) y;
				float t = getLineParameter(y, from.y, to.y);
				newX = (int) (from.x+t*(to.x-from.x));
			} else if(from.y > to.y-OFFSET && from.y < to.y+OFFSET) {
				newX = (int) x;
				float t = getLineParameter(x, from.x, to.x);
				newY = (int) (from.y+t*(to.y-from.y));
			} else {
				float left = getLineParameter(x, from.x, to.x);
				float right = getLineParameter(y, from.y, to.y);
				
				//float left1 = left-(left*0.2f);
				//float left2 = left+(left*0.2f);
				
				//if(inRange(right, left1, left2)) {
				float t = (left+right)/2;
				
				newX = (int) (from.x+t*(to.x-from.x));
				newY = (int) (from.y+t*(to.y-from.y));
			}
				
				for(Point point : listOfMiddlePoints) {
					if(point.equals(newX, newY)) {
						this.selectedPoint = point;
						return;
					}
				}
				Point newPoint = new Point(newX, newY);
				this.selectedPoint = newPoint;
				if(from == parent.getPosition()) {
					listOfMiddlePoints.add(0,newPoint);
				} else {
					int a = 1;
					for(Point point : listOfMiddlePoints) {
						if(from == point) {
							listOfMiddlePoints.add(a,newPoint);
							return;
						}
						a++;
					}
				}
			//}
		}
	}

}
