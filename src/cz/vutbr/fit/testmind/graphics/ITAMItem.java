package cz.vutbr.fit.testmind.graphics;

import android.graphics.Canvas;
import android.graphics.Paint;

public interface ITAMItem {
	
	public int getType();
	
	public boolean hit(int x, int y);

	public void highlight(boolean enable);
	
	public void setBackgroud(int background);
	
	public int getBackground();
	
	public void setForeground(int foreground);
	
	public int getForeground();
	
	public void setHighlightColor(int highlightColor);
	
	public int getHighlightColor();
	
	public boolean isHighlighted();

	public void move(int dx, int dy);
	
	public void setEnabled(boolean enable);
	
	public boolean isEnabled();
	
	public void draw(Canvas canvas, Paint paint);
	
	public TAMGraph getGraph();

}
