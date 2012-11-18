package cz.vutbr.fit.testmind.opensave;

import java.io.File;
import java.io.FilenameFilter;

public class TestmindFilenameFilter implements FilenameFilter
{    
    public boolean accept(File dir, String name)
    {
        return name.endsWith(".db");
    }
}