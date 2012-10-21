package cz.vutbr.fit.testmind.editor.controls;

import java.io.File;

import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import cz.vutbr.fit.testmind.MainActivity;
import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.io.Serializer;

/**
 * 
 * TODO: trieda v ktorej bude implementovy open/save suboru
 *
 */
public class TAMEditorOpenSaveControl extends TAMEditorAbstractControl implements ITAMMenuListener{

	public TAMEditorOpenSaveControl(ITAMEditor editor) {
		super(editor);
		// TODO Auto-generated constructor stub
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
	    Log.d("testmind", "Saving");
        File cardDirectory = Environment.getExternalStorageDirectory();
        Serializer serializer = new Serializer(cardDirectory.getPath()+"/TestMind.db");
        serializer.serialize(MainActivity.getProfile());		
	}

	public void openFile()
	{
	    Log.d("testmind", "Loading");
        File cardDirectory = Environment.getExternalStorageDirectory();
        Serializer serializer = new Serializer(cardDirectory.getPath()+"/TestMind.db");
        serializer.deserialize(MainActivity.getProfile());    
	}

}
