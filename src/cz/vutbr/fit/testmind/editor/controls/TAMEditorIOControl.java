package cz.vutbr.fit.testmind.editor.controls;

import android.view.MenuItem;
import cz.vutbr.fit.testmind.MainActivity;
import cz.vutbr.fit.testmind.editor.ITAMEditor;

/**
 * 
 * TODO: trieda v ktorej bude implementovany import/export
 *
 */
public class TAMEditorIOControl extends TAMEditorAbstractControl implements ITAMMenuListener{

	public TAMEditorIOControl(ITAMEditor editor) {
		super(editor);
		// TODO Auto-generated constructor stub
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		
		if(MainActivity.MenuItems.importFile == item.getItemId()){
			importFile();
		}
		else if(MainActivity.MenuItems.exportFile == item.getItemId()){
			exportFile();
		}
		
		return true;
			
	}

	public void exportFile() {
		// TODO Auto-generated method stub
		
	}

	public void importFile() {
		// TODO Auto-generated method stub
		
	}
	
	

}
