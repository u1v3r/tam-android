package com.example.testmind.draw;

import java.util.ArrayList;
import java.util.List;

import com.example.testmind.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

public class DrawingSurface extends SurfaceView implements SurfaceHolder.Callback{

	private static final String TAG = "DrawingSurface";
	private DrawingThread drawingThread;
	private List<SurfaceObject> nodes;
	

	public DrawingSurface(Context context) {
		this(context,null);		
	}
	
	public DrawingSurface(Context context, AttributeSet attrs){		
		this(context,attrs,0);
	}
	
	public DrawingSurface(Context context, AttributeSet attrs, int defStyle) {
		super(context,attrs,defStyle);		
		this.drawingThread = new DrawingThread(getHolder(), this);
		this.nodes = new ArrayList<SurfaceObject>();
		getHolder().addCallback(this);
		
		setFocusable(true);// umozni dotyky
		setFocusableInTouchMode(true);		
		
		DataRectangle node = new DataRectangle(context, 30, 80);
		node.setTitle("node1");
		this.nodes.add(node);	
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {		
		/*for (SurfaceObject node : nodes) {
			Log.i(TAG,node.toString());
			node.draw(canvas);
		}	*/
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		synchronized (drawingThread.getSurfaceHolder()) {			
			Log.i(TAG, "X:" + event.getX());
			Log.i(TAG, "Y:" + event.getY());
			
			DataRectangle node = new DataRectangle(getContext(), event.getX(), event.getY());
			node.setTitle("node_dalej");
			return nodes.add(node);
		}			
		
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	public void surfaceCreated(SurfaceHolder holder) {
		
		this.drawingThread.setRunning(true);
		this.drawingThread.start();
		
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		
		boolean retry = true;
	    this.drawingThread.setRunning(false);
	    while (retry) {
	        try {
	            this.drawingThread.join();
	            retry = false;
	        } catch (InterruptedException e) {
	            // cakanie na dokoncenie vsetkych vlakien
	        }
	    }			
	}
	
	
	class DrawingThread extends Thread{
				
		private DrawingSurface panel;
		private SurfaceHolder surfaceHolder;
		private boolean run;


		public DrawingThread(SurfaceHolder surface, DrawingSurface panel) {
			this.surfaceHolder = surface;
			this.panel = panel;
		}
		
		public SurfaceHolder getSurfaceHolder() {
			return this.surfaceHolder;
		}

		public void setRunning(boolean run){
			this.run = run;
		}
		
		@Override
		public void run() {

			Canvas canvas;
			while (this.run) {
				canvas = null;
				try {
					canvas = this.surfaceHolder.lockCanvas(null);
					synchronized (this.surfaceHolder) {
						this.panel.onDraw(canvas);
					}
				} finally {
					if (canvas != null) {
						this.surfaceHolder.unlockCanvasAndPost(canvas);
					}
				}
			}
		}
	}	
}