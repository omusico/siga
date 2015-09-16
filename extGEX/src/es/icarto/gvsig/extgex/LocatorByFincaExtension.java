package es.icarto.gvsig.extgex;

import javax.swing.JOptionPane;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.icarto.gvsig.extgex.locators.LocatorByFinca;
import es.icarto.gvsig.extgex.preferences.DBNames;
import es.icarto.gvsig.navtableforms.utils.TOCLayerManager;
import es.icarto.gvsig.siga.AbstractExtension;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class LocatorByFincaExtension extends AbstractExtension {

    @Override
    public void execute(String actionCommand) {
	LocatorByFinca fincaLocator = new LocatorByFinca();
	if (fincaLocator.init()) {
	    PluginServices.getMDIManager().addCentredWindow(fincaLocator);
	} else {
	    JOptionPane.showMessageDialog(null, PluginServices.getText(this,
		    "alphanumeric_table_no_loaded"));
	}
    }

    @Override
    public void initialize() {
	// Nothing to do
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
	if (toc.getLayerByName(DBNames.LAYER_FINCAS) != null) {
	    return true;
	}
	return false;
    }

}
