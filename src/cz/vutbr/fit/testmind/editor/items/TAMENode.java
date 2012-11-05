package cz.vutbr.fit.testmind.editor.items;

import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.editor.TAMEditor;
import cz.vutbr.fit.testmind.editor.controls.TAMEditorHidingControl.ITAMHidingControlNode;
import cz.vutbr.fit.testmind.graphics.ITAMGNode;
import cz.vutbr.fit.testmind.profile.TAMPNode;

public class TAMENode implements ITAMENode, ITAMHidingControlNode {
	
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
		for(TAMPNode node : profile.getListOfChildNodes()) {
			TAMENode child = ((TAMENode) (node.getEReference(editor)));
			if(child.getGui().isEnabled()) {
				return true;
			}
		}
		return false;
	}
	
	public void setChildsVisible(boolean visible, boolean oneLevel) {
		
		//if(this.hasVisibleChilds == visible) return;
		
		for(TAMPNode node : profile.getListOfChildNodes()) {
			TAMENode child = ((TAMENode) (node.getEReference(editor)));
			if(child.getGui().isEnabled() != visible) {
				if(oneLevel && visible) {
					child.getGui().setEnabled(visible);
					if(!child.getProfile().getListOfChildNodes().isEmpty()) {
						child.gui.setNodeState(ITAMGNode.NODE_STATE_COLLAPSE);
					}
				} else {
					child.enable(visible);
				}
			}
		}
		
		if(visible) {
			gui.setNodeState(ITAMGNode.NODE_STATE_DEFAULT);
		} else {
			gui.setNodeState(ITAMGNode.NODE_STATE_COLLAPSE);
		}
		
		//this.hasVisibleChilds = visible;
	}
	
	private void enable(boolean enable) {
		
		//System.out.println("enabling: " + gui.getText() + " " + enable + " " + gui.isEnabled());
		
		if(gui.isEnabled() == enable) return;
		
		gui.setEnabled(enable);
		
		//if(hasVisibleChilds) {
			for(TAMPNode node : profile.getListOfChildNodes()) {
				TAMENode child = ((TAMENode) (node.getEReference(editor)));
				if(child.getGui().isEnabled() != enable) {
					child.enable(enable);
				}
			}
		//}
	}

	public void dispose() {
		editor.getListOfENodes().remove(this);
		gui.dispose();
		gui = null;
		editor = null;
		profile = null;
	}

}
