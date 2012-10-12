package cz.vutbr.fit.testmind.graphics;

public interface ITAMConnection extends ITAMItem {

	public final int CONNECTION_TYPE_DEFAULT = 1;
	
	public ITAMNode getParentNode();
	
	public ITAMNode getChildNode();
	
	public void setSelectedPoint(float x, float y);
	
	public void moveSelectedPoint(int dx, int dy);
}
