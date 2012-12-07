package cz.vutbr.fit.testmind;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
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
import android.widget.Toast;

import com.commonsware.cwac.richedit.Effect;
import com.commonsware.cwac.richedit.RichEditText;
import com.commonsware.cwac.richedit.RichEditText.OnSelectionChangedListener;

import cz.vutbr.fit.testmind.editor.controls.TAMENodeControl;
import cz.vutbr.fit.testmind.editor.items.ITAMENode;
import cz.vutbr.fit.testmind.profile.Tag;


public class EditNodeActivity extends FragmentActivity implements AnimationListener, 
	OnSelectionChangedListener,TextWatcher {
	
	
	public final static class RadioButtonItems{
		public static final int GREEN = R.id.edit_node_radio1;
		public static final int BLUE = R.id.edit_node_radio2;
		public static final int RED = R.id.edit_node_radio3;
		public static final int PURPLE = R.id.edit_node_radio4;
	}

	private static final String TAG = "EditNodeActivity";

	/**
	 * Udava minimalnu dlzku, ktoru musi mat vybrane slovo aby bolo ulozene ako TAG
	 */
	private static final int TAG_MIN_LENGTH = 3;

	/**
	 * Znak, ktory sa zobrazi pred kazdym tagom
	 */
	private static final String TAG_MARKER = "#";	
	private static final String TAG_TYPEFACE = "serif";
	private static final int TAG_FOREGROUND_COLOR = android.R.color.holo_purple;
	
	private EditText title;
	private RadioButton radioButtonBlue;
	private RadioButton radioButtonRed;
	private RadioButton radioButtonGreen;
	private RadioButton radioButtonPurple;
		
	private int color;

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

	/**
	 * Obsahuje vsetky taky prijate cez Intent
	 */
	private List<Tag> listOfTags;
	
	/**
	 * Obsahuje len lokalne vytvorene tagy
	 */
	private List<Tag> listOfLocalTags;
	
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
		int backgroundColor = intent.getIntExtra(TAMENodeControl.NODE_COLOR, 0);	
		String bodyText = intent.getStringExtra(TAMENodeControl.NODE_BODY);		
		listOfTags = (List<Tag>) intent.getSerializableExtra(TAMENodeControl.NODE_TAGS);
		listOfLocalTags = new ArrayList<Tag>();
		
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
	
    private void setRadioButtonColor(int backgroundColor) {
    	
    	switch (backgroundColor) {
			case ITAMENode.BLUE:
				radioButtonBlue.setChecked(true);
				break;
			case ITAMENode.GREEN:
				radioButtonGreen.setChecked(true);
				break;
			case ITAMENode.RED:
				radioButtonRed.setChecked(true);
				break;
			case ITAMENode.PURPLE:
				radioButtonPurple.setChecked(true);
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
    		finish();
    	}
    	    	
		return super.onOptionsItemSelected(item);
		    	
    }
    
    private void saveValues() {
    	
    	String titleText = title.getText().toString();
    	String bodyText = Html.toHtml(richTextEditor.getEditableText());
    	Set<Tag> setOfTags = new HashSet<Tag>();
    	
    	int indexOfStart = 0;
    	
    	setOfTags.addAll(listOfLocalTags);
    	setOfTags.addAll(listOfTags);
    	listOfTags.clear();
    	listOfLocalTags.clear();
    	
    	// spoj vsetky tagy (lokalne pridane + existujuce)
    	
    	
    	for (Tag localTag : setOfTags) {
			bodyText = bodyText.replace(TAG_MARKER + localTag.getTag(), "<tag>" + TAG_MARKER + localTag.getTag() + "</tag>");
			indexOfStart = bodyText.indexOf(TAG_MARKER + localTag.getTag());
			localTag.setStart(indexOfStart);
			localTag.setEnd(indexOfStart + localTag.getTag().length());
			listOfTags.add(localTag);
			Log.d(TAG,localTag.toString());
		}
    	
    	//Log.d(TAG, bodyText);
    	
    	
    	Intent intent = new Intent();    	
    	intent.putExtra(TAMENodeControl.NODE_TITLE, titleText);
    	intent.putExtra(TAMENodeControl.NODE_BODY, bodyText);
    	intent.putExtra(TAMENodeControl.NODE_COLOR,color);
    	    	
    	//Log.d(TAG, "vsetky:" + listOfTags.toString());
    	
    	intent.putExtra(TAMENodeControl.NODE_TAGS, (Serializable)(listOfTags));
    	setResult(TAMENodeControl.EDIT_NODE_RESULT_CODE, intent);
    	finish();
    	
	}

	public void onSelectColorClicked(View view){
    	
    	boolean checked = ((RadioButton) view).isChecked();
        
    	if(checked) {
    		switch(view.getId()) {
	        case RadioButtonItems.BLUE:
	            color = ITAMENode.BLUE;              
	            break;
	        case RadioButtonItems.RED:
	        	color = ITAMENode.RED;               
	            break;
	        case RadioButtonItems.GREEN:
	        	color = ITAMENode.GREEN;               
	            break;
	        case RadioButtonItems.PURPLE:
	        	color = ITAMENode.PURPLE;             
	            break;
	    }
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
		
		//Log.d(TAG, "obsahuje:" + listOfTags.toString());
		
		int start = richTextEditor.getSelectionStart();
		//int startTag = start;
		int end = richTextEditor.getSelectionEnd();
		//boolean hasMarkerPrefix = false;// urcuje si bol vybraty aj tag marker
		
		// ake je spravne dlzka
		if(( end - start) < TAG_MIN_LENGTH ) return;
		
		String tagString = (richTextEditor.getText().toString().substring(start, end)).trim();
		
		//ako tag moze byt len jedno slovo
		if(tagString.contains(" ")){
			Toast.makeText(
					this, 
					getResources().getString(R.string.edit_activity_more_words_selected), 
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		/*
		if(tagString.startsWith(TAG_MARKER)){
			hasMarkerPrefix = true;
			tagString = tagString.substring(TAG_MARKER.length());// z tagu odstran tag marker
		}else{
			// ak vyber neobsahuje prefix tak treba posunut start o -1
			startTag = startTag - 1;
		}
		*/
		
		String textInside = richTextEditor.getText().toString().substring(start, start+(TAG_MARKER.length()));
		
		if(textInside.equals(TAG_MARKER)){
			tagString = tagString.substring(TAG_MARKER.length());// z tagu odstran tag marker
		}
		
		Tag tag = new Tag(tagString);		
			
		boolean containsListOfTags = listOfTags.contains(tag);
		boolean containsListOfLocalTags = listOfLocalTags.contains(tag);
				
		// ak uzivatel vybral uz existujuci tak ho odstran
		if(containsListOfTags || containsListOfLocalTags){
			
			String textBefore = richTextEditor.getText().toString().substring(start-(TAG_MARKER.length()), start);
			
			
			// ak je vybrany text ktory uz je ulozeny ako tag a nie je pred nim alebo v nom TAG_MARKER
			if(!textBefore.equals(TAG_MARKER) && !textInside.equals(TAG_MARKER)){
				Toast.makeText(this, getResources().getString(R.string.edit_activity_tag_exists), Toast.LENGTH_SHORT).show();
				return;
			}
			
			
			// ak je zmena len lokalna odstran z lokalnych inak zo vsetkych			
			if(containsListOfTags){				
				listOfTags.remove(tag);
			}else{
				listOfLocalTags.remove(tag);						
			}
			
						
			// ak nebol vybrany tag marker ale je to tag, treba rozsirit vyber
			//if(!hasMarkerPrefix) start = startTag;
			
			richTextEditor.setSelection(start, end);
			richTextEditor.applyEffect(RichEditText.BOLD, false);
			richTextEditor.applyEffect(RichEditText.ITALIC, false);
			richTextEditor.applyEffect(RichEditText.UNDERLINE, false);
			richTextEditor.applyEffect(RichEditText.TYPEFACE, "default");
			
			// odstrani font color z textu a taktiez <tag>
			ForegroundColorSpan[] span = 
					richTextEditor.getEditableText().getSpans(start, end, ForegroundColorSpan.class);
			
			if(span.length > 0){
				richTextEditor.getEditableText().replace(
						richTextEditor.getText().getSpanStart(span[0]), 
						richTextEditor.getText().getSpanEnd(span[0]), 
						tagString);
				richTextEditor.getText().removeSpan(span[0]);
			}
						
			//richTextEditor.getEditableText().setSpan(new ForegroundColorSpan(Color.BLACK), start, end, 0);
			//replaceTextInRichTextEditor(TAG_MARKER + tagString, tagString);
			
			Log.d(TAG, "mazem tag: " + tag.toString());
		}else{// inak ho pridaj ako tag
					
			// kvoli pridanemu tag marker treba posunut poziciu			
			//tag.setStart(start);
			//tag.setEnd(end + 1);
			//tag.setEnd(end);
			listOfLocalTags.add(tag);
			
			Log.d(TAG, "pridavam tag:" + tag.toString());
			
			// pripoji znak tagu pred vybrane slovo a slovo zvyrazni
			//replaceTextInRichTextEditor(tagString, TAG_MARKER + tagString);
			
			//richTextEditor.setSelection(start, end+1);
			
			richTextEditor.setSelection(start, end);
			richTextEditor.applyEffect(RichEditText.BOLD, true);
			richTextEditor.applyEffect(RichEditText.ITALIC, true);
			//richTextEditor.applyEffect(RichEditText.UNDERLINE, true);
			richTextEditor.applyEffect(RichEditText.TYPEFACE, TAG_TYPEFACE);
			
			richTextEditor.getEditableText().replace(start, end, TAG_MARKER + tagString);
			
			richTextEditor.getEditableText().setSpan(
					new ForegroundColorSpan(getResources().getColor(TAG_FOREGROUND_COLOR)), start, end+(TAG_MARKER.length()), 0);
		}
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
		
		
		//Log.d(TAG,"bold " + isSelectionBold + ",italic " 
			//	+ isSelectionItalic + ", under " + isSelectionUnderline);
		
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
