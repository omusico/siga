package es.icarto.gvsig.siga;

import java.net.URL;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.project.documents.view.gui.View;

public abstract class AbstractExtension extends Extension {

    public final String id = this.getClass().getName();

    protected void registerIcon(String iconName) {
	URL iconUrl = this.getClass().getClassLoader()
		.getResource("images/" + iconName.toLowerCase() + ".png");
	PluginServices.getIconTheme().registerDefault(iconName, iconUrl);
    }

    @Override
    public void initialize() {
	registerIcon(id);
    }

    @Override
    public boolean isVisible() {
	return true;
    }

    /**
     * Returns the active window if is a View. Returns null elsewhere
     */
    protected View getView() {
	IWindow iWindow = PluginServices.getMDIManager().getActiveWindow();
	if (iWindow instanceof View) {
	    return (View) iWindow;
	}
	return null;
    }

}