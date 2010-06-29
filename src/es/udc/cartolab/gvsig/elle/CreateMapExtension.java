package es.udc.cartolab.gvsig.elle;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.udc.cartolab.gvsig.elle.gui.SaveMapWindow;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class CreateMapExtension extends Extension {

	@Override
	public void execute(String actionCommand) {
		View view = (View) PluginServices.getMDIManager().getActiveWindow();
		SaveMapWindow window = new SaveMapWindow(view);
		PluginServices.getMDIManager().addWindow(window);
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isEnabled() {
		DBSession session = DBSession.getCurrentSession();
		if (session != null) {
			return session.getDBUser().isAdmin();
		}
		return false;
	}

	@Override
	public boolean isVisible() {
		return PluginServices.getMDIManager().getActiveWindow() instanceof View;
	}

}
