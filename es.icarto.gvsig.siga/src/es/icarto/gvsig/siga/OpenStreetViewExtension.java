package es.icarto.gvsig.siga;

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
    public final static String KEY = "open-street-view";

    @Override
    public void initialize() {
	// ImageIcon icon = new ImageIcon("images/open-street-view.png");
	URL iconResource = getClass().getClassLoader().getResource(
		"images/open-street-view.png");
	URL cursorResource = getClass().getClassLoader().getResource(
		"images/open-street-view-cursor.png");
	PluginServices.getIconTheme().registerDefault(KEY, iconResource);
	PluginServices.getIconTheme().registerDefault(KEY + "-cursor",
		cursorResource);
    }

    @Override
    public void execute(String actionCommand) {

	View view = (View) PluginServices.getMDIManager().getActiveWindow();
	MapControl mc = view.getMapControl();
	if (!mc.getNamesMapTools().containsKey(KEY)) {
	    StreetViewListener fpl = new StreetViewListener(mc);
	    mc.addMapTool(KEY, new PointBehavior(fpl));
	}

	mc.setTool(KEY);

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
