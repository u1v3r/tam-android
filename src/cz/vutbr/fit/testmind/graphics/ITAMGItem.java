package cz.vutbr.fit.testmind.graphics;

import android.graphics.Canvas;
import android.graphics.Paint;

public interface ITAMGItem {
	
	public int getType();
	
	public boolean hit(float x, float y);
	
	public void setColorBackground(int color);
		
	public int getColorBackground();
	
	public void setColorText(int color);
	
	public int getColorText();
	
	public void setColorBackgroundHighlight(int color);
	
	public int getColorBackgroundHighlight();
	
	public int getColorStroke();

	public void setColorStroke(int color);
	
	public int getColorStrokeHighlight();

	public void setColorStrokeHighlight(int color);
	
	public String getText();
	
	public void setText(String text);

	public void move(int dx, int dy);
	
	public void setHighlighted(boolean enable);
	
	public boolean isHighlighted();
	
	public void setSelected(boolean enable);
	
	public boolean isSelected();
	
	public void setEnabled(boolean enable);
	
	public boolean isEnabled();
	
	public void draw(Canvas canvas, Paint paint);
	
	public TAMGraph getGraph();
	
	public void setHelpObject(Object object);
	
	public Object getHelpObject();
	
	public void dispose();

}
