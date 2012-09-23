package es.icarto.gvsig.extgia;

import javax.swing.JOptionPane;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.extgia.forms.taludes.TaludesForm;
import es.icarto.gvsig.extgia.preferences.Preferences;
import es.icarto.gvsig.navtableforms.ormlite.ORMLite;
import es.icarto.gvsig.navtableforms.utils.TOCLayerManager;

public class FormLauncherExtension extends Extension {

    private FLyrVect layer;

    @Override
    public void execute(String actionCommand) {
	// TODO. check if layer and tables are correctly loaded

	if (actionCommand.equals("form_launcher")) {
	    new Foo().execute();
	    actionCommand = "taludes";
	}

	this.layer = getLayerFromTOC(actionCommand);
	if (this.layer != null) {
	    final TaludesForm form = new TaludesForm(this.layer);
	    if (form.init()) {
		PluginServices.getMDIManager().addWindow(form);
	    }
	} else {
	    JOptionPane.showMessageDialog(null, "La capa " + actionCommand
		    + " no está cargada en el TOC", "Capa no encontrada",
		    JOptionPane.ERROR_MESSAGE);
	}
    }

    private FLyrVect getLayerFromTOC(String actionCommand) {
	final String layerName = ORMLite
		.getDataBaseObject(
			Preferences.getPreferences().getXMLFilePath())
		.getTable(actionCommand).getTableName();
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

    @Override
    public boolean isVisible() {
	return true;
    }

}
