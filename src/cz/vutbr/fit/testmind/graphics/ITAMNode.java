package cz.vutbr.fit.testmind.graphics;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

public interface ITAMNode extends ITAMItem {
	
	public final int NODE_TYPE_RECTANGLE = 1;
	
	public ITAMItem addChild(int x, int y, String text);
	
	public ITAMItem addChild(int type, int x, int y, String text);
	
	public Rect getBounds();
	
	public Point getPosition();
	
	public Point getSize();
	
	public void draw(Canvas canvas);

}
