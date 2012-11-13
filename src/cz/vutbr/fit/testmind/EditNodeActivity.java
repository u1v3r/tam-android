package cz.vutbr.fit.testmind;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView.BufferType;

import com.commonsware.cwac.richedit.Effect;
import com.commonsware.cwac.richedit.RichEditText;
import com.commonsware.cwac.richedit.RichEditText.OnSelectionChangedListener;

import cz.vutbr.fit.testmind.editor.controls.TAMENodeControl;
import cz.vutbr.fit.testmind.editor.controls.TAMENodeControl.BackgroundStyle;


public class EditNodeActivity extends FragmentActivity implements AnimationListener, 
	OnSelectionChangedListener,TextWatcher {
	
	
	public final static class RadioButtonItems{
		public static final int GREEN = R.id.edit_node_radio1;
		public static final int BLUE = R.id.edit_node_radio2;
		public static final int RED = R.id.edit_node_radio3;
		public static final int PURPLE = R.id.edit_node_radio4;
	}

	private static final String TAG = "EditNodeActivity";
	
	private EditText title;
	private RadioButton radioButtonBlue;
	private RadioButton radioButtonRed;
	private RadioButton radioButtonGreen;
	private RadioButton radioButtonPurple;
		
	private BackgroundStyle color;

	private LinearLayout formatToolbar;

	private Animation animFromRight;
	private Animation animFromLeft;

	private RichEditText richTextEditor;
	private ImageButton boldBtn;
	private ImageButton italicBtn;
	private ImageButton underlineBtn;
	private ImageButton tagBtn;
	private ImageButton hideBtn;

	private boolean isToolbarVisible = false;

	private boolean isSelectionBold = false;
	private boolean isSelectionItalic = false;
	private boolean isSelectionUnderline = false;
	
	private boolean isBoldBtnActive = false;
	private boolean isItalicBtnActive = false;
	private boolean isUnderlineBtnActive = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_edit_node);
				
		/* acitity buttons */
		title = (EditText)findViewById(R.id.edit_node_title);			
		radioButtonBlue = (RadioButton)findViewById(R.id.edit_node_radio2);
		radioButtonRed = (RadioButton)findViewById(R.id.edit_node_radio3); 
		radioButtonGreen = (RadioButton)findViewById(R.id.edit_node_radio1);
		radioButtonPurple = (RadioButton)findViewById(R.id.edit_node_radio4);		
		
		/* prijme intent z main acitivity */
		Intent intent = getIntent();
		String titleString = intent.getStringExtra(TAMENodeControl.NODE_TITLE);		
		BackgroundStyle backgroundColor = (BackgroundStyle) intent.getSerializableExtra(TAMENodeControl.NODE_COLOR);	
		String bodyText = intent.getStringExtra(TAMENodeControl.NODE_BODY);
		
		/* nastavenie titulku */
		title.setText(titleString);		
		
		/* nastavenie radio button pre zvolenie farby */
		setRadioButtonColor(backgroundColor);
				
		getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		
		/* nastavenie animacie pre toolbar */
		formatToolbar = (LinearLayout)findViewById(R.id.edit_node_format_toolbar);
		animFromRight = AnimationUtils.loadAnimation(this, R.anim.anim_slide_from_right);
		animFromLeft = AnimationUtils.loadAnimation(this, R.anim.anim_slide_from_left);
				
		animFromRight.setAnimationListener(this);
		animFromLeft.setAnimationListener(this);
				
		/* natavenie textu pre editor */
	    richTextEditor = (RichEditText)findViewById(R.id.edit_node_text_node_text);
	    richTextEditor.setText(new SpannableString(Html.fromHtml(bodyText)), BufferType.SPANNABLE);
	    richTextEditor.setOnSelectionChangedListener(this);
	    richTextEditor.addTextChangedListener(this);
	    
	    title.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			public void onFocusChange(View v, boolean hasFocus) {				
				if(hasFocus){
					formatToolbar.setVisibility(View.GONE);
				}
			}
		});
	    
	    // zobrazit toolbar az ked ziska focus
	    richTextEditor.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					formatToolbar.setVisibility(View.VISIBLE);
					formatToolbar.clearAnimation();
					formatToolbar.startAnimation(animFromRight);
				}
			}
		});
	    
	    
	    // v action menu (ne)zobrazi formatovaci button
	    richTextEditor.enableActionModes(false);
	    
	    /* toolbar buttons */
	    boldBtn = (ImageButton)findViewById(R.id.edit_node_text_boldbtn);
	    italicBtn = (ImageButton)findViewById(R.id.edit_node_text_italicbtn);
	    underlineBtn = (ImageButton)findViewById(R.id.edit_node_text_underlinebtn);
	    tagBtn = (ImageButton)findViewById(R.id.edit_node_text_tagbtn);
		hideBtn = (ImageButton)findViewById(R.id.edit_node_hidebtn);		
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
    	String bodyText = Html.toHtml(richTextEditor.getEditableText());
    	
    	Intent intent = new Intent();    	
    	intent.putExtra(TAMENodeControl.NODE_TITLE, titleText);
    	intent.putExtra(TAMENodeControl.NODE_BODY, bodyText);
    	intent.putExtra(TAMENodeControl.NODE_COLOR,color);
    	setResult(TAMENodeControl.EDIT_NODE_RESULT_CODE, intent);
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
		
		// ma dva stavy
		if(isBoldBtnActive){
			isBoldBtnActive = false;
		}else{
			isBoldBtnActive = true;
		}
		
		setSelectionBooleanEffect(RichEditText.BOLD, isSelectionBold);
		
		// umozni prepinat efekt ked je text zvyrazneny
		if(isSelectionBold){			
			isSelectionBold = false;
		}else{
			isSelectionBold = true;
		}
	}
	
	public void onItalicBtnClick(View w){
		setSelectionBooleanEffect(RichEditText.ITALIC, isSelectionItalic);

		// umozni prepinat efekt ked je text zvyrazneny
		if(isSelectionItalic){			
			isSelectionItalic = false;
		}else{
			isSelectionItalic = true;
		}
	}

	public void onUnderlineBtnClick(View w){
		setSelectionBooleanEffect(RichEditText.UNDERLINE, isSelectionUnderline);

		// umozni prepinat efekt ked je text zvyrazneny
		if(isSelectionUnderline){			
			isSelectionUnderline = false;
		}else{
			isSelectionUnderline = true;
		}
	}
	
	private void setSelectionBooleanEffect(Effect<Boolean> effect, boolean isSelectionEnabled){
		if(isSelectionEnabled){
			richTextEditor.applyEffect(effect, false);
		}else{
			richTextEditor.applyEffect(effect, true);
		}
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
			showToolbar();				
		}
	}

	private void showToolbar() {
		
		hideBtn.setVisibility(View.VISIBLE);
		boldBtn.setVisibility(View.VISIBLE);
		italicBtn.setVisibility(View.VISIBLE);
		underlineBtn.setVisibility(View.VISIBLE);
		tagBtn.setVisibility(View.VISIBLE);		
	}

	public void onAnimationEnd(Animation animation) {
				
		if(isToolbarVisible){
			hideToolbar();			
			isToolbarVisible = false;			
		}else {
			isToolbarVisible = true;
		}
		
	}

	private void hideToolbar() {
		
		boldBtn.setVisibility(View.GONE);
		italicBtn.setVisibility(View.GONE);
		underlineBtn.setVisibility(View.GONE);
		tagBtn.setVisibility(View.GONE);
	}
	
	

	public void onAnimationRepeat(Animation animation) {
		// no op		
	}

	public void onSelectionChanged(int start, int end, List<Effect<?>> effects) {
				
		isSelectionBold = false;
		isSelectionItalic = false;
		isSelectionUnderline = false;
		
		for (Effect<?> effect : effects) {
			
			if(effect.equals(RichEditText.BOLD)){
				// TODO: zmenit stav BOLD button
				isSelectionBold = true;
			}
			
			if(effect.equals(RichEditText.ITALIC)){
				// TODO: zmenit stav ITALIC button
				isSelectionItalic = true;
			}
					
			if(effect.equals(RichEditText.UNDERLINE)){
				// TODO: zmenit stav UNDERLINE button
				isSelectionUnderline = true;
			}
		
		}
		
		
		Log.d(TAG,"bold " + isSelectionBold + ",italic " 
				+ isSelectionItalic + ", under " + isSelectionUnderline);
		
	}

	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {		
		
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		/*
		if(isBoldBtnActive){
			Log.d(TAG, "start " + start + ",before " + before + ", count " + count + ",chars " + s);
			
			
				
			CharSequence charsBefore = s.subSequence(0,s.length()-1);			
			CharSequence lastChar = s.subSequence(s.length()-1, s.length());
			
			Log.d(TAG, charsBefore + " " + lastChar);
			
			
			SpannableString lastCharSpan = new SpannableString(lastChar);
			lastCharSpan.setSpan(new StyleSpan(Typeface.BOLD), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			
			
			
		}
		*/
	}

	public void afterTextChanged(Editable s) {	
		
	}
}
