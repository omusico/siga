package es.udc.cartolab.gvsig.elle.gui.wizard;

import java.util.ArrayList;
import java.util.Map;

import javax.swing.JPanel;

public abstract class WizardComponent extends JPanel {

	private ArrayList<WizardListener> listeners = new ArrayList<WizardListener>();
	protected Map<String, Object> properties;

	public WizardComponent(Map<String, Object> properties) {
		this.properties = properties;
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
			listener.wizardChanged();
		}
	}

	public abstract String getWizardComponentName();

	public abstract void showComponent() throws WizardException;

	public abstract void finish() throws WizardException;

	public abstract void setProperties() throws WizardException;
	
	public void putProperty(String key, Object value) {
		properties.put(key, value);
	}
	
	public Object getProperty(String key) {
		return properties.get(key);
	}

}
