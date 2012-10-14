package cz.vutbr.fit.testmind.editor;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import cz.vutbr.fit.testmind.editor.items.TAMEditorConnection;
import cz.vutbr.fit.testmind.editor.items.TAMEditorFactory;
import cz.vutbr.fit.testmind.editor.items.TAMEditorNode;
import cz.vutbr.fit.testmind.graphics.ITAMNode;
import cz.vutbr.fit.testmind.graphics.TAMGraph;

public interface ITAMEditor {

	public TAMEditorNode createRoot(int type, int x, int y,
			String title, String body);

	public boolean containsNode(int id);

	public TAMEditorNode getNode(int id);

	public boolean containsConnection(int id);

	public TAMEditorConnection getConnection(int id);

	public TAMEditorNode getRoot();
		
	public TAMEditorFactory getFactory();

	public List<TAMEditorNode> getListOfNodes();

	public List<TAMEditorConnection> getListOfConnections();
	
	public boolean hasRootNode();
	
	public ITAMNode getLastSelectedNode();

	public Context getContext();

	public Resources getResources();

	public int getWidth();

	public int getHeight();
	
	public void invalidate();
	
	public boolean onOptionsItemSelected(MenuItem item);
	
	public void onSelectEvent(ITAMNode node);

	public void onUnselectEvent(ITAMNode node);
}