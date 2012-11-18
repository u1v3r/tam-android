package cz.vutbr.fit.testmind.editor.controls;

import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
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
public class TAMEOpenSaveControl extends TAMEAbstractControl implements ITAMMenuListener{

	private static final String TAG = "TAMEditorOpenSaveControl";
	public static final class OpenSaveId
	{
	    public static final String Save = "save";
	    public static final String Open = "open";
	}
	private MainActivity mainActivity;

	public TAMEOpenSaveControl(ITAMEditor editor, MainActivity mainActivity)
	{
		super(editor);
		this.mainActivity = mainActivity;
		editor.getListOfMenuControls().add(this);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		
		if(MainActivity.MenuItems.open == item.getItemId()){
			openFile();
		}
		else if(MainActivity.MenuItems.save == item.getItemId()){
			saveFile();
		}
		
		return true;
			
	}

	public void saveFile()
	{
	    startActivity(OpenSaveId.Save);
        Serializer serializer = new Serializer(TAMProfile.TESTMIND_DIRECTORY.getPath()+"/TestMind.db");
        serializer.serialize(MainActivity.getProfile());		
	}

	public void openFile()
	{
	    startActivity(OpenSaveId.Open);
        Serializer serializer = new Serializer(TAMProfile.TESTMIND_DIRECTORY.getPath()+"/TestMind.db");
        serializer.deserialize(MainActivity.getProfile());    
	}

	private void startActivity(String which)
	{
	    Intent i = new Intent(mainActivity, OpenSaveActivity.class);
    
	    i.putExtra("cz.vutbr.fit.testmind.opensave.which", which);
    
	    mainActivity.startActivity(i);
	}
}
