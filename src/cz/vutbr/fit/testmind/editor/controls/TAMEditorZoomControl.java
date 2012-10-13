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
	
	private ZoomControls zoomControls;
	private TAMGraph graph;

	public TAMEditorZoomControl(TAMEditor editor, int id) {
		setEditor(editor);
		this.zoomControls = (ZoomControls) ((FragmentActivity) editor.getContext()).findViewById(R.id.zoom_controls);
		System.out.println(zoomControls);
		this.graph = (TAMGraph) getEditor();
		addListeners();
	}
	
	public void zoom(float sx, float sy, float px, float py) {
		getEditor().zoom(sx, sy, px, py);
	}
	
	private void addListeners() {

		zoomControls.setOnZoomInClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				//controller.zoomIn(EventObjects.editor);
				graph.zoom(graph.sx*2, graph.sy*2, graph.getWidth()*0.5f, graph.getHeight()*0.5f);
			}
		});
        zoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				graph.zoom(graph.sx*0.5f, graph.sy*0.5f, graph.getWidth()*0.5f, graph.getHeight()*0.5f);
			}
		});
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
