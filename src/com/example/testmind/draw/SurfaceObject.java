package com.example.testmind.draw;

import android.content.Context;
import android.view.View;

abstract class SurfaceObject extends View{
	
	private String title;
	private String content;
	
	protected SurfaceObject(Context context){
		super(context);
	}
	
	public void setTitle(String title){
		this.title = title;		
	}
	
	public void setContent(String content){
		this.content = content;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public String getContent(){
		return this.content;
	}

	@Override
	public String toString() {
		return "SurfaceObject [title=" + title + ", content=" + content + "]";
	}
}
