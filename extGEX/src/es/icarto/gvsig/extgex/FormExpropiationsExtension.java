package es.icarto.gvsig.extgex;

import javax.swing.JOptionPane;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.plugins.IExtension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.About;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.gui.panels.FPanelAbout;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.icarto.gvsig.extgex.forms.FormExpropiations;
import es.icarto.gvsig.extgex.preferences.DBNames;
import es.icarto.gvsig.extgex.utils.AlphanumericTableLoader;
import es.icarto.gvsig.extgex.utils.managers.TOCLayerManager;
import es.udc.cartolab.gvsig.users.DBConnectionExtension;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class FormExpropiationsExtension extends Extension {

    private FLyrVect layer;
    private FormExpropiations dialog;

    public void execute(String actionCommand) {
	DBSession.getCurrentSession().setSchema(DBNames.EXPROPIATIONS_SCHEMA);
	if (AlphanumericTableLoader.loadTables()) {
	    layer = getLayer();
	    dialog = new FormExpropiations(layer, null);
	    if (dialog.init()) {
		PluginServices.getMDIManager().addWindow(dialog);
	    }
	} else {
	    JOptionPane.showMessageDialog(null, PluginServices.getText(this,
		    "alphanumeric_table_no_loaded"));
	}
    }

    private FLyrVect getLayer() {
	String layerName = DBNames.LAYER_FINCAS;
	TOCLayerManager toc = new TOCLayerManager();
	return toc.getLayerByName(layerName);
    }

    protected void registerIcons() {
	PluginServices.getIconTheme().registerDefault(
		"extgex-expropiations",
		this.getClass().getClassLoader()
		.getResource("images/extgpe.png"));
    }

    public void initialize() {
	About about = (About) PluginServices.getExtension(About.class);
	FPanelAbout panelAbout = about.getAboutPanel();
	java.net.URL aboutURL = this.getClass().getResource("/about.htm");
	panelAbout.addAboutUrl("GEX", aboutURL);

	registerIcons();
    }

    @Override
    public void postInitialize() {
	PluginServices.getMDIManager().closeAllWindows();
	// launch dbconnection dialog
	IExtension dbconnection = PluginServices
		.getExtension(DBConnectionExtension.class);
	dbconnection.execute(null);
    }

    public boolean isEnabled() {
	if ((DBSession.getCurrentSession() != null) &&
		hasView() &&
		isLayerLoaded(DBNames.LAYER_FINCAS)) {
	    return true;
	} else {
	    return false;
	}
    }

    public boolean isVisible() {
	return true;
    }


    private boolean isLayerLoaded(String layerName) {
	TOCLayerManager toc = new TOCLayerManager();
	FLyrVect layer = toc.getLayerByName(layerName);
	if(layer == null) {
	    return false;
	}
	return true;
    }

    private boolean hasView() {
	IWindow window = PluginServices.getMDIManager().getActiveWindow();
	if(window instanceof View) {
	    return true;
	}
	return false;
    }

}
