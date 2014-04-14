package es.udc.cartolab.gvsig.elle.gui.wizard.load;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.udc.cartolab.gvsig.elle.gui.wizard.load.LoadMapWizard;

public class LoadConstantsWizard extends LoadMapWizard {

	public LoadConstantsWizard(View view) {
		super(view);

	}

	public WindowInfo getWindowInfo() {
		WindowInfo wi = super.getWindowInfo();
		wi.setHeight(300);
		wi.setTitle(PluginServices.getText(this, "load_constans"));
		return wi;
	}

	protected void addWizardComponents() {
		views.add(new LoadConstantsWizardComponent(properties));
	}
}
