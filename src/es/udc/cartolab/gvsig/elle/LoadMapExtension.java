package es.udc.cartolab.gvsig.elle;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.preferences.IPreference;
import com.iver.andami.preferences.IPreferenceExtension;
import com.iver.cit.gvsig.AddLayer;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.udc.cartolab.gvsig.elle.gui.EllePreferencesPage;
import es.udc.cartolab.gvsig.elle.gui.ElleWizard;
import es.udc.cartolab.gvsig.elle.gui.wizard.load.LoadMapWizard;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class LoadMapExtension extends Extension implements IPreferenceExtension {

	public static EllePreferencesPage ellePreferencesPage = new EllePreferencesPage();

	public void execute(String actionCommand) {
		// TODO Auto-generated method stub
		LoadMapWizard wizard = new LoadMapWizard((View) PluginServices.getMDIManager().getActiveWindow());
		wizard.open();
	}

	public void initialize() {
		// TODO Auto-generated method stub
		//		carga la pestaña en añadir capa
		AddLayer.addWizard(ElleWizard.class);
	}

	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return PluginServices.getMDIManager().getActiveWindow() instanceof View;
	}

	public boolean isVisible() {
		// TODO Auto-generated method stub
		DBSession dbs = DBSession.getCurrentSession();
		return dbs!=null;
	}

	@Override
	public IPreference[] getPreferencesPages() {
		// TODO Auto-generated method stub
		IPreference[] preferences=new IPreference[1];
		preferences[0]=ellePreferencesPage;
		return preferences;
	}



}
