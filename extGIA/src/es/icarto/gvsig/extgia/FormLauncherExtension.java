package es.icarto.gvsig.extgia;

import javax.swing.JOptionPane;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.extgia.forms.utils.LaunchGIAForms;
import es.icarto.gvsig.navtableforms.utils.TOCLayerManager;

public class FormLauncherExtension extends Extension {

    private FLyrVect layer;

    @Override
    public void execute(String actionCommand) {
	this.layer = getLayerFromTOC(actionCommand);
	if (this.layer != null) {
	    LaunchGIAForms.callFormDependingOfLayer(actionCommand, false);
	} else {
	    JOptionPane.showMessageDialog(null, "La capa " + actionCommand
		    + " no est� cargada en el TOC", "Capa no encontrada",
		    JOptionPane.ERROR_MESSAGE);
	}
    }

    private FLyrVect getLayerFromTOC(String actionCommand) {
	final TOCLayerManager toc = new TOCLayerManager();
	return toc.getLayerByName(actionCommand);
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
