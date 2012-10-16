package cz.vutbr.fit.testmind.graphics;


public class TAMGItemFactory {
	
	public ITAMGNode createNode(TAMGraph graph, int type, float x, float y, String text) {
		
		ITAMGNode node = null;
		
		TAMGZoom zoom = graph.getZoom();
		
		float dx = zoom.px-zoom.px*zoom.sx;
		float dy = zoom.py-zoom.py*zoom.sy;
		
		if(type == ITAMGNode.NODE_TYPE_RECTANGLE) {
			node = new TAMGRectangleNode(graph, (int)((x-dx)/zoom.sx), (int)((y-dy)/zoom.sy), text);
		}
		
		graph.listOfNodes.add(node);
		graph.listOfDrawableItems.add(node);
		
		return node;
	}
	
	public ITAMGConnection createConnection(TAMGraph graph, ITAMGNode parent, ITAMGNode child, int type) {
		
		ITAMGConnection connection = null;
		
		if(type == ITAMGConnection.CONNECTION_TYPE_DEFAULT) {
			connection = new TAMGDefaultConnection(graph, parent, child);
		}
		
		graph.listOfConnections.add(connection);
		
		parent.getListOfChildConnections().add(connection);
		child.getListOfParentConnections().add(connection);
		
		// move on top //
		graph.listOfDrawableItems.add(connection);
		graph.listOfDrawableItems.remove(parent);
		graph.listOfDrawableItems.add(parent);
		graph.listOfDrawableItems.remove(child);
		graph.listOfDrawableItems.add(child);
		
		return connection;
	}

}
