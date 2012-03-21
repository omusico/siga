/* gvSIG. Geographic Information System of the Valencian Government
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Government (CIT)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 * 
 */

/*
 * AUTHORS (In addition to CIT):
 * 2011 Software Colaborativo (www.scolab.es)   development
 */

package com.iver.cit.gvsig.project.documents.view.toc.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JDialog;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;
import com.iver.cit.gvsig.project.documents.view.toc.gui.ChangeName;

public class MyMoveListener extends AbstractAction {

	private FLayers lpd;
	private int oldPos;
	private int newto;
	private MapContext mapContext;
	private JDialog dlgPop;

	public MyMoveListener(FLayers lpd, int newto, int oldPos,
			MapContext mapContext, JDialog dlgPop) {
		this.lpd = lpd;
		this.newto = newto;
		this.oldPos = oldPos;
		this.mapContext = mapContext;
		this.dlgPop = dlgPop;

	}

	public void createLayerGroup(FLayer[] selectedLayers) {
		// ITocItem tocItem = (ITocItem) getNodeUserObject();
		ChangeName changename = new ChangeName(null);
		PluginServices.getMDIManager().addWindow(changename);
		if (!changename.isAccepted())
			return;
		String nombre = changename.getName();

		if (nombre != null) {

			FLayers parent = selectedLayers[0].getParentLayer();
			// FLayers newGroup = new FLayers(getMapContext(),parent);
			FLayers newGroup = mapContext.getNewGroupLayer(parent);
			newGroup.setName(nombre);
			int pos = 0;
			for (int i = 0; i < parent.getLayersCount(); i++) {
				if (parent.getLayer(i).equals(selectedLayers[0])) {
					pos = i;
					continue;
				}
			}
			for (int j = 0; j < selectedLayers.length; j++) {
				FLayer layer = selectedLayers[j];
				parent.removeLayer(layer);
				newGroup.addLayer(layer);
			}
			parent.addLayer(pos, newGroup);
		}
	}

	public void actionPerformed(ActionEvent e) {
		mapContext.beginAtomicEvent();
		try {

			FLayers all = mapContext.getLayers();

			// obtenemos la capa sobre la que hemos dejado caer la otra.
			String layerName = lpd.getLayer(newto).getName();
			int pos = -1;
			for (int j = 0; j < lpd.getLayersCount(); j++) {
				if (lpd.getLayer(j).getName().equalsIgnoreCase(layerName)) {
					pos = lpd.getLayersCount() - j - 1;
					break;
				}
			}
			if (e.getActionCommand().equalsIgnoreCase("UP")) {
				if (oldPos > pos)
					pos = pos;
				else
					pos = pos - 1;
			}
			if (e.getActionCommand().equalsIgnoreCase("DOWN")) {
				if (oldPos > pos)
					pos = pos + 1;
				else
					pos = pos;
			}

//			System.out.println("oldPos=" + oldPos + " newto=" + newto + " pos="
//					+ pos);
			if (e.getActionCommand().equalsIgnoreCase("GROUP")) {
				FLayer origin = lpd.getLayer(lpd.getLayersCount() - oldPos - 1);
				FLayer dest = lpd.getLayer(layerName);
				// Creamos una nueva agrupación
				FLayer[] selected = new FLayer[2];
				selected[0] = origin;
				selected[1] = dest;
				createLayerGroup(selected);

			} else
				lpd.moveTo(oldPos, pos);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		dlgPop.setVisible(false);
		dlgPop.dispose();
		mapContext.endAtomicEvent();
		

	}

}
