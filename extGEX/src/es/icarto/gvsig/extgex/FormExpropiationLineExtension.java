package es.icarto.gvsig.extgex;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.icarto.gvsig.extgex.forms.FormExpropiationLine;
import es.icarto.gvsig.extgex.utils.managers.TOCLayerManager;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class FormExpropiationLineExtension extends Extension {

    @Override
    public void initialize() {
    }

    @Override
    public void execute(String actionCommand) {
	FLyrVect layer = getLayer();
	FormExpropiationLine dialog = new FormExpropiationLine(layer);
	if (dialog.init()) {
	    PluginServices.getMDIManager().addCentredWindow(dialog);
	}
    }

    private FLyrVect getLayer() {
	String layerName = FormExpropiationLine.LAYER_TOC_NAME;
	TOCLayerManager toc = new TOCLayerManager();
	return toc.getLayerByName(layerName);
    }

    @Override
    public boolean isEnabled() {
	if ((DBSession.getCurrentSession() != null) && hasView()
		&& isLayerLoaded(FormExpropiationLine.LAYER_TOC_NAME)) {
	    return true;
	} else {
	    return false;
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

    @Override
    public boolean isVisible() {
	return true;
    }

}
