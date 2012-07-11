package es.icarto.gvsig.extpm.wrapperscadtools;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.InsertPointExtension;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;

import es.udc.cartolab.gvsig.navtable.ToggleEditing;


public class InsertPointWrapper extends InsertPointExtension {
    public void initialize() {
	super.initialize();
    }

    public void execute(String s) {
	setActiveLayerForPM();
	ToggleEditing te = new ToggleEditing();
	FLayer activeLayer = getActiveLayer();
	if (!activeLayer.isEditing()) {
	    te.startEditing(activeLayer);
	}
	super.execute(s);
    }

    public boolean isEnabled() {
	return true;
    }

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
	    if (layerName.equalsIgnoreCase("PM")) {
		layersInTOC.getLayer(i).setActive(true);
	    }	
	}
    }
}
