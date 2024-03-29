package org.gvsig.gpe.kml.parser.v21.geometries;

import java.io.IOException;

import javax.xml.namespace.QName;

import org.gvsig.gpe.kml.parser.GPEDefaultKmlParser;
import org.gvsig.gpe.kml.parser.v21.coordinates.CoordinatesTypeIterator;
import org.gvsig.gpe.kml.utils.Kml2_1_Tags;
import org.gvsig.gpe.xml.stream.IXmlStreamReader;
import org.gvsig.gpe.xml.stream.XmlStreamException;
import org.gvsig.gpe.xml.utils.CompareUtils;

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
/* CVS MESSAGES:
 *
 * $Id:LineStringTypeBinding.java 357 2008-01-09 17:50:08Z jpiera $
 * $Log$
 * Revision 1.2  2007/06/07 14:53:59  jorpiell
 * Add the schema support
 *
 * Revision 1.1  2007/05/11 07:06:29  jorpiell
 * Refactoring of some package names
 *
 * Revision 1.4  2007/05/08 08:22:37  jorpiell
 * Add comments to create javadocs
 *
 * Revision 1.3  2007/05/02 11:46:50  jorpiell
 * Writing tests updated
 *
 * Revision 1.2  2007/04/20 08:38:59  jorpiell
 * Tests updating
 *
 * Revision 1.1  2007/04/13 13:16:21  jorpiell
 * Add KML reading support
 *
 * Revision 1.1  2007/03/07 08:19:10  jorpiell
 * Pasadas las clases de KML de libGPE-GML a libGPE-KML
 *
 * Revision 1.1  2007/02/28 11:48:31  csanchez
 * *** empty log message ***
 *
 * Revision 1.1  2007/02/20 10:53:20  jorpiell
 * Añadidos los proyectos de kml y gml antiguos
 *
 * Revision 1.1  2007/02/12 13:49:18  jorpiell
 * A�adido el driver de KML
 *
 *
 */
/**
 * It parses the LineQName tag. Example:
 * <p>
 * <pre>
 * <code>
 * &lt;LineString&gt;
 * &lt;coord&gt;&lt;X&gt;56.1&lt;/X&gt;&lt;Y&gt;0.45&lt;/Y&gt;&lt;/coord&gt;
 * &lt;coord&gt;&lt;X&gt;67.23&lt;/X&gt;&lt;Y&gt;0.98&lt;/Y&gt;&lt;/coord&gt;
 * &lt;/LineString&gt;
 * </code>
 * </pre>
 * </p> 
 * @author Jorge Piera Llodr� (piera_jor@gva.es)
 * @see http://code.google.com/apis/kml/documentation/kml_tags_21.html#linestring
 */
public class LineStringTypeBinding {

	/**
	 * It parses the LineQName tag
	 * @param parser
	 * The XML parser
	 * @param handler
	 * The GPE parser that contains the content handler and
	 * the error handler
	 * @return
	 * A line string
	 * @throws IOException 
	 * @throws XmlStreamException 
	 * @throws XmlStreamException
	 * @throws IOException
	 */
	public Object parse(IXmlStreamReader parser,GPEDefaultKmlParser handler) throws XmlStreamException, IOException {
		boolean endFeature = false;
		int currentTag;
		Object lineString = null;

		String id = handler.getProfile().getGeometryBinding().getID(parser, handler);

		QName tag = parser.getName();
		currentTag = parser.getEventType();

		while (!endFeature){
			switch(currentTag){
			case IXmlStreamReader.START_ELEMENT:
				if (CompareUtils.compareWithOutNamespace(tag,Kml2_1_Tags.COORDINATES)){
					CoordinatesTypeIterator coordinatesIterator = handler.getProfile().getCoordinatesTypeBinding();
					coordinatesIterator.initialize(parser, handler, Kml2_1_Tags.LINESTRING);
					lineString = handler.getContentHandler().startLineString(id,
							coordinatesIterator,								
							Kml2_1_Tags.DEFAULT_SRS);	
					return lineString;
				}
				break;
			case IXmlStreamReader.END_ELEMENT:
				if (CompareUtils.compareWithOutNamespace(tag,Kml2_1_Tags.LINESTRING)){						
					endFeature = true;
					handler.getContentHandler().endLineString(lineString);
				}
				break;
			case IXmlStreamReader.CHARACTERS:					

				break;
			}
			if (!endFeature){					
				currentTag = parser.next();
				tag = parser.getName();
			}
		}			
		return lineString;	
	}
}
