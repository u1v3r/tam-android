package cz.vutbr.fit.testmind.graphics;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;

public class TAMGraph extends View {
	
	//protected Paint paint = new Paint();
	protected List<ITAMItem> listOfItems;
	protected List<ITAMItem> listOfSelectedItems;
	protected TAMItemFactory factory;
	protected Point actualPoint;
	//Canvas canvas;

	public TAMGraph(Context context) {
		super(context);
		factory = new TAMItemFactory();
		listOfItems = new ArrayList<ITAMItem>();
		listOfSelectedItems = new ArrayList<ITAMItem>();;
		actualPoint = new Point();
		
		setLongClickable(true);
	}
	
	protected TAMItemFactory getItemFactory() {
		return factory;
	}
	
	public ITAMItem addRoot(int type, int x, int y) {
		ITAMItem item = factory.createNode(this, type, x, y);
		listOfItems.add(item);
		return item;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
        
        //paint.setColor(Color.BLUE);
		
		for(ITAMItem item : listOfItems) {
			
			if(item.getType() == ITAMItem.ITEM_TYPE_RECTANGLE) {
				item.draw(canvas);
			}
		}
	}
	
	@Override
	public boolean onDragEvent(DragEvent event) {
		System.out.println(event);
		return super.onDragEvent(event);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		
		int x = (int) e.getX();
		int y = (int) e.getY();
		
		if(e.getAction() == MotionEvent.ACTION_DOWN) {
			//System.out.println(e);
			
			//System.out.println("click: " + e.getX() + " " + e.getY());
				
			ITAMItem result = null;
			
			for(ITAMItem item : listOfItems) {
				if(item.hit(x, y)) {
					result = item;
				}
			}
			
			select(result);
			
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
						item.move(dx,dy);
					}
				}
				
				actualPoint.x = x;
				actualPoint.y = y;
			}
		}
		
		//invalidate();


		
		
		
		return super.onTouchEvent(e);
	}
	
	public void select(ITAMItem selectedItem) {
		
		if(selectedItem != null) {
			if(listOfSelectedItems.contains(selectedItem)) {
				return;
			}
		}
		
		for(ITAMItem item : listOfSelectedItems) {
			item.setHighlight(false);
		}
		
		listOfSelectedItems.clear();
		
		if(selectedItem != null) {
			listOfSelectedItems.add(selectedItem);
			
			selectedItem.setHighlight(true);
		}
	}

}
