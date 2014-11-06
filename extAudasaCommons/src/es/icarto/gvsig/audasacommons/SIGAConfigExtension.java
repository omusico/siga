package es.icarto.gvsig.audasacommons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.preferences.IPreference;
import com.iver.andami.preferences.IPreferenceExtension;
import com.iver.cit.gvsig.About;
import com.iver.cit.gvsig.gui.panels.FPanelAbout;

import es.icarto.gvsig.utils.SIGAFormatter;
import es.udc.cartolab.gvsig.elle.ConfigExtension;
import es.udc.cartolab.gvsig.elle.utils.MapFilter;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class SIGAConfigExtension extends Extension implements
	IPreferenceExtension {

    @Override
    public void initialize() {
	About about = (About) PluginServices.getExtension(About.class);
	FPanelAbout panelAbout = about.getAboutPanel();
	java.net.URL aboutURL = this.getClass().getResource("/about.htm");
	panelAbout.addAboutUrl("AUDASA", aboutURL);
    }

    @Override
    public void postInitialize() {
	ConfigExtension configExt = (ConfigExtension) PluginServices
		.getExtension(ConfigExtension.class);
	configExt.setWizardTitle("AUDASA");
	configExt.setMapFilter(new MapFilter() {
	    @Override
	    public String[] filter(String[] maps) {
		List<String> mapsToShow = new ArrayList<String>();
		for (int i = 0; i < maps.length; i++) {
		    if (maps[i].startsWith("BDD_")) {
			mapsToShow.add(maps[i]);
		    }
		}
		Collections.sort(mapsToShow);
		return mapsToShow.toArray(new String[0]);
	    }
	});
	DBSession.setFormatter(new SIGAFormatter());
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
