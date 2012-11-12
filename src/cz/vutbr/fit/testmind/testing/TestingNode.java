package cz.vutbr.fit.testmind.testing;

import java.util.ArrayList;


public class TestingNode
{
    private String title;
    private CharSequence body;
    private TestingNode parent;
    private ArrayList<TestingNode> childs;

    public TestingNode(String title, CharSequence body)
    {
        this.parent = null;
        this.title = title;
        this.body = body;
    }
    
    public TestingNode(String title, CharSequence body, TestingNode parent)
    {
        this.parent = parent;
        this.title = title;
        this.body = body;
    }
    
    public String getTitle()
    {
        return title;
    }

    public CharSequence getBody()
    {
        return body;
    }

    public TestingNode getParent()
    {
        return parent;
    }

    public ArrayList<TestingNode> getChilds()
    {
        return childs;
    }
    
    public TestingNode appendChild(String title, CharSequence body)
    {
        TestingNode child = new TestingNode(title, body, this);
        childs.add(child);
        return child;
    }
}