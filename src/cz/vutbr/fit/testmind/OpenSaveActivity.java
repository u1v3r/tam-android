package cz.vutbr.fit.testmind;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.Arrays;

import cz.vutbr.fit.testmind.profile.TAMProfile;
import cz.vutbr.fit.testmind.editor.controls.TAMENodeControl;
import cz.vutbr.fit.testmind.editor.controls.TAMEOpenSaveControl;
import cz.vutbr.fit.testmind.editor.controls.TAMEAbstractControl.REQUEST_CODES;
import cz.vutbr.fit.testmind.opensave.TestmindFilenameFilter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
    private enum ActivityMode {OPEN, SAVE};
    
    public static final String INTENT_ID_MODE = "cz.vutbr.fit.testmind.opensave.mode";
    public static final String INTENT_ID_RESULT = "cz.vutbr.fit.testmind.opensave.result";

    private static ActivityMode mode;
    
    private MenuItem menuOK;
    private MenuItem menuCancel;
    private EditText editTextFile;
    
    /**
     *  create activity 
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {       
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_save);
        
        Bundle b = getIntent().getExtras();
        int which = b.getInt(INTENT_ID_MODE);
        
        editTextFile = (EditText) findViewById(R.id.editText_file_name);;
        
        if(which == TAMEOpenSaveControl.REQUEST_CODES.OPEN)
        {
            mode = ActivityMode.OPEN;
            LinearLayout firstLine = (LinearLayout) findViewById(R.id.linearLayout_open_save);
            firstLine.setVisibility(View.GONE);
            this.setTitle(R.string.menu_open);
        }
        else if(which == TAMEOpenSaveControl.REQUEST_CODES.SAVE)
        {
            mode = ActivityMode.SAVE;
            this.setTitle(R.string.menu_save);
        }
        
        loadFiles();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.activity_open_save, menu);

        menuOK = menu.findItem(R.id.open_save_acitivty_ok);
        menuCancel = menu.findItem(R.id.open_save_acitivty_cancel);
        
        if(mode == ActivityMode.OPEN)
        {
            menuOK.setVisible(false);
        }
        
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if(menuOK.getItemId() == id)
        {
            String text = editTextFile.getText().toString();
            if(text.isEmpty())
            {
                Toast.makeText(this, R.string.open_save_error_empty_file, Toast.LENGTH_LONG).show();
            }
            else
            {
                finalizeActivity(text);
            }
        }
        else if(menuCancel.getItemId() == id)
        {
            cancelActivity();
        }
             
        return true;
    }
    
    /**
     * listener for listview
     */
    AdapterView.OnItemClickListener listViewHandler = new AdapterView.OnItemClickListener()
    {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            TextView item =(TextView)parent.getChildAt(position);
            String itemText = item.getText().toString();

            if(mode == ActivityMode.OPEN)
            {
                finalizeActivity(itemText);
            }
            else if(mode == ActivityMode.SAVE)
            {
                
                editTextFile.setText(itemText);
            }
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
            cancelActivity();
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
        listView.setOnItemClickListener(listViewHandler);
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
}