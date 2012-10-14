package cz.vutbr.fit.testmind.graphics;


public class TAMGItemFactory {
	
	public ITAMGNode createNode(TAMGraph graph, int type, float x, float y, String text) {
		
		ITAMGNode node = null;
		
		float dx = graph.px-graph.px*graph.sx;
		float dy = graph.py-graph.py*graph.sy;
		
		if(type == ITAMGNode.NODE_TYPE_RECTANGLE) {
			node = new TAMGRectangleNode(graph, (int)((x-dx)/graph.sx), (int)((y-dy)/graph.sy), text);
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
