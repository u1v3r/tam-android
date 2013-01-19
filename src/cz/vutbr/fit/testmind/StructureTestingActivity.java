package cz.vutbr.fit.testmind;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import cz.vutbr.fit.testmind.editor.TAMEditorTest;

public class StructureTestingActivity extends FragmentActivity {

	public static TAMEditorTest editorTest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_structure_testing);
		
		
		editorTest = (TAMEditorTest) findViewById(R.id.acitity_test_tam_editor);
		editorTest.initialize(MainActivity.getProfile());
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	
    	
    	
    	getMenuInflater().inflate(R.menu.activity_structure_testing, menu);		
    	return true;
    }

}
