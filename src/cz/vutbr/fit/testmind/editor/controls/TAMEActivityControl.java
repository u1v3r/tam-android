package cz.vutbr.fit.testmind.editor.controls;

import android.content.Intent;
import android.view.MenuItem;

import cz.vutbr.fit.testmind.MainActivity;
import cz.vutbr.fit.testmind.TestActivity;
import cz.vutbr.fit.testmind.editor.ITAMEditor;

/**
 *  class for control open other activity
 */
public class TAMEActivityControl extends TAMEAbstractControl implements ITAMMenuListener{

    private static final String TAG = "TAMEditorActivityControl";

    /**
     * constructor
     * @param editor
     */
    public TAMEActivityControl(ITAMEditor editor)
    {
        super(editor);
        editor.getListOfMenuControls().add(this);
    }

    /**
     * method for differnet menu items
     */
    public boolean onOptionsItemSelected(MenuItem item)
    {
        
        if(MainActivity.MenuItems.testing == item.getItemId())
        {
            openTestActivity();
        }
        
        return true;
            
    }

    /**
     * open testing activity
     */
    public void openTestActivity()
    {
        MainActivity mainActivityInstance = MainActivity.getMainActivityInstance();
        Intent i = new Intent(mainActivityInstance, TestActivity.class);
        mainActivityInstance.startActivity(i);
    }
}