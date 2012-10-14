package cz.vutbr.fit.testmind.graphics;

import java.util.List;

import cz.vutbr.fit.testmind.editor.items.TAMENode;

import android.graphics.Point;
import android.graphics.Rect;

public interface ITAMGNode extends ITAMGItem {
	
	public interface OnNodeSelectListener{
		public void onSelectNodeEvent(ITAMGNode node);
		public void onUnselectNodeEvent(ITAMGNode node);
		public void onMoveNodeEvent(ITAMGNode node);
	}
		
	public final int NODE_TYPE_RECTANGLE = 1;
	public final int NODE_TYPE_ROUND_RECTANGLE = 2;
	
	public ITAMGItem addChild(int x, int y, String text);
	
	public ITAMGItem addChild(int type, int x, int y, String text);
	
	public Rect getBounds();
	
	public Point getPosition();
	
	public void actualizePosition(int x, int y);
	
	public float getWidth();
	
	public float getHeight();
	
	//public void draw(Canvas canvas);
	
	public void actualizeSize();
	
	public String getText();
	
	public void setText(String text);
	
	public List<ITAMGConnection> getListOfParentConnections();
	
	public List<ITAMGConnection> getListOfChildConnections();

    public int getBackgroundStroke();
    
    public void setBackgroundStroke(int backgroundStroke);
    
	public void setSelectEventListener(OnNodeSelectListener eventListner);
}
