package cz.vutbr.fit.tesmind.graphics;

public class TAMItemFactory {
	
	protected ITAMItem createNode(TAMGraph graph, int type, int x, int y) {
		
		ITAMItem item = null;
		
		if(type == ITAMItem.ITEM_TYPE_RECTANGLE) {
			item = new TAMRactangleNode(graph, x, y);
		}
		
		return item;
	}

}
