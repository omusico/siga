package es.icarto.gvsig.extgex;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.plugins.IExtension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.CADExtension;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.gui.cad.CADTool;
import com.iver.cit.gvsig.gui.cad.tools.PolylineCADTool;
import com.iver.cit.gvsig.listeners.CADListenerManager;
import com.iver.cit.gvsig.listeners.EndGeometryListener;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.icarto.gvsig.extgex.forms.FormExpropiationLine;
import es.icarto.gvsig.extgex.forms.FormExpropiations;
import es.icarto.gvsig.extgex.preferences.DBNames;
import es.icarto.gvsig.extgex.utils.managers.TOCLayerManager;
import es.icarto.gvsig.extgia.forms.utils.LaunchGIAForms;
import es.udc.cartolab.gvsig.users.DBConnectionExtension;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class FormExpropiationsExtension extends Extension {

    public static final String KEY_NAME = "es.udc.cartolab.gvsig.navtable";

    private FLyrVect layer;
    private FormExpropiations dialog;

    public void execute(String actionCommand) {
	DBSession.getCurrentSession().setSchema(DBNames.EXPROPIATIONS_SCHEMA);
	layer = getLayer();
	dialog = new FormExpropiations(layer, null);
	if (dialog.init()) {
	    PluginServices.getMDIManager().addWindow(dialog);
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
	NTEndGeometryListener listener = new NTEndGeometryListener();
	CADListenerManager.addEndGeometryListener(KEY_NAME, listener);
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

    private class NTEndGeometryListener implements EndGeometryListener {

	public void endGeometry(FLayer layer, String cadToolKey) {
	    if (layer.getName().equalsIgnoreCase("Reversiones")) {
		CADTool cadTool = CADExtension.getCADTool();
		IGeometry insertedGeom = null;
		if (cadTool instanceof PolylineCADTool) {
		    insertedGeom = ((PolylineCADTool) cadTool).getGeometry();
		}
		es.icarto.gvsig.extgex.forms.FormReversions dialog = new es.icarto.gvsig.extgex.forms.FormReversions((FLyrVect) layer, insertedGeom);
		if (dialog.init()) {
		    PluginServices.getMDIManager().addCentredWindow(dialog);
		    dialog.last();
		}
	    }else if (layer.getName().equalsIgnoreCase("Fincas")) {
		DBSession.getCurrentSession().setSchema(DBNames.EXPROPIATIONS_SCHEMA);
		IGeometry insertedGeom = null;
		CADTool cadTool = CADExtension.getCADTool();
		if (cadTool instanceof PolylineCADTool) {
		    insertedGeom = ((PolylineCADTool) cadTool).getGeometry();
		}
		es.icarto.gvsig.extgex.forms.FormExpropiations dialog = new es.icarto.gvsig.extgex.forms.FormExpropiations((FLyrVect) layer, insertedGeom);
		if (dialog.init()) {
		    PluginServices.getMDIManager().addCentredWindow(dialog);
		    dialog.last();
		}
	    } else if (layer.getName().equalsIgnoreCase(FormExpropiationLine.LAYER_TOC_NAME)) {
		FormExpropiationLine dialog = new FormExpropiationLine((FLyrVect) layer);
		if (dialog.init() ) {
		    PluginServices.getMDIManager().addCentredWindow(dialog);
		    dialog.last();
		}
	    }
	    
	    else {
		LaunchGIAForms.callFormDependingOfLayer(layer.getName(), true);
	    }
	}

    }
}
