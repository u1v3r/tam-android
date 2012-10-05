package cz.vutbr.fit.tesmind.graphics;

import cz.vutbr.fit.testmind.graphics.ITAMItem;
import android.graphics.Canvas;
import android.graphics.Paint;

public interface ITAMConnection extends ITAMItem {

	public final int CONNECTION_TYPE_DEFAULT = 1;
	
	public void draw(Canvas canvas, Paint paint);
}
