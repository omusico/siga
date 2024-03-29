/*
 * Created on 03-feb-2007
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
* Revision 1.2.2.2  2007-03-21 19:49:16  azabala
* implementation of dwg 12, 13, 14.
*
* Revision 1.6  2007/03/20 19:57:08  azabala
* source code cleaning
*
* Revision 1.5  2007/03/06 19:39:38  azabala
* Changes to adapt dwg 12 to general architecture
*
* Revision 1.4  2007/03/02 20:31:22  azabala
* *** empty log message ***
*
* Revision 1.3  2007/03/01 19:59:00  azabala
* *** empty log message ***
*
* Revision 1.2  2007/02/07 12:44:27  fdiaz
* A�adido o modificado el metodo clone para que el DwgObject se encargue de las propiedades comunes a todos los objetos.
* A�adido el metodo fill.
*
* Revision 1.1  2007/02/05 07:03:22  azabala
* *** empty log message ***
*
*
*/
package com.iver.cit.jdwglib.dwg.objects;

import com.iver.cit.jdwglib.dwg.DwgObject;
import com.iver.cit.jdwglib.dwg.IDwgVertex;

/**
 * 
 * A vertex whose superentity is a DwgPFacePolyline
 * (a polyface mesh vertex)
 * 
 * This vertex is similar to Vertex3D.
 * */
public class DwgVertexPFace extends DwgObject implements IDwgVertex {

	private int flags;
	private double[] point;

	public DwgVertexPFace(int index) {
		super(index);
	}

	public void setFlags(int flags) {
		this.flags = flags;
	}

	public void setPoint(double[] ds) {
		this.point = ds;
	}

	public int getFlags() {
		return flags;
	}

	public double[] getPoint() {
		return point;
	}
	public Object clone(){
		DwgVertexPFace obj = new DwgVertexPFace(index);
		this.fill(obj);
		return obj;
	}
	
	protected void fill(DwgObject obj){
		super.fill(obj);
		DwgVertexPFace myObj = (DwgVertexPFace)obj;

		myObj.setFlags(flags);
		myObj.setPoint(point);
	}
	
	public String toString(){
		return "[x="+point[0]+", y="+point[1]+", z="+point[2]+"]";
	}

}

