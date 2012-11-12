package cz.vutbr.fit.testmind.profile;

import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.editor.items.ITAMENode;

public class TAMPConnectionFactory {
	
	private TAMPConnectionFactory(){}
	
	public static final ITAMENode addEReference(TAMPNode node, ITAMEditor editor, int x, int y){
		
		if(!node.getEditorReferences().containsKey(editor)) {
			ITAMENode eNode = editor.createNode(node, x, y);
			node.getEditorReferences().put(editor, eNode);
			return eNode;
		} else {
			return null;
		}
	}
		
	public static final ITAMENode addEReference(TAMPNode node, ITAMEditor editor, int x, int y, int type) {
		
		if(!node.getEditorReferences().containsKey(editor)) {
			ITAMENode eNode = editor.createNode(node, x, y, type);
			node.getEditorReferences().put(editor, eNode);
			return eNode;
		} else {
			return null;
		}
	}	
}
