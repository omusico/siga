package org.gvsig.gpe.kml.parser.v21.features;

import java.io.IOException;

import javax.xml.namespace.QName;

import org.gvsig.gpe.kml.parser.GPEDefaultKmlParser;
import org.gvsig.gpe.kml.utils.Kml2_1_Tags;
import org.gvsig.gpe.xml.stream.IXmlStreamReader;
import org.gvsig.gpe.xml.stream.XmlStreamException;
import org.gvsig.gpe.xml.utils.CompareUtils;
import org.gvsig.gpe.xml.utils.XMLAttributesIterator;

/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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
 *   Av. Blasco Ibáñez, 50
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
 * $Id: StyleBinding.java 357 2008-01-09 17:50:08Z jpiera $
 * $Log$
 * Revision 1.2  2007/06/07 14:53:59  jorpiell
 * Add the schema support
 *
 * Revision 1.1  2007/05/11 07:06:29  jorpiell
 * Refactoring of some package names
 *
 * Revision 1.2  2007/05/08 08:22:37  jorpiell
 * Add comments to create javadocs
 *
 * Revision 1.1  2007/04/13 13:16:21  jorpiell
 * Add KML reading support
 *
 *
 */
/**
 * This class parses a StyleMap tag. Example:
 * <p>
 * <pre>
 * <code>
 * 	<StyleMap id="msn_ylw-pushpin">
		<Pair>
			<key>normal</key>
			<styleUrl>#sn_ylw-pushpin</styleUrl>
		</Pair>
		<Pair>
			<key>highlight</key>
			<styleUrl>#sh_ylw-pushpin</styleUrl>
		</Pair>
	</StyleMap>
 * </code>
 * </pre>
 * </p>
 * @see http://code.google.com/apis/kml/documentation/kml_tags_21.html#stylemap
 */
public class StyleMapBinding {

	/**
	 * It parses the StyleMap tag
	 * @param parser
	 * The XML parser
	 * @param handler
	 * The GPE parser that contains the content handler and
	 * the error handler
	 * @throws IOException 
	 * @throws XmlStreamException 
	 * @throws XmlStreamException
	 * @throws IOException
	 */
	public static Object parse(IXmlStreamReader parser,GPEDefaultKmlParser handler) throws XmlStreamException, IOException{
		boolean endFeature = false;
		boolean endPair = false;
		int currentTag;				

		QName tag = parser.getName();
		currentTag = parser.getEventType();
		
		XMLAttributesIterator attributesIterator = new XMLAttributesIterator(parser);
		
		String styleId = parser.getAttributeValue(0);
		
//		System.out.println("Leyendo STYLEMAP " + styleId);
		
		Object metadata = handler.getContentHandler().startMetadata("STYLEMAP", styleId, attributesIterator);
	
		XMLAttributesIterator attIterator = new XMLAttributesIterator(parser); 
		Object subData = null;
		
		while (!endFeature){
			switch(currentTag){
			case IXmlStreamReader.START_ELEMENT:
				if (CompareUtils.compareWithOutNamespace(tag,Kml2_1_Tags.PAIR)){
					parser.next();
					subData = handler.getContentHandler().startMetadata(tag.getLocalPart(), styleId, attIterator);										
				}								
				else if (CompareUtils.compareWithOutNamespace(tag,Kml2_1_Tags.KEY)){
					parser.next();
					String key = parser.getText();
					Object keyData = handler.getContentHandler().startMetadata(tag.getLocalPart(), key, attIterator);
					handler.getContentHandler().addMetadataToMetadata(keyData, subData);

										
				}								
				else if (CompareUtils.compareWithOutNamespace(tag,Kml2_1_Tags.STYLEURL)){
					parser.next();
					String styleUrl = parser.getText();
					Object styleUrlData = handler.getContentHandler().startMetadata(tag.getLocalPart(), styleUrl, attIterator);
					handler.getContentHandler().addMetadataToMetadata(styleUrlData, subData);
					
				}
				break;
			case IXmlStreamReader.END_ELEMENT:
				if (CompareUtils.compareWithOutNamespace(tag,Kml2_1_Tags.STYLEMAP)){
					endFeature = true;
				}
				else if (CompareUtils.compareWithOutNamespace(tag,Kml2_1_Tags.PAIR)){
					handler.getContentHandler().addMetadataToMetadata(subData, metadata);
					endPair = true;
				}
				
				break;
			case IXmlStreamReader.CHARACTERS:	
				break;
			}
			if (!endFeature){					
				currentTag = parser.next();
				tag = parser.getName();
//				System.out.println("tag = " + tag);
			}
		}			
		return metadata;
	}
}
