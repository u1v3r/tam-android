package cz.vutbr.fit.testmind;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

import cz.vutbr.fit.testmind.profile.TAMProfile;
import cz.vutbr.fit.testmind.opensave.TestmindFilenameFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * activity for select file dor open or save
 * @author jules
 *
 */
public class OpenSaveActivity extends FragmentActivity
{
    private static final String testmindFilesFilter = "*.db"; 
    
    /**
     *  create activity 
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {       
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_save);
        
        Bundle b = getIntent().getExtras();
        String which = b.getString("cz.vutbr.fit.testmind.opensave.which");
        
        loadFiles();
    }
    
    private void loadFiles()
    {
        FilenameFilter filter = new TestmindFilenameFilter();
        
        String[] files = TAMProfile.TESTMIND_DIRECTORY.list(filter);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                                                                android.R.layout.simple_list_item_1,
                                                                android.R.id.text1,
                                                                files);

        ListView listView = (ListView) findViewById(R.id.listView_files);
        listView.setAdapter(adapter);         
    }
}