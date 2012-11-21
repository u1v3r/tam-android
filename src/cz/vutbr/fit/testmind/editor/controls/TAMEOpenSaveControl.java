package cz.vutbr.fit.testmind.editor.controls;

import android.app.Activity;
import android.content.Intent;
import android.preference.PreferenceManager.OnActivityResultListener;
import android.util.Log;
import android.view.MenuItem;
import cz.vutbr.fit.testmind.MainActivity;
import cz.vutbr.fit.testmind.OpenSaveActivity;
import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.io.Serializer;
import cz.vutbr.fit.testmind.profile.TAMProfile;

/**
 * open and save mind maps 
 * @author jules
 *
 */
public class TAMEOpenSaveControl extends TAMEAbstractControl implements ITAMMenuListener, OnActivityResultListener
{
	private static final String TAG = "TAMEditorOpenSaveControl";

	public TAMEOpenSaveControl(ITAMEditor editor)
	{
		super(editor);
		
		editor.getListOfMenuControls().add(this);
		editor.getListOfOnActivityResultControls().add(this);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		
		if(MainActivity.MenuItems.open == item.getItemId()){
			startActivity();
		}
		else if(MainActivity.MenuItems.save == item.getItemId()){
		    			
			if(editor.getListOfENodes().size() == 0) return true;
			
			/*
			 * TODO: treba vyriesit ukladanie aj pri rovnakych menach (pridat datum ulozenie alebo nieco podobne)			
			 */
			String rootTitle = editor.getListOfENodes().get(0).getProfile().getTitle();
			Serializer serializer = new Serializer(
					String.format("%s/%s.db", TAMProfile.TESTMIND_DIRECTORY.getPath(), rootTitle));
			serializer.serialize(MainActivity.getProfile());			
		}
		
		return true;			
	}

    public boolean onActivityResult(int requestCode, int resultCode, Intent data)
    {
    	Log.d(TAG,"req:" + requestCode + ", result:" + resultCode);
    	
        if(resultCode == Activity.RESULT_OK && requestCode == PICK_FILE_RESULT_CODE)
        {
            String file = data.getStringExtra(OpenSaveActivity.INTENT_ID_RESULT);
            
            Serializer serializer = new Serializer(String.format("%s/%s.db", TAMProfile.TESTMIND_DIRECTORY.getPath(), file));
            
            serializer.deserialize(MainActivity.getProfile());           
            
            editor.invalidate();
            
            return true;
        }

        return false;
    }

	/**
	 * start activity for selecting file
	 * @param which
	 */
	private void startActivity()
	{
	    Intent i = new Intent(activity, OpenSaveActivity.class);    
	    activity.startActivityForResult(i, PICK_FILE_RESULT_CODE);	    
	}
}
