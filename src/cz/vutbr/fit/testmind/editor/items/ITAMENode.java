package cz.vutbr.fit.testmind.editor.items;

import java.io.Serializable;

import cz.vutbr.fit.testmind.graphics.ITAMGNode;
import cz.vutbr.fit.testmind.profile.TAMPNode;

public interface ITAMENode extends ITAMEItem, Serializable{

	public TAMPNode getProfile();
	
	public ITAMGNode getGui();
}
