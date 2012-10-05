package com.example.testmind.profile;

/**
 * interface for settings of node
 */
public interface INodeSettings
{
    public String getTitle();
    public void setTitle(String title);
    public String getContent();
    public void setContent(String content);
    public IPosition getPosition();
    public void setPosition(double x, double y);
}