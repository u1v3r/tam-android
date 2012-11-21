package cz.vutbr.fit.testmind.editor.controls;

import android.support.v4.app.FragmentManager;
import cz.vutbr.fit.testmind.dialogs.NodeMainTopicDialog;
import cz.vutbr.fit.testmind.dialogs.NodeMainTopicDialog.OnMainTopicSetDialogListener;
import cz.vutbr.fit.testmind.editor.ITAMEditor;

public class TAMERootInitializeControl extends TAMEAbstractControl implements OnMainTopicSetDialogListener {
		
	public interface ITAMRootControlListener {
		public boolean createDefaultRootNode(String title);
		public boolean createDefaultRootNode(String title, int x, int y);
	}
	
	public TAMERootInitializeControl(ITAMEditor editor) {
		super(editor);
		showAddNodeDialog();
	}
		
/*
	public void onHitEvent(MotionEvent e, TAMGMotionEvent ge) {
		if(((ITAMRootControlListener) getEditor()).createDefaultRootNode((int) ge.dx, (int) ge.dy)) {
			getEditor().getListOfTouchControls().remove(this);
		}
	}

	public void onTouchEvent(MotionEvent e, float dx, float dy) {
		// TODO Auto-generated method stub
		
	}

	public void onFinishAddChildNodeDialog(String title) {
		// TODO Auto-generated method stub
		
	}
*/	

	/**
	 * Zobrazí dialog na pridanie uzlu
	 */
	private void showAddNodeDialog() {			
		FragmentManager fm = activity.getSupportFragmentManager();		
		NodeMainTopicDialog dialog = new NodeMainTopicDialog(this);
		dialog.show(fm, "fragment_add_node");		
	}
	
	/**
	 * Metoda volana pri ulozeni dialogu
	 */
	public void onFinishNodeEditDialog(String title) {
		((ITAMRootControlListener) getEditor()).createDefaultRootNode(title);
	}
}
