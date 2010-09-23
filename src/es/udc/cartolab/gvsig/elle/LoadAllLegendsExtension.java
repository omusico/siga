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
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.udc.cartolab.gvsig.elle.gui.wizard.load.LoadLegendWizard;

public class LoadAllLegendsExtension extends Extension {

	public void execute(String actionCommand) {
		// TODO Auto-generated method stub
		//		LoadAllLegendsDialog dialog;
		//		try {
		//			dialog = new LoadAllLegendsDialog(
		//					(View)PluginServices.getMDIManager().getActiveWindow());
		//			PluginServices.getMDIManager().addCentredWindow(dialog);
		//		} catch (Exception e) {
		//			// TODO Auto-generated catch block
		//			JOptionPane.showMessageDialog(null,
		//				    e.getMessage(),
		//				    "Error",
		//				    JOptionPane.ERROR_MESSAGE);
		//		}
		LoadLegendWizard wizard = new LoadLegendWizard((View) PluginServices.getMDIManager().getActiveWindow());
		wizard.open();

	}

	public void initialize() {
		// TODO Auto-generated method stub

	}

	public boolean isEnabled() {
		// TODO Auto-generated method stub
		IWindow w = PluginServices.getMDIManager().getActiveWindow();
		if (w instanceof View) {
			FLayers layers = ((View) w).getMapControl().getMapContext().getLayers();
			return layers.getLayersCount() > 0;
		}
		return false;
	}

	public boolean isVisible() {
		// TODO Auto-generated method stub
		IWindow w = PluginServices.getMDIManager().getActiveWindow();
		return w instanceof View;
	}

}
