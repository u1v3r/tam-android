package cz.vutbr.fit.testmind.editor.controls;

import android.graphics.Canvas;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ZoomControls;
import cz.vutbr.fit.testmind.R;
import cz.vutbr.fit.testmind.editor.TAMEditor;
import cz.vutbr.fit.testmind.graphics.TAMGraph;

public class TAMEditorZoomControl extends TAMEditorAbstractControl {
	
	public interface ZoomInOutEventListener {
		
		void onZoomIn();
		void onZoomOut();
		
	}
	
	private ZoomControls zoomControls;
	private TAMGraph graph;

	public TAMEditorZoomControl(TAMEditor editor, int id) {
		setEditor(editor);
		this.zoomControls = (ZoomControls) ((FragmentActivity) editor.getContext()).findViewById(R.id.zoom_controls);
		System.out.println(zoomControls);
		this.graph = (TAMGraph) getEditor();
		addListeners();
	}
	/*
	public void zoom(float sx, float sy, float px, float py) {
		getEditor().zoom(sx, sy, px, py);
	}
	*/
	
	private void addListeners() {

		zoomControls.setOnZoomInClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				zoomIn(graph);				
			}
		});
        zoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				zoomOut(graph);				
			}
		});
	}

	private void zoomIn(ZoomInOutEventListener object){
		object.onZoomIn();		
	}
	
	private void zoomOut(ZoomInOutEventListener object){
		object.onZoomOut();		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTouchEvent(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
