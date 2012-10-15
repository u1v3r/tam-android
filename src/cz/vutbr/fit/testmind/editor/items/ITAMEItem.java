package cz.vutbr.fit.testmind.editor.items;

import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.graphics.ITAMGItem;
import cz.vutbr.fit.testmind.profile.TAMPItem;

public interface ITAMEItem {

	public ITAMEditor getEditor();
	
	public TAMPItem getProfile();
	
	public ITAMGItem getGui();

	public void dispose();
}
