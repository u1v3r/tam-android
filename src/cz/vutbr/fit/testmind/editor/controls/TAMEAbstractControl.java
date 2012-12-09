package cz.vutbr.fit.testmind.editor.controls;

import java.io.File;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.GestureDetector.OnGestureListener;
import cz.vutbr.fit.testmind.MainActivity;
import cz.vutbr.fit.testmind.R;
import cz.vutbr.fit.testmind.dialogs.NodeMainTopicDialog;
import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.editor.items.TAMENode;
import cz.vutbr.fit.testmind.io.Serializer;
import cz.vutbr.fit.testmind.profile.TAMProfile;

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
	    public static final int EDIT_NODE = 100;
	    public static final int OPEN = 101;
	    public static final int SAVE = 102;
	    public static final int NEW_MAP = 103;
	}
	
	public static final int PICK_FILE_RESULT_CODE = 200;
	public static final int EDIT_NODE_RESULT_CODE = 201;
	public static final int IMPORT_FILE = 202;
	public static final int EXPORT_FILE = 203;
	
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
	
	/**
	 * Ulozi mapu pod menom root uzlu
	 */
	protected void saveMindMap() {
		
		if(editor.getListOfENodes().size() == 0) return;
		
		/*
		 * TODO: treba vyriesit ukladanie aj pri rovnakych menach (pridat datum ulozenie alebo nieco podobne)			
		 */
		editor.getProfile().setFileName(editor.getListOfENodes().get(0).getProfile().getTitle());
			
		Serializer serializer = new Serializer(
				String.format("%s/%s.%s", 
						TAMProfile.TESTMIND_DIRECTORY.getPath(), editor.getProfile().getFileName(),
						TAMEOpenSaveControl.TESTMIND_FILE_EXTENSION));
		
		serializer.serialize(MainActivity.getProfile());
		
			
		return;
	}
	
	/*public void setOnGestureListner(OnGestureListener listener){
		editor.addOnGestureLisener(listener,editor.getContext());
	}*/
}
