package cz.vutbr.fit.testmind.editor;

import java.util.List;

import android.view.View;
import cz.vutbr.fit.testmind.editor.items.TAMEditorConnection;
import cz.vutbr.fit.testmind.editor.items.TAMEditorFactory;
import cz.vutbr.fit.testmind.editor.items.TAMEditorNode;
import cz.vutbr.fit.testmind.graphics.ITAMNode;
import cz.vutbr.fit.testmind.graphics.TAMGraph;

public interface ITAMEditor {

	public TAMEditorNode createRoot(int type, int x, int y,
			String title, String body, ITAMNode core);

	public boolean containsNode(int id);

	public TAMEditorNode getNode(int id);

	public boolean containsConnection(int id);

	public TAMEditorConnection getConnection(int id);

	public TAMGraph getGraph();

	public TAMEditorNode getRoot();
	
	public void setRoot(TAMEditorNode root);
	
	public TAMEditorFactory getFactory();

	public List<TAMEditorNode> getListOfNodes();

	public List<TAMEditorConnection> getListOfConnections();
	
	public View getView();
	
	public boolean hasRootNode();
	
	public TAMEditorNode getLastSelectedNode();
}