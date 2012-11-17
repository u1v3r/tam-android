package cz.vutbr.fit.testmind.graphics;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

public class TAMGraph extends SurfaceView implements OnGestureListener, OnDoubleTapListener, OnGlobalLayoutListener {
	
	private static final String TAG = "TAMGraph";

	private static final float ZOOM_IN_STEP = 0.900f;
	private static final float ZOOM_OUT_STEP = 0.5f;
	/**
	 * Udava rychlost zoomu pri geste, rozumne hodnoty su v radoch tisicov.
	 * <br><br>
	 * Cim vacsie cislo tym pomalejsi zoom.
	 */
	private static final float ZOOM_SPEED = 4500;
	
	/**
	 * Urci pociatocny zoom plochy
	 * <br><br>
	 * <i>* Pouziva sa v <code>onGlobalLayout()</code> na nastavenie pociatocnej velkosti uzlu</i>
	 */
	public static final float DEFAULT_ZOOM = 0.5f;

	private static final float MIN_ZOOM = 0.09f;
	private static final float MAX_ZOOM = 2.0f;

	private static final int TOUCH = 0;
	private static final int PINCH = 1;
	private static final int IDLE = 2;
	
	protected DrawingThread drawingThread;
	protected Paint paint = new Paint();
	protected List<ITAMGNode> listOfNodes;
	protected List<ITAMGConnection> listOfConnections;
	protected List<ITAMGItem> listOfDrawableItems;
	protected List<ITAMGItem> listOfSelectedItems;
	protected ITAMGNode lastSelectedNode;
	protected TAMGItemFactory factory;
	protected PointF actualPoint;
	protected PointF translationPoint;
	//Canvas canvas;
	private boolean activeTouchEvent = false;
	private TAMGZoom zoom;
	private GestureDetector gestureDetector;
	private float ax, ay;
	
	private int touchState = IDLE;
	private float distStart = 1, distCurrent = 1;
	private boolean moveActionAllowed = false;// urcuje ci je dovolena ACTION_MOVE
	
	protected List<GestureDetector> listOfGestureControls;
	protected List<ITAMDrawListener> listOfDrawControls;
	protected List<ITAMItemListener> listOfItemControls;
	protected List<ITAMBlankAreaGestureListener> listOfBlankAreaGestureControls;
	protected List<ITAMTouchListener> listOfTouchControls;
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
		public void onBlankMoveEvent(MotionEvent e, float dx, float dy);
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
		listOfItemGestureControls = new ArrayList<ITAMItemGestureListener>();
		listOfButtons = new ArrayList<ITAMGButton>();
		
		actualPoint = new PointF();	
		translationPoint = new PointF();
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
		
		getViewTreeObserver().addOnGlobalLayoutListener(this);
	}
	
	protected void initialize() {
		gestureDetector = new GestureDetector(this.getContext(), this);
		zoom = new TAMGZoom(this);
		Log.d(TAG,getWidth() + " " + getHeight());
	}

	/**
	 * 
	 * @return itemFactory
	 */
	public TAMGItemFactory getGItemFactory() {
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
			Log.d(TAG, size + "");
			for(int i = 0; i < size; i++) {
				Log.d(TAG, i + "");
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
		
		Log.d(TAG,"scale " + zoom.sx + " " + zoom.sy + " " + zoom.px + " " + zoom.py);
		Log.d(TAG,getWidth() + " " + getHeight());
		
		
		//canvas.scale(zoom.sx, zoom.sy, zoom.px, zoom.py);
		canvas.scale(zoom.sx, zoom.sy, zoom.px, zoom.py);
		canvas.translate(translationPoint.x, translationPoint.y);
		
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

			float distx, disty, distCurrentPrev;
			float x = e.getX();
			float y = e.getY();

			TAMGMotionEvent ge;
			activeTouchEvent = true;

			switch(e.getAction() & MotionEvent.ACTION_MASK){

			case MotionEvent.ACTION_DOWN:

				Log.d(TAG, "ACTION_DOWN");

				touchState = TOUCH;
				moveActionAllowed  = true;// ak je len dotyk jedneho prstu, tak sa moze presuvat

				ITAMGItem result = null;

				float dx = zoom.px-zoom.px*zoom.sx;
				float dy = zoom.py-zoom.py*zoom.sy;

				ax = ((x-dx)/zoom.sy)-translationPoint.x;
				ay = ((y-dy)/zoom.sy)-translationPoint.y;

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
				break;

			case MotionEvent.ACTION_POINTER_DOWN:

				Log.d(TAG, "ACTION_POINTER_DOWN");
				touchState = PINCH;				
				moveActionAllowed = false;// v pripade ak sa dotkne aj druhy prst, tak sa nesmie povolit hybanie

				//Vypocet zaciatocnej vzdialenosti dotykov
				distx = e.getX(0) - e.getX(1);
				disty = e.getY(0) - e.getY(1);
				distStart = FloatMath.sqrt(distx * distx + disty * disty);
				break;

			case MotionEvent.ACTION_MOVE:

				Log.d(TAG, "ACTION_MOVE");

				// ak je aktivne gestoo pinch zoom
				if(touchState == PINCH){
					Log.d(TAG, "zoooming");
					//Log.d(TAG, "posX1:" + e.getX(0) + ",posX2:" + e.getX(1));
					moveActionAllowed = false;

					//vzdialenost dvoch dotykovych bodov v ramci os x a y 
					distx = e.getX(0) - e.getX(1);
					disty = e.getY(0) - e.getY(1);

					// predchadzajuca hodnota						
					distCurrentPrev = distCurrent;

					// aktualna vzdialenost dotykov
					distCurrent = FloatMath.sqrt(distx * distx + disty * disty);
					Log.d(TAG, "cur:" + distCurrent + ",prev:"+distCurrentPrev);																

					float pivotX = (e.getX(0) + e.getX(1))/2f; //pivot X je v strede dotyku
					float pivotY = (e.getY(0) + e.getY(1))/2f; //pivot Y je v strede dotyku

					// udava rychlost skalovania				
					float scale = (distCurrent + ZOOM_SPEED)/(distStart + ZOOM_SPEED);

					// pri zmene pohybu v jednom geste je kvoli plynulosti treba reinicializovat
					// startovaciu poziciu
					if(scale > 1){// pribizenie							
						if(distCurrentPrev > distCurrent){// ak pri priblizovani zmeni gesto na oddalovanie								
							distStart = distCurrent;
							distCurrentPrev = distStart;
						}

					}else {// oddialenie
						if(distCurrentPrev < distCurrent){// ak pri oddalovani zmeni gesto na priblizovanie								
							distStart = distCurrent;
							distCurrentPrev = distStart;
						}
					}

					Log.d(TAG, "scale:" + scale + ", distCurrent:" + distCurrent + ", distStart:" + distStart);
					//Log.d(TAG, "pivodX: " + pivotX + ", pivotY: " + pivotY);

					zoom(zoom.sx*scale, zoom.sy*scale, pivotX, pivotY);

				} else if(touchState == TOUCH && moveActionAllowed) {
					Log.d(TAG, "mooving");
					float dx1 = x - actualPoint.x;
					float dy1 = y - actualPoint.y;

					if(dx1 > 1 || dy1 > 1 || dx1 < -1 || dy1 < -1) {

						int ddx = (int) (dx1/zoom.sx);
						int ddy = (int) (dy1/zoom.sy);

						if(!listOfSelectedItems.isEmpty()) {

							for(ITAMGItem item : listOfSelectedItems) {
								ge = new TAMGMotionEvent(item, ddx, ddy);
								onItemMoveEvent(e, ge);
							}
						} else {
							//ge = new TAMGMotionEvent(null, dx, dy);
							onMoveEvent(e, ddx, ddy);
						}

						actualPoint.x = x;
						actualPoint.y = y;
					}
				}

				break;
			case MotionEvent.ACTION_UP:

				//Dokoncenie press gesta prveho dotyku
				Log.d(TAG, "ACTION_UP");					
				touchState = IDLE;
				moveActionAllowed = true;//pri uvolneni sa moze hybat

				if(isLongPressed) {
					if(!listOfSelectedItems.isEmpty()) {
						ITAMGItem item = listOfSelectedItems.get(listOfSelectedItems.size()-1);
						if(item instanceof ITAMGNode) {
							onItemLongReleaseEvent(e, (ITAMGNode) item);
						}
					}
				}
				break;
			case MotionEvent.ACTION_POINTER_UP:
				//Dokoncenie press druheho dotyku
				Log.d(TAG, "ACTION_POINTER_UP");
				touchState = TOUCH;
				moveActionAllowed = false;
				break;
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
		Log.d(TAG, "onSingleTapUp");
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
	public void onMoveEvent(MotionEvent e, float dx, float dy) {
		/*for(ITAMGItem item : listOfNodes) {
			item.move(dx,dy);
		}
		for(ITAMGItem item : listOfConnections) {
			item.move(dx,dy);
		}*/
		//System.out.println(dx  + " " + dy + " " + zoom.px + " " + zoom.py);
		//zoom(zoom.sx, zoom.sy, zoom.px+dx, zoom.py+dy);
		move(dx, dy);
		//System.out.println(zoom.px + " " + zoom.py);
		
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
		
		Log.d(TAG, "long select");
		
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
	
	public void translate(float dx, float dy) {
		translationPoint.x = dx;
		translationPoint.y = dy;
		
		// actualize pivot //
		//zoom.px = -dx;
		//zoom.py = -dy;
		
		invalidate();
	}
	
	public void move(float dx, float dy) {
		translationPoint.x = translationPoint.x+dx;
		translationPoint.y = translationPoint.y+dy;
		
		// actualize pivot //
		//zoom.px -= dx;
		//zoom.py -= dy;
	}
	
	public void onZoomIn() {
		//System.out.println(getWidth()*0.5f + " " + getHeight()*0.5f);

		float sx = zoom.sx*2;
		float sy = zoom.sy*2;

		//float px = zoom.px+(getWidth()/2)*sx;
		//float py = zoom.py+(getHeight()/2)*sx;
		//System.out.println("zoomIn");

		zoom(sx, sy, getWidth()/2, getHeight()/2);
		invalidate();
	}

	public void onZoomOut() {
		//System.out.println(getWidth()*0.5f + " " + getHeight()*0.5f);
		zoom(zoom.sx*ZOOM_OUT_STEP, zoom.sy*ZOOM_OUT_STEP, getWidth()/2, getHeight()/2);
		invalidate();
	}
	
	public TAMGZoom getZoom() {
		return zoom;
	}	
	
	public PointF getTranslation() {
		return translationPoint;
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

	public void onGlobalLayout() {
		
		zoom.sx = zoom.sx*TAMGraph.DEFAULT_ZOOM;
		zoom.sy = zoom.sy*TAMGraph.DEFAULT_ZOOM;
		zoom(
				zoom.sx,
				zoom.sy,    				
				getWidth()/2, 
				getHeight()/2);
		
		getViewTreeObserver().removeGlobalOnLayoutListener(this);
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
