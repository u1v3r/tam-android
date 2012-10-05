package com.example.testmind.profile;

/**
 * class with settings of node
 */
public class NodeSettings implements INodeSettings
{
    private String title;
    private String content;
    private IPosition position;
    
    public String getTitle()
    {
        return title;
    }
    
    public void setTitle(String title)
    {
        this.title = title;
    }
    
    public String getContent()
    {
        return content;
    }
    
    public void setContent(String content)
    {
        this.content = content;
    }
    
    public IPosition getPosition()
    {
        return position;
    }
    
    public void setPosition(double x, double y)
    {
        this.position = new Position(x, y);
    }    
}