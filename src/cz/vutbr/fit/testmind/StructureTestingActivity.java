package cz.vutbr.fit.testmind;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import cz.vutbr.fit.testmind.editor.TAMEditorTest;

public class StructureTestingActivity extends FragmentActivity {

	public static TAMEditorTest editorTest;
	public static final int menuShow = R.id.menu_show_result;
	public static final int menuNext = R.id.menu_next_question;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_structure_testing);				
		initEditorTest();
	}

	private void initEditorTest() {
		editorTest = (TAMEditorTest) findViewById(R.id.acitity_test_tam_editor);
		editorTest.initialize(MainActivity.getProfile());
		//MainActivity.setActualEditor(editorTest);
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
