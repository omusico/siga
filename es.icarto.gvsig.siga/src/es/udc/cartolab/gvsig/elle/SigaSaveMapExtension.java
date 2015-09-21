package es.udc.cartolab.gvsig.elle;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.udc.cartolab.gvsig.elle.gui.wizard.WizardWindow;
import es.udc.cartolab.gvsig.elle.gui.wizard.save.SaveMapWizard;
import es.udc.cartolab.gvsig.elle.gui.wizard.save.SigaSaveMapWizardComponent;

public class SigaSaveMapExtension extends SaveMapExtension {

    @SuppressWarnings("serial")
    @Override
    public void execute(String actionCommand) {
	View view = (View) PluginServices.getMDIManager().getActiveWindow();
	WizardWindow wizard = new SaveMapWizard(view) {
	    @Override
	    protected void addWizardComponents() {
		views.add(new SigaSaveMapWizardComponent(properties));
	    };
	};
	wizard.open();
    }

}
