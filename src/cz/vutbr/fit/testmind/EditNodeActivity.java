package cz.vutbr.fit.testmind;

import java.io.Serializable;

import cz.vutbr.fit.testmind.MainActivity.EventObjects;
import cz.vutbr.fit.testmind.editor.controls.TAMEditorNodesControl;
import cz.vutbr.fit.testmind.editor.controls.TAMEditorNodesControl.BackgroundStyle;
import cz.vutbr.fit.testmind.editor.items.TAMENode;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class EditNodeActivity extends Activity {
	
	
	public final static class RadioButtonItems{
		public static final int GREEN = R.id.edit_node_radio1;
		public static final int BLUE = R.id.edit_node_radio2;
		public static final int RED = R.id.edit_node_radio3;
		public static final int PURPLE = R.id.edit_node_radio4;
	}

	private static final String TAG = "EditNodeActivity";
	
	private EditText title;
	private EditText body;
	private Button okBtn;
	private Button cancelBtn;
	private RadioButton radioButtonBlue;
	private RadioButton radioButtonRed;
	private RadioButton radioButtonGreen;
	private RadioButton radioButtonPurple;
	
	private TAMENode selectedNode;
	private BackgroundStyle color;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_edit_node);
		
		title = (EditText)findViewById(R.id.edit_node_title);
		body = (EditText)findViewById(R.id.edit_node_body);		
		radioButtonBlue = (RadioButton)findViewById(R.id.edit_node_radio2);
		radioButtonRed = (RadioButton)findViewById(R.id.edit_node_radio3); 
		radioButtonGreen = (RadioButton)findViewById(R.id.edit_node_radio1);
		radioButtonPurple = (RadioButton)findViewById(R.id.edit_node_radio4);
		okBtn = (Button)findViewById(R.id.edit_node_acitivty_ok);
		cancelBtn = (Button)findViewById(R.id.edit_node_acitivty_cancel);

		
		Intent intent = getIntent();		
		String titleString = intent.getStringExtra(TAMEditorNodesControl.NODE_TITLE);
		String bodyString = intent.getStringExtra(TAMEditorNodesControl.NODE_BODY);
				
		// prenasat farbu asi nebude treba		
		BackgroundStyle backgroundColor = (BackgroundStyle) intent.getSerializableExtra(TAMEditorNodesControl.NODE_COLOR);	
		
		title.setText(titleString);
		body.setText(bodyString);
		
		
		setRadioButtnColor(backgroundColor);
		
	}
	
    private void setRadioButtnColor(BackgroundStyle backgroundColor) {
    	
    	if(backgroundColor.equals(BackgroundStyle.BLUE)){
    		radioButtonBlue.setChecked(true);
    	}else if(backgroundColor.equals(BackgroundStyle.GREEN)){
    		radioButtonGreen.setChecked(true);
    	}else if(backgroundColor.equals(BackgroundStyle.RED)){
    		radioButtonRed.setChecked(true);  
    	}else if(backgroundColor.equals(BackgroundStyle.PURPLE)){
    		radioButtonPurple.setChecked(true);
    	}
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	getMenuInflater().inflate(R.menu.activity_edit_node, menu);
		
    	return true;
    }
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {    	
    	
    	if(item.getItemId() == R.id.edit_node_acitivty_ok){
    		saveValues();
    	}else if(item.getItemId() == R.id.edit_node_acitivty_cancel){
    		finish();
    	}
    	    	
		return super.onOptionsItemSelected(item);
		    	
    }
    
    private void saveValues() {
    	
    	String titleText = title.getText().toString();
    	String bodyText = body.getText().toString();
		
    	Intent intent = new Intent();
    	intent.putExtra(TAMEditorNodesControl.NODE_TITLE, titleText);
    	intent.putExtra(TAMEditorNodesControl.NODE_BODY, bodyText);
    	intent.putExtra(TAMEditorNodesControl.NODE_COLOR,color);
    	setResult(TAMEditorNodesControl.EDIT_NODE_RESULT_CODE, intent);
    	finish();
	}

	public void onSelectColorClicked(View view){
    	
    	boolean checked = ((RadioButton) view).isChecked();
        
    	switch(view.getId()) {
	        case RadioButtonItems.BLUE:
	            if (checked) color = BackgroundStyle.BLUE;              
	            break;
	        case RadioButtonItems.RED:
	        	if (checked) color = BackgroundStyle.RED;               
	            break;
	        case RadioButtonItems.GREEN:
	        	if (checked) color = BackgroundStyle.GREEN;               
	            break;
	        case RadioButtonItems.PURPLE:
	        	if (checked) color = BackgroundStyle.PURPLE;             
	            break;
	    }	
    }
}
