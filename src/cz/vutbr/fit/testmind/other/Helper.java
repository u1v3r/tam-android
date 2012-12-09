package cz.vutbr.fit.testmind.other;

import java.io.File;

import android.content.Intent;
import android.net.Uri;
import cz.vutbr.fit.testmind.editor.controls.TAMEOpenSaveControl;
import cz.vutbr.fit.testmind.profile.TAMProfile;

public class Helper {

	public static Intent createShareMapChooserIntent(String fileName){
		
		String pathToFile = 
				String.format("%s/%s.%s", 
						TAMProfile.TESTMIND_DIRECTORY.getPath(), 
						fileName,
						TAMEOpenSaveControl.TESTMIND_FILE_EXTENSION);
		
		
		Uri uriToFile = Uri.fromFile(new File(pathToFile));
		
		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.putExtra(Intent.EXTRA_STREAM, uriToFile);
		shareIntent.setType("application/*");
		
		return shareIntent;		
	}
}
