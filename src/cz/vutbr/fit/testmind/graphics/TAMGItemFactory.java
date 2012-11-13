package cz.vutbr.fit.testmind.graphics;


public class TAMGItemFactory {
	
	public ITAMGNode createNode(TAMGraph graph, int type, int x, int y, String text) {
		
		ITAMGNode node = null;
		
		//TAMGZoom zoom = graph.getZoom();
		
		//float dx = zoom.px-zoom.px*zoom.sx;
		//float dy = zoom.py-zoom.py*zoom.sy;
		
		if(type == ITAMGNode.NODE_TYPE_RECTANGLE) {
			//node = new TAMGRectangleNode(graph, (int)((x-dx)/zoom.sx), (int)((y-dy)/zoom.sy), text);
			node = new TAMGRectangleNode(graph, x, y, text);
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
	
	public void deleteNode(ITAMGNode node, boolean withDisposeConnections) {
		
		for(ITAMGConnection connection : node.getListOfChildConnections()) {
			if(withDisposeConnections) {
				deleteConnection(connection);
			} else {
				connection.setEnabled(false);
			}
		}
		
		TAMGraph graph = node.getGraph();
		
		node.dispose();
		
		if(graph != null) {
			graph.getListOfNodes().remove(node);
		}
	}
	
	public void deleteConnection(ITAMGConnection connection) {
		
		TAMGraph graph = connection.getGraph();
		
		if(graph != null) {
			
			graph.getListOfConnections().remove(connection);
		}
		
		connection.dispose();
	}
	
	public ITAMGButton createButton(TAMGraph graph, int type) {
		
		ITAMGButton button = null;
		
		if(type == ITAMGButton.BUTTON_TYPE_MENU) {
			button = new TAMGMenuButton(graph, type);
		}
		
		graph.getListOfButtons().add(button);
		//graph.organizeButtons();
		
		return button;
		
	}

}
