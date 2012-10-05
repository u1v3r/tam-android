package cz.vutbr.fit.tesmind.graphics;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

public interface ITAMItem {
	
	public int getType();
	
	public boolean hit(int x, int y);

	public void setHighlight(boolean enable);
	
	public void setBackgroud(int background);
	
	public int getBackground();
	
	public void setHighlightColor(int highlightColor);
	
	public int getHighlightColor();
	
	public boolean isHighlighted();

	public void move(int dx, int dy);

}
