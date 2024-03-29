/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ib��ez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
package com.iver.cit.gvsig.fmap.rendering;

import java.util.ArrayList;

/**
 * Abstract class that implements the interface for legends.It is
 * considered as the father of all XXXLegends and will implement all the methods that
 * these classes had not developed.
 * 
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 * @author pepe vidal salvador - jose.vidal.salvador@iver.ses
 */
public abstract class AbstractLegend implements ILegend{
	/**
	 * ArrayList of LegendListeners
	 */
	private ArrayList<LegendContentsChangedListener> listeners = new ArrayList<LegendContentsChangedListener>();

	
	public void addLegendListener(LegendContentsChangedListener listener) {
		if (listener!=null && !listeners.contains(listener))
			listeners.add(listener);
	}

	public void removeLegendListener(LegendContentsChangedListener listener) {
		listeners.remove(listener);
	}

	
	public void fireDefaultSymbolChangedEvent(SymbolLegendEvent event) {

		for (int i = 0; i < listeners.size(); i++) {
			listeners.get(i).symbolChanged(event);
		}
	}

	public LegendContentsChangedListener[] getListeners() {
		return listeners.toArray(new LegendContentsChangedListener[listeners.size()]);
	}
}
