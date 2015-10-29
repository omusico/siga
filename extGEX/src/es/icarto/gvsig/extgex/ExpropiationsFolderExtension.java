package es.icarto.gvsig.extgex;

import java.awt.Component;
import java.io.File;

import javax.swing.JOptionPane;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.utiles.XMLEntity;

import es.icarto.gvsig.siga.PreferencesPage;
import es.icarto.gvsig.utils.DesktopApi;

public class ExpropiationsFolderExtension extends Extension {

    @Override
    public void initialize() {
	// Nothing to do here
    }

    @Override
    public void execute(String actionCommand) {
	String folderStr = null;
	XMLEntity xml = PluginServices.getPluginServices(
		PreferencesPage.PLUGIN_NAME).getPersistentXML();
	if (xml.contains(PreferencesPage.EXPROPIATATIONS_FOLDER_KEY)) {
	    folderStr = xml
		    .getStringProperty(PreferencesPage.EXPROPIATATIONS_FOLDER_KEY);
	}

	File folder = new File(folderStr);
	if (!folder.isDirectory()) {
	    showError("Configure correctamente el directorio en la página de preferencias");
	    return;
	}
	DesktopApi.open(folder);
    }

    @Override
    public boolean isEnabled() {
	return true;
    }

    @Override
    public boolean isVisible() {
	return true;
    }

    private void showError(String msg) {
	JOptionPane.showMessageDialog(
		(Component) PluginServices.getMainFrame(), msg, "Error",
		JOptionPane.ERROR_MESSAGE);
    }

}
