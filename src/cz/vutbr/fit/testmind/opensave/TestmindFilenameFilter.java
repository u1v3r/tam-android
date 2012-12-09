package cz.vutbr.fit.testmind.opensave;

import java.io.File;
import java.io.FilenameFilter;

import cz.vutbr.fit.testmind.editor.controls.TAMEOpenSaveControl;

public class TestmindFilenameFilter implements FilenameFilter
{    
    public boolean accept(File dir, String name)
    {
        return name.endsWith("." + TAMEOpenSaveControl.TESTMIND_FILE_EXTENSION);
    }
}