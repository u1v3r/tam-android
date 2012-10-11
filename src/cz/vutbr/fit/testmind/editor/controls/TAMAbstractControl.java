package cz.vutbr.fit.testmind.editor.controls;

public abstract class TAMAbstractControl {
	
	private boolean enabled;

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

}
