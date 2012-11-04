package cz.vutbr.fit.testmind.fragments;

import java.util.List;

import com.commonsware.cwac.richedit.Effect;
import com.commonsware.cwac.richedit.RichEditText;
import com.commonsware.cwac.richedit.RichEditText.OnSelectionChangedListener;

import cz.vutbr.fit.testmind.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView.BufferType;
/**
 * @deprecated Odstranit
 */
public class RichTextEditorFragment extends Fragment implements Callback{

	private static final String TAG = "RichTextEditorFragment";
	private RichEditText editor;
	private ImageButton boldBtn;
	private ImageButton italicBtn;
	private ImageButton underlineBtn;
	private ImageButton tagBtn;
	
	public static RichTextEditorFragment newInstance(CharSequence text){
		
		RichTextEditorFragment fragment = new RichTextEditorFragment();		
		Bundle args = new Bundle();
		args.putCharSequence("text", text);
		fragment.setArguments(args);
		
		return fragment;
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
					
		setRetainInstance(true);	
		/*
	    View result = inflater.inflate(R.layout.fragment_rich_text_editor, container, false);

	    editor = (RichEditText)result.findViewById(R.id.fragment_rich_text_node_text);	    
	    setSpannedText(new SpannableString(getArguments().getCharSequence("text")));
	    
	    // v action menu zobrazi formatovaci button
	    editor.enableActionModes(true);
	    
	    boldBtn = (ImageButton)result.findViewById(R.id.fragment_rich_text_boldbtn);
	    italicBtn = (ImageButton)result.findViewById(R.id.fragment_rich_text_italicbtn);
	    underlineBtn = (ImageButton)result.findViewById(R.id.fragment_rich_text_underlinebtn);
	    tagBtn = (ImageButton)result.findViewById(R.id.fragment_rich_text_tagbtn);
	    
	    boldBtn.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {				
				editor.applyEffect(RichEditText.BOLD, true);				
			}
		});
	    
	    italicBtn.setOnClickListener(new View.OnClickListener() {

	    	public void onClick(View v) {				
	    		editor.applyEffect(RichEditText.ITALIC, true);				
	    	}
	    });

	    underlineBtn.setOnClickListener(new View.OnClickListener() {

	    	public void onClick(View v) {				
	    		editor.applyEffect(RichEditText.UNDERLINE, true);				
	    	}
	    });

	    tagBtn.setOnClickListener(new View.OnClickListener() {

	    	public void onClick(View v) {	    		
	    		// TODO: tu treba implementovat pridanie slova do zoznamu tagov				
	    	}
	    });
	    
	    
	    editor.setOnSelectionChangedListener(new OnSelectionChangedListener() {
			
			public void onSelectionChanged(int start, int end, List<Effect<?>> effects) {
				
				// TODO: tu treba implementovat vypnutie efektu
				
				Log.d(TAG,effects.toString());
				
			}
		});
	    */
	    return super.onCreateView(inflater, container, savedInstanceState);
	    //return(result);
	}
	
	public void setSpannedText(SpannableString text){	
		editor.setText(text, BufferType.SPANNABLE);
	}
	
	public Editable getSpannedText(){
		return editor.getEditableText();
	}


	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		Log.d(TAG,"onCreateActionMode");
		return false;
	}


	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		Log.d(TAG,"onPrepareActionMode");
		return false;
	}


	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		Log.d(TAG,"onActionItemClicked");
		return false;
	}


	public void onDestroyActionMode(ActionMode mode) {
		Log.d(TAG,"onDestroyActionMode");		
	}	
}
