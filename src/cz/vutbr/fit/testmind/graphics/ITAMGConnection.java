package cz.vutbr.fit.testmind.graphics;

public interface ITAMGConnection extends ITAMGItem {

	public final int CONNECTION_TYPE_DEFAULT = 1;
	
	public ITAMGNode getParentNode();
	
	public ITAMGNode getChildNode();
	
	public boolean selectPoint(float x, float y);
	
	public void modifySelectedPoint();
	
	public void moveSelectedPoint(int dx, int dy);
}
