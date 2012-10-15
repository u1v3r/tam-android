package cz.vutbr.fit.testmind.profile;

import java.util.ArrayList;
import java.util.List;

public class TAMProfile {
	
	protected List<TAMPNode> listOfPNodes;
	protected List<TAMPConnection> listOfPConnections;
	protected TAMPNode root;
	
	public TAMProfile() {
		listOfPNodes = new ArrayList<TAMPNode>();
		listOfPConnections = new ArrayList<TAMPConnection>();
		root = null;
	}
	
	public TAMPNode getNode(int id) {
		for(TAMPNode node : listOfPNodes) {
			if(node.getId() == id) {
				return node;
			}
		}
		return null;
	}
	
	public TAMPConnection getConnection(int id) {
		for(TAMPConnection connection : listOfPConnections) {
			if(connection.getId() == id) {
				return connection;
			}
		}
		return null;
	}
	
	public TAMPNode createRoot(String title, String body) {
		if(root != null) {
			throw new TAMItemException("Root already exists");
		} else {
			root = createNode(title, body);
			return root;
		}
	}
	
	public TAMPNode importRoot(String title, String body, int id) {
		if(root != null) {
			throw new TAMItemException("Root already exists");
		} else {
			root = createNode(title, body);
			return root;
		}
	}
	
	public TAMPNode createNode(String title, String body) {
		TAMPNode node = new TAMPNode(title, body);
		listOfPNodes.add(node);
		return node;
	}
	
	public TAMPNode importNode(String title, String body, int id) {
		for(TAMPNode node : listOfPNodes) {
			if(node.getId() == id) {
				throw new TAMItemException("Node with id " + id + " already exists");
			}
		}
		TAMPNode node = new TAMPNode(title, body, id);
		listOfPNodes.add(node);
		return node;
	}
	
	public TAMPConnection createConnection(TAMPNode parent, TAMPNode child) {
		
		if(parent == null || child == null) return null;
		
		TAMPConnection connection = new TAMPConnection(parent, child);
		listOfPConnections.add(connection);
		return connection;
	}
	
	public TAMPConnection createConnection(int parentID, int childID) {
		
		TAMPNode parent = getNode(parentID);
		if(parent == null) return null;
		TAMPNode child = getNode(childID);
		if(child == null) return null;
		
		TAMPConnection connection = new TAMPConnection(parent, child);
		listOfPConnections.add(connection);
		
		return connection;
	}
	
	public TAMPConnection importConnection(TAMPNode parent, TAMPNode child, int id) {
		for(TAMPConnection connection : listOfPConnections) {
			if(connection.getId() == id) {
				throw new TAMItemException("Connection with id " + id + " already exists");
			}
		}
		if(parent == null || child == null) return null;
		TAMPConnection connection = new TAMPConnection(parent, child, id);
		listOfPConnections.add(connection);
		return connection;
	}
	
	public TAMPConnection importConnection(int parentID, int childID, int id) {
		for(TAMPConnection connection : listOfPConnections) {
			if(connection.getId() == id) {
				throw new TAMItemException("Connection with id " + id + " already exists");
			}
		}
		TAMPNode parent = getNode(parentID);
		if(parent == null) return null;
		TAMPNode child = getNode(childID);
		if(child == null) return null;
		TAMPConnection connection = new TAMPConnection(parent, child, id);
		listOfPConnections.add(connection);
		return connection;
	}
	
	public void deleteNode(TAMPNode node) {
		
		if(listOfPNodes.contains(node)) {
			listOfPNodes.remove(node);
		}
	}
	
	public void deleteNode(int nodeID) {
		
		TAMPNode node = getNode(nodeID);
		
		if(node != null) {
			node.dispose();
			listOfPNodes.remove(node);
		}
	}
	
	public void deleteConnection(TAMPConnection connection) {
		
		if(listOfPConnections.contains(connection)) {
			listOfPConnections.remove(connection);
		}
	}
	
	public void deleteConnection(int connectionID) {
		
		TAMPConnection connection = getConnection(connectionID);
		
		if(connection != null) {
			connection.dispose();
			listOfPConnections.remove(connection);
		}
	}
	
	public void reset() {
		
		for(TAMPNode node : listOfPNodes) {
			node.dispose();
		}
		
		for(TAMPConnection connection : listOfPConnections) {
			connection.dispose();
		}
		
		root = null;
	}
	
	public class TAMItemException extends RuntimeException {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -8225234621793549461L;

		public TAMItemException() {
			super();
		}
		
		public TAMItemException(String s) {
			super(s);
		}
	}
}