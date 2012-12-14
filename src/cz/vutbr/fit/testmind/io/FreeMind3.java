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
	static final String E_HTML = "richcontent";
	static final String E_FONT = "font";
	static final String A_ID = "ID";
	static final String A_CREATED = "CREATED";
	static final String A_MODIFIED = "MODIFIED";
	static final String A_POSITION = "POSSITION";
	static final String A_TEXT = "TEXT";
	static final int HSPACE = 250;
	static final int VSPACE = 50;
	
	public FreeMind3(ITAMEditor editor) {
		this.editor = editor;
		this.source = editor.getProfile().getFileName();
	}
	
	/** Special for others sources
	 *  
	 * @param editor
	 * @param source
	 */
	public FreeMind3(ITAMEditor editor, String source) {
		this.editor = editor;
		this.source = source;
	}
	
	/** Special for test
	 * 
	 * @param editor
	 * @param bTest
	 */
	public FreeMind3(ITAMEditor editor, boolean bTest) {
		this.editor = editor;
		this.source = "/mnt/sdcard/TestMind/test2.mm";
	}
	
	/** Sequence of part of import
	 * 
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public void runImport() throws XmlPullParserException, IOException {
		this.importXML();
		this.calculateDimensions(rootNode);
		this.createTAMTree(rootNode, null, 0, 0);
		this.editor.invalidate();
	}
	
	/** Load XML file to tree
	 * 
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private void importXML() throws XmlPullParserException, IOException {
		//Log.d("FreeMind3", source);
		File file = new File(source);
		InputStream in = new FileInputStream(file);
        
		try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            ////Log.d("importXML", "getRoot");
            rootNode = createRoot(parser);
        } finally {
            in.close();
        }
	}

	/** Create root of tree
	 * 
	 * @param parser
	 * @return
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private IXMLNode createRoot(XmlPullParser parser) throws XmlPullParserException, IOException {
		IXMLNode rootNode = null;
		
	    parser.require(XmlPullParser.START_TAG, null, E_MAP);

	    parser.nextTag();
	    String name = parser.getName();
		if (name.equals(E_NODE)) {
			//Log.d("importXML", "readNode");
			rootNode = readNode(parser);
        }
		
		editor.getProfile().setFileName(rootNode.getContent());
		
	    return rootNode;
	}
	
	/** Read node of XML
	 * 
	 * @param parser
	 * @return
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	private IXMLNode readNode(XmlPullParser parser) throws IOException, XmlPullParserException {
		boolean bHTML = false;
		String sID = "";
		String sCreated = "";
		String sModified = "";
		String sPossition = "";
		String text = "";
		//Log.d("readNode", "START");
		IXMLNode node = null;
	    parser.require(XmlPullParser.START_TAG, null, E_NODE);
	    String tag = parser.getName();
	    ////Log.d("TAG", tag);
	    if (tag.equals(E_NODE)) {
	    	sID = parser.getAttributeValue(null, A_ID).substring(3);
	    	sCreated = parser.getAttributeValue(null, A_CREATED);
	    	sModified = parser.getAttributeValue(null, A_MODIFIED);
	    	sPossition = parser.getAttributeValue(null, A_POSITION);
	    	text = parser.getAttributeValue(null, A_TEXT);
	    	if (text == null) {
	    		parser.nextTag();
	    	    tag = parser.getName();
	    	    if (tag.equals(E_HTML)) {
		    		text = readHTML(parser);
		    		bHTML = true;
		    		//Log.d("HTML", text);
	    	    } else {
	    	    	// exception
	    	    }
	    	}
	    	node = new XMLNode(
    			Long.parseLong(sID),
    			Long.parseLong(sCreated),
    			Long.parseLong(sModified),
    			sPossition,
    			text,
    			bHTML
	    	);
	    	node.setActive(true);
			node.setHeight(editor.getDefaultNodeHeight());
			node.setWidth(editor.getDefaultNodeWidth(node.getName()) * 2);
			
			//rootNode.addChild(node);
			
			////Log.d("OUT_DEEP", String.valueOf(parser.getDepth()));
			while (parser.nextTag() != XmlPullParser.END_TAG) {
				if (parser.getEventType() == parser.START_TAG && parser.getName().equals(E_FONT)) {
					parser.nextTag();
					//Log.d("FONT-desc", parser.getPositionDescription());
					parser.nextTag();
					//Log.d("FONT-desc", parser.getPositionDescription());
					/*
	    	    	Log.d("FONT-desc", parser.getPositionDescription());
	    	    	parser.nextToken();
	    	    	Log.d("FONT-desc", parser.getPositionDescription());
	    	    	parser.nextToken();
	    	    	Log.d("FONT-desc", parser.getPositionDescription());
	    	    	parser.nextToken();
	    	    	Log.d("FONT-desc", parser.getPositionDescription());
	    	    	*/
	    	    }
		    	//Log.d("TEXT", parser.getAttributeValue(null, A_TEXT));
		    	//Log.d("DEEP", String.valueOf(parser.getDepth()));
		    	//Log.d("Next TAG", "nalezen");
		    	node.addChild(readNode(parser));
		    }
	    }
	    //Log.d("readNode", "END");
	    return node;
	}

	/**
	 * Read HTML of Node
	 * @param parser
	 * @return
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	private String readHTML(XmlPullParser parser) throws IOException, XmlPullParserException {
		String html = "";
		int depth = parser.getDepth();
		//Log.d("2HTML-depth", String.valueOf(parser.getDepth()));
		//Log.d("2HTML", parser.getName());
		parser.nextToken();
		
		while (depth != parser.getDepth()) {

			if(parser.getEventType() == parser.START_TAG) {
				//Log.d(">HTML-name", parser.getName());
				html += "<"+parser.getName()+">";
			} else if (parser.getEventType() == parser.END_TAG) {
				//Log.d(">HTML-name", parser.getName());
				html += "</"+parser.getName()+">";
			} else if (parser.getEventType() == parser.TEXT) {
				//Log.d(">HTML-text", parser.getText());
				html += parser.getText();
			} else {
				//Log.d("HTML", "Exception");
			}
		
			//Log.d("HTML-line", String.valueOf(parser.getLineNumber()));
			//Log.d(">HTML-depth", String.valueOf(parser.getDepth()));
			//Log.d(">HTML-desc", parser.getPositionDescription());
			parser.nextToken();
		}
		
		//Log.d("HTML", "END WHILE");
		parser.nextToken();
		//Log.d("HTML-line", String.valueOf(parser.getLineNumber()));
		//Log.d(">HTML-depth", String.valueOf(parser.getDepth()));
		//Log.d(">HTML-desc", parser.getPositionDescription());
		return html;
	}
	
	/** Calculate dimensions of tree nodes
	 * 
	 * @param node
	 * @return
	 */
	private double calculateDimensions(IXMLNode node) {
		ArrayList<IXMLNode> childs = node.getChilds();
		double heightSum = 0;
		
		if (childs != null) {
			for(IXMLNode child : childs) {
				heightSum += calculateDimensions(child) + VSPACE;
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

	/** Create TAM tree
	 * 
	 * @param node
	 * @param parent
	 * @param x coord-x
	 * @param y coord-y
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
			double y_new = y; 
			// - (node.getHeight() / 3) * (childs.size() - 1);
			for(IXMLNode child : childs) {
				createTAMTree(child, parent, x + (int)Math.floor(node.getWidth()) + HSPACE, (int)Math.floor(y_new));
				y_new += child.getHeight() + VSPACE;
			}
		}
	}

}
