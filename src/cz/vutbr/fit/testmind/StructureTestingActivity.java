package cz.vutbr.fit.testmind;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import cz.vutbr.fit.testmind.editor.TAMEditorTest;
import cz.vutbr.fit.testmind.editor.controls.TAMEOpenSaveControl;
import cz.vutbr.fit.testmind.io.Serializer;
import cz.vutbr.fit.testmind.profile.TAMProfile;

public class StructureTestingActivity extends FragmentActivity {
	
	public static final int menuShow = R.id.menu_show_result;
	public static final int menuNext = R.id.menu_next_question;
	
	public static TAMEditorTest editorTest;
	
	private static final String TAG = "StructureTestingActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_structure_testing);				
		initEditorTest();
	}

	private void initEditorTest() {
		editorTest = (TAMEditorTest) findViewById(R.id.acitity_test_tam_editor);
		editorTest.initialize(MainActivity.getProfile());
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {    	
    	getMenuInflater().inflate(R.menu.activity_structure_testing, menu);		
    	return true;
    } 
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	return editorTest.onOptionsItemSelected(item);    	
    }    
}

