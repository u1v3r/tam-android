package cz.vutbr.fit.testmind.editor.items;

import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.editor.TAMEditor;
import cz.vutbr.fit.testmind.graphics.ITAMGNode;
import cz.vutbr.fit.testmind.profile.TAMPNode;

public class TAMENode implements ITAMENode {
	
	private static final String TAG = "TAMEditorNode";
	
	private ITAMEditor editor;
	private ITAMGNode gui;
	private TAMPNode profile;
	private boolean hasVisibleChilds;
	
	private static int defaultType = ITAMGNode.NODE_TYPE_RECTANGLE;
	
	public TAMENode(TAMEditor editor, TAMPNode profile, int x, int y) {
		this(editor, profile, x, y, defaultType);
	}
	
	public TAMENode(TAMEditor editor, TAMPNode profile, int x, int y, int type) {
		this.editor = editor;
		this.profile = profile;
		this.gui = editor.getItemFactory().createNode(editor, type, x, y, profile.getTitle());
		this.gui.setHelpObject(this);
		this.hasVisibleChilds = true;
	}

	public ITAMEditor getEditor() {
		return editor;
	}
	
	public TAMPNode getProfile() {
		return profile;
	}
	
	public ITAMGNode getGui() {
		return gui;
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
		
		if(gui.isEnabled() == enable) return;
		
		gui.setEnabled(true);
		
		if(hasVisibleChilds == enable) {
			for(TAMPNode node : profile.getListOfChildNodes()) {
				((TAMENode) (node.getEReference(editor))).enable(enable);
			}
		}
	}

	public void dispose() {
		editor.getListOfENodes().remove(this);
		gui.dispose();
		gui = null;
		editor = null;
		profile = null;
	}

}
