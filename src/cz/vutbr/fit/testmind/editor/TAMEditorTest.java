package cz.vutbr.fit.testmind.editor;

import android.content.Context;
import android.util.AttributeSet;
import cz.vutbr.fit.testmind.editor.items.ITAMEConnection;
import cz.vutbr.fit.testmind.editor.items.ITAMENode;
import cz.vutbr.fit.testmind.profile.TAMPConnection;
import cz.vutbr.fit.testmind.profile.TAMPNode;

public class TAMEditorTest extends TAMAbstractEditor implements ITAMEditor {
	
	public TAMEditorTest(Context context) {
		this(context, null);
	}

	public TAMEditorTest(Context context, AttributeSet attrs) {
		super(context, attrs);
		// do not type anything there - use initializeControls method instead //
	}

	public int getMode() {
		// TODO Auto-generated method stub
		return 0;
	}

	public ITAMENode createNode(TAMPNode profile, int x, int y) {
		// TODO Auto-generated method stub
		return null;
	}

	public ITAMENode createNode(TAMPNode profile, int x, int y, int type) {
		// TODO Auto-generated method stub
		return null;
	}

	public ITAMEConnection createConnection(TAMPConnection profile) {
		// TODO Auto-generated method stub
		return null;
	}

	public ITAMEConnection createConnection(TAMPConnection profile, int type) {
		// TODO Auto-generated method stub
		return null;
	}

	public ITAMENode createNodeWithProfileAndConnection(String title, String body, ITAMENode parent, int posX, int posY) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void initializeControls() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void modeChanged(int id) {
		// TODO Auto-generated method stub
		
	}

}
