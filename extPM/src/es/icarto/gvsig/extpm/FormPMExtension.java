package es.icarto.gvsig.extpm;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.extpm.utils.managers.TOCLayerManager;
import es.icarto.gvsig.extpm.forms.FormPM;

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
	// TODO Auto-generated method stub
	return true;
    }

    @Override
    public boolean isVisible() {
	// TODO Auto-generated method stub
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

}
