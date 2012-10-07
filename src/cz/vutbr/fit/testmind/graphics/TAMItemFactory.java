package cz.vutbr.fit.testmind.graphics;


public class TAMItemFactory {
	
	protected ITAMNode createNode(TAMGraph graph, int type, int x, int y, String text) {
		
		ITAMNode node = null;
		
		float dx = graph.px-graph.px*graph.sx;
		float dy = graph.py-graph.py*graph.sy;
		
		if(type == ITAMNode.NODE_TYPE_RECTANGLE) {
			node = new TAMRectangleNode(graph, (int)((x-dx)/graph.sx), (int)((y-dy)/graph.sy), text);
		}
		
		graph.listOfNodes.add(node);
		graph.listOfDrawableItems.add(node);
		
		return node;
	}
	
	protected ITAMConnection createConnection(TAMGraph graph, ITAMNode parent, ITAMNode child, int type) {
		
		ITAMConnection connection = null;
		
		if(type == ITAMConnection.CONNECTION_TYPE_DEFAULT) {
			connection = new TAMDefaultConnection(graph, parent, child);
		}
		
		graph.listOfConnections.add(connection);
		
		// move on top //
		graph.listOfDrawableItems.add(connection);
		graph.listOfDrawableItems.remove(parent);
		graph.listOfDrawableItems.add(parent);
		graph.listOfDrawableItems.remove(child);
		graph.listOfDrawableItems.add(child);
		
		return connection;
	}

}
