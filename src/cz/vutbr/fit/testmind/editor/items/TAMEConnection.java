package cz.vutbr.fit.testmind.editor.items;

import cz.vutbr.fit.testmind.editor.TAMEditor;
import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.graphics.ITAMGConnection;
import cz.vutbr.fit.testmind.graphics.ITAMGNode;
import cz.vutbr.fit.testmind.profile.TAMPConnection;

public class TAMEConnection implements ITAMEConnection {
	
	private ITAMEditor editor;
	private ITAMGConnection gui;
	private TAMPConnection profile;
	private static int defaultType = ITAMGConnection.CONNECTION_TYPE_DEFAULT;
	
	public TAMEConnection(TAMEditor editor, TAMPConnection profile) {
		this(editor, profile, defaultType);
	}
	
	public TAMEConnection(TAMEditor editor, TAMPConnection profile, int type) {
		this.editor = editor;
		this.profile = profile;
		this.gui = editor.getItemFactory().createConnection(editor,
				(ITAMGNode) (((ITAMEItem) (profile.getParent().getEReference(editor))).getGui()),
				(ITAMGNode) (((ITAMEItem) (profile.getChild().getEReference(editor))).getGui()), type);
		this.gui.setHelpObject(this);
	}
	
	public ITAMEditor getEditor() {
		return editor;
	}
	
	public TAMPConnection getProfile() {
		return profile;
	}

	public ITAMGConnection getGui() {
		return gui;
	}

	public static int getDefaultType() {
		return defaultType;
	}

	public static void setDefaultType(int defaultConnectionType) {
		TAMEConnection.defaultType = defaultConnectionType;
	}

	public void dispose() {
		gui.dispose();
		gui = null;
		editor = null;
		profile = null;
	}

}
