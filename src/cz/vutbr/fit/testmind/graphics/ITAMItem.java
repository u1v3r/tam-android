package cz.vutbr.fit.testmind.graphics;

import android.graphics.Canvas;
import android.graphics.Paint;

public interface ITAMItem {
	
	public int getType();
	
	public boolean hit(float x, float y);
	
	public void setBackground(int background);
	
	public int getBackground();
	
	public void setForeground(int foreground);
	
	public int getForeground();
	
	public void setHighlightColor(int highlightColor);
	
	public int getHighlightColor();

	public void move(int dx, int dy);
	
	public void setHighlighted(boolean enable);
	
	public boolean isHighlighted();
	
	public void setSelected(boolean enable);
	
	public boolean isSelected();
	
	public void setEnabled(boolean enable);
	
	public boolean isEnabled();
	
	public void draw(Canvas canvas, Paint paint);
	
	public TAMGraph getGraph();

}
