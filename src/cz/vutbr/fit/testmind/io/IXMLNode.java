package cz.vutbr.fit.testmind.io;

import java.util.ArrayList;

/**
 * interface for xml nodes of mind map
 */
public interface IXMLNode
{

	IXMLNode addChild(IXMLNode child);
	void removeChild(IXMLNode child);
	void setChilds(ArrayList<IXMLNode> childs);

	long getID();
	String getName();
	String getContent();
	ArrayList<IXMLNode> getChilds();
	
	boolean isActive();
	long getCreated();
	long getModified();
	int getPosition();
	double getWidth();
	double getHeight();
	double getMBBWidth();
	double getMBBHeight();
	
	void setID(long ID);
	void setName(String name);
	void setContent(String content);
	void setActive(boolean active);
	void setCreated(long created);
	void setModified(long modified);
	void setPosition(int position);
	void setWidth(double width);
	void setHeight(double height);
	void setMBBWidth(double MBBwidth);
	void setMBBHeight(double MBBheight);
}