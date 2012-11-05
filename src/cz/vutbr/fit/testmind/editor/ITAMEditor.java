package cz.vutbr.fit.testmind.editor;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.preference.PreferenceManager.OnActivityResultListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MenuItem;
import cz.vutbr.fit.testmind.editor.controls.ITAMMenuListener;
import cz.vutbr.fit.testmind.editor.items.ITAMEConnection;
import cz.vutbr.fit.testmind.editor.items.ITAMENode;
import cz.vutbr.fit.testmind.graphics.ITAMGItem;
import cz.vutbr.fit.testmind.graphics.ITAMGNode;
import cz.vutbr.fit.testmind.graphics.TAMGZoom;
import cz.vutbr.fit.testmind.graphics.TAMGraph.ITAMDrawListener;
import cz.vutbr.fit.testmind.graphics.TAMGraph.ITAMItemGestureListener;
import cz.vutbr.fit.testmind.graphics.TAMGraph.ITAMItemListener;
import cz.vutbr.fit.testmind.graphics.TAMGraph.ITAMTouchListener;
import cz.vutbr.fit.testmind.profile.TAMPConnection;
import cz.vutbr.fit.testmind.profile.TAMPNode;
import cz.vutbr.fit.testmind.profile.TAMProfile;

public interface ITAMEditor {
	
	public TAMProfile getProfile();
	
	public int getMode();
	
	public ITAMENode createNode(TAMPNode profile, int x, int y);

	public ITAMENode createNode(TAMPNode profile, int x, int y, int type);

	public ITAMEConnection createConnection(TAMPConnection profile);

	public ITAMEConnection createConnection(TAMPConnection profile, int type);

	public boolean containsNode(int id);

	public boolean containsConnection(int id);

	public List<ITAMENode> getListOfENodes();

	public List<ITAMEConnection> getListOfEConnections();
	
	public boolean onOptionsItemSelected(MenuItem item);
	
	public void onActivityResult(int requestCode, int resultCode, Intent data);
	
	public ITAMENode createNodeWithProfileAndConnection(String title, String body, ITAMENode parent, int posX, int posY);
	
	// TAMGraph functions //
	
	public ITAMGNode getLastSelectedNode();
	
	public int getWidth();

	public int getHeight();
	
	public List<ITAMTouchListener> getListOfTouchControls();

	public List<ITAMDrawListener> getListOfDrawControls();

	public List<ITAMItemListener> getListOfItemControls();
	
	public List<ITAMItemGestureListener> getListOfItemGestureControls();
	
	public List<ITAMMenuListener> getListOfMenuControls();
	
	public List<OnActivityResultListener> getListOfOnActivityResultControls();
	
	public List<ITAMGItem> getListOfSelectedItems();
	
	// Surface View functions //

	public Context getContext();

	public Resources getResources();
	
	public void addOnGestureLisener(OnGestureListener listener, Context context);
	
	public void invalidate();
	
	public TAMGZoom getZoom();
	
	public void zoom(float scaleX, float scaleY, float pivotX, float pivotY);
	
	public void unselectAll();

	
}