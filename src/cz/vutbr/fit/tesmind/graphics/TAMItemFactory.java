package cz.vutbr.fit.tesmind.graphics;

public class TAMItemFactory {
	
	protected ITAMNode createNode(TAMGraph graph, int type, int x, int y) {
		
		ITAMNode node = null;
		
		if(type == ITAMNode.NODE_TYPE_RECTANGLE) {
			node = new TAMRactangleNode(graph, x, y);
		}
		
		graph.listOfNodes.add(node);
		
		return node;
	}
	
	protected ITAMConnection createConnection(TAMGraph graph, ITAMNode parent, ITAMNode child, int type) {
		
		ITAMConnection connection = null;
		
		if(type == ITAMConnection.CONNECTION_TYPE_DEFAULT) {
			connection = new TAMDefaultConnection(graph, parent, child);
		}
		
		graph.listOfConnections.add(connection);
		
		return connection;
	}

}
