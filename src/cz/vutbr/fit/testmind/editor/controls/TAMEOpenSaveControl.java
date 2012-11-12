package cz.vutbr.fit.testmind.editor.controls;

import java.io.File;

import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import cz.vutbr.fit.testmind.MainActivity;
import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.io.Serializer;

/**
 * 
 * TODO: trieda v ktorej bude implementovy open/save suboru
 *
 */
public class TAMEOpenSaveControl extends TAMEAbstractControl implements ITAMMenuListener{

	private static final String TAG = "TAMEditorOpenSaveControl";

	public TAMEOpenSaveControl(ITAMEditor editor) {
		super(editor);
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
	    Log.d(TAG, "Saving");
        File cardDirectory = Environment.getExternalStorageDirectory();
        Serializer serializer = new Serializer(cardDirectory.getPath()+"/TestMind.db");
        serializer.serialize(MainActivity.getProfile());		
	}

	public void openFile()
	{
	    Log.d(TAG, "Loading");
        File cardDirectory = Environment.getExternalStorageDirectory();
        Serializer serializer = new Serializer(cardDirectory.getPath()+"/TestMind.db");
        serializer.deserialize(MainActivity.getProfile());    
	}

}
