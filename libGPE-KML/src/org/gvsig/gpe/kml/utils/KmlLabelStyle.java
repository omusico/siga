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
 
package org.gvsig.gpe.kml.utils;

import java.io.IOException;

import org.gvsig.gpe.xml.stream.IXmlStreamWriter;

/**
 * @author Francisco José Peñarrubia (fjp@scolab.es)
 *
 * Labels are exported as features. Google Earth will render the attribute <name>
 * with this color.
 */
public class KmlLabelStyle extends KmlColorStyle {
	
	protected float scale = 1;

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	@Override
	public void writeXml(IXmlStreamWriter writer) throws IOException {
		writer.writeStartElement(Kml2_1_Tags.LABEL_STYLE);

		writer.writeStartElement(Kml2_1_Tags.COLOR);
		writer.writeValue(KMLUtilsParser.fromColorToABGR(color));
		writer.writeEndElement();
		if (scale != 1) {
			writer.writeStartElement(Kml2_1_Tags.SCALE);
			writer.writeValue(scale);
			writer.writeEndElement();
		}

		writer.writeEndElement();


	}

}

