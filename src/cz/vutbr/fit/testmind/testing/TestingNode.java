package cz.vutbr.fit.testmind.testing;

import java.util.ArrayList;

public class TestingNode
{
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
}