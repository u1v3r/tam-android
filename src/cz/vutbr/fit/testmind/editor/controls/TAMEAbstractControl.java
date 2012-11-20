package cz.vutbr.fit.testmind.editor.controls;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.GestureDetector.OnGestureListener;
import cz.vutbr.fit.testmind.dialogs.AddNodeDialog;
import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.editor.items.TAMENode;

public abstract class TAMEAbstractControl {
	
	protected ITAMEditor editor;
	protected FragmentActivity activity;
	
	/**
	 * REQUEST CODES for activities
	 * @author jules
	 *
	 */
	public static final class REQUEST_CODES
	{
	    public static final int EDIT_NODE = 0;
	    public static final int OPEN = 1;
	    public static final int SAVE = 2;
	}
	
	public static final int PICK_FILE_RESULT_CODE = 0;
	public static final int EDIT_NODE_RESULT_CODE = 1;
	
	//public boolean isDialogOpen = false;
	private boolean enabled;
		
	public TAMEAbstractControl(ITAMEditor editor) {
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
	
	/*public void setOnGestureListner(OnGestureListener listener){
		editor.addOnGestureLisener(listener,editor.getContext());
	}*/
	
	/**
	 * Zobrazí dialog na pridanie uzlu
	 * @param parent 
	 */
	protected void showAddNodeDialog(TAMENode parent) {
			
		//if(isDialogOpen == false){
			//isDialogOpen = true;
			FragmentManager fm = activity.getSupportFragmentManager();		
			AddNodeDialog dialog = new AddNodeDialog(parent, this);
			dialog.show(fm, "fragment_add_node");
		//}
		
	}
}
