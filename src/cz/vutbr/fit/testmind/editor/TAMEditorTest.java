package cz.vutbr.fit.testmind.editor;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MenuItem;

public class TAMEditorTest extends TAMAbstractEditor implements ITAMEditor {
	
	public TAMEditorTest(Context context) {
		this(context, null);
	}

	public TAMEditorTest(Context context, AttributeSet attrs) {
		super(context, attrs);
		// do not type anything there - use initializeControls method instead //
	}

	@Override
	protected void initializeControls() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void modeChanged(MenuItem item) {
		// this editor has only one mode //
	}
	
	public int getMode() {
		// this editor has only one mode //
		return 0;
	}

	@Override
	protected void actualizeModeMenu(int visibility) {
		// this editor has no mode menu yet //
	}

}
