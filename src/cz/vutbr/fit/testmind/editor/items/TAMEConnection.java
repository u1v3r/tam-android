package cz.vutbr.fit.testmind.editor.items;

import android.util.Log;
import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.graphics.ITAMGConnection;
import cz.vutbr.fit.testmind.graphics.ITAMGNode;
import cz.vutbr.fit.testmind.graphics.TAMGraph;
import cz.vutbr.fit.testmind.profile.TAMPConnection;

public class TAMEConnection implements ITAMEConnection {
	
	private ITAMEditor editor;
	private ITAMGConnection gui;
	private TAMPConnection profile;
	private static int defaultType = ITAMGConnection.CONNECTION_TYPE_DEFAULT;
	
	public TAMEConnection(ITAMEditor editor, TAMPConnection profile) {
		this(editor, profile, defaultType);
	}
	
	public TAMEConnection(ITAMEditor editor, TAMPConnection profile, int type) {
		this.editor = editor;
		this.profile = profile;
		this.gui = editor.getGItemFactory().createConnection((TAMGraph)editor,
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
		
		editor.getListOfEConnections().remove(this);
		editor.getGItemFactory().deleteConnection(gui);
		gui = null;
		editor = null;
		profile = null;
	}

}
