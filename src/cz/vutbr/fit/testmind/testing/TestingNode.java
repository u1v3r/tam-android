package cz.vutbr.fit.testmind.testing;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * class for nodes in testing
 * @author jules
 *
 */
public class TestingNode implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String title;
    private String body;
    private TestingNode parent;
    private ArrayList<TestingNode> childs = new ArrayList<TestingNode>();

    public TestingNode(String title, String body)
    {
        this(title, body, null);
    }
    
    public TestingNode(String title, String body, TestingNode parent)
    {
        this.parent = parent;
        this.title = title;
        this.body = body;
    }
    
    public String getTitle()
    {
        return title;
    }

    public String getBody()
    {
        return body;
    }

    public TestingNode getParent()
    {
        return parent;
    }

    public void setParent(TestingNode parent)
    {
        this.parent = parent;
    }

    public ArrayList<TestingNode> getChilds()
    {
        return childs;
    }
    
    public TestingNode appendChild(TestingNode node)
    {
        node.setParent(this);
        childs.add(node);
        return node;
    }
    
    /**
     * return nodes which have childs or body is not empty
     * @return
     */
    public ArrayList<TestingNode> getListTestingNodes()
    {
        ArrayList<TestingNode> result = new ArrayList<TestingNode>();
        
        if(childs.size() > 0 || (!body.isEmpty() && !body.equals(title)))
        {
            result.add(this);
        }
        
        for(TestingNode child: childs)
        {
            result.addAll(child.getListTestingNodes());
        }
        
        return result;
    }
}