package cz.vutbr.fit.testmind.graphics;

import java.util.ArrayList;
import java.util.List;

import cz.vutbr.fit.testmind.editor.controls.ITAMMenuListener;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.preference.PreferenceManager.OnActivityResultListener;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ZoomControls;

public class TAMGraph extends SurfaceView implements OnGestureListener, OnDoubleTapListener {
	
	private static final String TAG = "TAMGraph";

	//private static final float ZOOM_STEP = 0.125f;
	protected static final float DEFAULT_ZOOM = 0.5f;

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
	//Canvas canvas;
	private boolean activeTouchEvent = false;
	private TAMGZoom zoom;
	private GestureDetector gestureDetector;
	private float ax, ay;
	
	
	protected List<GestureDetector> listOfGestureControls;
	protected List<ITAMDrawListener> listOfDrawControls;
	protected List<ITAMItemListener> listOfItemControls;
	protected List<ITAMBlankAreaGestureListener> listOfBlankAreaGestureControls;
	protected List<ITAMTouchListener> listOfTouchControls;
	protected List<OnActivityResultListener> listOfOnActivityResultControls;
	protected List<ITAMItemGestureListener> listOfItemGestureControls;
	protected List<ITAMGButton> listOfButtons;
	//private List<OnGestureListener> listOfGestureControls;
	
	public final class TAMGMotionEvent {
		
		final public ITAMGItem item;
		final public float dx;
		final public float dy;
		
		public TAMGMotionEvent(ITAMGItem item, float dx, float dy) {
			this.item = item;
			this.dx = dx;
			this.dy = dy;
		}
		
	}
	
	public interface ITAMDrawListener {
		public void onDraw(Canvas canvas);
	};
	
	public interface ITAMItemListener {
		public void onItemHitEvent(MotionEvent e, TAMGMotionEvent ge);	
		public void onItemMoveEvent(MotionEvent e, TAMGMotionEvent ge);
		public void onItemSelectEvent(ITAMGItem item, boolean selection);
	};
	
	public interface ITAMItemGestureListener {
		public void onItemLongSelectEvent(MotionEvent e, ITAMGNode node);
		public void onItemLongReleaseEvent(MotionEvent e, ITAMGNode node);
		public void onItemDoubleTapEvent(MotionEvent e, ITAMGNode node);
	}
	
	public interface ITAMBlankAreaGestureListener {
		public void onBlankMoveEvent(MotionEvent e, int dx, int dy);
		public void onBlankLongPressEvent(MotionEvent e, float dx, float dy);
		public void onBlankDoubleTapEvent(MotionEvent e, float dx, float dy);
	}
	
	public interface ITAMTouchListener {
		//public void onTouchEvent(MotionEvent e, float dx, float dy);
		public void onHitEvent(MotionEvent e, TAMGMotionEvent ge);
	}

	public boolean isLongPressed = false;
       
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
		listOfBlankAreaGestureControls = new ArrayList<ITAMBlankAreaGestureListener>();
		listOfTouchControls = new ArrayList<ITAMTouchListener>();
		listOfGestureControls = new ArrayList<GestureDetector>();
		listOfOnActivityResultControls = new ArrayList<OnActivityResultListener>();
		listOfItemGestureControls = new ArrayList<ITAMItemGestureListener>();
		listOfButtons = new ArrayList<ITAMGButton>();
		zoom = new TAMGZoom(this);
		
		actualPoint = new Point();	
		setLongClickable(true);		
		setFocusable(true);// umozni dotyky
		setFocusableInTouchMode(true);
		drawingThread = new DrawingThread(getHolder(), this);
		//getHolder().addCallback(this);	
		setWillNotDraw(false);
		
		factory.createButton(this, ITAMGButton.BUTTON_TYPE_MENU);
		factory.createButton(this, ITAMGButton.BUTTON_TYPE_MENU);
		factory.createButton(this, ITAMGButton.BUTTON_TYPE_MENU);
		factory.createButton(this, ITAMGButton.BUTTON_TYPE_MENU);
		factory.createButton(this, ITAMGButton.BUTTON_TYPE_MENU);
		factory.createButton(this, ITAMGButton.BUTTON_TYPE_MENU);
		factory.createButton(this, ITAMGButton.BUTTON_TYPE_MENU);
		factory.createButton(this, ITAMGButton.BUTTON_TYPE_MENU);
		factory.createButton(this, ITAMGButton.BUTTON_TYPE_MENU);
		factory.createButton(this, ITAMGButton.BUTTON_TYPE_MENU);
		factory.createButton(this, ITAMGButton.BUTTON_TYPE_MENU);
		
		invalidate();
	}
	
	protected void initialize() {
		gestureDetector = new GestureDetector(this.getContext(), this);
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
	 * @return
	 */
	public List<ITAMGNode> getListOfNodes() {
		return listOfNodes;
	}

	/**
	 * 
	 * @return
	 */
	public List<ITAMGConnection> getListOfConnections() {
		return listOfConnections;
	}

	/**
	 * 
	 * @return
	 */
	public List<ITAMGItem> getListOfDrawableItems() {
		return listOfDrawableItems;
	}

	/**
	 * 
	 * @return
	 */
	public List<ITAMGItem> getListOfSelectedItems() {
		return listOfSelectedItems;
	}

	/**
	 * 
	 * @return
	 */
	public List<ITAMGButton> getListOfButtons() {
		return listOfButtons;
	}

	/**
	 * 
	 * @return
	 */
	public List<ITAMDrawListener> getListOfDrawControls() {
		return listOfDrawControls;
	}

	/**
	 * 
	 * @return
	 */
	public List<ITAMItemListener> getListOfItemControls() {
		return listOfItemControls;
	}

	/**
	 * 
	 * @return
	 */
	public List<ITAMItemGestureListener> getListOfItemGestureControls() {
		return listOfItemGestureControls;
	}

	/**
	 * 
	 * @return
	 */
	public List<ITAMBlankAreaGestureListener> getListOfMoveGestureControls() {
		return listOfBlankAreaGestureControls;
	}

	/**
	 * 
	 * @return
	 */
	public List<ITAMTouchListener> getListOfTouchControls() {
		return listOfTouchControls;
	}
	
	public List<GestureDetector> getListOfGestureControls(){
		return listOfGestureControls;
	}
	
	public List<OnActivityResultListener> getListOfOnActivityResultControls() {
		return listOfOnActivityResultControls;
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
	
	/**
	 * Prida triedu implementujucu rozhranie {@link OnGestureListener}
	 * do zoznamu tried odchytavajucich gesta
	 * 
	 * @param control Trieda implementujuca {@link OnGestureListener}
	 * @param context 
	 */
	public void addOnGestureLisener(OnGestureListener control,Context context){
		listOfGestureControls.add(new GestureDetector(context,control));
	}
	
	public void moveOnTop(ITAMGItem selectedItem) {
		
		// only if item is enabled (is in list of drawable items), it can be moved on top of drawable list//
		if(selectedItem.isEnabled()) {
			
			// move items to the end of the drawable list, so they will be drawn on the top of all items //
			if(selectedItem instanceof ITAMGNode) {
				
				ITAMGNode selectedNode = (ITAMGNode) selectedItem;
				
				for(ITAMGConnection parentConnection : selectedNode.getListOfParentConnections()) {
					if(parentConnection.isEnabled()) {
						listOfDrawableItems.remove(parentConnection);
						listOfDrawableItems.add(parentConnection);
						ITAMGNode parentNode = parentConnection.getParentNode();
						listOfDrawableItems.remove(parentNode);
						listOfDrawableItems.add(parentNode);
					}
				}
				
				for(ITAMGConnection childConnection : selectedNode.getListOfChildConnections()) {
					if(childConnection.isEnabled()) {
						listOfDrawableItems.remove(childConnection);
						listOfDrawableItems.add(childConnection);
						ITAMGNode childNode = childConnection.getChildNode();
						listOfDrawableItems.remove(childNode);
						listOfDrawableItems.add(childNode);
					}
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
		super.onDraw(canvas);
		
		canvas.scale(zoom.sx, zoom.sy, zoom.px, zoom.py);
		
		for(ITAMGItem item : listOfDrawableItems) {
			item.draw(canvas, paint);
		}
		
		/*for(ITAMGButton button : listOfButtons) {
			if(button.isEnabled()) {
				button.draw(canvas, paint);
			}
		}*/
		
		for(ITAMDrawListener control : listOfDrawControls) {
			control.onDraw(canvas);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		
		synchronized (drawingThread.getSurfaceHolder()) {
						
			activeTouchEvent = true;
			
			int x = (int) e.getX();
			int y = (int) e.getY();
		
			TAMGMotionEvent ge;

			if(e.getAction() == MotionEvent.ACTION_DOWN) {
								
				ITAMGItem result = null;
				
				float dx = zoom.px-zoom.px*zoom.sx;
				float dy = zoom.py-zoom.py*zoom.sy;
				
				ax = ((x-dx)/zoom.sy);
				ay = ((y-dy)/zoom.sy);
				
				/*for(ITAMGItem item : listOfConnections) {
					if(item.hit(ax, ay)) {
						result = item;
					}
				}

				for(ITAMGItem item : listOfNodes) {
					if(item.hit(ax, ay)) {
						result = item;
					}
				}*/
				
				for(ITAMGItem item : listOfDrawableItems) {
					if(item.hit(ax, ay)) {
						result = item;
					}
				}
				ge = new TAMGMotionEvent(result, ax, ay);

				unselectAllWithout(result);
				
				if(result == null) {
					lastSelectedNode = null;
				}// else {
					onItemHitEvent(e, ge);
				//}

				actualPoint.x = x;
				actualPoint.y = y;
				
				isLongPressed = false;
				
			} else if (e.getAction() == MotionEvent.ACTION_MOVE) {

				int dx = x - actualPoint.x;
				int dy = y - actualPoint.y;

				if(dx > 0 || dy > 0 || dx < 0 || dy < 0) {
					
					int ddx = (int) (dx/zoom.sx);
					int ddy = (int) (dy/zoom.sy);
					
					if(!listOfSelectedItems.isEmpty()) {

						for(ITAMGItem item : listOfSelectedItems) {
							ge = new TAMGMotionEvent(item, ddx, ddy);
							onItemMoveEvent(e, ge);
						}
					} else {
						ge = new TAMGMotionEvent(null, ddx, ddy);
						onMoveEvent(e, ddx, ddy);
					}

					actualPoint.x = x;
					actualPoint.y = y;
				}
				
			} else if(e.getAction() == MotionEvent.ACTION_UP){
				
				if(isLongPressed) {
					if(!listOfSelectedItems.isEmpty()) {
						ITAMGItem item = listOfSelectedItems.get(listOfSelectedItems.size()-1);
						if(item instanceof ITAMGNode) {
							onItemLongReleaseEvent(e, (ITAMGNode) item);
						}
					}
				}
			}
			
			/*for(ITAMTouchListener control : listOfTouchControls) {
				control.onTouchEvent(e, ax, ay);
			}*/
			
			for(GestureDetector gDetector : listOfGestureControls){
				gDetector.onTouchEvent(e);				
			}
			
			gestureDetector.onTouchEvent(e);
			
			invalidate();
			
			return super.onTouchEvent(e);
		}	
	}

	public boolean onDown(MotionEvent e) {
		//System.out.println("G: onDown");
		return false;
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		//System.out.println("G: onFling");
		return false;
	}
	
	public void onLongPress(MotionEvent e) {
		//System.out.println("G: onLongPress");
		
		isLongPressed = true;
		
		if(listOfSelectedItems.isEmpty()) {
			
			// could open some settings //
			onBlankLongPress(e, ax, ay);
			
		} else {
			
			ITAMGItem item = listOfSelectedItems.get(listOfSelectedItems.size()-1);
			
			if(item instanceof ITAMGConnection) {
				
				((ITAMGConnection) item).modifySelectedPoint();
				
			} else if(item instanceof ITAMGNode) {
				
				// could do something with node //
				onItemLongSelectEvent(e, (ITAMGNode) item);
				
			}
		}
		
		invalidate();
	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		//System.out.println("G: onScroll");
		return false;
	}

	public void onShowPress(MotionEvent e) {
		//System.out.println("G: onShowPress");
		
	}

	public boolean onSingleTapUp(MotionEvent e) {
		//System.out.println("G: onSingleTapUp");
		return false;
	}
	
	public boolean onDoubleTap(MotionEvent e) {
		if(lastSelectedNode == null) {
			for(ITAMBlankAreaGestureListener control : listOfBlankAreaGestureControls) {
				control.onBlankDoubleTapEvent(e, ax, ay);
			}
		} else {
			for(ITAMItemGestureListener control : listOfItemGestureControls) {
				control.onItemDoubleTapEvent(e, lastSelectedNode);
			}
		}
		return false;
	}

	public boolean onDoubleTapEvent(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean onSingleTapConfirmed(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	
	
	/**
	 * 
	 * Private listener used in controls when some item is hit.
	 * 
	 * @param e
	 * @param item
	 * @param ax
	 * @param ay
	 */
	public void onItemHitEvent(MotionEvent e, TAMGMotionEvent ge) {
		
		ITAMGItem item = ge.item;
		float ax = ge.dx;
		float ay = ge.dy;
		
		for(ITAMTouchListener control : listOfTouchControls) {
			control.onHitEvent(e, ge);
		}
		
		if(item != null) {
			if(item instanceof ITAMGNode) {
				lastSelectedNode = (ITAMGNode) item;
			} else if(item instanceof ITAMGConnection) {
				//if(item.isSelected()) {
					((ITAMGConnection) item).selectPoint(ax, ay);
				//}
			}
			if(!item.isSelected()) {
				item.setSelected(true);
			}
			
			for(ITAMItemListener control : listOfItemControls) {
				control.onItemHitEvent(e, ge);
			}
		}
	}
	
	/**
	 * 
	 * Private listener used in controls when some item is moved.
	 * 
	 * @param e
	 * @param item
	 * @param dx
	 * @param dy
	 */
	public void onItemMoveEvent(MotionEvent e, TAMGMotionEvent ge) {
		ITAMGItem item = ge.item;
		int dx = (int) ge.dx;
		int dy = (int) ge.dy;
		if(item instanceof ITAMGNode) {
			item.move(dx,dy);
		} else if(item instanceof ITAMGConnection) {
			((ITAMGConnection) item).moveSelectedPoint(dx,dy);
		}
		
		for(ITAMItemListener control : listOfItemControls) {
			control.onItemMoveEvent(e, ge);
		}
	}
	
	/**
	 * 
	 * Private listener used in controls when blank area is moved.
	 * 
	 * @param e
	 * @param dx
	 * @param dy
	 */
	public void onMoveEvent(MotionEvent e, int dx, int dy) {
		for(ITAMGItem item : listOfNodes) {
			item.move(dx,dy);
		}
		for(ITAMGItem item : listOfConnections) {
			item.move(dx,dy);
		}
		
		for(ITAMBlankAreaGestureListener control : listOfBlankAreaGestureControls) {
			control.onBlankMoveEvent(e, dx, dy);
		}
	}
	
	/**
	 * 
	 * Private listener used in controls when some item is selected.
	 * 
	 * @param item
	 * @param selection
	 */
	public void onItemSelectEvent(ITAMGItem item, boolean selection) {
		
		for(ITAMItemListener control : listOfItemControls) {
			control.onItemSelectEvent(item, selection);
		}
	}
	
	/**
	 * 
	 * Private listener used in controls when blank area is long pressed.
	 * 
	 * @param e
	 */
	private void onBlankLongPress(MotionEvent e, float dx, float dy) {
		for(ITAMBlankAreaGestureListener control : listOfBlankAreaGestureControls) {
			control.onBlankLongPressEvent(e, dx, dy);
		}
	}
	
	/**
	 * 
	 * Private listener used in controls when some item is long selected.
	 * 
	 * @param e
	 * @param node
	 */
	private void onItemLongSelectEvent(MotionEvent e, ITAMGNode node) {
		
		System.out.println("long select");
		
		for(ITAMItemGestureListener control : listOfItemGestureControls) {
			control.onItemLongSelectEvent(e, node);
		}
	}
	
	/**
	 * 
	 * Private listener used in controls when some actually selected item is released after long press.
	 * 
	 * @param e
	 * @param node
	 */
	private void onItemLongReleaseEvent(MotionEvent e, ITAMGNode node) {
		for(ITAMItemGestureListener control : listOfItemGestureControls) {
			control.onItemLongReleaseEvent(e, node);
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
	public void zoom(float scaleX, float scaleY, float pivotX, float pivotY){
		//Log.d(TAG,"pivotX: " + px + " ,pivotY" + py
		//		+ ", scaleX:"+ sx + ", scaleY"	 + sy);
		
		if(scaleX < MIN_ZOOM || scaleY < MIN_ZOOM) return;
		if(scaleY >= MAX_ZOOM || scaleY >= MAX_ZOOM) return;
		
		zoom.px = pivotX;
		zoom.py = pivotY;
		zoom.sx = scaleX;
		zoom.sy = scaleY;
		invalidate();	
	}
	
	public void onZoomIn() {
		zoom(zoom.sx*2, zoom.sy*2, getWidth()*0.5f, getHeight()*0.5f);
	}

	public void onZoomOut() {		
		zoom(zoom.sx*0.5f, zoom.sy*0.5f, getWidth()*0.5f, getHeight()*0.5f);
	}
	
	public TAMGZoom getZoom() {
		return zoom;
	}	
	
	
	public void surfaceCreated(SurfaceHolder holder) {
		
		if(drawingThread.isRunning()){
			this.drawingThread.setRunning(true);
			this.drawingThread.start();
		}
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
		
		public boolean isRunning() {
			return this.run;
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

	public void reset() {
		listOfNodes.clear();
		listOfConnections.clear();
		listOfSelectedItems.clear();
		listOfDrawableItems.clear();
		lastSelectedNode = null;
	}

	/*public void organizeButtons() {
		System.out.println(getWidth() + " " + getHeight());
		int x = getWidth()-TAMGMenuButton.WIDTH-10;
		int y = getHeight()-TAMGMenuButton.HEIGHT-10;
		int distance = TAMGMenuButton.WIDTH*2;
		
		for(ITAMGButton button : listOfButtons) {
			if(button.isEnabled() && button.getType() == ITAMGButton.BUTTON_TYPE_MENU) {
				button.actualizePosition(x, y);
				x = x-distance-20;
			}
		}
		
		invalidate();
	}*/

}
