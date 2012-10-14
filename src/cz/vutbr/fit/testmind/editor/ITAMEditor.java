package cz.vutbr.fit.testmind.editor;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import cz.vutbr.fit.testmind.editor.items.TAMEConnection;
import cz.vutbr.fit.testmind.editor.items.TAMEItemFactory;
import cz.vutbr.fit.testmind.editor.items.TAMENode;
import cz.vutbr.fit.testmind.graphics.ITAMGNode;
import cz.vutbr.fit.testmind.graphics.TAMGraph;

public interface ITAMEditor {

	public TAMENode createRoot(int type, int x, int y,
			String title, String body);

	public boolean containsNode(int id);

	public TAMENode getNode(int id);

	public boolean containsConnection(int id);

	public TAMEConnection getConnection(int id);

	public TAMENode getRoot();
		
	public TAMEItemFactory getFactory();

	public List<TAMENode> getListOfNodes();

	public List<TAMEConnection> getListOfConnections();
	
	public boolean hasRootNode();
	
	public ITAMGNode getLastSelectedNode();

	public Context getContext();

	public Resources getResources();

	public int getWidth();

	public int getHeight();
	
	public void invalidate();
	
	public boolean onOptionsItemSelected(MenuItem item);
	
	public void onSelectEvent(ITAMGNode node);

	public void onUnselectEvent(ITAMGNode node);
	
}