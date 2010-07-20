package es.udc.cartolab.gvsig.elle;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.udc.cartolab.gvsig.elle.gui.wizard.save.SaveLegendsWizard;

public class SaveAllLegendsExtension extends Extension {

	public void execute(String actionCommand) {

		int option = -1;

		if (actionCommand.equals("ACTIVES")) {
			option = SaveLegendsWizard.ACTIVES;
		}
		if (actionCommand.equals("VISIBLES")) {
			option = SaveLegendsWizard.VISIBLES;
		}
		if (actionCommand.equals("ALL")) {
			option = SaveLegendsWizard.ALL;
		}

		SaveLegendsWizard w = new SaveLegendsWizard((View) PluginServices.getMDIManager().getActiveWindow(), option);
		w.open();

	}

	public void initialize() {
		// TODO Auto-generated method stub

	}

	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isVisible() {
		// TODO Auto-generated method stub
		IWindow window = PluginServices.getMDIManager().getActiveWindow();
		if (window instanceof View) {
			if (((View) window).getMapControl().getMapContext().getLayers().getLayersCount()>0) {
				return true;
			}
		}
		return false;
	}


}
