package cz.vutbr.fit.testmind.editor.controls;

import android.view.MenuItem;
import android.widget.Toast;
import cz.vutbr.fit.testmind.MainActivity;
import cz.vutbr.fit.testmind.editor.ITAMEditor;

/**
 * 
 * TODO: trieda v ktorej bude implementovy open/save suboru
 *
 */
public class TAMEditorOpenSaveControl extends TAMEditorAbstractControl implements ITAMMenuListener{

	public TAMEditorOpenSaveControl(ITAMEditor editor) {
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

	public void saveFile() {
		// TODO Auto-generated method stub
		Toast.makeText(activity, "je tu", Toast.LENGTH_LONG).show();
	}

	public void openFile() {
		// TODO Auto-generated method stub
		
	}

}
