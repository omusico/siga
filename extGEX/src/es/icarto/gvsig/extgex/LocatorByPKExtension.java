package es.icarto.gvsig.extgex;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.icarto.gvsig.extgex.locators.LocatorByPK;
import es.icarto.gvsig.extgex.preferences.DBNames;
import es.icarto.gvsig.extgex.utils.managers.TOCLayerManager;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class LocatorByPKExtension extends Extension {

    @Override
    public void execute(String actionCommand) {
	LocatorByPK pkLocator = new LocatorByPK();
	pkLocator.open();
    }

    @Override
    public void initialize() {
	// Nothing to do
    }

    @Override
    public boolean isEnabled() {
	if((DBSession.getCurrentSession() != null) && 
		hasView() && 
		isLayerLoaded()) {
	    return true;
	}
	return false;
    }

    @Override
    public boolean isVisible() {
	return true;
    }

    private boolean isLayerLoaded() {
	TOCLayerManager toc = new TOCLayerManager();
	if(toc.getLayerByName(DBNames.LAYER_PKS) != null) {
	    return true;
	}
	return false;
    }

    private boolean hasView() {
	IWindow f = PluginServices.getMDIManager().getActiveWindow();
	if (f instanceof View) {
	    return true;
	}
	return false;
    }

}
