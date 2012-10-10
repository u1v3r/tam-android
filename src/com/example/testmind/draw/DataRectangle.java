package com.example.testmind.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewDebug.ExportedProperty;

public class DataRectangle extends SurfaceObject{

	private static final String TAG = "DataRectangle";
	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private float x;
	private float y;
	private ShapeDrawable shape;
	
	public DataRectangle(Context context,float x, float y) {
		super(context);
		this.x = x;
		this.y = y;
		this.paint.setColor(0xFFFF0000);
		setFocusable(true);
		setFocusableInTouchMode(true);
		float[] outerR = new float[] { 30, 30, 0, 0, 0, 0, 0, 0 };
		RectF   inset = new RectF(6, 6, 6, 6);
		float[] innerR = new float[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		this.shape = new ShapeDrawable(new RoundRectShape(outerR, inset, innerR));
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		
		this.shape.getPaint().setColor(Color.BLACK);
		this.shape.setBounds((int)this.x, (int)this.y, (int)this.x + 40, (int)this.y + 60);
		this.shape.draw(canvas);
		this.paint.setColor(Color.WHITE);
		canvas.drawText("testujem", x, y, paint);
		
		super.onDraw(canvas);
	}
	
/*	@Override
	protected void onFocusChanged(boolean gainFocus, int direction,
			Rect previouslyFocusedRect) {
		
		Log.i(TAG,"je tu focus");
		
		super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.i(TAG, "X:" + this.x + " Y:" + this.y);
		this.x = event.getX();
		this.y = event.getY();
		
		
		
		
		return true;
	}	*/
}
