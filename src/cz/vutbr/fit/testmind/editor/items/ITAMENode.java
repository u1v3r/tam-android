package cz.vutbr.fit.testmind.editor.items;

import java.io.Serializable;

import cz.vutbr.fit.testmind.R;
import cz.vutbr.fit.testmind.graphics.ITAMGNode;
import cz.vutbr.fit.testmind.profile.TAMPNode;

public interface ITAMENode extends ITAMEItem, Serializable {
	
	public final int BLUE = R.color.node_background_1;
	public final int GREEN = R.color.node_background_2;
	public final int RED = R.color.node_background_3;
	public final int PURPLE = R.color.node_background_4;
	
	public void setBackgroundStyle(int background);
	
	public int getBackgroundStyle();

	public TAMPNode getProfile();
	
	public ITAMGNode getGui();
}
