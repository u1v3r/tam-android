package com.example.testmind.view;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class MyGLSurfaceView extends GLSurfaceView {
	
	private GLSurfaceView mGLView;
	
	public MyGLSurfaceView(Context context) {
		super(context);
		setEGLContextClientVersion(2);
		// Render the view only when there is a change in the drawing data
		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		
		setRenderer(new MyRenderer());
	}

}
