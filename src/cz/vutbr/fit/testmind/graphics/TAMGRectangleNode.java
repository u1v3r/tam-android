package cz.vutbr.fit.testmind.graphics;

import android.graphics.drawable.shapes.RoundRectShape;

public class TAMGRoundRectangleNode extends TAMGAbstractNode {

	public static final int TEXT_SIZE = 14;
	private static final int type = NODE_TYPE_RECTANGLE;
	public static final int OFFSET_X = 20;
	public static final int OFFSET_Y = 20;
	
	public TAMGRoundRectangleNode(TAMGraph graph, int x, int y, String text) {
		super(graph, x, y, OFFSET_X, OFFSET_Y, text, new RoundRectShape(null, null, null), type);
	}

	@Override
	public boolean hit(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void move(int dx, int dy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actualizePosition(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actualizeSize() {
		// TODO Auto-generated method stub
		
	}

}
