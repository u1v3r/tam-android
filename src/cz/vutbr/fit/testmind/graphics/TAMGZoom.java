package cz.vutbr.fit.testmind.graphics;

public class TAMGZoom {
	
	public float sx, sy, px, py;
	
	public TAMGZoom(TAMGraph graph) {
		// default zoom na stred
		this(graph.DEFAULT_ZOOM, graph.DEFAULT_ZOOM, graph.getWidth()/2, graph.getHeight()/2);
		System.out.println(graph.getWidth() + " " + graph.getHeight());
		System.out.println("initialize:" + sx + " " + sy + " " + px + " " + py);
	}
	
	public TAMGZoom(float sx, float sy, float px, float py) {
		this.sx = sx;
		this.sy = sy;
		this.px = px;
		this.py = py;
	}

}
