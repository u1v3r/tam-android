package cz.vutbr.fit.testmind;

import cz.vutbr.fit.testmind.MainActivity.EventObjects;
import cz.vutbr.fit.testmind.editor.items.TAMENode;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
	
	private EditText title;
	private EditText body;
	private Button okBtn;
	private Button cancelBtn;
	private RadioButton radioButtonBlue;
	private RadioButton radioButtonRed;
	private RadioButton radioButtonGreen;
	private RadioButton radioButtonPurple;
	
	private TAMENode selectedNode;
	private int color = 0;

	
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
		String titleString = intent.getStringExtra(MainActivity.NODE_TITLE);
		String bodyString = intent.getStringExtra(MainActivity.NODE_BODY);
				
		// prenasat farbu asi nebude treba
		//int backgroundColor = (int)Long.parseLong(intent.getStringExtra(MainActivity.NODE_COLOR),16);	
		
		title.setText(titleString);
		body.setText(bodyString);
		
		//setRadioButtnColor(backgroundColor);
	}
	
    private void setRadioButtnColor(int backgroundColor) {
    	
    	switch(backgroundColor) {
	        case RadioButtonItems.BLUE:
	            radioButtonBlue.setSelected(true);	        	              
	            break;
	        case RadioButtonItems.RED:
	        	radioButtonRed.setSelected(true);              
	            break;
	        case RadioButtonItems.GREEN:
	        	radioButtonGreen.setSelected(true);
	            break;
	        case RadioButtonItems.PURPLE:
	        	radioButtonPurple.setSelected(true);
	            break;
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
    		finishActivity(RESULT_CANCELED);
    		
    	}
    	    	
		return super.onOptionsItemSelected(item);
		    	
    }
    
    private void saveValues() {
    	
    	String titleText = title.getText().toString();
    	String bodyText = body.getText().toString();
		
    	Intent intent = new Intent();
    	intent.putExtra(MainActivity.NODE_TITLE, titleText);
    	intent.putExtra(MainActivity.NODE_BODY, bodyText);
    	
    	setResult(MainActivity.EDIT_NODE_RESULT_CODE, intent);
    	finish();
	}

	public void onSelectColorClicked(View view){
    	
    	boolean checked = ((RadioButton) view).isChecked();
        
    	switch(view.getId()) {
	        case RadioButtonItems.BLUE:
	            if (checked) color = R.color.node_background_1;              
	            break;
	        case RadioButtonItems.RED:
	        	if (checked) color = R.color.node_background_3;               
	            break;
	        case RadioButtonItems.GREEN:
	        	if (checked) color = R.color.node_background_2;               
	            break;
	        case RadioButtonItems.PURPLE:
	        	if (checked) color = R.color.node_background_4;             
	            break;
	    }	
    }
}
