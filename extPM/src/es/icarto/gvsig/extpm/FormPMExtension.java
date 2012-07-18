package es.icarto.gvsig.extpm;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.listeners.CADListenerManager;
import com.iver.cit.gvsig.listeners.EndGeometryListener;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.icarto.gvsig.extpm.preferences.Preferences;
import es.icarto.gvsig.extgex.utils.managers.TOCLayerManager;
import es.icarto.gvsig.extpm.forms.FormPM;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class FormPMExtension extends Extension {
    
    private FLyrVect layer;
    private FormPM dialog;
    
    public static final String KEY_NAME = "es.udc.cartolab.gvsig.navtable";

    @Override
    public void execute(String actionCommand) {
	DBSession.getCurrentSession().setSchema(Preferences.PM_SCHEMA);
	layer = getPMLayer();
	dialog = new FormPM(layer);
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
	    if (layer instanceof FLyrVect) {
		FLyrVect l = (FLyrVect) layer;
		l.setActive(true);
		dialog = new FormPM(getPMLayer());
		if (dialog.init()) {
		    PluginServices.getMDIManager().addWindow(dialog);
		}
	    }
	}
    }
}
