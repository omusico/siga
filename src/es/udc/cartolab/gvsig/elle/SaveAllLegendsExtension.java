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
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.udc.cartolab.gvsig.elle.gui.wizard.save.SaveLegendsWizard;

public class SaveAllLegendsExtension extends Extension {

	public void execute(String actionCommand) {

		int option = -1;

		if (actionCommand.equals("ACTIVES")) {
			option = SaveLegendsWizard.ACTIVES;
		}
		if (actionCommand.equals("VISIBLES")) {
			option = SaveLegendsWizard.VISIBLES;
		}
		if (actionCommand.equals("ALL")) {
			option = SaveLegendsWizard.ALL;
		}

		SaveLegendsWizard w = new SaveLegendsWizard((View) PluginServices.getMDIManager().getActiveWindow(), option);
		w.open();

	}

	public void initialize() {
		// TODO Auto-generated method stub

	}

	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isVisible() {
		// TODO Auto-generated method stub
		IWindow window = PluginServices.getMDIManager().getActiveWindow();
		if (window instanceof View) {
			if (((View) window).getMapControl().getMapContext().getLayers().getLayersCount()>0) {
				return true;
			}
		}
		return false;
	}


}
