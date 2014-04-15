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

public class KmlCompoundStyle extends KmlStyle {
	
	protected KmlIconStyle iconStyle = null;
	protected KmlLineStyle lineStyle = null;
	protected KmlPolygonStyle polygonStyle = null;
	protected KmlLabelStyle labelStyle = null;
	
	@Override
	public void writeXml(IXmlStreamWriter writer) throws IOException {
		if (iconStyle != null)
			iconStyle.writeXml(writer);
		if (lineStyle != null)
			lineStyle.writeXml(writer);
		if (polygonStyle != null)
			polygonStyle.writeXml(writer);
		if (labelStyle != null)
			labelStyle.writeXml(writer);
	}

	public KmlIconStyle getIconStyle() {
		return iconStyle;
	}

	public void setIconStyle(KmlIconStyle iconStyle) {
		this.iconStyle = iconStyle;
	}

	public KmlLineStyle getLineStyle() {
		return lineStyle;
	}

	public void setLineStyle(KmlLineStyle lineStyle) {
		this.lineStyle = lineStyle;
	}

	public KmlPolygonStyle getPolygonStyle() {
		return polygonStyle;
	}

	public void setPolygonStyle(KmlPolygonStyle polygonStyle) {
		this.polygonStyle = polygonStyle;
	}

	public KmlLabelStyle getLabelStyle() {
		return labelStyle;
	}

	public void setLabelStyle(KmlLabelStyle labelStyle) {
		this.labelStyle = labelStyle;
	}

}

