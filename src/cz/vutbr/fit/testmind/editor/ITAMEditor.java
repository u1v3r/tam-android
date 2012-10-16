package cz.vutbr.fit.testmind.editor;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.GestureDetector.OnGestureListener;
import android.view.MenuItem;
import cz.vutbr.fit.testmind.editor.controls.ITAMMenuListener;
import cz.vutbr.fit.testmind.editor.items.ITAMEConnection;
import cz.vutbr.fit.testmind.editor.items.ITAMENode;
import cz.vutbr.fit.testmind.graphics.ITAMGNode;
import cz.vutbr.fit.testmind.graphics.TAMGZoom;
import cz.vutbr.fit.testmind.graphics.TAMGraph.ITAMDrawListener;
import cz.vutbr.fit.testmind.graphics.TAMGraph.ITAMItemListener;
import cz.vutbr.fit.testmind.graphics.TAMGraph.ITAMTouchListener;
import cz.vutbr.fit.testmind.profile.TAMPConnection;
import cz.vutbr.fit.testmind.profile.TAMPNode;

public interface ITAMEditor {
	
	public ITAMENode createNode(TAMPNode profile, int x, int y);

	public ITAMENode createNode(TAMPNode profile, int x, int y, int type);

	public ITAMEConnection createConnection(TAMPConnection profile);

	public ITAMEConnection createConnection(TAMPConnection profile, int type);

	public boolean containsNode(int id);

	public boolean containsConnection(int id);

	public List<ITAMENode> getListOfENodes();

	public List<ITAMEConnection> getListOfEConnections();
	
	public boolean onOptionsItemSelected(MenuItem item);
	
	// TAMGraph functions //
	
	public ITAMGNode getLastSelectedNode();
	
	public int getWidth();

	public int getHeight();
	
	public List<ITAMTouchListener> getListOfTouchControls();

	public List<ITAMDrawListener> getListOfDrawControls();

	public List<ITAMItemListener> getListOfItemControls();
	
	public List<ITAMMenuListener> getListOfMenuControls();
	
	// Surface View functions //

	public Context getContext();

	public Resources getResources();
	
	public void addOnGestureLisener(OnGestureListener listener, Context context);
	
	public void invalidate();
	
	public TAMGZoom getZoom();
}