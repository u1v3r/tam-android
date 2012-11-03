package cz.vutbr.fit.testmind.graphics;

public interface ITAMGButton extends ITAMGItem {
	
	public final int BUTTON_TYPE_MENU = 1;
	public final int BUTTON_TYPE_NODE = 2;
	
	public void actualizePosition(int x, int y);

}
