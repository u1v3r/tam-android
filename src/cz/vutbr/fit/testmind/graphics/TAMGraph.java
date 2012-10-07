package cz.vutbr.fit.testmind.graphics;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.ZoomControls;

public class TAMGraph extends SurfaceView implements SurfaceHolder.Callback {
	
	private static final String TAG = "TAMGraph";

	public static final float ZOOM_STEP = 0.125f;
	public static final float DEFAULT_ZOOM = 0.4f;

	private static final float MIN_ZOOM = 0;
	private static final float MAX_ZOOM = 2;
	
	protected DrawingThread drawingThread;
	protected Paint paint = new Paint();
	protected List<ITAMNode> listOfNodes;
	protected List<ITAMConnection> listOfConnections;
	protected List<ITAMItem> listOfDrawableItems;
	protected List<ITAMItem> listOfSelectedItems;
	protected ITAMNode lastSelectedNode;
	protected TAMItemFactory factory;
	protected Point actualPoint;
	protected ZoomControls zoomControls;
	//Canvas canvas;
	private boolean activeTouchEvent = false;

	public float sx, sy, px, py;
       
	public TAMGraph(Context context) {
		this(context,null);		
	}
	
	public TAMGraph(Context context, AttributeSet attrs){		
		this(context,attrs,0);
	}
	
	public TAMGraph(Context context, AttributeSet attrs, int defStyle) {
		super(context,attrs,defStyle);
		factory = new TAMItemFactory();
		listOfNodes = new ArrayList<ITAMNode>();
		listOfConnections = new ArrayList<ITAMConnection>();
		listOfSelectedItems = new ArrayList<ITAMItem>();
		listOfDrawableItems = new ArrayList<ITAMItem>();
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
	protected TAMItemFactory getItemFactory() {
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
	public ITAMNode addRoot(int type, int x, int y, String text) {
		ITAMNode node = factory.createNode(this, type, x, y, text);
		return node;
	}
	
	/**
	 * 
	 * @param selectedItem
	 */
	public void select(ITAMItem selectedItem) {
		
		// add to list of selected items and move to top //
		if(selectedItem != null && !listOfSelectedItems.contains(selectedItem)) {
			
			listOfSelectedItems.add(selectedItem);
			
			selectedItem.highlight(true);
			
			// move items to the end of the drawable list, so they will be drawn on the top of all items //
			if(selectedItem instanceof ITAMNode) {
				
				ITAMNode selectedNode = (ITAMNode) selectedItem;
				ITAMConnection parentConnection = selectedNode.getParentConnection();
				
				if(parentConnection != null) {
					listOfDrawableItems.remove(parentConnection);
					listOfDrawableItems.add(parentConnection);
					ITAMNode parentNode = parentConnection.getParentNode();
					listOfDrawableItems.remove(parentNode);
					listOfDrawableItems.add(parentNode);
				}
				
				for(ITAMConnection childConnection : selectedNode.getListOfChildConnections()) {
					listOfDrawableItems.remove(childConnection);
					listOfDrawableItems.add(childConnection);
					ITAMNode childNode = childConnection.getChildNode();
					listOfDrawableItems.remove(childNode);
					listOfDrawableItems.add(childNode);
				}
				
				listOfDrawableItems.remove(selectedItem);
				listOfDrawableItems.add(selectedItem);
			} else if(selectedItem instanceof ITAMConnection) {
				
				ITAMConnection selectedConnection = (ITAMConnection) selectedItem;
				listOfDrawableItems.remove(selectedConnection);
				listOfDrawableItems.add(selectedConnection);
				
				ITAMNode parentNode = selectedConnection.getParentNode();
				listOfDrawableItems.remove(parentNode);
				listOfDrawableItems.add(parentNode);
				
				ITAMNode childNode = selectedConnection.getChildNode();
				listOfDrawableItems.remove(childNode);
				listOfDrawableItems.add(childNode);
			}
		}
	}
	
	/**
	 * 
	 * @return lastSelectedNode
	 */
	public ITAMNode getLastSelectedNode() {
		return lastSelectedNode;
	}
	
	/**
	 * 
	 */
	public void unselectAll() {
		
		for(ITAMItem item : listOfSelectedItems) {
			item.highlight(false);
		}
		
		listOfSelectedItems.clear();
	}
	
	/**
	 * 
	 * @param item
	 */
	public void unselect(ITAMItem item) {
		
		if(listOfSelectedItems.contains(item)) {
			listOfSelectedItems.remove(item);
			item.highlight(false);
		}
	}
	
	public void enable(ITAMNode enabledItem) {
		
		enabledItem.setEnabled(true);
			
		// add to list of drawable items and move to top //
		if(enabledItem != null && !listOfDrawableItems.contains(enabledItem)) {
			
			if(enabledItem instanceof ITAMNode) {
				
				ITAMNode enabledNode = (ITAMNode) enabledItem;
				ITAMConnection parentConnection = enabledNode.getParentConnection();
				
				if(parentConnection != null) {
					ITAMNode parentNode = parentConnection.getParentNode();
					if(!listOfDrawableItems.contains(parentNode)) {
						listOfDrawableItems.add(parentConnection);
						parentConnection.setEnabled(true);
					}
				}
				
				for(ITAMConnection childConnection : enabledNode.getListOfChildConnections()) {
					ITAMNode childNode = childConnection.getChildNode();
					if(!listOfDrawableItems.contains(childNode)) {
						listOfDrawableItems.add(childConnection);
						childConnection.setEnabled(true);
					}
				}
				
				listOfDrawableItems.add(enabledNode);
				
			} else if(enabledItem instanceof ITAMConnection) {
				
				ITAMConnection selectedConnection = (ITAMConnection) enabledItem;
				listOfDrawableItems.add(selectedConnection);
				
				ITAMNode parentNode = selectedConnection.getParentNode();
				enable(parentNode);
				
				ITAMNode childNode = selectedConnection.getChildNode();
				enable(childNode);
			}
		}
	}
	
	/**
	 * 
	 */
	public void enableAll() {
		
		disableAll();
		
		for(ITAMConnection connection : listOfConnections) {
			listOfDrawableItems.add(connection);
			connection.setEnabled(true);
		}
		
		for(ITAMNode node : listOfNodes) {
			listOfDrawableItems.add(node);
			node.setEnabled(true);
		}
	}
	
	/**
	 * 
	 * @param disabledItem
	 */
	public void disable(ITAMNode disabledItem) {
		
		disabledItem.setEnabled(false);
		
		if(disabledItem != null && listOfDrawableItems.contains(disabledItem)) {
			
			if(disabledItem instanceof ITAMNode) {
				
				ITAMNode disabledNode = (ITAMNode) disabledItem;
				ITAMConnection parentConnection = disabledNode.getParentConnection();
				
				if(parentConnection != null) {
					listOfDrawableItems.remove(parentConnection);
					parentConnection.setEnabled(false);
				}
				
				for(ITAMConnection childConnection : disabledNode.getListOfChildConnections()) {
					listOfDrawableItems.remove(childConnection);
					childConnection.setEnabled(false);
				}
				
				listOfDrawableItems.remove(disabledNode);
				
			} else if(disabledItem instanceof ITAMConnection) {
				
				ITAMConnection disabledConnection = (ITAMConnection) disabledItem;
				listOfDrawableItems.remove(disabledConnection);
				
				ITAMNode parentNode = disabledConnection.getParentNode();
				disable(parentNode);
				
				ITAMNode childNode = disabledConnection.getChildNode();
				disable(childNode);
			}
		}
	}
	
	/**
	 * 
	 */
	public void disableAll() {
		
		for(ITAMItem item : listOfDrawableItems) {
			item.setEnabled(false);
		}
		
		listOfDrawableItems.clear();
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
		
		for(ITAMNode node : listOfNodes) {
			node.highlight(false);
		}
	}
	
	/**
	 * 
	 */
	public void unhighlightAllConnections() {
		
		for(ITAMConnection connection : listOfConnections) {
			connection.highlight(false);
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) { 
		
		//canvas.scale(2, 2, 0, 0);
		canvas.scale(sx, sy, px, py);
		
		//System.out.println("ahoj");
				
        //paint.setColor(Color.BLUE);
		
		/*for(ITAMConnection connection : listOfConnections) {
			connection.draw(canvas, paint);
		}
		
		for(ITAMNode node : listOfNodes) {
				node.draw(canvas);
		}*/
		
		for(ITAMItem item : listOfDrawableItems) {
			item.draw(canvas, paint);
		}
	}
	
	@Override
	public boolean onDragEvent(DragEvent event) {
		//System.out.println(event);
		return super.onDragEvent(event);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		
		
		synchronized (drawingThread.getSurfaceHolder()) {
						
			activeTouchEvent = true;
			
			int x = (int) e.getX();
			int y = (int) e.getY();

			if(e.getAction() == MotionEvent.ACTION_DOWN) {
				//System.out.println(e);

				//System.out.println("click: " + e.getX() + " " + e.getY());

				ITAMItem result = null;

				/*for(ITAMItem item : listOfConnections) {
					if(item.hit(x, y)) {
						result = item;
					}
				}*/
				
				float dx = px-px*sx;
				float dy = py-py*sy;

				for(ITAMItem item : listOfNodes) {
					if(item.hit((int)((x-dx)/sy), (int)((y-dy)/sy))) {
						result = item;
					}
				}

				unselectAll();
				select(result);
				
				if(result == null) {
					lastSelectedNode = null;
				} else {
					if(result instanceof ITAMNode) {
						lastSelectedNode = (ITAMNode) result;
					}
				}

				actualPoint.x = x;
				actualPoint.y = y;

			} else if (e.getAction() == MotionEvent.ACTION_MOVE) {

				//System.out.println("move: " + e.getX() + " " + e.getY());

				//Point newPoint = new Point((int) e.getX(), (int) e.getY());

				int dx = x - actualPoint.x;
				int dy = y - actualPoint.y;


				if(dx > 0 || dy > 0 || dx < 0 || dy < 0) {
					if(!listOfSelectedItems.isEmpty()) {

						for(ITAMItem item : listOfSelectedItems) {
							item.move((int)(dx/sx),(int)(dy/sy));
						}
					} else {
						for(ITAMItem item : listOfNodes) {
							item.move((int)(dx/sx),(int)(dy/sy));
						}
					}

					actualPoint.x = x;
					actualPoint.y = y;
				}
			}

			invalidate();

			return super.onTouchEvent(e);
		}
	
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
	
	
	public void zoom(float scaleX, float scaleY, float pivotX, float pivotY){
		Log.d(TAG,"pivotX: " + px + " ,pivotY" + py
				+ ", scaleX:"+ sx + ", scaleY"	 + sy);
		
		
		if(scaleX <= MIN_ZOOM || scaleY <= MIN_ZOOM) return;
		if(scaleY >= MAX_ZOOM || scaleY >= MAX_ZOOM) return;
		
		px = pivotX;
		py = pivotY;
		sx = scaleX;
		sy = scaleY;
		/*setPivotX(pivotX);		
		setPivotY(pivotY);
		setScaleX(scaleX);
		setScaleY(scaleY);*/	
		invalidate();	
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
