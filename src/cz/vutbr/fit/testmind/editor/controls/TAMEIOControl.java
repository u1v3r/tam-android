package cz.vutbr.fit.testmind.editor.controls;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.preference.PreferenceManager.OnActivityResultListener;
import android.view.MenuItem;
import android.widget.Toast;
import cz.vutbr.fit.testmind.MainActivity;
import cz.vutbr.fit.testmind.R;
import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.io.ImportFile;
import cz.vutbr.fit.testmind.io.Serializer;
import cz.vutbr.fit.testmind.other.Helper;

/**
 * 
 * Class for import / export
 *
 */
public class TAMEIOControl extends TAMEAbstractControl implements ITAMMenuListener,OnActivityResultListener{
	
	/**
	 * constants of states
	 */
	private static final String INTENT_MIME_TYPE = "*/*";	
	private static final String TAG = "TAMEIOControl";

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
		} else if(MainActivity.MenuItems.shareFile == id){
			shareFile();
		}
		
		return true;
			
	}

	/** Export File - behavior
	 * TODO
	 * 
	 */
	public void shareFile() {
			
		if(editor.getProfile().getFileName() == null) return;
		if(editor.getProfile().getFileName() == "") return;
		
		saveMindMap();
		
		Intent shareIntent = Helper.createShareMapChooserIntent(editor.getProfile().getFileName());		
		activity.startActivity(Intent.createChooser(shareIntent, activity.getResources().getText(R.string.send_to)));
	}

	/** Import File - behavior
	 * 
	 */
	private void importFile() {
		// <temporaly code>
		/*
		try {
			Log.d("debug", "control->import Start");
			ImportFile importFile = new ImportFile(editor, null, ImportFile.FREE_MIND);
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
		// </temporaly code>

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
		if (requestCode == IMPORT_FILE && resultCode == Activity.RESULT_OK) {
			//Log.d("TAMEIOControl", data.getData().getEncodedPath());
			//editor.getProfile().setFileName(data.getData().getEncodedPath());

			//Log.d("debug", "control->import Start");

			String filepath = data.getDataString();
			String extension = filepath.substring(filepath.lastIndexOf(".") + 1);
			String encodedPath = data.getData().getEncodedPath();
			
			if(encodedPath == null) return true;
						
			if(extension.equals(TAMEOpenSaveControl.TESTMIND_FILE_EXTENSION)){
				return importTestMind(encodedPath);
			}else{    			
				return importFreeMind(encodedPath);	    			
			}

		}
		//Log.d("debug", "control->onActivity END");
        return false;
	}


	private boolean importTestMind(String encodedPath) {
		
		//Log.d(TAG, "encode path: " + encodedPath);
		
		String fileName = encodedPath.substring(encodedPath.lastIndexOf("/")+1, encodedPath.lastIndexOf("."));
		
		//Log.d(TAG, "fileName: " + fileName);
		
		editor.getProfile().setFileName(fileName);           
        
        Serializer serializer = new Serializer(encodedPath);
        
        serializer.deserialize(MainActivity.getProfile());           
        
        editor.invalidate();
        
        return true;
	}


	private boolean importFreeMind(String encodedPath) {
		
		try {			
			ImportFile importFile = new ImportFile(editor, encodedPath, ImportFile.FREE_MIND);
			saveMindMap();
			return true;
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return false;
		}
		//Log.d("debug", "control->import End");
		//freeMind.getMindMap();
		
	}
}
