package cz.vutbr.fit.testmind.editor.items;

import android.content.res.Resources;
import cz.vutbr.fit.testmind.R;
import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.editor.TAMEditorMain;
import cz.vutbr.fit.testmind.editor.controls.TAMENodeControl;
import cz.vutbr.fit.testmind.editor.controls.TAMEHidingControl.ITAMHidingControlNode;
import cz.vutbr.fit.testmind.graphics.ITAMGNode;
import cz.vutbr.fit.testmind.graphics.TAMGraph;
import cz.vutbr.fit.testmind.profile.TAMPNode;

public class TAMENode implements ITAMENode, ITAMHidingControlNode {
	
	private static final String TAG = "TAMEditorNode";
	
	private ITAMEditor editor;
	private ITAMGNode gui;
	private TAMPNode profile;
	//private boolean hasVisibleChilds;
	private int backgroundStyle;
	
	private static int defaultType = ITAMGNode.NODE_TYPE_RECTANGLE;
	
	public TAMENode(ITAMEditor editor, TAMPNode profile, int x, int y) {
		this(editor, profile, x, y, defaultType);
	}
	
	public TAMENode(ITAMEditor editor, TAMPNode profile, int x, int y, int type) {
		this.editor = editor;
		this.profile = profile;
		this.gui = editor.getGItemFactory().createNode((TAMGraph) editor, type, x, y, profile.getTitle());
		this.gui.setHelpObject(this);
		
		gui.setColorText(editor.getResources().getColor(R.color.node_text));
		gui.setColorBackgroundHighlight(editor.getResources().getColor(R.color.node_highlight_background));
		gui.setColorStrokeHighlight(editor.getResources().getColor(R.color.node_highlight_background_stroke));
		setBackgroundStyle(BLUE);
		//this.hasVisibleChilds = true;
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
		editor.getGItemFactory().deleteNode(gui, false);
		gui = null;
		editor = null;
		profile = null;
	}
	
	@Override
	public String toString() {
		return getGui().getText();
	}
	
	public void setBackgroundStyle(int style){
    	
    	Resources res = editor.getResources();
    	if(BLUE == style){
    		backgroundStyle = style;
    		gui.setColorBackground(res.getColor(R.color.node_background_1));
    		gui.setColorStroke(res.getColor(R.color.node_background_stroke_1));
    	}else if(GREEN == style){
    		backgroundStyle = style;
    		gui.setColorBackground(res.getColor(R.color.node_background_2));
    		gui.setColorStroke(res.getColor(R.color.node_background_stroke_2));
    	}else if(RED == style){
    		backgroundStyle = style;
    		gui.setColorBackground(res.getColor(R.color.node_background_3));
    		gui.setColorStroke(res.getColor(R.color.node_background_stroke_3));
    	}else if(PURPLE == style){
    		backgroundStyle = style;
    		gui.setColorBackground(res.getColor(R.color.node_background_4));
    		gui.setColorStroke(res.getColor(R.color.node_background_stroke_4));
    	}
    }
	
	public int getBackgroundStyle(){
    	return backgroundStyle;
    }
}
