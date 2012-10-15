package cz.vutbr.fit.testmind.editor.items;

import cz.vutbr.fit.testmind.graphics.ITAMGConnection;
import cz.vutbr.fit.testmind.graphics.ITAMGNode;
import cz.vutbr.fit.testmind.profile.TAMPConnection;
import cz.vutbr.fit.testmind.profile.TAMPNode;

public interface ITAMEConnection extends ITAMEItem {

	public TAMPConnection getProfile();
	
	public ITAMGConnection getGui();
}
