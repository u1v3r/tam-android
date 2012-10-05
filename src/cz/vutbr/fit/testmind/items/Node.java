package cz.vutbr.fit.testmind.items;

import java.util.ArrayList;

/**
 * class for node of mind map
 */
public class Node implements INode
{
    private INode parent;
    private ArrayList<INode> childs;
    
    protected void setParent(INode parent)
    {
        this.parent = parent;
    }
    
    public INode getParent()
    {
        return parent;
    }
}