package cz.vutbr.fit.testmind.graphics;

import java.util.ArrayList;
import java.util.List;

import cz.vutbr.fit.testmind.R;
import android.graphics.Canvas;
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
	
	private static final float STROKE_WIDTH = 5f;
	
	private TAMGraph graph;
	private String text;
	private List<ITAMConnection> listOfChildConnections;
	private List<ITAMConnection> listOfParentConnections;
	private Object object;
	
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
	private boolean isEnabled;
	private boolean isSelected;

	private OnNodeSelectListener selectListener;
	
	
	public TAMAbstractNode(TAMGraph graph, int x, int y, int offsetX, int offsetY, String text, Shape shape, int type) {
		super(shape);
		
		this.graph = graph;
		this.text = text;
		this.listOfChildConnections = new ArrayList<ITAMConnection>();
		this.listOfParentConnections = new ArrayList<ITAMConnection>();

		this.type = type;
		this.background = graph.getResources().getColor(R.color.node_background);
		this.backgroundStroke = graph.getResources().getColor(R.color.node_background_stroke);
		this.foreground = graph.getResources().getColor(R.color.node_text);
		this.highlightColor = graph.getResources().getColor(R.color.node_highlight_background);
		this.highlightColorStroke = graph.getResources().getColor(R.color.node_highlight_background_stroke);
		
		this.isHighlited = false;
		this.isEnabled = true;
		this.isSelected = false;
		
		this.position = new Point(x,y);
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		
		position = new Point(x, y);
		
		actualizeSize();
	}
	
	public abstract boolean hit(float x, float y);
	
	public abstract void move(int dx, int dy);
	
	public abstract void actualizePosition(int x, int y);

	public abstract void actualizeSize();
	
	public void setSelectEventListener(OnNodeSelectListener eventListner){
		this.selectListener = eventListner;
	}
	
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
	
	public void setBackground(int background) {
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
	
	public void setHighlighted(boolean enable) {
		isHighlited = enable;
	}
	
	public boolean isHighlighted() {
		return isHighlited;
	}
	
	public int getBackgroundStroke()
    {
        return backgroundStroke;
    }

    public void setBackgroundStroke(int backgroundStroke)
    {
        this.backgroundStroke = backgroundStroke;
    }

    public void setSelected(boolean enable) {
		
		if(isSelected != enable) {
			
			if(enable) {
				graph.listOfSelectedItems.add(this);
				graph.moveOnTop(this);
				
				if(selectListener != null){
					selectListener.onSelectNodeEvent(this);
				}
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
			
			for(ITAMConnection parentConnection : listOfParentConnections) {
				ITAMNode parentNode = parentConnection.getParentNode();
				if(parentNode.isEnabled() != enable) {
					if(enable) {
						graph.listOfDrawableItems.add(parentConnection);
					} else {
						graph.listOfDrawableItems.remove(parentConnection);
					}
					parentConnection.setEnabled(enable);
				}
			}
			
			for(ITAMConnection childConnection : listOfChildConnections) {
				ITAMNode childNode = childConnection.getChildNode();
				if(childNode.isEnabled() != enable) {
					if(enable) {
						graph.listOfDrawableItems.add(childConnection);
					} else {
						graph.listOfDrawableItems.remove(childConnection);
					}
					childConnection.setEnabled(enable);
				}
			}
			
			if(enable) {
				graph.listOfDrawableItems.add(this);
			} else {
				graph.listOfDrawableItems.remove(this);
			}
			
			this.isEnabled = enable;
		}
	}
	
	public boolean isEnabled() {
		return isEnabled;
	}
	
	public List<ITAMConnection> getListOfParentConnections() {
		return listOfParentConnections;
	}

	public List<ITAMConnection> getListOfChildConnections() {
		return listOfChildConnections;
	}

	@Override
	protected void onDraw(Shape shape, Canvas canvas, Paint paint) {
		
		paint.setStyle(Paint.Style.FILL);
		
		if(isHighlited) {
			paint.setColor(highlightColor);
		} else {
			paint.setColor(background);
		}
					
		super.onDraw(shape, canvas, paint);
		
		paint.setStrokeWidth(STROKE_WIDTH);
		paint.setStyle(Paint.Style.STROKE);
		
		if(isHighlited) {
			paint.setColor(highlightColorStroke);
		} else {
			paint.setColor(backgroundStroke);
		}
		
		super.onDraw(shape, canvas, paint);
		
		paint.setStyle(Paint.Style.FILL);
		
		paint.setColor(foreground);
		canvas.drawText(text, offsetX, offsetY, paint);
	}
	
	public void draw(Canvas canvas, Paint paint) {
		draw(canvas);
	}
	
	public void setHelpObject(Object object) {
		this.object = object;
	}

	public Object getHelpObject() {
		return object;
	}
}
