package es.udc.cartolab.gvsig.elle.gui.wizard;

import java.util.ArrayList;

import javax.swing.JPanel;

public abstract class WizardComponent extends JPanel {

	private ArrayList<WizardListener> listeners = new ArrayList<WizardListener>();
	protected WizardWindow parentWindow;

	public WizardComponent(WizardWindow parent) {
		parentWindow = parent;
	}

	public abstract boolean canFinish();

	public abstract boolean canNext();

	public void addWizardListener(WizardListener l) {
		if (!listeners.contains(l)) {
			listeners.add(l);
		}
	}

	public void removeWizardListener(WizardListener l) {
		if (listeners.contains(l)) {
			listeners.remove(l);
		}
	}

	protected void callStateChanged() {
		for(WizardListener listener : listeners) {
			listener.WizardChanged();
		}
	}

	public abstract String getWizardComponentName();

	public abstract void showComponent();

	public abstract void finish();

}
