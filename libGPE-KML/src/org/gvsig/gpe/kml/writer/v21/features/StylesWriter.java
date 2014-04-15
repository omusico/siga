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
 
package org.gvsig.gpe.kml.writer.v21.features;

import java.io.IOException;
import java.util.Properties;

import org.gvsig.gpe.kml.utils.Kml2_1_Tags;
import org.gvsig.gpe.kml.utils.KmlStyle;
import org.gvsig.gpe.kml.writer.GPEKmlWriterHandlerImplementor;
import org.gvsig.gpe.xml.stream.EventType;
import org.gvsig.gpe.xml.stream.IXmlStreamWriter;

/**
 * @author Francisco José Peñarrubia (fjp@scolab.es)
 * Example:
 *  <Style id="transPurpleLineGreenPoly">
      <LineStyle>
        <color>7fff00ff</color>
        <width>4</width>
      </LineStyle>
      <PolyStyle>
        <color>7f00ff00</color>
      </PolyStyle>
    </Style>
    <Style id="highlightPlacemark">
      <IconStyle>
        <Icon>
          <href>http://maps.google.com/mapfiles/kml/paddle/red-stars.png</href>
        </Icon>
      </IconStyle>
    </Style>    

 */
public class StylesWriter {
	/**
	 * Writes a Style init tag and its attributes
	 * @param writer
	 * Writer to write the Styles
	 * @param handler
	 * The writer handler implementor
	 * @param styles
	 * KmlStyle array
	 * 
	 * @throws IOException
	 */
	public void writeStyles(IXmlStreamWriter writer, GPEKmlWriterHandlerImplementor handler, KmlStyle[] styles) throws IOException{
		for (int i=0; i < styles.length; i++) {
			KmlStyle style = styles[i];
			writer.writeStartElement(Kml2_1_Tags.STYLE);
			writer.writeStartAttribute(Kml2_1_Tags.ID);
			writer.writeValue(style.getId());			
			writer.writeEndAttributes();	
			
			style.writeXml(writer);

			writer.writeEndElement();
		}
	}


	

}

