package es.icarto.gvsig.extgia;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.extgia.preferences.Preferences;
import es.icarto.gvsig.navtableforms.ormlite.ORMLite;
import es.icarto.gvsig.navtableforms.utils.TOCLayerManager;

public class FormLauncherExtension extends Extension {

    private FLyrVect layer;

    @Override
    public void execute(String actionCommand) {
	this.layer = getLayerFromTOC();
	final TaludesForm form = new TaludesForm(this.layer);
	if (form.init()) {
	    PluginServices.getMDIManager().addWindow(form);
	}

    }

    private FLyrVect getLayerFromTOC() {
	final String layerName = ORMLite
		.getDataBaseObject(Preferences.XMLDATAFILE_PATH)
		.getTable("taludes").getTableName();
	final TOCLayerManager toc = new TOCLayerManager();
	return toc.getLayerByName(layerName);
    }

    protected void registerIcons() {
	PluginServices.getIconTheme().registerDefault(
		"extgia-openform",
		this.getClass().getClassLoader()
			.getResource("images/extgia-openform.png"));
    }

    @Override
    public void initialize() {
	registerIcons();
    }

    @Override
    public boolean isEnabled() {
	return true;
    }

    private boolean isExampleDataSetLoaded() {
	if (getLayerFromTOC() == null) {
	    return false;
	}
	return true;
    }

    @Override
    public boolean isVisible() {
	return true;
    }

}
