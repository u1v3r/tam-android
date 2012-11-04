package cz.vutbr.fit.testmind;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView.BufferType;

import com.commonsware.cwac.richedit.RichEditText;

import cz.vutbr.fit.testmind.editor.controls.TAMEditorNodeControl;
import cz.vutbr.fit.testmind.editor.controls.TAMEditorNodeControl.BackgroundStyle;
import cz.vutbr.fit.testmind.editor.items.TAMENode;
import cz.vutbr.fit.testmind.fragments.RichTextEditorFragment;

public class EditNodeActivity extends FragmentActivity implements AnimationListener {
	
	
	public final static class RadioButtonItems{
		public static final int GREEN = R.id.edit_node_radio1;
		public static final int BLUE = R.id.edit_node_radio2;
		public static final int RED = R.id.edit_node_radio3;
		public static final int PURPLE = R.id.edit_node_radio4;
	}

	private static final String TAG = "EditNodeActivity";
	
	private EditText title;
	private Button okBtn;
	private Button cancelBtn;
	private RadioButton radioButtonBlue;
	private RadioButton radioButtonRed;
	private RadioButton radioButtonGreen;
	private RadioButton radioButtonPurple;
	
	private TAMENode selectedNode;
	private BackgroundStyle color;

	private LinearLayout formatToolbar;

	private Animation animFromRight;
	private Animation animFromLeft;

	private RichEditText editor;

	private ImageButton boldBtn;

	private ImageButton italicBtn;

	private ImageButton underlineBtn;

	private ImageButton tagBtn;

	private ImageButton hideBtn;

	private boolean isToolbarVisible = false;
	private boolean firstAnimation = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// prijme intent z main acitivity
		Intent intent = getIntent();
		setContentView(R.layout.activity_edit_node);
		
		/* acitity buttons */
		title = (EditText)findViewById(R.id.edit_node_title);			
		radioButtonBlue = (RadioButton)findViewById(R.id.edit_node_radio2);
		radioButtonRed = (RadioButton)findViewById(R.id.edit_node_radio3); 
		radioButtonGreen = (RadioButton)findViewById(R.id.edit_node_radio1);
		radioButtonPurple = (RadioButton)findViewById(R.id.edit_node_radio4);
		okBtn = (Button)findViewById(R.id.edit_node_acitivty_ok);
		cancelBtn = (Button)findViewById(R.id.edit_node_acitivty_cancel);
			
		String titleString = intent.getStringExtra(TAMEditorNodeControl.NODE_TITLE);		
		BackgroundStyle backgroundColor = (BackgroundStyle) intent.getSerializableExtra(TAMEditorNodeControl.NODE_COLOR);	
		CharSequence bodyText = intent.getCharSequenceExtra(TAMEditorNodeControl.NODE_BODY);
		title.setText(titleString);		
		
		setRadioButtonColor(backgroundColor);
				
		getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		
		/* natavenie textu pre editor */
	    editor = (RichEditText)findViewById(R.id.edit_node_text_node_text);
	    editor.setText(new SpannableString(bodyText), BufferType.SPANNABLE);	    
	    
	    // v action menu zobrazi formatovaci button
	    editor.enableActionModes(false);
	    
	    /* toolbar buttons */
	    boldBtn = (ImageButton)findViewById(R.id.edit_node_text_boldbtn);
	    italicBtn = (ImageButton)findViewById(R.id.edit_node_text_italicbtn);
	    underlineBtn = (ImageButton)findViewById(R.id.edit_node_text_underlinebtn);
	    tagBtn = (ImageButton)findViewById(R.id.edit_node_text_tagbtn);
		hideBtn = (ImageButton)findViewById(R.id.edit_node_hidebtn);
		
		/* animacia toolbar */
		formatToolbar = (LinearLayout)findViewById(R.id.edit_node_format_toolbar);
		animFromRight = AnimationUtils.loadAnimation(this, R.anim.anim_slide_from_right);
		animFromLeft = AnimationUtils.loadAnimation(this, R.anim.anim_slide_from_left);
				
		animFromRight.setAnimationListener(this);
		animFromLeft.setAnimationListener(this);
		
		formatToolbar.clearAnimation();
		formatToolbar.startAnimation(animFromRight);
		
	}
	
    private void setRadioButtonColor(BackgroundStyle backgroundColor) {
    	
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
    	Editable bodyText = editor.getEditableText();
    	    	   	
    	Intent intent = new Intent();    	
    	intent.putExtra(TAMEditorNodeControl.NODE_TITLE, titleText);
    	intent.putExtra(TAMEditorNodeControl.NODE_BODY, bodyText);
    	intent.putExtra(TAMEditorNodeControl.NODE_COLOR,color);
    	setResult(TAMEditorNodeControl.EDIT_NODE_RESULT_CODE, intent);
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
	
	public void onBoldBtnClick(View w){
		
	}
	
	public void onItalicBtnClick(View w){
	}
	
	public void onUnderlineBtnClick(View w){
		
	}
	
	public void onTagBtnClick(View w){
		
	}
	public void onHideBtnClick(View w){
		
		if(isToolbarVisible){
			formatToolbar.clearAnimation();
			formatToolbar.startAnimation(animFromLeft);
						
		} else {
			formatToolbar.clearAnimation();
			formatToolbar.startAnimation(animFromRight);			
			
		}		
	}

	public void onAnimationStart(Animation animation) {
		
		if(isToolbarVisible == false){
			boldBtn.setVisibility(View.VISIBLE);
			italicBtn.setVisibility(View.VISIBLE);
			underlineBtn.setVisibility(View.VISIBLE);
			tagBtn.setVisibility(View.VISIBLE);			
		}
	}

	public void onAnimationEnd(Animation animation) {
				
		if(isToolbarVisible){
			boldBtn.setVisibility(View.GONE);
			italicBtn.setVisibility(View.GONE);
			underlineBtn.setVisibility(View.GONE);
			tagBtn.setVisibility(View.GONE);
			isToolbarVisible = false;			
		}else {
			isToolbarVisible = true;
		}
		
	}

	public void onAnimationRepeat(Animation animation) {
		
		
	}
}
