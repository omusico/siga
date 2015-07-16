package es.icarto.gvsig.extgia;

import java.awt.Component;

import javax.swing.JOptionPane;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.extgia.utils.ApplySignalSimbology;
import es.icarto.gvsig.navtableforms.utils.TOCLayerManager;

public class SignalSimbologyExtension extends Extension {

    @Override
    public void initialize() {
    }

    @Override
    public void execute(String actionCommand) {
	TOCLayerManager tocLayerManager = new TOCLayerManager();
	final FLyrVect postes = tocLayerManager
		.getLayerByName(DBFieldNames.SENHALIZACION_VERTICAL_LAYERNAME);
	final FLyrVect signals = tocLayerManager
		.getLayerByName(DBFieldNames.SENHALIZACION_VERTICAL_SENHALES_LAYERNAME);
	if ((postes == null) || (signals == null)) {
	    showWarning("La capas 'Senhalizacion_Vertical' y 'Senhales' deben estar cargadas en el TOC");
	    return;
	}
	new ApplySignalSimbology(postes, signals);
    }

    private void showWarning(String msg) {
	JOptionPane.showMessageDialog(
		(Component) PluginServices.getMainFrame(), msg, "Aviso",
		JOptionPane.WARNING_MESSAGE);

    }

    @Override
    public boolean isEnabled() {
	IWindow window = PluginServices.getMDIManager().getActiveWindow();
	if (window instanceof View) {
	    return true;
	}
	return false;
    }

    @Override
    public boolean isVisible() {
	return true;
    }

}
