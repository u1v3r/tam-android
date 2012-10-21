package cz.vutbr.fit.testmind.graphics;

import java.util.ArrayList;
import java.util.List;

import cz.vutbr.fit.testmind.R;
import cz.vutbr.fit.testmind.editor.controls.TAMEditorNodesControl;
import cz.vutbr.fit.testmind.editor.controls.TAMEditorNodesControl.BackgroundStyle;
import android.content.res.Resources;
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
public abstract class TAMGAbstractNode extends ShapeDrawable implements ITAMGNode {
	
	private static final float STROKE_WIDTH = 5f;

	private static final String TAG = "TAMGAbstractNode";
	
	private TAMGraph graph;
	private String text;
	private List<ITAMGConnection> listOfChildConnections;
	private List<ITAMGConnection> listOfParentConnections;
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

	private BackgroundStyle backgroundStyle;	
	
	public TAMGAbstractNode(TAMGraph graph, int x, int y, int offsetX, int offsetY, String text, Shape shape, int type) {
		super(shape);
		
		this.graph = graph;
		this.text = text;
		this.listOfChildConnections = new ArrayList<ITAMGConnection>();
		this.listOfParentConnections = new ArrayList<ITAMGConnection>();

		this.type = type;
		this.background = graph.getResources().getColor(R.color.node_background_1);		
		this.backgroundStroke = graph.getResources().getColor(R.color.node_background_stroke_1);
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

	public ITAMGItem addChild(int x, int y, String text) {
		return addChild(type, x, y, text);
	}

	public ITAMGItem addChild(int type, int x, int y, String text) {
		
		// create new node from item factory //
		ITAMGNode node = graph.getItemFactory().createNode(graph, type, x, y, text);
		
		// create new connection between these nodes from item factory //
		graph.getItemFactory().createConnection(graph, this, node, ITAMGConnection.CONNECTION_TYPE_DEFAULT);
		
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
    
    public void setBackgroundStyle(TAMEditorNodesControl.BackgroundStyle style){
    	
    	Resources res = graph.getResources();
    	if(BackgroundStyle.BLUE == style){
    		backgroundStyle = style;
    		setBackground(res.getColor(R.color.node_background_1));
    		setBackgroundStroke(res.getColor(R.color.node_background_stroke_1));
    	}else if(BackgroundStyle.GREEN == style){
    		backgroundStyle = style;
    		setBackground(res.getColor(R.color.node_background_2));
    		setBackgroundStroke(res.getColor(R.color.node_background_stroke_2));
    	}else if(BackgroundStyle.RED == style){
    		backgroundStyle = style;
    		setBackground(res.getColor(R.color.node_background_3));
    		setBackgroundStroke(res.getColor(R.color.node_background_stroke_3));
    	}else if(BackgroundStyle.PURPLE == style){
    		backgroundStyle = style;
    		setBackground(res.getColor(R.color.node_background_4));
    		setBackgroundStroke(res.getColor(R.color.node_background_stroke_4));
    	}
    }
    
    public TAMEditorNodesControl.BackgroundStyle getBackgroundStyle(){
    	return backgroundStyle;
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
			graph.onItemSelectEvent(this, enable);
		}
	}
	
	public boolean isSelected() {
		return isSelected;
	}
	
	public void setEnabled(boolean enable) {
		
		if(isEnabled != enable) {
			
			for(ITAMGConnection parentConnection : listOfParentConnections) {
				ITAMGNode parentNode = parentConnection.getParentNode();
				if(parentNode.isEnabled() != enable) {
					if(enable) {
						graph.listOfDrawableItems.add(parentConnection);
					} else {
						graph.listOfDrawableItems.remove(parentConnection);
					}
					parentConnection.setEnabled(enable);
				}
			}
			
			for(ITAMGConnection childConnection : listOfChildConnections) {
				ITAMGNode childNode = childConnection.getChildNode();
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
	
	public List<ITAMGConnection> getListOfParentConnections() {
		return listOfParentConnections;
	}

	public List<ITAMGConnection> getListOfChildConnections() {
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
	
	public void dispose() {
		graph = null;
		text = null;
		listOfChildConnections.clear();
		listOfParentConnections.clear();
		object = null;
	}
}
