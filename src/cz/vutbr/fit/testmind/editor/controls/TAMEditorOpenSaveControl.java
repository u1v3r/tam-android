package cz.vutbr.fit.testmind.editor.controls;

import android.view.MenuItem;
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

	public void saveFile() {
		// TODO Auto-generated method stub
		
	}

	public void openFile() {
		// TODO Auto-generated method stub
		
	}

}
