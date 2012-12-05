package cz.vutbr.fit.testmind.io;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import cz.vutbr.fit.testmind.editor.ITAMEditor;

public class ImportFile {
	private ITAMEditor editor;
	private String source = "";
	private int type = 0;
	
	/** 
	 * Import of Free Mind type
	 */
	public static final int FREE_MIND = 1;
	
	/** main construct for run type of import
	 * 
	 * @param editor editor with profile and file address
	 * @param source path + fileName
	 * @param type type of file
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public ImportFile(ITAMEditor editor, String source, int type) throws XmlPullParserException, IOException {
		this.editor = editor;
		this.source = source;
		this.type = type;
		run(source, type);
	}
	
	/** Run specify import of file type
	 * 
	 * @param type type of file
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private void run(String source, int type) throws XmlPullParserException, IOException {
		switch (type) {
		case FREE_MIND:
			FreeMind3 freeMind = new FreeMind3(editor, source);
			freeMind.runImport();
			break;

		default:
			break;
		}
	}

	// Getters & Setters
	/** get working editor 
	 * 
	 * @return
	 */
	public ITAMEditor getEditor() {
		return editor;
	}

	/** set working editor
	 * 
	 * @param editor
	 */
	public void setEditor(ITAMEditor editor) {
		this.editor = editor;
	}

	/** get constant of file type
	 * 
	 * @return
	 */
	public int getType() {
		return type;
	}

	/** set file type
	 * 
	 * @param type
	 */
	public void setType(int type) {
		this.type = type;
	}
}
