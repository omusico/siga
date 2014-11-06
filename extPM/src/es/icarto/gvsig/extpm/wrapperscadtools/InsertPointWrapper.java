package es.icarto.gvsig.extpm.wrapperscadtools;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.InsertPointExtension;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.icarto.gvsig.extpm.preferences.Preferences;
import es.icarto.gvsig.extpm.utils.managers.ToggleEditingManager;
import es.icarto.gvsig.navtableforms.utils.TOCLayerManager;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class InsertPointWrapper extends InsertPointExtension {
    @Override
    public void initialize() {
	super.initialize();
    }

    @Override
    public void execute(String s) {
	setActiveLayerForPM();
	ToggleEditingManager tem = new ToggleEditingManager();
	FLayer activeLayer = getActiveLayer();
	if (!activeLayer.isEditing()) {
	    tem.startEditing(activeLayer);
	}
	super.execute(s);
    }

    @Override
    public boolean isEnabled() {
	if ((DBSession.getCurrentSession() != null) && hasView()
		&& isLayerLoaded(Preferences.PM_LAYER_NAME)) {
	    return true;
	} else {
	    return false;
	}
    }

    @Override
    public boolean isVisible() {
	return true;
    }

    private FLayer getActiveLayer() {
	BaseView view = (BaseView) PluginServices.getMDIManager()
		.getActiveWindow();
	MapControl mapControl = view.getMapControl();
	FLayers flayers = mapControl.getMapContext().getLayers();
	FLyrVect actLayer = null;
	for (int i = 0; i < flayers.getActives().length; i++) {

	    if (!(flayers.getActives()[i] instanceof FLayers)) {
		actLayer = (FLyrVect) flayers.getActives()[i];
	    }
	}
	return actLayer;
    }

    public void setActiveLayerForPM() {
	BaseView view = (BaseView) PluginServices.getMDIManager()
		.getActiveWindow();
	MapControl mapControl = view.getMapControl();
	FLayers layersInTOC = mapControl.getMapContext().getLayers();
	layersInTOC.setAllActives(false);
	for (int i = 0; i < layersInTOC.getLayersCount(); i++) {
	    String layerName = layersInTOC.getLayer(i).getName();
	    if (layerName.equalsIgnoreCase(Preferences.PM_LAYER_NAME)) {
		layersInTOC.getLayer(i).setActive(true);
	    }
	}
    }

    private boolean isLayerLoaded(String layerName) {
	TOCLayerManager toc = new TOCLayerManager();
	FLyrVect layer = toc.getLayerByName(layerName);
	if (layer == null) {
	    return false;
	}
	return true;
    }

    private boolean hasView() {
	IWindow window = PluginServices.getMDIManager().getActiveWindow();
	if (window instanceof View) {
	    return true;
	}
	return false;
    }
}
