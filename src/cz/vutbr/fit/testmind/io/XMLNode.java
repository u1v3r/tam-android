package cz.vutbr.fit.testmind.io;

import java.util.ArrayList;

import cz.vutbr.fit.testmind.graphics.TAMGraph;
import android.util.Log;

/**
 * class for xml node of mind map
 */
public class XMLNode implements IXMLNode
{
    private IXMLNode parent;
    private ArrayList<IXMLNode> childs = new ArrayList<IXMLNode>();
    
    private long ID;
    private String name;
    private String content;
    private double width;
    private double height;
    private double MBBwidth;
    private double MBBheight;
    private boolean active;
    private long created;
    private long modified;
    private int position = 0;
    
    /**
     * Inside setting of node
     */
    static final double MAX_WIDTH = 100;
    static final double MAX_HEIGHT = 100;
    static final int POS_LEFT = 1;
    static final int POS_RIGHT = 2;
    static final int LENGTH_NAME = 10;

    
    /** Constructor for import of node - special for Free Mind
     * 
     * @param ID
     * @param created
     * @param modified
     * @param position
     * @param text
     */
    public XMLNode(long ID, long created, long modified, String position, String text) {
		this.ID = ID;
		this.created = created;
		this.modified = modified;
		this.content = text;
		if (text.length() > LENGTH_NAME) {
			this.name = text.substring(LENGTH_NAME);
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

	/** Set parent of node 
	 * 
	 * @param parent
	 */
    protected void setParent(IXMLNode parent)
    {
        this.parent = parent;
    }
    
    /** Get parent of node
     * 
     * @return
     */
    public IXMLNode getParent()
    {
        return parent;
    }

	/** Get childs of the node
	 *
     * @return
	 */
    public ArrayList<IXMLNode> getChilds() {
		return childs;
	}

	/** Set childs of the node
	 *
	 * @param childs
	 */
    public void setChilds(ArrayList<IXMLNode> childs) {
		this.childs = childs;
	}
	
	/** Add child to node
	 *
	 * @param child
	 */
    public IXMLNode addChild(IXMLNode child) {
		// Log.d("Connect: " + this.name, child.getName());
		childs.add(child);
		// Log.d("add child", "OK");
		return this;
	}

	/** Remove child from node
	 *
	 * @param child
	 */
	public void removeChild(IXMLNode child) {
		this.childs.remove(child);
	}
	
	/** Get ID of node
	 *
	 * @return
	 */
	public long getID() {
		return ID;
	}

	/** Set ID of node
	 *
	 * @param ID
	 */
	public void setID(long ID) {
		this.ID = ID;
	}

	/** Get name alias title of node
	 *
	 * @return
	 */
	public String getName() {
		return name;
	}

	/** Set name of node
	 *
	 * @param name alias title
	 */
	public void setName(String name) {
		this.name = name;
	}

	/** Get content alias body of node
	 *
	 * @return
	 */
	public String getContent() {
		return content;
	}

	/** Set content of node
	 *
	 * @param content alias body
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/** Get width of node
	 *
	 * @return
	 */
	public double getWidth() {
		return width;
	}

	/** Set width of node
	 *
	 * @param width
	 */
	public void setWidth(double width) {
		this.width = width;
	}

	/** Get height of node
	 *
	 * @return
	 */
	public double getHeight() {
		return height;
	}

	/** Set height of node
	 *
	 * @param height
	 */
	public void setHeight(double height) {
		this.height = height;
	}

	/** Is node set to active
	 *
	 * @return
	 */
	public boolean isActive() {
		return active;
	}

	/** Set active of node
	 *
	 * @param active
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/** Get created of node - special for Free Mind
	 *
	 * @return
	 */
	public long getCreated() {
		return created;
	}

	/** Set created of node - special for Free Mind
	 *
	 * @param created
	 */
	public void setCreated(long created) {
		this.created = created;
	}

	/** Get modified of node - special for Free Mind
	 *
	 * @return
	 */
	public long getModified() {
		return modified;
	}

	/** Set modified of node - special for Free Mind
	 *
	 * @param modified
	 */
	public void setModified(long modified) {
		this.modified = modified;
	}

	/** Get position of node - special for Free Mind
	 * Comment: Constants are here
	 *
	 * @return
	 */
	public int getPosition() {
		return position;
	}

	/** Set position of node - special for Free Mind
	 * Comment: Constants are here
	 *
	 * @param position
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/** Get width of Minimal Bounding Box of node
	 *
	 * @return
	 */
	public double getMBBWidth() {
		return MBBwidth;
	}

	/** Set width of Minimal Bounding Box of node
	 *
	 * @param MBBwidth
	 */
	public void setMBBWidth(double MBBwidth) {
		this.MBBwidth = MBBwidth;
	}

	/** Get height Minimal Bounding Box of node
	 *
	 * @return
	 */
	public double getMBBHeight() {
		return MBBheight;
	}

	/** Set height of Minimal Bounding Box of node
	 *
	 * @param MBBwidth
	 */
	public void setMBBHeight(double MBBheight) {
		this.MBBheight = MBBheight;
	}
}