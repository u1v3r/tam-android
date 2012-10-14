package cz.vutbr.fit.testmind.graphics;

import java.util.ArrayList;
import java.util.List;

import cz.vutbr.fit.testmind.editor.controls.TAMEditorZoomControl.ZoomInOutEventListener;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnDragListener;
import android.widget.ZoomControls;

public class TAMGraph extends SurfaceView implements SurfaceHolder.Callback,ZoomInOutEventListener {
	
	private static final String TAG = "TAMGraph";

	//private static final float ZOOM_STEP = 0.125f;
	private static final float DEFAULT_ZOOM = 0.5f;

	private static final float MIN_ZOOM = 0.03125f;
	private static final float MAX_ZOOM = 2;
	
	protected DrawingThread drawingThread;
	protected Paint paint = new Paint();
	protected List<ITAMGNode> listOfNodes;
	protected List<ITAMGConnection> listOfConnections;
	protected List<ITAMGItem> listOfDrawableItems;
	protected List<ITAMGItem> listOfSelectedItems;
	protected ITAMGNode lastSelectedNode;
	protected TAMGItemFactory factory;
	protected Point actualPoint;
	protected ZoomControls zoomControls;
	//Canvas canvas;
	private boolean activeTouchEvent = false;
	
	protected List<ITAMDrawListener> listOfDrawControls;
	protected List<ITAMItemListener> listOfItemControls;
	protected List<ITAMGestureListener> listOfGestureControls;
	protected List<ITAMTouchListener> listOfTouchControls;
	
	public interface ITAMDrawListener {
		public void onDraw(Canvas canvas);
	};
	
	public interface ITAMItemListener {
		public void onItemHitEvent(MotionEvent e, ITAMGItem item, float ax, float ay);	
		public void onItemMoveEvent(MotionEvent e, ITAMGItem item, int dx, int dy);
	};
	
	public interface ITAMGestureListener {
		public void onMoveEvent(MotionEvent e, int dx, int dy);
	}
	
	public interface ITAMTouchListener {
		public boolean onTouchEvent(MotionEvent e);
	}

	public float sx, sy, px, py;

	public boolean isDragging = false;;
       
	public TAMGraph(Context context) {
		this(context,null);
	}
	
	public TAMGraph(Context context, AttributeSet attrs){		
		this(context,attrs,0);
	}
	
	public TAMGraph(Context context, AttributeSet attrs, int defStyle) {
		super(context,attrs,defStyle);
		factory = new TAMGItemFactory();
		
		listOfNodes = new ArrayList<ITAMGNode>();
		listOfConnections = new ArrayList<ITAMGConnection>();
		listOfSelectedItems = new ArrayList<ITAMGItem>();
		listOfDrawableItems = new ArrayList<ITAMGItem>();
		
		listOfDrawControls = new ArrayList<ITAMDrawListener>();
		listOfItemControls = new ArrayList<ITAMItemListener>();
		listOfGestureControls = new ArrayList<ITAMGestureListener>();
		listOfTouchControls = new ArrayList<ITAMTouchListener>();
		
		actualPoint = new Point();	
		setLongClickable(true);		
		setFocusable(true);// umozni dotyky
		setFocusableInTouchMode(true);
		drawingThread = new DrawingThread(getHolder(), this);
		getHolder().addCallback(this);	
		setWillNotDraw(false);
				
		// default zoom na stred 
		sx = sy = DEFAULT_ZOOM;
		px = getWidth()/2;
		py = getHeight()/2;
		
		invalidate();
	}
	
	/**
	 * 
	 * @return itemFactory
	 */
	public TAMGItemFactory getItemFactory() {
		return factory;
	}
	
	/**
	 * 
	 * @param type
	 * @param x
	 * @param y
	 * @param text
	 * @return rootNode
	 */
	public ITAMGNode addRoot(int type, int x, int y, String text) {
		ITAMGNode node = factory.createNode(this, type, x, y, text);
		return node;
	}
	
	public void moveOnTop(ITAMGItem selectedItem) {
		
		// move items to the end of the drawable list, so they will be drawn on the top of all items //
		if(selectedItem instanceof ITAMGNode) {
			
			ITAMGNode selectedNode = (ITAMGNode) selectedItem;
			
			for(ITAMGConnection parentConnection : selectedNode.getListOfParentConnections()) {
				listOfDrawableItems.remove(parentConnection);
				listOfDrawableItems.add(parentConnection);
				ITAMGNode parentNode = parentConnection.getParentNode();
				listOfDrawableItems.remove(parentNode);
				listOfDrawableItems.add(parentNode);
			}
			
			for(ITAMGConnection childConnection : selectedNode.getListOfChildConnections()) {
				listOfDrawableItems.remove(childConnection);
				listOfDrawableItems.add(childConnection);
				ITAMGNode childNode = childConnection.getChildNode();
				listOfDrawableItems.remove(childNode);
				listOfDrawableItems.add(childNode);
			}
			
			listOfDrawableItems.remove(selectedItem);
			listOfDrawableItems.add(selectedItem);
		} else if(selectedItem instanceof ITAMGConnection) {
			
			ITAMGConnection selectedConnection = (ITAMGConnection) selectedItem;
			listOfDrawableItems.remove(selectedConnection);
			listOfDrawableItems.add(selectedConnection);
			
			ITAMGNode parentNode = selectedConnection.getParentNode();
			listOfDrawableItems.remove(parentNode);
			listOfDrawableItems.add(parentNode);
			
			ITAMGNode childNode = selectedConnection.getChildNode();
			listOfDrawableItems.remove(childNode);
			listOfDrawableItems.add(childNode);
		}
	}
	
	/**
	 * 
	 * @return lastSelectedNode
	 */
	public ITAMGNode getLastSelectedNode() {
		return lastSelectedNode;
	}
	
	/**
	 * 
	 */
	public void unselectAll() {
		
		int size = listOfSelectedItems.size();
		for(int i = 0; i < size; i++) {
			listOfSelectedItems.get(0).setSelected(false);
		}
	}
	
	public void unselectAllWithout(ITAMGItem actual) {
		
		if(actual == null) {
			unselectAll();
		} else {
			int size = listOfSelectedItems.size();
			int a = 0;
			System.out.println(size);
			for(int i = 0; i < size; i++) {
				System.out.println(i);
				ITAMGItem item = listOfSelectedItems.get(a);
				if(item != actual) {
					item.setSelected(false);
				} else {
					a = 1;
				}
			}
		}
	}
	
	/**
	 * 
	 */
	public void enableAll() {
		
		for(ITAMGConnection connection : listOfConnections) {
			connection.setEnabled(true);
		}
		
		for(ITAMGNode node : listOfNodes) {
			node.setEnabled(true);
		}
	}
	
	/**
	 * 
	 */
	public void disableAll() {
		
		int size = listOfDrawableItems.size();
		
		for(int i = 0; i < size; i++) {
			listOfDrawableItems.get(0).setEnabled(false);
		}
	}
	
	/**
	 * 
	 */
	public void unhighlightAllItems() {
		
		unhighlightAllNodes();
		unhighlightAllConnections();
	}
	
	/**
	 * 
	 */
	public void unhighlightAllNodes() {
		
		for(ITAMGNode node : listOfNodes) {
			node.setHighlighted(false);
		}
	}
	
	/**
	 * 
	 */
	public void unhighlightAllConnections() {
		
		for(ITAMGConnection connection : listOfConnections) {
			connection.setHighlighted(false);
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) { 
		
		canvas.scale(sx, sy, px, py);
		
		for(ITAMGItem item : listOfDrawableItems) {
			item.draw(canvas, paint);
		}
		
		for(ITAMDrawListener control : listOfDrawControls) {
			control.onDraw(canvas);
		}
	}
	
	@Override
	public boolean onDragEvent(DragEvent event) {
		// TODO Auto-generated method stub
		
		Log.e(TAG,"drag event");
		return true;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		
		synchronized (drawingThread.getSurfaceHolder()) {
						
			activeTouchEvent = true;
			
			int x = (int) e.getX();
			int y = (int) e.getY();

			if(e.getAction() == MotionEvent.ACTION_DOWN) {
								
				ITAMGItem result = null;
				
				float dx = px-px*sx;
				float dy = py-py*sy;
				
				float ax = ((x-dx)/sy);
				float ay = ((y-dy)/sy);
				
				for(ITAMGItem item : listOfConnections) {
					if(item.hit(ax, ay)) {
						result = item;
					}
				}

				for(ITAMGItem item : listOfNodes) {
					if(item.hit(ax, ay)) {
						result = item;
					}
				}

				unselectAllWithout(result);
				
				if(result == null) {
					lastSelectedNode = null;
				} else {
					onItemHitEvent(e, result, ax, ay);
				}

				actualPoint.x = x;
				actualPoint.y = y;
				
				isDragging = true;
				
			} else if (e.getAction() == MotionEvent.ACTION_MOVE) {

				int dx = x - actualPoint.x;
				int dy = y - actualPoint.y;

				if(dx > 0 || dy > 0 || dx < 0 || dy < 0) {
					
					int ddx = (int) (dx/sx);
					int ddy = (int) (dy/sy);
					
					if(!listOfSelectedItems.isEmpty()) {

						for(ITAMGItem item : listOfSelectedItems) {
							onItemMoveEvent(e, item, ddx, ddy);
						}
					} else {
						onMoveEvent(e, ddx, ddy);
					}

					actualPoint.x = x;
					actualPoint.y = y;
				}
				
				isDragging  = true;
			} else if(e.getAction() == MotionEvent.ACTION_UP){
				isDragging = false;
			}

			invalidate();
			
			for(ITAMTouchListener control : listOfTouchControls) {
				control.onTouchEvent(e);
			}

			return super.onTouchEvent(e);
		}	
	}

	public void onItemHitEvent(MotionEvent e, ITAMGItem item, float ax, float ay) {
		if(item instanceof ITAMGNode) {
			lastSelectedNode = (ITAMGNode) item;
		} else if(item instanceof ITAMGConnection) {
			if(item.isSelected()) {
				((ITAMGConnection) item).setSelectedPoint(ax, ay);
			}
		}
		if(!item.isSelected()) {
			item.setSelected(true);
		}
		
		for(ITAMItemListener control : listOfItemControls) {
			control.onItemHitEvent(e, item, ax, ay);
		}
	}
	
	public void onItemMoveEvent(MotionEvent e, ITAMGItem item, int dx, int dy) {
		if(item instanceof ITAMGNode) {
			item.move(dx,dy);
		} else if(item instanceof ITAMGConnection) {
			((ITAMGConnection) item).moveSelectedPoint(dx,dy);
		}
		
		for(ITAMItemListener control : listOfItemControls) {
			control.onItemMoveEvent(e, item, dx, dy);
		}
	}
	
	public void onMoveEvent(MotionEvent e, int dx, int dy) {
		for(ITAMGItem item : listOfNodes) {
			item.move(dx,dy);
		}
		for(ITAMGItem item : listOfConnections) {
			item.move(dx,dy);
		}
		
		for(ITAMGestureListener control : listOfGestureControls) {
			control.onMoveEvent(e, dx, dy);
		}
	}
	
	
	
	/**
	 * 
	 * Sluzi na zoom objektu
	 * 
	 * @param scaleX
	 * @param scaleY
	 * @param pivotX
	 * @param pivotY
	 */
	protected void zoom(float scaleX, float scaleY, float pivotX, float pivotY){
		//Log.d(TAG,"pivotX: " + px + " ,pivotY" + py
		//		+ ", scaleX:"+ sx + ", scaleY"	 + sy);
		
		if(scaleX < MIN_ZOOM || scaleY < MIN_ZOOM) return;
		if(scaleY >= MAX_ZOOM || scaleY >= MAX_ZOOM) return;
		
		px = pivotX;
		py = pivotY;
		sx = scaleX;
		sy = scaleY;
		invalidate();	
	}
	
	public void onZoomIn() {
		zoom(sx*2, sy*2, getWidth()*0.5f, getHeight()*0.5f);
	}

	public void onZoomOut() {		
		zoom(sx*0.5f, sy*0.5f, getWidth()*0.5f, getHeight()*0.5f);
	}
	
	
	
	
	
	
	public void surfaceCreated(SurfaceHolder holder) {
		this.drawingThread.setRunning(true);
		this.drawingThread.start();
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
	    drawingThread.setRunning(false);
	    while (retry) {
	        try {
	            this.drawingThread.join();
	            retry = false;
	        } catch (InterruptedException e) {
	            // cakanie na dokoncenie vsetkych vlakien
	        }
	    }
	}
	
	/**
	 * Trieda sa postará o vytvorenie samostatného vlákna na vykreslenie plátna
	 * @author Radovan Dvorsky
	 *
	 */
	class DrawingThread extends Thread{
		
		private TAMGraph panel;
		private SurfaceHolder surfaceHolder;
		private boolean run;
		


		public DrawingThread(SurfaceHolder surface, TAMGraph panel) {
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
			if(activeTouchEvent){
				Canvas canvas;
				while (this.run) {
					canvas = null;
					try {
						canvas = this.surfaceHolder.lockCanvas(null);
						if(canvas != null){
							synchronized (this.surfaceHolder) {		
								
								this.panel.onDraw(canvas);
							}
						}
					} finally {
						if (canvas != null) {
							this.surfaceHolder.unlockCanvasAndPost(canvas);
							activeTouchEvent = false;
						}
					}
				}
			}
		}
	}
}
