package cz.vutbr.fit.testmind.graphics;

import java.util.List;

import cz.vutbr.fit.testmind.editor.controls.TAMEditorNodeControl;

import android.graphics.Point;
import android.graphics.Rect;

public interface ITAMGNode extends ITAMGItem {
		
	public final int NODE_TYPE_RECTANGLE = 1;
	public final int NODE_TYPE_ROUND_RECTANGLE = 2;
	
	public final int NODE_STATE_DEFAULT = 0;
	public final int NODE_STATE_COLLAPSE = 1;
	
	public int getNodeState();
	
	public void setNodeState(int state);
	
	public ITAMGItem addChild(int x, int y, String text);
	
	public ITAMGItem addChild(int type, int x, int y, String text);
	
	public void setBackgroundStyle(TAMEditorNodeControl.BackgroundStyle background);
	
	public TAMEditorNodeControl.BackgroundStyle getBackgroundStyle();
	
	public Rect getBounds();
	
	public Point getPosition();
	
	public void actualizePosition(int x, int y);
	
	public float getWidth();
	
	public float getHeight();
	
	//public void draw(Canvas canvas);
	
	public void actualizeSize();
	
	public List<ITAMGConnection> getListOfParentConnections();
	
	public List<ITAMGConnection> getListOfChildConnections();

    public int getBackgroundStroke();
    
    public void setBackgroundStroke(int backgroundStroke);
}
