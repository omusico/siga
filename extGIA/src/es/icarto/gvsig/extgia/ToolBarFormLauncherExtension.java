package es.icarto.gvsig.extgia;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.icarto.gvsig.extgia.forms.utils.LaunchGIAForms;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.navtableforms.utils.TOCLayerManager;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class ToolBarFormLauncherExtension extends Extension {

    @Override
    public void initialize() {
	registerIcons();
    }

    @Override
    public void execute(String actionCommand) {
	TOCLayerManager toc = new TOCLayerManager();
	FLyrVect layer = toc.getActiveLayer();
	if (layer != null) {
	    LaunchGIAForms.callFormDependingOfLayer(layer.getName(), false);
	}
    }

    protected void registerIcons() {
	PluginServices.getIconTheme().registerDefault(
		"extgia-openform",
		this.getClass().getClassLoader()
		.getResource("images/extgia-openform.png"));
    }

    @Override
    public boolean isEnabled() {
	if ((DBSession.getCurrentSession() != null) &&
		hasView() && isAnyLayerLoaded()) {
	    return true;
	} else {
	    return false;
	}
    }

    private boolean hasView() {
	IWindow window = PluginServices.getMDIManager().getActiveWindow();
	if(window instanceof View) {
	    return true;
	}
	return false;
    }

    private boolean isAnyLayerLoaded() {
	for (int i=0; i<DBFieldNames.Elements.values().length; i++) {
	    if (isLayerLoaded(DBFieldNames.Elements.values()[i].toString())) {
		return true;
	    }
	}
	return false;

    }

    private boolean isLayerLoaded(String layerName) {
	TOCLayerManager toc = new TOCLayerManager();
	FLyrVect layer = toc.getLayerByName(layerName);
	if(layer == null) {
	    return false;
	}
	return true;
    }

    @Override
    public boolean isVisible() {
	return true;
    }

}
