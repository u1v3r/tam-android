package cz.vutbr.fit.testmind.items;

import java.util.ArrayList;

/**
 * main mind map class
 */
public class MindMap implements IMindMap
{
    private INode rootNode;
    private ArrayList<INode> listNodes = new ArrayList<INode>();
    
    public INode getRootNode()
    {
        return rootNode;
    }
    
    public void setRootNode(INode rootNode)
    {
        this.rootNode = rootNode;
    }
}