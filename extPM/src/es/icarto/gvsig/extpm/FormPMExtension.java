package es.icarto.gvsig.extpm;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.icarto.gvsig.extpm.utils.managers.TOCLayerManager;
import es.icarto.gvsig.extpm.forms.FormPM;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class FormPMExtension extends Extension {
    
    private FLyrVect layer;
    private FormPM dialog;

    @Override
    public void execute(String actionCommand) {
	layer = getLayer();
	 dialog = new FormPM(layer);
	    if (dialog.init()) {
		PluginServices.getMDIManager().addWindow(dialog);
	    }
    }

    @Override
    public void initialize() {
	// TODO Auto-generated method stub
	
    }

    @Override
    public boolean isEnabled() {
	if ((DBSession.getCurrentSession() != null) &&
		hasView() &&
		isLayerLoaded("PM")) {
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
    
    private FLyrVect getLayer() {
	String layerName = "PM";
	TOCLayerManager toc = new TOCLayerManager();
	return toc.getLayerByName(layerName);
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
}
