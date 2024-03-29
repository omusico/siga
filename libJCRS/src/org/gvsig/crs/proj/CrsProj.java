/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 Instituto de Desarrollo Regional and Generalitat Valenciana.
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
 *   Instituto de Desarrollo Regional (Universidad de Castilla La-Mancha)
 *   Campus Universitario s/n
 *   02071 Alabacete
 *   Spain
 *
 *   +34 967 599 200
 */

package org.gvsig.crs.proj;

import com.iver.andami.messages.NotificationManager;

/**
 * Clase que representa un CRS desde el punto de vista de proj4.
 * Ser� utilizada para realizar operaciones con porj4.
 * 
 * @author Miguel Garc�a Jim�nez (garciajimenez.miguel@gmail.com)
 *
 */

public class CrsProj extends JNIBaseCrs {
	
	/**
	 * Constructor
	 * 
	 * @param strCrs Cadena proj4 que representa el CRS(y los par�metros 
	 * de transformaci�n).
	 */
	public CrsProj(String strCrs) {
		try {
			createCrs(strCrs);
		} catch(CrsProjException e) {
			NotificationManager.addError(e);
		}
		
	}
		
	protected void finalize() {
		deleteCrs();
	}
}
