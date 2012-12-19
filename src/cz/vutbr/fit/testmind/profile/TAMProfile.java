package cz.vutbr.fit.testmind.profile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;
import android.util.Log;

import cz.vutbr.fit.testmind.editor.ITAMEditor;

public class TAMProfile {
	
	private static final String TAG = "TAMProfile";
	
	protected List<TAMPNode> listOfPNodes;
	protected List<TAMPConnection> listOfPConnections;
	protected List<ITAMEditor> listOfEditors;
	protected TAMPNode root;
	
	/**
	 * Meno suboru pod ktorym je mapa ulozena v suborovom systeme
	 */
	private String fileName; 
		
	public static final File TESTMIND_DIRECTORY = createCardPath();
	
	public TAMProfile() {
		listOfPNodes = new ArrayList<TAMPNode>();
		listOfPConnections = new ArrayList<TAMPConnection>();
		listOfEditors = new ArrayList<ITAMEditor>();
		root = null;
	}
	
	public List<TAMPNode> getListOfPNodes() {
        return listOfPNodes;
    }

    public List<TAMPConnection> getListOfPConnections() {
        return listOfPConnections;
    }

    public List<ITAMEditor> getListOfEditors() {
		return listOfEditors;
	}
	
	public TAMPNode getRoot() {
		return root;
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
	
	public TAMPConnection getConnection(TAMPNode one, TAMPNode two) {
		for(TAMPConnection connection : listOfPConnections) {
			if(connection.getParent() == one && connection.getChild() == two
			|| connection.getParent() == two && connection.getChild() == one) {
				return connection;
			}
		}
		return null;
	}
	
	/**
	 * Meno suboru pod ktorym je mapa ulozena
	 * @return
	 */
	public String getFileName() {
		if(fileName == null){
			if(root != null){
				fileName = root.getTitle();
				
				fileName = checkFileName(fileName);
			}
		}
		
		return fileName;
	}

	/**
	 * Nastavi meno suboru pod ktorym je mapa ulozena
	 * @param fileName String
	 */
	public void setFileName(String fileName) {
	    fileName = checkFileName(fileName);
		this.fileName = fileName;
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
			root = importNode(title, body, id);
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
		System.out.println("connection created");
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
			node.dispose();
			listOfPNodes.remove(node);
		}
	}
	
	
	public void deleteNodeTest(TAMPNode node) {
		
		if(listOfPNodes.contains(node)) {
			node.disposeWithoutParent();
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
			connection.dispose();
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
	
	public void reset(int nodeCounter, int connectionCounter) {
		
		for(TAMPNode node : listOfPNodes) {
			node.dispose();
		}
		
		for(TAMPConnection connection : listOfPConnections) {
			connection.dispose();
		}
		
		for(ITAMEditor editor : listOfEditors) {
			editor.reset();
		}
		
		TAMPNode.resetSequenceNumber(nodeCounter);
		TAMPConnection.resetSequenceNumber(connectionCounter);
		
		listOfPNodes.clear();
		listOfPConnections.clear();
		
		root = null;
	}
	
	public void reset() {
		
		reset(0, 0);
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
	
	private static File createCardPath()
	{
        File cardDirectory = new File(Environment.getExternalStorageDirectory().getPath()+"/TestMind");
        if(!cardDirectory.exists())
        {
            if(!cardDirectory.mkdir())
            {
                cardDirectory = null;
            }
        }
        return cardDirectory;
	}
	
	private static String checkFileName(String fileName)
	{
	    fileName = fileName.replace("/", "_");
	    
	    return fileName;
	}
}
