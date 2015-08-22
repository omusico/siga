package es.icarto.gvsig.audasacommons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.udc.cartolab.gvsig.elle.gui.wizard.WizardComponent;
import es.udc.cartolab.gvsig.elle.gui.wizard.load.LoadConstantsWizardComponent;
import es.udc.cartolab.gvsig.elle.gui.wizard.load.SigaLoadMapWizard;

public class ConstantReloadExtension extends AbstractExtension {

    @Override
    public void execute(String actionCommand) {
	View view = (View) PluginServices.getMDIManager().getActiveWindow();

	Map<String, Object> properties = new HashMap<String, Object>();
	properties.put(LoadConstantsWizardComponent.PROPERTY_VIEW, view);

	List<WizardComponent> cpms = new ArrayList<WizardComponent>(1);
	LoadConstantsWizardComponent constantsCmp = new LoadConstantsWizardComponent(
		properties);
	constantsCmp.setReload(true);
	cpms.add(constantsCmp);
	new SigaLoadMapWizard(view, cpms).open();
    }

    @Override
    public boolean isEnabled() {
	return getView() != null;
    }

}
