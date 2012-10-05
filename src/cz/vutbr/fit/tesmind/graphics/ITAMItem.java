package cz.vutbr.fit.tesmind.graphics;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

public interface ITAMItem {
	
	public int ITEM_TYPE_RECTANGLE = 1;
	
	public ITAMItem addChild(int x, int y);
	
	public ITAMItem addChild(int type, int x, int y);
	
	public int getType();
	
	public Point getPosition();
	
	public Point getSize();
	
	public void draw(Canvas canvas);
	
	public Rect getBounds();
	
	public boolean hit(int x, int y);

	public void setHighlight(boolean enable);
	
	public void setBackgroud(int background);
	
	public int getBackground();
	
	public void highlightColor(int highlightColor);
	
	public int highlightColor();
	
	public boolean isHighlighted();

	public void move(int dx, int dy);

}
