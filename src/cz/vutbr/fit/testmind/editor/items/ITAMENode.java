package cz.vutbr.fit.testmind.editor.items;

import cz.vutbr.fit.testmind.graphics.ITAMGNode;
import cz.vutbr.fit.testmind.profile.TAMPNode;

public interface ITAMENode extends ITAMEItem {

	public TAMPNode getProfile();
	
	public ITAMGNode getGui();
}
