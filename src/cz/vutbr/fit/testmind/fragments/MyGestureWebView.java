package cz.vutbr.fit.testmind.fragments;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.webkit.WebView;

public class MyGestureWebView extends WebView implements OnGestureListener {

	private static final String TAG = "MyWebView";
	private GestureDetector gd;

   public MyGestureWebView(Context context, AttributeSet attrs) {	   
       super(context,attrs);
       gd = new GestureDetector(context, this);
   }

	public boolean onDown(MotionEvent e) {
		Log.d(TAG, "onDown");
		return false;
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		Log.d(TAG, "onFling");
		return true;
	}

	public void onLongPress(MotionEvent e) {
		Log.d(TAG, "onLongPress");
	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		Log.d(TAG, "onScroll");		
		return true;
	}

	public void onShowPress(MotionEvent e) {
		Log.d(TAG, "onShowPress");
		
	}
	
	public boolean onSingleTapUp(MotionEvent e) {
		Log.d(TAG, "onSingleTapUp");
		return true;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {		
		return gd.onTouchEvent(event);
	}
}
