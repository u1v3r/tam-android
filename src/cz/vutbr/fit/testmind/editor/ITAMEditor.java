package cz.vutbr.fit.testmind.editor;

import java.util.List;

import com.touchmenotapps.widget.radialmenu.menu.v1.RadialMenuItem;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PointF;
import android.preference.PreferenceManager.OnActivityResultListener;
import android.view.MenuItem;
import android.view.View;
import cz.vutbr.fit.testmind.R;
import cz.vutbr.fit.testmind.editor.controls.ITAMButtonListener;
import cz.vutbr.fit.testmind.editor.controls.ITAMMenuListener;
import cz.vutbr.fit.testmind.editor.items.ITAMEConnection;
import cz.vutbr.fit.testmind.editor.items.ITAMENode;
import cz.vutbr.fit.testmind.graphics.ITAMGItem;
import cz.vutbr.fit.testmind.graphics.ITAMGNode;
import cz.vutbr.fit.testmind.graphics.TAMGItemFactory;
import cz.vutbr.fit.testmind.graphics.TAMGZoom;
import cz.vutbr.fit.testmind.graphics.TAMGraph.ITAMBlankAreaGestureListener;
import cz.vutbr.fit.testmind.graphics.TAMGraph.ITAMGraphDrawingFinishedListener;
import cz.vutbr.fit.testmind.graphics.TAMGraph.ITAMPostDrawListener;
import cz.vutbr.fit.testmind.graphics.TAMGraph.ITAMItemGestureListener;
import cz.vutbr.fit.testmind.graphics.TAMGraph.ITAMItemListener;
import cz.vutbr.fit.testmind.graphics.TAMGraph.ITAMPreDrawListener;
import cz.vutbr.fit.testmind.graphics.TAMGraph.ITAMTouchListener;
import cz.vutbr.fit.testmind.profile.TAMPConnection;
import cz.vutbr.fit.testmind.profile.TAMPNode;
import cz.vutbr.fit.testmind.profile.TAMProfile;

public interface ITAMEditor {
	
	public TAMProfile getProfile();
	
	public void setEditorVisibility(int visibility);
	
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
	
	public void onButtonSelected(View view);
	
	public void onActivityResult(int requestCode, int resultCode, Intent data);
	
	public void reset();
	
	public List<ITAMMenuListener> getListOfMenuControls();
	
	public List<ITAMButtonListener> getListOfButtonControls();
	
	// TAMGraph functions //
	
	public TAMGItemFactory getGItemFactory();
	
	public ITAMGNode getLastSelectedNode();
	
	public int getWidth();

	public int getHeight();
	
	public List<ITAMTouchListener> getListOfTouchControls();

	public List<ITAMPreDrawListener> getListOfPreDrawControls();

	public List<ITAMPostDrawListener> getListOfPostDrawControls();

	public List<ITAMItemListener> getListOfItemControls();
	
	public List<ITAMItemGestureListener> getListOfItemGestureControls();
	
	public List<OnActivityResultListener> getListOfOnActivityResultControls();
	
	public List<ITAMBlankAreaGestureListener> getListOfMoveGestureControls();
	
	public List<ITAMGItem> getListOfSelectedItems();
	
	public List<ITAMGraphDrawingFinishedListener> getListOfGraphDrawingFinishedListener();
	
	public List<ITAMRadialMenu> getListOfRadialMenuListeners();
	
	// Surface View functions //

	public Context getContext();

	public Resources getResources();
	
	//public void addOnGestureLisener(OnGestureListener listener, Context context);
	
	public void invalidate();
	
	public TAMGZoom getZoom();
	
	public void zoom(float scaleX, float scaleY, float pivotX, float pivotY);
		
	public void unselectAll();

	public PointF getTranslation();
	
	public void translate(float tx, float ty);
	
	public int getDefaultNodeHeight();
	
	public int getDefaultNodeWidth(String text);	
	
	public void addRadialMenuItem(RadialMenuItem item);
	
	public void showRadialMenu(int posX, int posY, View anchor);
	
	public void dissmisRadialMenu();
}