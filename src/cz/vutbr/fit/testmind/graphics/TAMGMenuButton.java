package cz.vutbr.fit.testmind.graphics;

import cz.vutbr.fit.testmind.R;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.shapes.RectShape;

public class TAMGMenuButton extends TAMGAbstractButton {
	
	public static int WIDTH = 20;
	public static int HEIGHT = 30;
	
	public TAMGMenuButton(TAMGraph graph, int type) {
		super(graph, new RectShape(), ITAMGButton.BUTTON_TYPE_MENU);
		
		setColorBackground(Color.GRAY);
		setColorStroke(Color.DKGRAY);
		setColorBackgroundHighlight(graph.getResources().getColor(R.color.node_highlight_background));
		setColorBackgroundHighlight(graph.getResources().getColor(R.color.node_highlight_background_stroke));
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean hit(float x, float y) {
		
		Rect rect = this.getBounds();
		Rect newRect = new Rect(rect.left, rect.top, rect.right, rect.bottom);
		
		// if point is situated in rectangle area then return true, otherwise return false //
		if(newRect.contains((int)x,(int)y)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void move(int dx, int dy) {
		// not supported //
	}

	@Override
	public void actualizePosition(int x, int y) {
		
		// actualize rectangle //
		this.setBounds(x-WIDTH, y-HEIGHT, x+WIDTH, y+HEIGHT);
		//System.out.println(getBounds());
	}

}
