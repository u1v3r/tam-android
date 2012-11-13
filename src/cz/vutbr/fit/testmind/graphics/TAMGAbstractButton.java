package cz.vutbr.fit.testmind.graphics;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;

public abstract class TAMGAbstractButton extends ShapeDrawable implements ITAMGButton {
	
	private static final float STROKE_WIDTH = 5f;
	
	private TAMGraph graph;
	private String text;
	private Object object;
	private int type;
	
	private int colorBackground;
	private int colorBackgroundHighlight;
	private int colorText;
	private int colorStroke;
	private int colorStrokeHighlight;
	
	private boolean isHighlited;
	private boolean isEnabled;
	private boolean isSelected;
	
	public TAMGAbstractButton(TAMGraph graph, Shape shape, int type) {
		super(shape);
		
		this.graph = graph;

		this.type = type;
		
		this.isHighlited = false;
		this.isEnabled = true;
		this.isSelected = false;
	}

	public abstract boolean hit(float x, float y);
	
	public abstract void move(int dx, int dy);
	
	public abstract void actualizePosition(int x, int y);
	
	public int getType() {
		return type;
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
	
	public void setHighlighted(boolean enable) {
		isHighlited = enable;
	}
	
	public boolean isHighlighted() {
		return isHighlited;
	}

	public void setSelected(boolean enable) {
		isSelected = enable;
		setHighlighted(enable);
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setEnabled(boolean enable) {
		isEnabled = enable;
	}

	public boolean isEnabled() {
		return isEnabled;
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public TAMGraph getGraph() {
		return graph;
	}

	public void setHelpObject(Object object) {
		this.object = object;
	}

	public Object getHelpObject() {
		return object;
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
		
		paint.setStrokeWidth(STROKE_WIDTH);
		paint.setStyle(Paint.Style.STROKE);
		
		if(isHighlited) {
			paint.setColor(colorStrokeHighlight);
		} else {
			paint.setColor(colorStroke);
		}
		
		super.onDraw(shape, canvas, paint);
		
	}
	
	public void draw(Canvas canvas, Paint paint) {
		draw(canvas);
	}

	public void dispose() {
		graph = null;
		text = null;
		object = null;
	}

}
