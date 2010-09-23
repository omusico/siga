/*
 * Copyright (c) 2010. Cartolab (Universidade da Coruña)
 * 
 * This file is part of extELLE
 * 
 * extELLE is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 * 
 * extELLE is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with extELLE.
 * If not, see <http://www.gnu.org/licenses/>.
 */

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
		LoadMapWizard wizard = new LoadMapWizard((View) PluginServices.getMDIManager().getActiveWindow());
		wizard.open();
	}

	public void initialize() {

		//		carga la pestaña en añadir capa
		AddLayer.addWizard(ElleWizard.class);
	}

	public boolean isEnabled() {
		return PluginServices.getMDIManager().getActiveWindow() instanceof View;
	}

	public boolean isVisible() {
		DBSession dbs = DBSession.getCurrentSession();
		return dbs!=null;
	}

	@Override
	public IPreference[] getPreferencesPages() {
		IPreference[] preferences=new IPreference[1];
		preferences[0]=ellePreferencesPage;
		return preferences;
	}



}
