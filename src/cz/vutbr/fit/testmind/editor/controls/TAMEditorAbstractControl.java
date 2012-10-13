package cz.vutbr.fit.testmind.editor.controls;

import android.graphics.Canvas;
import android.view.MenuItem;
import cz.vutbr.fit.testmind.editor.ITAMEditor;

public abstract class TAMEditorAbstractControl {
	
	private boolean enabled;
	private ITAMEditor editor;
	
	public void setEditor(ITAMEditor editor){
		this.editor = editor;
	}
	
	public ITAMEditor getEditor(){
		return this.editor;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public abstract boolean onOptionsItemSelected(MenuItem item);

	public abstract void onDraw(Canvas canvas);

}
