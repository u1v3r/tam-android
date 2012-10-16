package cz.vutbr.fit.testmind.profile;

import java.util.HashMap;
import java.util.Map;

import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.editor.items.ITAMEItem;

public abstract class TAMPItem {
	
	protected Map<ITAMEditor, ITAMEItem> editorReferences;
	
	public TAMPItem() {
		editorReferences = new HashMap<ITAMEditor, ITAMEItem>();
	}
	
	public ITAMEItem getEReference(ITAMEditor editor) {
		return editorReferences.get(editor);
	}
	
	public void dispose() {
		
		for(ITAMEItem item : editorReferences.values()) {
			item.dispose();
		}
		editorReferences.clear();
	}
	
	public void removeEReference(ITAMEditor editor) {
		
		ITAMEItem item = getEReference(editor);
		
		if(item != null) {
			item.dispose();
			editorReferences.remove(editor);
		}
	}
	
	public void removeEReference(ITAMEItem selectedItem) {
		
		ITAMEItem item = getEReference(selectedItem.getEditor());
		
		if(item == selectedItem) {
			item.dispose();
			editorReferences.remove(selectedItem.getEditor());
		}
	}

    public Map<ITAMEditor, ITAMEItem> getEditorReferences()
    {
        return editorReferences;
    }
}
