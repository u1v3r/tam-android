package cz.vutbr.fit.testmind.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import cz.vutbr.fit.testmind.editor.TAMEditorMain;
import cz.vutbr.fit.testmind.editor.items.TAMENode;
import android.util.Log;
import android.util.Xml;

/**
 * main mind map class
 * 
 * deprecated
 */
public class FreeMind
{
	TAMEditorMain editor = null;
    private IXMLNode rootNode = null;
    // file address
    private String source = "/data/testMind/test.mm";//null;
    
    static final String MAP = "map";
    static final String NODE = "node";
   
    public IXMLNode getMindMap() throws XmlPullParserException, IOException
    {
    	if (rootNode == null) {
        	if (source == null) {
        		//source = "/data/testMind/test.mm";
        		//importXML(); // pro testovani
        		// TODO: error -> missing source
        	} else {
        		importXML();
        		createTree();
        	}
    	}
    		
        return this.rootNode;
    }
    
    public IXMLNode getMindMap(String source) throws XmlPullParserException, IOException {
    	this.source = source;
    	importXML();
    	createTree();
    	return this.rootNode;
    }
    
    private void createTree() {
    	editor = new TAMEditorMain(null);
  	
    	
    	/* TODO: TREBA OPRAVIT
    	TAMENode rootNodeEditor = editor.createRoot(0, 0, 0, rootNode.getName(), rootNode.getContent());
    	*/
    	
    	
    	for (IXMLNode child : rootNode.getChilds()) {
    		
    		
    		/* TODO: TREBA OPRAVIT
    		buildTree(rootNodeEditor.addChild(0, 0, child.getName(), child.getContent()), child);
    		*/
    		
    		

    		Log.d("root", child.getName());
    	}
    }
    
    private void buildTree(TAMENode nodeEditor, IXMLNode node) {
    	for (IXMLNode child : node.getChilds()) {

    		
    		
    		/* TODO: TREBA OPRAVIT
    		buildTree(nodeEditor.addChild(0, 0, child.getName(), child.getContent()), child);
    		*/
    		

    		Log.d("node", child.getName());
    	}
    }
    
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	private void importXML() throws XmlPullParserException, IOException {
		File file = new File(source);
		InputStream in = new FileInputStream(file);
        
		try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            rootNode = getRoot(parser);
        } finally {
            in.close();
        }
	}

	private IXMLNode getRoot(XmlPullParser parser) throws XmlPullParserException, IOException {
		IXMLNode rootNode = null;
		
	    parser.require(XmlPullParser.START_TAG, null, MAP);

	    parser.nextTag();
	    String name = parser.getName();
		if (name.equals(NODE)) {
        	rootNode = readNode(parser);
        }
	    return rootNode;
	}

	private IXMLNode readNode(XmlPullParser parser) throws IOException, XmlPullParserException {
		IXMLNode node = null;
	    parser.require(XmlPullParser.START_TAG, null, NODE);
	    String tag = parser.getName();
	    if (tag.equals(NODE)) {
	    	node = new XMLNode(
    			Long.parseLong(parser.getAttributeValue(null, "ID").substring(3)),
    			Long.parseLong(parser.getAttributeValue(null, "CREATED")),
    			Long.parseLong(parser.getAttributeValue(null, "MODIFIED")),
    			parser.getAttributeValue(null, "POSITION"),
    			parser.getAttributeValue(null, "TEXT")
	    	);
	    }
	    while (parser.nextTag() != XmlPullParser.END_TAG) {
	    	node.addChild(readNode(parser));
	    }
	    return node;
	}
}