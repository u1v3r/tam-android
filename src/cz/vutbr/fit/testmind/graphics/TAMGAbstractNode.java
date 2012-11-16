package cz.vutbr.fit.testmind.graphics;

import java.util.ArrayList;
import java.util.List;

import cz.vutbr.fit.testmind.R;
import cz.vutbr.fit.testmind.editor.controls.TAMENodeControl;
import cz.vutbr.fit.testmind.editor.controls.TAMENodeControl.BackgroundStyle;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;

/**
 * Abstract rectangle class providing basic functionality of drawable nodes.
 * 
 * @author jurij
 *
 */
public abstract class TAMGAbstractNode extends ShapeDrawable implements ITAMGNode {
	
	/**
	 * Velkost border {normal,collapse}
	 */
	public static final float [] STROKE_WIDTH = { 6.4f , 11f };
	
	/**
	 * Zakladna velkost fontu.
	 * <br><br>
	 * Child triedy si musia fonty samy skalovat v zavilosti od tejto velkosti.
	 */
	protected static final float FONT_DEFAULT_SIZE = 50f;
	
	private static final String TAG = "TAMGAbstractNode";
	
	private TAMGraph graph;
	private String text;
	private List<ITAMGConnection> listOfChildConnections;
	private List<ITAMGConnection> listOfParentConnections;
	private Object object;
	
	private int type;
	private int state = NODE_STATE_DEFAULT;
	
	private Point position;
	private int offsetX;
	private int offsetY;
	
	private int colorBackground;
	private int colorBackgroundHighlight;
	private int colorText;
	private int colorStroke;
	private int colorStrokeHighlight;
	
	private boolean isHighlited;
	private boolean isEnabled;
	private boolean isSelected;

	private BackgroundStyle backgroundStyle;

	private Paint textPaint;	
	
	public TAMGAbstractNode(TAMGraph graph, int x, int y, int offsetX, int offsetY, String text, Shape shape, int type) {
		super(shape);
		
		this.graph = graph;
		this.listOfChildConnections = new ArrayList<ITAMGConnection>();
		this.listOfParentConnections = new ArrayList<ITAMGConnection>();

		this.type = type;
		this.colorBackground = graph.getResources().getColor(R.color.node_background_1);		
		this.colorStroke = graph.getResources().getColor(R.color.node_background_stroke_1);
		this.colorText = graph.getResources().getColor(R.color.node_text);
		this.colorBackgroundHighlight = graph.getResources().getColor(R.color.node_highlight_background);
		this.colorStrokeHighlight = graph.getResources().getColor(R.color.node_highlight_background_stroke);
		this.backgroundStyle = BackgroundStyle.BLUE;
		
		this.isHighlited = false;
		this.isEnabled = true;
		this.isSelected = false;
		
		this.position = new Point(x,y);
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		
		position = new Point(x, y);
		setText(text);
		
		textPaint = new Paint();
		textPaint.setTextSize(FONT_DEFAULT_SIZE);
		//actualizeSize();
	}
	
	public abstract boolean hit(float x, float y);
	
	public abstract void move(int dx, int dy);
	
	public abstract void actualizePosition(int x, int y);

	public abstract void actualizeSize();
		
	public Point getPosition() {
		return position;
	}
	
	public int getNodeState() {
		return state;
	}
	
	public void setNodeState(int state) {
		if(state == NODE_STATE_DEFAULT || state == NODE_STATE_COLLAPSE) {
			this.state = state;
		}
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
	    if(text == null) {
	        this.text = "";
	    } else {
	        this.text = text;
	    }
		actualizeSize();
	}

	public int getColorBackground() {
		return colorBackground;
	}
	
	public void setColorBackground(int color) {
		this.colorBackground = color;
	}
	
	public int getColorBackgroundHighlight() {
		return colorBackgroundHighlight;
	}
	
	public void setColorBackgroundHighlight(int color) {
		this.colorBackgroundHighlight = color;
	}
	
	public int getColorText() {
		return colorText;
	}

	public void setColorText(int color) {
		this.colorText = color;
	}

	public int getColorStrokeHighlight() {
		return colorStrokeHighlight;
	}

	public void setColorStrokeHighlight(int color) {
		this.colorStrokeHighlight = color;
	}

	public int getColorStroke() {
		return colorStroke;
	}

	public void setColorStroke(int strokeColor) {
		this.colorStroke = strokeColor;
	}

	public ITAMGItem addChild(int x, int y, String text) {
		return addChild(type, x, y, text);
	}

	public ITAMGItem addChild(int type, int x, int y, String text) {
		
		// create new node from item factory //
		ITAMGNode node = graph.getGItemFactory().createNode(graph, type, x, y, text);
		
		// create new connection between these nodes from item factory //
		graph.getGItemFactory().createConnection(graph, this, node, ITAMGConnection.CONNECTION_TYPE_DEFAULT);
		
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
        return colorStroke;
    }

    public void setBackgroundStroke(int backgroundStroke)
    {
        this.colorStroke = backgroundStroke;
    }
    
    public void setBackgroundStyle(TAMENodeControl.BackgroundStyle style){
    	
    	Resources res = graph.getResources();
    	if(BackgroundStyle.BLUE == style){
    		backgroundStyle = style;
    		setColorBackground(res.getColor(R.color.node_background_1));
    		setBackgroundStroke(res.getColor(R.color.node_background_stroke_1));
    	}else if(BackgroundStyle.GREEN == style){
    		backgroundStyle = style;
    		setColorBackground(res.getColor(R.color.node_background_2));
    		setBackgroundStroke(res.getColor(R.color.node_background_stroke_2));
    	}else if(BackgroundStyle.RED == style){
    		backgroundStyle = style;
    		setColorBackground(res.getColor(R.color.node_background_3));
    		setBackgroundStroke(res.getColor(R.color.node_background_stroke_3));
    	}else if(BackgroundStyle.PURPLE == style){
    		backgroundStyle = style;
    		setColorBackground(res.getColor(R.color.node_background_4));
    		setBackgroundStroke(res.getColor(R.color.node_background_stroke_4));
    	}
    }
    
    public TAMENodeControl.BackgroundStyle getBackgroundStyle(){
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
			
			this.isEnabled = enable;
			if(enable) {
				graph.listOfDrawableItems.add(this);
			} else {
				graph.listOfDrawableItems.remove(this);
			}
			
			for(ITAMGConnection parentConnection : listOfParentConnections) {
				//ITAMGNode parentNode = parentConnection.getParentNode();
				//if(parentNode.isEnabled() != enable) {
				//	if(enable) {
				//		graph.listOfDrawableItems.add(parentConnection);
				//	} else {
				//		graph.listOfDrawableItems.remove(parentConnection);
				//	}
					parentConnection.setEnabled(enable);
				//}
			}
			
			for(ITAMGConnection childConnection : listOfChildConnections) {
				//ITAMGNode childNode = childConnection.getChildNode();
				//if(childNode.isEnabled() != enable) {
				//	if(enable) {
				//		graph.listOfDrawableItems.add(childConnection);
				//	} else {
				//		graph.listOfDrawableItems.remove(childConnection);
				//	}
					childConnection.setEnabled(enable);
				//}
			}
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
			paint.setColor(colorBackgroundHighlight);
		} else {
			paint.setColor(colorBackground);
		}
					
		super.onDraw(shape, canvas, paint);
		
		paint.setStrokeWidth(STROKE_WIDTH[state]);
		paint.setStyle(Paint.Style.STROKE);
		
		if(isHighlited) {
			paint.setColor(colorStrokeHighlight);
		} else {
			paint.setColor(colorStroke);
		}
		
		super.onDraw(shape, canvas, paint);
		
		paint.setStyle(Paint.Style.FILL);
		
		textPaint.setColor(colorText);
		textPaint.setTextSize(FONT_DEFAULT_SIZE);
		textPaint.setTypeface(Typeface.DEFAULT_BOLD);
		canvas.drawText(text, offsetX, offsetY, textPaint);
		
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
