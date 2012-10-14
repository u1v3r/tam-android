package cz.vutbr.fit.testmind.editor.items;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.editor.TAMEditor;
import cz.vutbr.fit.testmind.graphics.ITAMGConnection;
import cz.vutbr.fit.testmind.graphics.ITAMGNode;

public class TAMENode {
	
	private static final String TAG = "TAMEditorNode";
	
	private int id;
	private ITAMEditor editor;
	private ITAMGNode core;
	private String body;
	private List<TAMENode> listOfChildNodes;
	
	private static int counter = 0;
	private static int defaultType = ITAMGConnection.CONNECTION_TYPE_DEFAULT;
	
	private boolean hasVisibleChilds;
	
	public TAMENode(TAMEditor editor, int x, int y, String title, String body, int type) {
		this(editor, x, y, title, body, type, getNewSequenceNumber());
	}	
	
	public TAMENode(TAMEditor editor, int x, int y, String title, String body, int type, int id) {
		this.id = id;
		this.editor = editor;
		this.body = body;
		this.listOfChildNodes = new ArrayList<TAMENode>();
		this.hasVisibleChilds = true;
		this.core = editor.getItemFactory().createNode(editor, type, x, y, title);
		this.core.setHelpObject(this);
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public ITAMGNode getCore() {
		return core;
	}
	
	public void setCore(ITAMGNode core) {
		this.core = core;
	}

	public int getId() {
		return id;
	}

	public ITAMEditor getEditor() {
		return editor;
	}

	public static int getNewSequenceNumber() {
		
		counter++;
		
		return counter;
	}

	public List<TAMENode> getListOfChildNodes() {
		return listOfChildNodes;
	}
	
	public TAMENode addChild(int x, int y, String title, String body) {
		
		return addChild(x, y, title, body, getDefaultType(), TAMEConnection.getDefaultType());
	}
	
	public TAMENode addChild(int x, int y, String title, String body, int nodeType, int connectionType) {
		
		TAMENode node = editor.getFactory().createNode(x, y, title, body, nodeType);
		editor.getFactory().createConnection(this, node, connectionType);
		
		return node;
	}

	public static int getDefaultType() {
		return defaultType;
	}

	public static void setDefaultType(int defaultType) {
		TAMENode.defaultType = defaultType;
	}
	
	public boolean hasVisibleChilds() {
		return hasVisibleChilds;
	}
	
	public void setChildsVisible(boolean visible) {
		
		if(this.hasVisibleChilds == visible) return;
		
		enable(visible);
		
		this.hasVisibleChilds = visible;
	}
	
	private void enable(boolean enable) {
		
		if(core.isEnabled() == enable) return;
		
		core.setEnabled(true);
		
		if(hasVisibleChilds == enable) {
			for(TAMENode node : listOfChildNodes) {
				node.enable(enable);
			}
		}
	}

}
