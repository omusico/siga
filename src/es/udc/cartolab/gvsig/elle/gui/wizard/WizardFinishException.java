package es.udc.cartolab.gvsig.elle.gui.wizard;

public class WizardFinishException extends Exception {

	public WizardFinishException(Exception e) {
		super(e);
	}

	public WizardFinishException(String message) {
		super(message);
	}

}
