/*
 * Created on 09-ene-2007
 *
 * gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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

/* CVS MESSAGES:
*
* $Id$
* $Log$
* Revision 1.1  2007-04-04 17:48:53  azabala
* first version in cvs after libDwg<->libFMap disacopling
*
* Revision 1.3  2007/03/20 19:55:27  azabala
* source code cleaning
*
* Revision 1.2  2007/01/18 13:35:22  azabala
* Refactoring general para evitar dar tantas pasadas en la carga, y para incrementar
* la legibilidad del codigo (demasiados if-else-if en vez de usar polimorfismo)
*
* Revision 1.1  2007/01/12 19:29:58  azabala
* first version in cvs
*
*
*/
package com.iver.cit.gvsig.drivers.dwg.fmapconverters;

import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.jdwglib.dwg.DwgObject;

/**
 * All dwg objects that are drawing entities
 * and that can be converted to a fmap geometry
 * implements this interface.
 * 
 * */
/*
 * TODO
 * Esta solucion tiene el problema de que es mas simple
 * (cada entidad dwg se ocupa de su conversion a fmap),pero acopla
 * libDwg con fmap 
 * (una clase Dwg2FMap lo desacoplar�a, pero llenar�a el codigo de
 * infinitos if(dwg instanceof ...) else if.....
 * 
 * */
public interface IDwg2FMap {
	public IGeometry toFMapGeometry(DwgObject entity, boolean is3DFile);
	public String toFMapString(boolean is3DFile);
	public String toString();
}

