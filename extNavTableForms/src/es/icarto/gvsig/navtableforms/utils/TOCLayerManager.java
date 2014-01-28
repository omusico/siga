package es.icarto.gvsig.navtableforms.utils;

import java.util.ArrayList;
import java.util.List;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;

public class TOCLayerManager {

    private FLayers layersInTOC = null;
    private MapControl mapControl = null;
    FLyrVect layer;

    public TOCLayerManager() {
	IWindow[] windows = PluginServices.getMDIManager().getOrderedWindows();
	BaseView view = null;
	for (IWindow w : windows) {
	    if (w instanceof BaseView) {
		view = (BaseView) w;
		break;
	    }
	}
	if(view != null) {
	    mapControl = view.getMapControl();
	    layersInTOC = mapControl.getMapContext().getLayers();
	}
    }

    public void setVisibleAllLayers() {
	if(layersInTOC != null) {
	    layersInTOC.setAllVisibles(true);
	}
    }

    public void setActiveAndVisibleLayer(String layerName) {
	if(layersInTOC != null) {
	    layersInTOC.setAllVisibles(false);
	    layersInTOC.setAllActives(false);
	    for (int i = 0; i < layersInTOC.getLayersCount(); i++) {
		FLayer layer = layersInTOC.getLayer(i);
		String name = layer.getName();
		if (name.equalsIgnoreCase(layerName)) {
		    layer.setVisible(true);
		    layer.setActive(true);
		}
	    }
	}
    }

    public FLyrVect getLayerByName(String layerName) {
	getLayerFromGroup(layerName, layersInTOC);
	return layer;
    }

    private void getLayerFromGroup(String layerName, FLayers layersInGroup) {
	for (int j = 0; j < layersInGroup.getLayersCount(); j++) {
	    if (layersInGroup.getLayer(j) instanceof FLayers) {
		FLayers layers = (FLayers) layersInGroup.getLayer(j);
		getLayerFromGroup(layerName, layers);
	    }
	    if (layersInGroup.getLayer(j).getName().equalsIgnoreCase(layerName)) {
		layer = (FLyrVect) layersInGroup.getLayer(j);
		break;
	    }
	}
    }

    public FLyrVect getActiveLayer() {
	if (mapControl != null) {
	    FLayer[] activeLayers = mapControl.getMapContext().getLayers()
		    .getActives();
	    for (FLayer layer : activeLayers) {
		if (layer instanceof FLyrVect) {
		    return (FLyrVect) layer;
		}
	    }
	}
	return null;
    }

    public FLyrVect[] getActiveLayers() {
	List<FLyrVect> layers = new ArrayList<FLyrVect>();
	if (mapControl != null) {
	    FLayer[] activeLayers = mapControl.getMapContext().getLayers()
		    .getActives();
	    for (FLayer layer : activeLayers) {
		if (layer instanceof FLyrVect) {
		    layers.add((FLyrVect) layer);
		}
	    }
	}
	return layers.toArray(new FLyrVect[0]);
    }

    public String getNameOfActiveLayer() {
	FLyrVect layer = getActiveLayer();
	if (layer != null) {
	    return layer.getName();
	}
	return null;
    }

}
