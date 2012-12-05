package cz.vutbr.fit.testmind.editor.controls;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.preference.PreferenceManager.OnActivityResultListener;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import cz.vutbr.fit.testmind.MainActivity;
import cz.vutbr.fit.testmind.R;
import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.io.ImportFile;

/**
 * 
 * Class for import / export
 *
 */
public class TAMEIOControl extends TAMEAbstractControl implements ITAMMenuListener,OnActivityResultListener{
	
	/**
	 * constants of states
	 */
	private static final String INTENT_MIME_TYPE = "text/xml";
	public static final int RESULT_OK = -1;

	/** Constructor for reference to editor
	 * 
	 * @param editor
	 */
	public TAMEIOControl(ITAMEditor editor) {
		super(editor);
		editor.getListOfMenuControls().add(this);
		editor.getListOfOnActivityResultControls().add(this);
	}

	
	/** Listener of menu - import/export
	 * 
	 * @param item
	 */
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
		int id = item.getItemId();
		
		if(MainActivity.MenuItems.importFile == id){
			importFile();
		} else if(MainActivity.MenuItems.exportFile == id){
			exportFile();
		}
		
		return true;
			
	}

	/** Export File - behavior
	 * TODO
	 * 
	 */
	public void exportFile() {
		// TODO Auto-generated method stub
	}

	/** Import File - behavior
	 * 
	 */
	private void importFile() {
		// <Doèasný kod>
		/*
		try {
			Log.d("debug", "control->import Start");
			ImportFile importFile = new ImportFile(editor, ImportFile.FREE_MIND);
			Log.d("debug", "control->import End");
			//freeMind.getMindMap();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		// </Doèasný kod>
		
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT); 
        intent.setType(INTENT_MIME_TYPE); 
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            activity.startActivityForResult(            		
                    Intent.createChooser(intent,
                    					 editor.getResources().getString(R.string.select_file_to_import)
                    					), IMPORT_FILE);
            // editor.getProfile().setFileName(intent.getDataString());
        } catch (ActivityNotFoundException e) {
            
        	// answer: need install file manager
            Toast.makeText(editor.getContext(), 
            		editor.getResources().getString(R.string.install_file_manager), 
                    Toast.LENGTH_SHORT).show();
            editor.getProfile().setFileName(null);
        }
	}

	/** Listener of menu - import/export
	 * 
	 * @param item
	 */
	public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
		//Log.d("debug", "control->onActivity START");
    	if (requestCode == IMPORT_FILE && resultCode == RESULT_OK) {
    		//Log.d("TAMEIOControl", data.getData().getEncodedPath());
    		//editor.getProfile().setFileName(data.getData().getEncodedPath());
    		try {
    			//Log.d("debug", "control->import Start");
    			ImportFile importFile = new ImportFile(editor, data.getData().getEncodedPath(), ImportFile.FREE_MIND);
    			//Log.d("debug", "control->import End");
    			//freeMind.getMindMap();
    			return true;
    		} catch (XmlPullParserException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        }
        //Log.d("debug", "control->onActivity END");
        return false;
	}
	
   

}
