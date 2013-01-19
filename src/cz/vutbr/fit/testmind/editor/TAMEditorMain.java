package cz.vutbr.fit.testmind.editor;

import android.content.Context;
import android.util.AttributeSet;
import cz.vutbr.fit.testmind.MainActivity;
import cz.vutbr.fit.testmind.editor.controls.TAMEHidingControl;
import cz.vutbr.fit.testmind.editor.controls.TAMEIOControl;
import cz.vutbr.fit.testmind.editor.controls.TAMENodeControl;
import cz.vutbr.fit.testmind.editor.controls.TAMENodeControl.ITAMNodeControlListener;
import cz.vutbr.fit.testmind.editor.controls.TAMEOpenSaveControl;
import cz.vutbr.fit.testmind.editor.controls.TAMERootInitializeControl;
import cz.vutbr.fit.testmind.editor.controls.TAMERootInitializeControl.ITAMRootControlListener;
import cz.vutbr.fit.testmind.editor.controls.TAMEZoomControl;
import cz.vutbr.fit.testmind.editor.items.ITAMENode;
import cz.vutbr.fit.testmind.profile.TAMPConnection;
import cz.vutbr.fit.testmind.profile.TAMPConnectionFactory;
import cz.vutbr.fit.testmind.profile.TAMPNode;

/**
 * Obsahuje zakladne funkcie na pracu s grafom 
 *
 */
public class TAMEditorMain extends TAMAbstractEditor implements ITAMEditor, 
	ITAMRootControlListener, ITAMNodeControlListener {
	
	private static final String TAG = "TAMEditor";
	
	private int mode;
	private boolean hasRoot = false;
	private boolean hasVisibleMenu = false;
	
	public TAMEditorMain(Context context) {
		this(context, null);
	}
	
	public TAMEditorMain(Context context, AttributeSet attrs) {		
		super(context,attrs);		
		// do not type anything there - use initializeControls method instead //
	}
	
	public boolean createDefaultRootNode(String title) {
		return createDefaultRootNode(title, getWidth()/2, getHeight()/2);
	}
	
	public boolean createDefaultRootNode(String title, int x, int y) {
		
		if(hasRoot) {
			return false;
		} else {
			TAMPNode pNode = MainActivity.getProfile().createRoot(title, "");			
			ITAMENode eNode = TAMPConnectionFactory.addEReference(pNode, this, x, y);
			eNode.getGui().setSelected(true);
			//showToolbar();
			
			this.hasRoot = true;
			
			return true;
		}
		
	}

	public int getMode() {
		return mode;
	}

	public ITAMENode createNodeWithProfileAndConnection(String title, String body, ITAMENode parent, int posX, int posY) {
		TAMPNode newProfileNode = profile.createNode(title, body);		
		ITAMENode newEditorNode = TAMPConnectionFactory.addEReference(newProfileNode, this, posX, posY);
		TAMPConnection pConnection = profile.createConnection(parent.getProfile(), newProfileNode);		
		TAMPConnectionFactory.addEReference(pConnection, this);
		newEditorNode.setBackgroundStyle(parent.getBackgroundStyle());
		
		return newEditorNode;
	}

	@Override
	protected void initializeControls() {		
		new TAMEZoomControl(this);
		new TAMENodeControl(this);
		new TAMEOpenSaveControl(this);
		new TAMEIOControl(this);
		new TAMEHidingControl(this);
		new TAMERootInitializeControl(this);
		
		//mode = MenuItems.create_mode;
		//actualItem = EventObjects.menu_create;
	}
	

	@Override
	public void setVisibility(int visibility) {
		super.setVisibility(visibility);
	}
}
