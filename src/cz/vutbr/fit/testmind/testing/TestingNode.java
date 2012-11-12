package cz.vutbr.fit.testmind.testing;

import java.util.ArrayList;

import android.text.SpannableString;

public class TestingNode
{
    private String title;
    private SpannableString body;
    private TestingNode parent;
    private ArrayList<TestingNode> childs;

    public TestingNode(String title, SpannableString body)
    {
        this.parent = null;
        this.title = title;
        this.body = body;
    }
    
    public TestingNode(String title, SpannableString body, TestingNode parent)
    {
        this.parent = parent;
        this.title = title;
        this.body = body;
    }
    
    public String getTitle()
    {
        return title;
    }

    public SpannableString getBody()
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
    
    public TestingNode appendChild(String title, SpannableString body)
    {
        TestingNode child = new TestingNode(title, body, this);
        childs.add(child);
        return child;
    }
}