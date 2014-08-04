package es.icarto.gvsig.audasacommons;

import java.net.URL;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.tools.Behavior.PointBehavior;
import com.iver.cit.gvsig.project.documents.view.gui.View;

/**
 * Opens the default browser in the Street View location selected by the user
 * clicking on the view
 * 
 */
public class OpenStreetViewExtension extends Extension {
    private final static String iconKey = "open-street-view";

    @Override
    public void initialize() {
	// ImageIcon icon = new ImageIcon("images/open-street-view.png");
	URL resource = getClass().getClassLoader().getResource(
		"images/open-street-view.png");
	PluginServices.getIconTheme().registerDefault(iconKey, resource);
    }

    @Override
    public void execute(String actionCommand) {

	View view = (View) PluginServices.getMDIManager().getActiveWindow();
	MapControl mc = view.getMapControl();
	if (!mc.getNamesMapTools().containsKey(iconKey)) {
	    StreetViewListener fpl = new StreetViewListener(mc);
	    mc.addMapTool(iconKey, new PointBehavior(fpl));
	}

	mc.setTool(iconKey);

    }

    @Override
    public boolean isEnabled() {
	IWindow iWindow = PluginServices.getMDIManager().getActiveWindow();
	return iWindow instanceof View;
    }

    @Override
    public boolean isVisible() {
	return true;
    }
}
