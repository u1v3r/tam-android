package cz.vutbr.fit.testmind.io;

import java.util.ArrayList;
import android.util.Log;

/**
 * class for xml node of mind map
 */
public class XMLNode implements IXMLNode
{
    private IXMLNode parent;
    private ArrayList<IXMLNode> childs;
    
    private long ID;
    private String name;
    private String content;
    private double width;
    private double height;
    private boolean active;
    private long created;
    private long modified;
    private int position = 0;
    static final double MAX_WIDTH = 100;
    static final double MAX_HEIGHT = 100;
    static final int POS_LEFT = 1;
    static final int POS_RIGHT = 2;

    
    public XMLNode(long ID, long created, long modified, String position, String text) {
		super();
		this.ID = ID;
		this.created = created;
		this.modified = modified;
		this.content = text;
		if (text.length() > 10) {
			this.name = text.substring(10);
		} else {
			this.name = text;
		}
		if (position != null) {
			if (position.equals("left")) {
				this.position = POS_LEFT;
			} else if (position.equals("right")) {
				this.position = POS_RIGHT;
			}
		}
	}

	protected void setParent(IXMLNode parent)
    {
        this.parent = parent;
    }
    
    public IXMLNode getParent()
    {
        return parent;
    }

	public ArrayList<IXMLNode> getChilds() {
		return childs;
	}

	public void setChilds(ArrayList<IXMLNode> childs) {
		this.childs = childs;
	}
	
	public void addChild(IXMLNode child) {
		String report = "ok";
		Log.d("add child", "fuck was complete");
		if (child == null) report = "null";
		Log.d("result is", report);
		Log.d("!" + this.name, child.getName());
			
	
	}
	
	public void removeChild(IXMLNode child) {
		this.childs.remove(child);
	}
	
	public long getID() {
		return ID;
	}

	public void setID(long ID) {
		this.ID = ID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public long getCreated() {
		return created;
	}

	public void setCreated(long created) {
		this.created = created;
	}

	public long getModified() {
		return modified;
	}

	public void setModified(long modified) {
		this.modified = modified;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
    
}