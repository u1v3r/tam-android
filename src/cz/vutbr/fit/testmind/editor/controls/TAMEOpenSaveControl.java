package cz.vutbr.fit.testmind.editor.controls;

import android.app.Activity;
import android.content.Intent;
import android.preference.PreferenceManager.OnActivityResultListener;
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

	private ITAMEditor editor;

	public TAMEOpenSaveControl(ITAMEditor editor)
	{
		super(editor);
		
		editor.getListOfMenuControls().add(this);
		editor.getListOfOnActivityResultControls().add(this);
		
		this.editor = editor;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		
		if(MainActivity.MenuItems.open == item.getItemId()){
			startActivity(REQUEST_CODES.OPEN);
		}
		else if(MainActivity.MenuItems.save == item.getItemId()){
		    startActivity(REQUEST_CODES.SAVE);
		}
		
		return true;
			
	}

    public boolean onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if((requestCode == REQUEST_CODES.OPEN || requestCode == REQUEST_CODES.SAVE)
           && resultCode == Activity.RESULT_OK)
        {
            String file = data.getStringExtra(OpenSaveActivity.INTENT_ID_RESULT);
            
            Serializer serializer = new Serializer(String.format("%s/%s.db", TAMProfile.TESTMIND_DIRECTORY.getPath(), file));
            if(requestCode == REQUEST_CODES.OPEN)
            {
                serializer.deserialize(MainActivity.getProfile());
            }
            else if(requestCode == REQUEST_CODES.SAVE)
            {
                serializer.serialize(MainActivity.getProfile());
            }
        }

        return true;
    }

	/**
	 * start activity for selecting file
	 * @param which
	 */
	private void startActivity(int which)
	{
	    Intent i = new Intent(activity, OpenSaveActivity.class);
    
	    i.putExtra(OpenSaveActivity.INTENT_ID_MODE, which);
    
	    activity.startActivityForResult(i, which);
	}
}
