package cz.vutbr.fit.testmind.graphics;

public class TAMGZoom {
	
	public float sx, sy, px, py;
	
	public TAMGZoom(TAMGraph graph) {
		// default zoom na stred
		this(TAMGraph.DEFAULT_ZOOM, TAMGraph.DEFAULT_ZOOM, graph.getWidth()/2, graph.getHeight()/2);
	}
	
	public TAMGZoom(float sx, float sy, float px, float py) {
		this.sx = sx;
		this.sy = sy;
		this.px = px;
		this.py = py;
	}

}
