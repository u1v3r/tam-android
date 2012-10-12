package cz.vutbr.fit.testmind.editor.items;

import java.util.ArrayList;
import java.util.List;

import cz.vutbr.fit.testmind.editor.TAMEditor;
import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.graphics.ITAMConnection;
import cz.vutbr.fit.testmind.graphics.ITAMNode;

public class TAMEditorNode {
	
	private int id;
	private ITAMEditor editor;
	private ITAMNode core;
	private String body;
	private List<TAMEditorNode> listOfChildNodes;
	
	private static int counter = 0;
	private static int defaultType = ITAMConnection.CONNECTION_TYPE_DEFAULT;
	
	private boolean hasVisibleChilds;
	
	public TAMEditorNode(TAMEditor editor, int x, int y, String title, String body, int type) {
		this(editor, x, type, title, body, type, getNewSequenceNumber());
	}
	
	public TAMEditorNode(TAMEditor editor, int x, int y, String title, String body, int type, ITAMNode core) {
		this(editor,x,y,title,body,type,getNewSequenceNumber(),core);
	}
	
	public TAMEditorNode(TAMEditor editor, int x, int y, String title, String body, int type, int id) {
		this(editor,x,y,title,body,type,id,null);
	}	
	
	public TAMEditorNode(TAMEditor editor, int x, int y, String title, String body, int type, int id, ITAMNode core) {
		this.id = id;
		this.editor = editor;
		this.body = body;
		this.core = core;
		this.listOfChildNodes = new ArrayList<TAMEditorNode>();
		this.hasVisibleChilds = true;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public ITAMNode getCore() {
		return core;
	}
	
	public void setCore(ITAMNode core) {
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

	public List<TAMEditorNode> getListOfChildNodes() {
		return listOfChildNodes;
	}
	
	public TAMEditorNode addChild(int x, int y, String title, String body) {
		
		return addChild(x, y, title, body, getDefaultType(), TAMEditorConnection.getDefaultType());
	}
	
	public TAMEditorNode addChild(int x, int y, String title, String body, int nodeType, int connectionType) {
		
		TAMEditorNode node = editor.getFactory().createNode(x, y, title, body, nodeType);
		editor.getFactory().createConnection(this, node, connectionType);
		
		return node;
	}

	public static int getDefaultType() {
		return defaultType;
	}

	public static void setDefaultType(int defaultType) {
		TAMEditorNode.defaultType = defaultType;
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
			for(TAMEditorNode node : listOfChildNodes) {
				node.enable(enable);
			}
		}
	}

}
