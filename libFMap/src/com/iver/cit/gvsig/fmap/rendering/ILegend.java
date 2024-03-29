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

import com.hardcode.gdbms.engine.data.driver.DriverException;
import com.iver.cit.gvsig.fmap.core.symbols.ISymbol;
import com.iver.cit.gvsig.fmap.layers.XMLException;
import com.iver.utiles.IPersistence;


/**
 * Information about the legend to be represented in the interface.
 * 
 */
public interface ILegend extends IPersistence{
	/**
	 * Obtains the default symbol of the legend.
	 *
	 * @return default symbol.
	 */
	ISymbol getDefaultSymbol();

	/**
	 * Clones the legend.
	 *
	 * @return Cloned legend.
	 *
	 * @throws XMLException
	 * @throws DriverException
	 */
	ILegend cloneLegend() throws XMLException;
	/**
	 * Adds a new listener to the legend.
	 * 
	 * @param listener to be added
	 */
	void addLegendListener(LegendContentsChangedListener listener);
	/**
	 * Removes a listener from the legend.
	 * 
	 * @param listener to be removed
	 */
	public void removeLegendListener(LegendContentsChangedListener listener);
	/**
	 * Executed when the default symbol of a legend is changed.
	 * 
	 * @param event 
	 */
	public void fireDefaultSymbolChangedEvent(SymbolLegendEvent event);
	/**
	 * Obtains the listeners of a legend.
	 * 
	 * @return LegendListener[] array composed by the listeners of a legend.
	 */
	public LegendContentsChangedListener[] getListeners();
}

