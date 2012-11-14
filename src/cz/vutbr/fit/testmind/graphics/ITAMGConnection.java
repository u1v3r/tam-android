package cz.vutbr.fit.testmind.graphics;

import java.util.List;

import android.graphics.Point;

public interface ITAMGConnection extends ITAMGItem {

	public final int CONNECTION_TYPE_DEFAULT = 1;
	
	public ITAMGNode getParentNode();
	
	public ITAMGNode getChildNode();
	
	public boolean selectPoint(float x, float y);
	
	public void modifySelectedPoint();
	
	public void moveSelectedPoint(int dx, int dy);
	
    public List<Point> getListOfMiddlePoints();

    public void setListOfMiddlePoints(List<Point> listOfMiddlePoints);

}
