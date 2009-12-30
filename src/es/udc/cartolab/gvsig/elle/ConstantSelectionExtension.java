package es.udc.cartolab.gvsig.elle;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;

import es.udc.cartolab.gvsig.elle.gui.ConstantLabel;
import es.udc.cartolab.gvsig.elle.gui.ConstantSelectionWindow;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class ConstantSelectionExtension extends Extension {
	
	private ConstantLabel label = new ConstantLabel();

	public void execute(String actionCommand) {
		// TODO Auto-generated method stub
		
		ConstantSelectionWindow window = new ConstantSelectionWindow();
		PluginServices.getMDIManager().addCentredWindow(window);
		
		
	}

	public void initialize() {
		// TODO Auto-generated method stub
		PluginServices.getMainFrame().addStatusBarControl(ConstantSelectionExtension.class, label);
	}

	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isVisible() {
		// TODO Auto-generated method stub
		DBSession dbs = DBSession.getCurrentSession();
		return dbs != null;
	}

}
