package es.icarto.gvsig.extpm;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.CADExtension;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.gui.cad.CADTool;
import com.iver.cit.gvsig.gui.cad.tools.EIELPolylineCADTool;
import com.iver.cit.gvsig.gui.cad.tools.PointCADTool;
import com.iver.cit.gvsig.listeners.CADListenerManager;
import com.iver.cit.gvsig.listeners.EndGeometryListener;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.icarto.gvsig.extgex.preferences.DBNames;
import es.icarto.gvsig.extgex.utils.AlphanumericTableLoader;
import es.icarto.gvsig.extpm.forms.FormPM;
import es.icarto.gvsig.extpm.preferences.Preferences;
import es.icarto.gvsig.extpm.utils.managers.TOCLayerManager;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class FormPMExtension extends Extension {

    private FLyrVect layer;
    private FormPM dialog;

    public static final String KEY_NAME = "es.udc.cartolab.gvsig.navtable";

    @Override
    public void execute(String actionCommand) {
	DBSession.getCurrentSession().setSchema(Preferences.PM_SCHEMA);
	layer = getPMLayer();
	dialog = new FormPM(layer, false, -1);
	if (dialog.init()) {
	    PluginServices.getMDIManager().addWindow(dialog);
	}
    }

    @Override
    public void initialize() {
	NTEndGeometryListener listener = new NTEndGeometryListener();
	CADListenerManager.addEndGeometryListener(KEY_NAME, listener);
    }

    @Override
    public boolean isEnabled() {
	if ((DBSession.getCurrentSession() != null) &&
		hasView() &&
		isLayerLoaded(Preferences.PM_LAYER_NAME)) {
	    return true;
	} else {
	    return false;
	}
    }

    @Override
    public boolean isVisible() {
	return true;
    }

    protected void registerIcons() {
	PluginServices.getIconTheme().registerDefault(
		"extpm-pmForm",
		this.getClass().getClassLoader()
		.getResource("images/pm_form.png"));
    }

    private FLyrVect getPMLayer() {
	TOCLayerManager toc = new TOCLayerManager();
	return toc.getLayerByName(Preferences.PM_LAYER_NAME);
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

	public void endGeometry(FLayer layer) {
	    if (layer.getName().equalsIgnoreCase(Preferences.PM_LAYER_NAME)) {
		CADTool cadTool = CADExtension.getCADTool();
		int insertedRow = ((PointCADTool) cadTool).getInsertedRow();
		if (layer instanceof FLyrVect) {
		    FLyrVect l = (FLyrVect) layer;
		    l.setActive(true);
		    dialog = new FormPM(getPMLayer(), true, insertedRow);
		    if (dialog.init()) {
			PluginServices.getMDIManager().addWindow(dialog);
			dialog.last();
		    }
		}
	    }else if (layer.getName().equalsIgnoreCase("Reversiones")) {
		FLyrVect l = (FLyrVect) layer;
		CADTool cadTool = CADExtension.getCADTool();
		IGeometry insertedGeom = null;
		if (cadTool instanceof EIELPolylineCADTool) {
		    insertedGeom = ((EIELPolylineCADTool) cadTool).getInsertedGeom();
		}
		es.icarto.gvsig.extgex.forms.FormReversions dialog = new es.icarto.gvsig.extgex.forms.FormReversions((FLyrVect) layer, insertedGeom);
		if (dialog.init()) {
		    PluginServices.getMDIManager().addWindow(dialog);
		    dialog.last();
		}
	    }else if (layer.getName().equalsIgnoreCase("Fincas")) {
		FLyrVect l = (FLyrVect) layer;
		DBSession.getCurrentSession().setSchema(DBNames.EXPROPIATIONS_SCHEMA);
		if (AlphanumericTableLoader.loadTables()) {
		    es.icarto.gvsig.extgex.forms.FormExpropiations dialog = new es.icarto.gvsig.extgex.forms.FormExpropiations((FLyrVect) layer);
		    if (dialog.init()) {
			PluginServices.getMDIManager().addWindow(dialog);
			dialog.last();
		    }
		}
	    }
	}
    }
}
