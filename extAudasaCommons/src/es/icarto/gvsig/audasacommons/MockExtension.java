package es.icarto.gvsig.audasacommons;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.preferences.IPreference;
import com.iver.andami.preferences.IPreferenceExtension;
import com.iver.cit.gvsig.About;
import com.iver.cit.gvsig.gui.panels.FPanelAbout;

public class MockExtension extends Extension implements IPreferenceExtension {

    @Override
    public void initialize() {
	About about = (About) PluginServices.getExtension(About.class);
	FPanelAbout panelAbout = about.getAboutPanel();
	java.net.URL aboutURL = this.getClass().getResource("/about.htm");
	panelAbout.addAboutUrl("AUDASA", aboutURL);
    }

    @Override
    public void execute(String actionCommand) {
    }

    @Override
    public boolean isEnabled() {
	return false;
    }

    @Override
    public boolean isVisible() {
	return false;
    }

    @Override
    public IPreference[] getPreferencesPages() {
	IPreference[] preferences = new IPreference[1];
	preferences[0] = new PreferencesPage();
	return preferences;
    }

}
