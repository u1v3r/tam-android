package cz.vutbr.fit.testmind.editor.controls;

import android.graphics.Canvas;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;
import android.view.MotionEvent;
import cz.vutbr.fit.testmind.dialogs.AddNodeDialog;
import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.editor.items.TAMENode;
import cz.vutbr.fit.testmind.graphics.ITAMGNode;
import cz.vutbr.fit.testmind.graphics.ITAMGNode.OnNodeSelectListener;

public abstract class TAMEditorAbstractControl implements OnNodeSelectListener{
	
	protected ITAMEditor editor;
	protected FragmentActivity activity;
	
	private boolean enabled;
		
	public TAMEditorAbstractControl(ITAMEditor editor) {
		this.editor = editor;
		this.activity = (FragmentActivity)editor.getContext();
	}
	
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
	
	/**
	 * Zobrazí dialog na pridanie uzlu
	 * @param parent 
	 */
	protected void showAddNodeDialog(TAMENode parent) {
			
		FragmentManager fm = activity.getSupportFragmentManager();		
		AddNodeDialog dialog = new AddNodeDialog(parent, this);
		dialog.show(fm, "fragment_add_node");
		
	}

	public abstract boolean onOptionsItemSelected(MenuItem item);

	public abstract void onDraw(Canvas canvas);

	public abstract void onTouchEvent(MotionEvent e);

}
