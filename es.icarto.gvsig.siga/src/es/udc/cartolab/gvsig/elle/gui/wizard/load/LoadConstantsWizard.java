package es.udc.cartolab.gvsig.elle.gui.wizard.load;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.project.documents.view.gui.View;

@SuppressWarnings("serial")
public class LoadConstantsWizard extends SigaLoadMapWizard {

    public LoadConstantsWizard(View view) {
	super(view);
    }

    @Override
    public WindowInfo getWindowInfo() {
	WindowInfo wi = super.getWindowInfo();
	wi.setTitle(PluginServices.getText(this, "load_constans"));
	return wi;
    }

    @Override
    protected void addWizardComponents() {
	views.add(new LoadConstantsWizardComponent(properties));
    }
}
