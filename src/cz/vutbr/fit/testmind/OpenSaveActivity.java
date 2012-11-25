package cz.vutbr.fit.testmind;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

import cz.vutbr.fit.testmind.profile.TAMProfile;
import cz.vutbr.fit.testmind.editor.controls.TAMEOpenSaveControl;
import cz.vutbr.fit.testmind.opensave.TestmindFilenameFilter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
        this.setTitle(R.string.menu_open);
                
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
		case R.id.open_save_acitivty_share:
			shareMindMap();
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
            case R.id.delete:
                TextView textItem =(TextView)info.targetView;
                deleteMap(textItem.getText().toString());
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
    
    private void shareMindMap() {
		//TODO tu niekedy v buducnosti bude zdielanie
		
	}

	private void createMindMap() {
		/* TODO tu by mala byt logika, ktora po kliknuti najskor ulozi cele aktualne platno,
		 * nasledne ho vycisti a prepne do MainAcitivty a uzivatel moze vytvarat novu mapu 
		 */
    	
		
	}
	
	/**
	 * delete file with mind map
	 * @param name
	 */
	private void deleteMap(String name)
	{
	    String path = String.format("%s/%s.db", TAMProfile.TESTMIND_DIRECTORY.getPath(), name);
	    File file = new File(path);
	    file.delete();
	    
	    loadFiles();
	}
}