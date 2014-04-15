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

public class KmlIconStyle extends KmlColorStyle {
	
	protected float heading = 0;
	protected String href = null;
	protected float scale = 1;

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}
	
	

	@Override
	public void writeXml(IXmlStreamWriter writer) throws IOException {
		writer.writeStartElement(Kml2_1_Tags.ICON_STYLE);

		writer.writeStartElement(Kml2_1_Tags.COLOR);
		writer.writeValue(KMLUtilsParser.fromColorToABGR(color));
		writer.writeEndElement();
		
		writer.writeStartElement(Kml2_1_Tags.HEADING);
		writer.writeValue(heading);
		writer.writeEndElement();
		
		if (scale != 1) {
			writer.writeStartElement(Kml2_1_Tags.SCALE);
			writer.writeValue(scale);
			writer.writeEndElement();
		}
		if (href != null) {
			writer.writeStartElement(Kml2_1_Tags.ICON);
			writer.writeStartElement(Kml2_1_Tags.HREF);
			writer.writeValue(href);
			writer.writeEndElement();

			writer.writeEndElement();
		}

		
		writer.writeEndElement();

		
	}

	public float getHeading() {
		return heading;
	}

	public void setHeading(float heading) {
		this.heading = heading;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

}

