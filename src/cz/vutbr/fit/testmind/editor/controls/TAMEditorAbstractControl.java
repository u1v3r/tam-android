package cz.vutbr.fit.testmind.editor.controls;

import android.graphics.Canvas;
import android.view.MenuItem;
import android.view.MotionEvent;
import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.graphics.ITAMNode.OnNodeSelectListener;

public abstract class TAMEditorAbstractControl implements OnNodeSelectListener{
	
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

	public abstract void onTouchEvent(MotionEvent e);
}
