package cz.vutbr.fit.testmind;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

import cz.vutbr.fit.testmind.profile.TAMProfile;
import cz.vutbr.fit.testmind.editor.controls.TAMEAbstractControl.REQUEST_CODES;
import cz.vutbr.fit.testmind.editor.controls.TAMEOpenSaveControl;
import cz.vutbr.fit.testmind.editor.controls.TAMERootInitializeControl;
import cz.vutbr.fit.testmind.opensave.TestmindFilenameFilter;
import cz.vutbr.fit.testmind.other.Helper;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.sax.RootElement;
import android.support.v4.app.FragmentActivity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * activity for select file dor open or save
 * @author jules
 *
 */
public class OpenSaveActivity extends FragmentActivity
{    
    public static final String INTENT_ID_MODE = "cz.vutbr.fit.testmind.opensave.mode";
    public static final String INTENT_ID_RESULT = "cz.vutbr.fit.testmind.opensave.result";

    /**
     *  create activity 
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {       
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_save);
        
        this.getActionBar().setHomeButtonEnabled(true);        
        this.setTitle(R.string.app_name);
                
        loadFiles();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.activity_open_save, menu);
        return true;
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.open_save_context_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {        
        
        switch (item.getItemId()) {
		case android.R.id.home:
			cancelActivity();
			break;
		case R.id.open_save_acitivty_add:
			createMindMap();
			break;				
		default:
			cancelActivity();
			break;
		}
        
                
        return true;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {    	
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId())
        {
            case R.id.open_save_activity_context_delete:
                TextView textItem =(TextView)info.targetView;
                deleteMap(textItem.getText().toString());
                return true;
            case R.id.open_save_activity_context_share:
            	String fileName = ((TextView)info.targetView).getText().toString();
                shareMindMap(fileName);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    
	/**
     * click listener for listview
     */
    AdapterView.OnItemClickListener listViewClickHandler = new AdapterView.OnItemClickListener()
    {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            TextView item =(TextView)parent.getChildAt(position);
            String itemText = item.getText().toString();

            finalizeActivity(itemText);
        }
    };
    
    /**
     * load filenames to listview
     */
    private void loadFiles()
    {
        FilenameFilter filter = new TestmindFilenameFilter();
        
        String[] files = TAMProfile.TESTMIND_DIRECTORY.list(filter);
        Arrays.sort(files);
        int count = files.length;
        
        if(count == 0)
        {
            Toast.makeText(this, R.string.open_save_error_any_file, Toast.LENGTH_LONG).show();
            //cancelActivity();           
            
        }
        
        for(int i=0; i < count; i++)
        {
            files[i] = files[i].substring(0, files[i].length()-3);
        }        
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                                                                android.R.layout.simple_list_item_1,
                                                                android.R.id.text1,
                                                                files);

        ListView listView = (ListView) findViewById(R.id.listView_files);
        listView.setAdapter(adapter);        
        listView.setOnItemClickListener(listViewClickHandler);
        
        this.registerForContextMenu(listView);
    }
    
    /**
     * return name of selected file
     * @param file
     */
    private void finalizeActivity(String file)
    {
        Intent intent = new Intent();        
        intent.putExtra(INTENT_ID_RESULT, file);
        setResult(Activity.RESULT_OK, intent);
        finish();    
    }
    
    /**
     * end activity when cancel
     */
    private void cancelActivity()
    {
        Intent intent = new Intent();       
        setResult(Activity.RESULT_CANCELED, intent);
        finish();         
    }
    
    private void shareMindMap(String fileName) {  	
    	
    	
    	Intent shareIntent = Helper.createShareMapChooserIntent(fileName);		
		
    	startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
	}

	private void createMindMap() {
		// zmaze profil a vytovri znovu aplikacie		
		SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.clear();		
		editor.commit();
		TAMERootInitializeControl.initControl = true;
		
		setResult(REQUEST_CODES.NEW_MAP);
		MainActivity.getProfile().reset();
		//Intent mainActivity = new Intent(this,MainActivity.class);
		finish();
		//startActivity(mainActivity);
		
	}
	
	/**
	 * delete file with mind map
	 * @param name
	 */
	private void deleteMap(String name)
	{
	    String path = String.format("%s/%s.%s", 
	    		TAMProfile.TESTMIND_DIRECTORY.getPath(), 
	    		name,TAMEOpenSaveControl.TESTMIND_FILE_EXTENSION);
	    File file = new File(path);
	    file.delete();
	    
	    loadFiles();
	}
}