package com.example.testmind.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.shapes.Shape;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class DrawingSurface extends SurfaceView implements SurfaceHolder.Callback{

	private static final String TAG = "DrawSurface";
	private DrawingThread drawingThread;
	private Paint paint;
	private float x = 20f;
	private float y = 80f;

	public DrawingSurface(Context context) {
		super(context);
		getHolder().addCallback(this);
		this.drawingThread = new DrawingThread(getHolder(), this);
		this.paint = new Paint();
		setFocusable(true);// umozni dotyky
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		
		canvas.drawColor(Color.WHITE);
		this.paint.setColor(Color.BLACK);
		canvas.drawRect(x, x, y, y, this.paint);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		this.x = event.getX();
		this.y = event.getY();
		Log.i(TAG, "X:" + this.x);
		Log.i(TAG, "Y:" + this.y);
		return true;
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
		
		public void setRunning(boolean run){
			this.run = run;
		}
		
		@Override
		public void run() {

			Canvas c;
			while (this.run) {
				c = null;
				try {
					c = this.surfaceHolder.lockCanvas(null);
					synchronized (this.surfaceHolder) {
						this.panel.onDraw(c);
					}
				} finally {
					// do this in a finally so that if an exception is thrown
					// during the above, we don't leave the Surface in an
					// inconsistent state
					if (c != null) {
						this.surfaceHolder.unlockCanvasAndPost(c);
					}
				}
			}

		}
	}		
	
}