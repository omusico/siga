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
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.FLyrDefault;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;
import com.iver.cit.gvsig.project.documents.view.toc.gui.ChangeName;
import com.iver.utiles.Utils;
import com.iver.utiles.XMLEntity;

public class MyPasteListener extends AbstractAction {

	private XMLEntity xml;
	private ITocItem item;
	private FLayer root;
	private MapContext mapContext;
	private JPopupMenu pop;

	public MyPasteListener(JPopupMenu pop, ITocItem item, XMLEntity xml, FLayer root,
			MapContext mapContext) {
		this.item = item;
		this.xml = xml;
		this.root = root;
		this.mapContext = mapContext;
		this.pop = pop;
	}

	public void actionPerformed(ActionEvent e) {
		// JOptionPane.showMessageDialog(null, e.getActionCommand());
		
		mapContext.beginAtomicEvent();
		try {
			
			FLayers all = mapContext.getLayers();
			CopyPasteLayersUtiles.getInstance().loadLayersFromXML(xml, all);
				// ponemos las capas en posición
				int pos = -1;
				for (int j=0; j < all.getLayersCount(); j++) {
					if (all.getLayer(j).getName().equalsIgnoreCase(root.getName())) {
						pos = j;
						break;
					}
				}
				int corrected = 1;
				if (e.getActionCommand().equalsIgnoreCase("UP"))
					corrected = 2;
				all.moveTo(0, all.getLayersCount()-corrected-pos);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
				
		mapContext.endAtomicEvent();
		mapContext.invalidate();

	}
	

}

