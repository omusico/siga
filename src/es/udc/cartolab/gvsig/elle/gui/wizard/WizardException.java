package es.udc.cartolab.gvsig.elle.gui.wizard;


public class WizardException extends Exception {

	private boolean closeWizard = true, showMessage = true;

	public WizardException(Exception e) {
		super(e);
	}

	public WizardException(String message) {
		super(message);
	}

	public WizardException(String message, boolean closeWizard) {
		this(message);
		this.closeWizard = closeWizard;
	}

	public WizardException(String message, boolean closeWizard, boolean showMessage) {
		this(message, closeWizard);
		this.showMessage = showMessage;
	}

	public WizardException(String text, Exception e1) {
		super(text, e1);
	}

	public boolean closeWizard() {
		return closeWizard;
	}

	public boolean showMessage() {
		return showMessage;
	}

}
