package cz.vutbr.fit.testmind.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import cz.vutbr.fit.testmind.editor.TAMEditor;
import cz.vutbr.fit.testmind.editor.items.TAMENode;
import android.util.Log;
import android.util.Xml;

/**
 * main mind map class
 */
public class FreeMind2
{
	TAMEditor editor = null;
    private TAMENode rootNode = null;
    private String source = "/data/testMind/test.mm";//null;
    
    static final String MAP = "map";
    static final String NODE = "node";
    static final int MAX_TITLE_LENGTH = 10;
   
    public TAMENode getMindMap() throws XmlPullParserException, IOException
    {
    	if (rootNode == null) {
        	if (source == null) {
        		//source = "/data/testMind/test.mm";
        		//importXML(); // pro testování
        		// TODO: error -> missing source
        	} else {
        		importXML();
        	}
    	}
    		
        return this.rootNode;
    }
    
    public TAMENode getMindMap(String source) throws XmlPullParserException, IOException {
    	this.source = source;
    	importXML();
    	return this.rootNode;
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

	private TAMENode getRoot(XmlPullParser parser) throws XmlPullParserException, IOException {
		editor = new TAMEditor(null);
		TAMENode rootNodeEditor = null;
		
	    parser.require(XmlPullParser.START_TAG, null, MAP);

	    parser.nextTag();
	    String name = parser.getName();
		if (name.equals(NODE)) {
			String content = parser.getAttributeValue(null, "TEXT");
			String title = getTitle(content);
			rootNodeEditor = editor.createRoot(0, 0, 0, title, content);
			readNode(rootNodeEditor, parser);
        }
	    return rootNodeEditor;
	}

	private void readNode(TAMENode nodeEditor, XmlPullParser parser) throws IOException, XmlPullParserException {
	    parser.require(XmlPullParser.START_TAG, null, NODE);
	    String tag = parser.getName();
	    if (tag.equals(NODE)) {
		    while (parser.nextTag() != XmlPullParser.END_TAG) {
				String content = parser.getAttributeValue(null, "TEXT");
				String title = getTitle(content);
		    	readNode(nodeEditor.addChild(0, 0, title, content), parser);
		    }
	    }
	}
	
	private String getTitle(String content) {
		String title = null;
		if (content.length() > MAX_TITLE_LENGTH) {
			title = content.substring(MAX_TITLE_LENGTH);
		} else {
			title = content;
		}
		return title;
	}
}