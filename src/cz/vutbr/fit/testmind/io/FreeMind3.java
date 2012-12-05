package cz.vutbr.fit.testmind.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import cz.vutbr.fit.testmind.editor.ITAMEditor;
import cz.vutbr.fit.testmind.editor.TAMEditorMain;
import cz.vutbr.fit.testmind.editor.items.TAMEConnection;
import cz.vutbr.fit.testmind.editor.items.TAMENode;
import cz.vutbr.fit.testmind.graphics.ITAMGConnection;
import cz.vutbr.fit.testmind.graphics.TAMGraph;
import cz.vutbr.fit.testmind.profile.TAMPConnection;
import cz.vutbr.fit.testmind.profile.TAMPConnectionFactory;
import cz.vutbr.fit.testmind.profile.TAMPNode;

import android.util.Log;
import android.util.Xml;

public class FreeMind3 {
	
	private ITAMEditor editor = null;
	private IXMLNode rootNode = null;
	// file address
	private String source = null;
	
	static final String E_MAP = "map";
	static final String E_NODE = "node";
	static final String A_ID = "ID";
	static final String A_CREATED = "CREATED";
	static final String A_MODIFIED = "MODIFIED";
	static final String A_POSITION = "POSSITION";
	static final String A_TEXT = "TEXT";
	static final int SPACE = 50;
	
	public FreeMind3(ITAMEditor editor) {
		this.editor = editor;
		this.source = editor.getProfile().getFileName();
	}
	
	// Speciálnì pro jiný zdroj, než je v profilu
	public FreeMind3(ITAMEditor editor, String source) {
		this.editor = editor;
		this.source = source;
	}
	
	// Speciálnì pro testovací úèely
	public FreeMind3(ITAMEditor editor, boolean bTest) {
		this.editor = editor;
		this.source = "/mnt/sdcard/TestMind/test.mm";
	}
	
	public void runImport() throws XmlPullParserException, IOException {
		this.importXML();
		this.calculateDimensions(rootNode);
		this.createTAMTree(rootNode, null, 0, 0);
		this.editor.invalidate();
	}
	

	private void importXML() throws XmlPullParserException, IOException {
		File file = new File(source);
		InputStream in = new FileInputStream(file);
        
		try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            Log.d("importXML", "getRoot");
            rootNode = getRoot(parser);
        } finally {
            in.close();
        }
	}

	private IXMLNode getRoot(XmlPullParser parser) throws XmlPullParserException, IOException {
		IXMLNode rootNode = null;
		
	    parser.require(XmlPullParser.START_TAG, null, E_MAP);

	    parser.nextTag();
	    String name = parser.getName();
		if (name.equals(E_NODE)) {
			Log.d("importXML", "readNode");
			rootNode = readNode(parser);
        }
	    return rootNode;
	}
	
	private IXMLNode readNode(XmlPullParser parser) throws IOException, XmlPullParserException {
		Log.d("readNode", "START");
		IXMLNode node = null;
	    parser.require(XmlPullParser.START_TAG, null, E_NODE);
	    String tag = parser.getName();
	    Log.d("TAG", tag);
	    if (tag.equals(E_NODE)) {
	    	String text = parser.getAttributeValue(null, A_TEXT);
	    	Log.d("TAG..", text);
	    	node = new XMLNode(
    			Long.parseLong(parser.getAttributeValue(null, A_ID).substring(3)),
    			Long.parseLong(parser.getAttributeValue(null, A_CREATED)),
    			Long.parseLong(parser.getAttributeValue(null, A_MODIFIED)),
    			parser.getAttributeValue(null, A_POSITION),
    			text
	    	);
	    	node.setActive(true);
			node.setHeight(editor.getDefaultNodeHeight() * 2);
			node.setWidth(editor.getDefaultNodeWidth(text) * 2);
			
			//rootNode.addChild(node);
			
			Log.d("OUT_DEEP", String.valueOf(parser.getDepth()));
		    while (parser.nextTag() != XmlPullParser.END_TAG) {
		    	Log.d("TEXT", parser.getAttributeValue(null, A_TEXT));
		    	Log.d("DEEP", String.valueOf(parser.getDepth()));
		    	Log.d("Next TAG", "nalezen");
		    	node.addChild(readNode(parser));
		    }
	    }
	    Log.d("readNode", "END");
	    return node;
	}
	
	private double calculateDimensions(IXMLNode node) {
		ArrayList<IXMLNode> childs = node.getChilds();
		double heightSum = 0;
		
		if (childs != null) {
			for(IXMLNode child : childs) {
				heightSum += calculateDimensions(child);
			}

			if (heightSum > node.getHeight()) {
				node.setHeight(heightSum);
				//node.setMBBHeight(heightSum);
			}
		}
		//node.setMBBWidth(node.getWidth());
		return node.getHeight();
	}
	
/*	
	private void calculateCoordinates() {
		ArrayList<IXMLNode> childs = node.getChilds();
		double heightSum = 0;
		
		if (childs != null) {
			for(IXMLNode child : childs) {
				heightSum += calculateDimensions(child, 0);
			}

			if (heightSum > node.getHeight()) {
				node.setHeight(heightSum);
			}
		}
		return node.getHeight();
	}
*/	

	private void createTAMTree(IXMLNode node, TAMPNode parent, int x, int y) {
		TAMPConnection connection = null;
		if (parent == null) {
			this.editor.getProfile().reset();
			parent = this.editor.getProfile().createRoot(node.getName(), node.getContent());
			TAMPConnectionFactory.addEReference(parent, this.editor, x, y, ITAMGConnection.CONNECTION_TYPE_DEFAULT);
		} else {
			TAMPNode child = this.editor.getProfile().createNode(node.getName(), node.getContent());
			connection = this.editor.getProfile().createConnection(parent, child);
			TAMPConnectionFactory.addEReference(child, this.editor, x, y, ITAMGConnection.CONNECTION_TYPE_DEFAULT);
			TAMPConnectionFactory.addEReference(connection, editor, ITAMGConnection.CONNECTION_TYPE_DEFAULT);
			parent = child;
		}

//		ITAMEConnection connection = new TAMEConnection();
//		TAMPConnectionFactory.addEReference(connection, editor, 1);
				
		ArrayList<IXMLNode> childs = node.getChilds();
		
		if (childs != null) {
			double y_new = y - (node.getHeight() / 3) * (childs.size() - 1);
			for(IXMLNode child : childs) {
				createTAMTree(child, parent, x + (int)Math.floor(node.getWidth()) + SPACE, (int)Math.floor(y_new));
				y_new += child.getHeight();
			}
		}
	}

}
