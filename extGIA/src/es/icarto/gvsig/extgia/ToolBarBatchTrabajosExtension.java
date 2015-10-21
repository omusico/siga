package es.icarto.gvsig.extgia;

import java.awt.Component;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.icarto.gvsig.extgia.forms.LaunchGIAForms;
import es.icarto.gvsig.navtableforms.utils.TOCLayerManager;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class ToolBarBatchTrabajosExtension extends Extension {

    private static final Logger logger = Logger
	    .getLogger(ToolBarBatchTrabajosExtension.class);

    private final List<String> layers = Arrays.asList(new String[] {
	    "Barrera_Rigida", "Isletas", "Taludes", "Senhalizacion_Vertical" });

    @Override
    public void initialize() {
	registerIcons();
    }

    private void showWarning(String msg) {
	JOptionPane.showMessageDialog(
		(Component) PluginServices.getMainFrame(), msg, "Aviso",
		JOptionPane.WARNING_MESSAGE);
    }

    @Override
    public void execute(String actionCommand) {

	TOCLayerManager toc = new TOCLayerManager();
	FLyrVect[] actLayers = toc.getActiveLayers();
	if (actLayers.length == 1) {
	    for (FLyrVect layer : actLayers) {
		if (layers.contains(layer.getName())) {
		    if (!hasRecordsSelected(layer)) {
			showWarning("Debe tener elementos seleccionados en la capa");
			return;
		    }
		    LaunchGIAForms.callBatchTrabajosSubFormDependingOfElement(
			    layer.getName(), layer.getName().toLowerCase()
				    + "_trabajos", null);
		    return;
		}
	    }
	}

	showWarning("Debe tener únicamente una de las siguientes capas activa en el TOC:\n Barrera_Rigida, Isletas, Taludes, Senhalizacion_Vertical");
    }

    private boolean hasRecordsSelected(FLyrVect layer) {
	try {
	    SelectableDataSource recordset = layer.getRecordset();
	    if (recordset.getSelection().cardinality() > 0) {
		return true;
	    }
	} catch (ReadDriverException e) {
	    logger.error(e.getStackTrace(), e);
	}
	return false;
    }

    @Override
    public boolean isEnabled() {
	if ((DBSession.getCurrentSession() != null) && hasView()
		&& isAnyLayerLoaded()) {
	    return true;
	} else {
	    return false;
	}
    }

    protected void registerIcons() {
	PluginServices.getIconTheme().registerDefault(
		"extgia-batchTrabajos",
		this.getClass().getClassLoader()
			.getResource("images/batch_trabajo_toolbar.png"));
    }

    private boolean hasView() {
	IWindow window = PluginServices.getMDIManager().getActiveWindow();
	if (window instanceof View) {
	    return true;
	}
	return false;
    }

    private boolean isAnyLayerLoaded() {
	IWindow iWindow = PluginServices.getMDIManager().getActiveWindow();
	if (!(iWindow instanceof View)) {
	    return false;
	}
	FLayers flayers = ((View) iWindow).getMapControl().getMapContext()
		.getLayers();
	return flayers.getLayersCount() > 0;
    }

    @Override
    public boolean isVisible() {
	return true;
    }

}
