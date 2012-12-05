package cz.vutbr.fit.testmind.io;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import cz.vutbr.fit.testmind.editor.ITAMEditor;

public class ImportFile {
	private ITAMEditor editor;
	private int type = 0;
	
	public static final int FREE_MIND = 1;
	
	public ImportFile(ITAMEditor editor, int type) throws XmlPullParserException, IOException {
		this.editor = editor;
		this.type = type;
		run(type);
	}
	
	private void run(int type) throws XmlPullParserException, IOException {
		switch (type) {
		case FREE_MIND:
			FreeMind3 freeMind = new FreeMind3(editor, true);
			freeMind.runImport();
			break;

		default:
			break;
		}
	}

	public ITAMEditor getEditor() {
		return editor;
	}

	public void setEditor(ITAMEditor editor) {
		this.editor = editor;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	
}
