package es.icarto.gvsig.extgex;

import es.icarto.gvsig.extgex.locators.LocatorByPK;
import es.icarto.gvsig.extgex.preferences.DBNames;
import es.icarto.gvsig.navtableforms.utils.TOCLayerManager;
import es.icarto.gvsig.siga.AbstractExtension;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class LocatorByPKExtension extends AbstractExtension {
    
    @Override
    public void execute(String actionCommand) {
	LocatorByPK pkLocator = new LocatorByPK();
	pkLocator.openDialog();
    }

    @Override
    public boolean isEnabled() {
	if ((DBSession.getCurrentSession() != null) && (getView() != null) && isLayerLoaded()) {
	    return true;
	}
	return false;
    }

    private boolean isLayerLoaded() {
	TOCLayerManager toc = new TOCLayerManager();
	if (toc.getLayerByName(DBNames.LAYER_PKS) != null) {
	    return true;
	}
	return false;
    }

}
