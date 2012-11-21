package cz.vutbr.fit.testmind.editor.controls;

import android.support.v4.app.FragmentManager;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import cz.vutbr.fit.testmind.R;
import cz.vutbr.fit.testmind.dialogs.NodeMainTopicDialog;
import cz.vutbr.fit.testmind.dialogs.NodeMainTopicDialog.OnMainTopicSetDialogListener;
import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.graphics.TAMGraph.ITAMGraphDrawingFinishedListener;

public class TAMERootInitializeControl extends TAMEAbstractControl implements OnMainTopicSetDialogListener, 
	ITAMGraphDrawingFinishedListener {
		
	public interface ITAMRootControlListener {
		public boolean createDefaultRootNode(String title);
		public boolean createDefaultRootNode(String title, int x, int y);
	}
	
	public TAMERootInitializeControl(ITAMEditor editor) {
		super(editor);
		this.editor.getListOfGraphDrawingFinishedListener().add(this);
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
		
		if( (editor.getListOfENodes().size() == 1) && (editor.getProfile().getRoot() != null) ){
			editor.getListOfENodes().get(0).getGui().setText(title);
			editor.getListOfENodes().get(0).getProfile().setTitle(title);
		}
		
		
	}

	public void onDrawingFinished() {
		((ITAMRootControlListener) getEditor()).createDefaultRootNode(
				this.editor.getResources().getString(R.string.node_main_topic_dialog_default_topic));
		
	}
}
