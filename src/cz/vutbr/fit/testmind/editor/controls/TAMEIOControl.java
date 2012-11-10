package cz.vutbr.fit.testmind.editor.controls;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import cz.vutbr.fit.testmind.MainActivity;
import cz.vutbr.fit.testmind.R;
import cz.vutbr.fit.testmind.editor.ITAMEditor;

/**
 * 
 * TODO: trieda v ktorej bude implementovany import/export
 *
 */
public class TAMEIOControl extends TAMEAbstractControl implements ITAMMenuListener{
	
	private static final String INTENT_MIME_TYPE = "text/xml";

	public TAMEIOControl(ITAMEditor editor) {
		super(editor);
		editor.getListOfMenuControls().add(this);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		
		/*		
		if(item.getItemId() == MenuItems.add) {
			showAddChildNodeDialog();
			return true;
		} else if(item.getItemId() == MenuItems.importFile) {
			FreeMind freeMind = new FreeMind();
			try {
				freeMind.getMindMap();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		} else {
			return false;
		}
*/	
		
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
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT); 
        intent.setType(INTENT_MIME_TYPE); 
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            activity.startActivityForResult(
            		
                    Intent.createChooser(intent, 
                    		editor.getResources().getString(R.string.select_file_to_upload)),
                    		PICK_FILE_RESULT_CODE);
            
        } catch (ActivityNotFoundException e) {
            
        	// vyzve uzivatela na instalaciu file managera
            Toast.makeText(editor.getContext(), 
            		editor.getResources().getString(R.string.install_file_manager), 
                    Toast.LENGTH_SHORT).show();
        }
	}
	
	

}
