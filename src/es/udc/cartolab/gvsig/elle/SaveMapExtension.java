package es.udc.cartolab.gvsig.elle;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.udc.cartolab.gvsig.elle.gui.wizard.save.SaveMapWizard;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class SaveMapExtension extends Extension {

	@Override
	public void execute(String actionCommand) {
		View view = (View) PluginServices.getMDIManager().getActiveWindow();
		SaveMapWizard wizard = new SaveMapWizard(view);
		wizard.open();
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isEnabled() {
		if (PluginServices.getMDIManager().getActiveWindow() instanceof View) {
			FLayers layers = ((View) PluginServices.getMDIManager().getActiveWindow()).getMapControl().getMapContext().getLayers();
			return layers.getLayersCount() > 0;
		}
		return false;
	}

	@Override
	public boolean isVisible() {
		if (PluginServices.getMDIManager().getActiveWindow() instanceof View) {
			DBSession session = DBSession.getCurrentSession();
			if (session != null) {
				return session.getDBUser().isAdmin();
			}
		}
		return false;
	}

}
