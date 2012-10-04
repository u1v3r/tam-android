package com.example.testmind.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class DataRectangle extends View{

	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private float x;
	private float y;
	private ShapeDrawable shape;
	
	public DataRectangle(Context context,float x, float y) {
		super(context);
		this.x = x;
		this.y = y;
		this.paint.setColor(0xFFFF0000);
		this.shape = new ShapeDrawable(new OvalShape());
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
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		return super.onTouchEvent(event);
	}
	
}
