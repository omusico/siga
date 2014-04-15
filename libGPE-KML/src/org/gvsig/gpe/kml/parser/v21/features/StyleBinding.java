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
 * This class parses a Style tag. Example:
 * <p>
 * <pre>
 * <code>
 * &lt;Style id="globeIcon"&gt;
 * &lt;IconStyle&gt;
 * &lt;Icon&gt;
 * &lt;href&gt;http://maps.google.com/mapfiles/kml/pal3/icon19.png&lt;/href&gt;
 * &lt;/Icon&gt;
 * &lt;/IconStyle&gt;
 * &lt;LineStyle&gt;
 * &lt;width&gt;2&lt;/width&gt;
 * &lt;/LineStyle&gt;
 * &lt;/Style&gt;
 * </code>
 * </pre>
 * </p>
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 * @see http://code.google.com/apis/kml/documentation/kml_tags_21.html#style
 */
public class StyleBinding {

	/**
	 * It parses the Style tag
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
		boolean endIconStyle = false;
		boolean endLineStyle = false;
		boolean endPolyStyle = false;
		int currentTag;				

		QName tag = parser.getName();
		currentTag = parser.getEventType();
		
		XMLAttributesIterator attributesIterator = new XMLAttributesIterator(parser);
		
		String styleId = parser.getAttributeValue(0);
		Object metadata = handler.getContentHandler().startMetadata("STYLE", styleId, attributesIterator);
	
//		System.out.println("Leyendo STYLE " + styleId);
		
		XMLAttributesIterator attIterator = new XMLAttributesIterator(parser); 
		Object subData = null;
		Object iconData = null;
		
		while (!endFeature){
			switch(currentTag){
			case IXmlStreamReader.START_ELEMENT:
				if (CompareUtils.compareWithOutNamespace(tag,Kml2_1_Tags.ICON_STYLE)){
//					parser.next();
					subData = handler.getContentHandler().startMetadata(tag.getLocalPart(), styleId, attIterator);										
				}								
				else if (CompareUtils.compareWithOutNamespace(tag,Kml2_1_Tags.LINE_STYLE)){
//					parser.next();
					subData = handler.getContentHandler().startMetadata(tag.getLocalPart(), styleId, attIterator);
					
										
				}								
				else if (CompareUtils.compareWithOutNamespace(tag,Kml2_1_Tags.POLY_STYLE)){
//					parser.next();
					subData = handler.getContentHandler().startMetadata(tag.getLocalPart(), styleId, attIterator);
					
				}
				else if (CompareUtils.compareWithOutNamespace(tag,Kml2_1_Tags.ICON)){
					parser.next();
					iconData = handler.getContentHandler().startMetadata(tag.getLocalPart(), styleId, attIterator);
					handler.getContentHandler().addMetadataToMetadata(iconData, subData);
					
				}
				else if (CompareUtils.compareWithOutNamespace(tag,Kml2_1_Tags.HREF)){
					parser.next();
					String str = parser.getText();
					Object hrefData = handler.getContentHandler().startMetadata(tag.getLocalPart(), str, attIterator);
					handler.getContentHandler().addMetadataToMetadata(hrefData, iconData);					
				}
				else if (CompareUtils.compareWithOutNamespace(tag,Kml2_1_Tags.SCALE)){
					parser.next();
					String str = parser.getText();
					Object scaleData = handler.getContentHandler().startMetadata(tag.getLocalPart(), str, attIterator);
					if (subData != null) {
					    handler.getContentHandler().addMetadataToMetadata(scaleData, subData);
					}					
				}
				else if (CompareUtils.compareWithOutNamespace(tag,Kml2_1_Tags.POLY_FILL)){
					parser.next();
					String str = parser.getText();
					Object fillData = handler.getContentHandler().startMetadata(tag.getLocalPart(), str, attIterator);
					handler.getContentHandler().addMetadataToMetadata(fillData, subData);					
				}
				else if (CompareUtils.compareWithOutNamespace(tag,Kml2_1_Tags.POLY_OUTLINE)){
					parser.next();
					String str = parser.getText();
					Object outlineData = handler.getContentHandler().startMetadata(tag.getLocalPart(), str, attIterator);
					handler.getContentHandler().addMetadataToMetadata(outlineData, subData);					
				}				
				else if (CompareUtils.compareWithOutNamespace(tag,Kml2_1_Tags.HEADING)){
					parser.next();
					String str = parser.getText();
					Object headingData = handler.getContentHandler().startMetadata(tag.getLocalPart(), str, attIterator);
					handler.getContentHandler().addMetadataToMetadata(headingData, subData);					
				}				
				else if (CompareUtils.compareWithOutNamespace(tag,Kml2_1_Tags.COLOR)){
					// TODO: REVISAR ESTO PARA CUANDO EL COLOR SE ASIGNA AL ICONSTYLE
					parser.next();
					String color = parser.getText();
					Object colorData = handler.getContentHandler().startMetadata(tag.getLocalPart(), color, attIterator);
					if (subData != null) {
					    handler.getContentHandler().addMetadataToMetadata(colorData, subData);
					}
					
				}
				else if (CompareUtils.compareWithOutNamespace(tag,Kml2_1_Tags.WIDTH)){
					parser.next();
					String width = parser.getText();
					Object widthData = handler.getContentHandler().startMetadata(tag.getLocalPart(), width, attIterator);
					handler.getContentHandler().addMetadataToMetadata(widthData, subData);
					
				}
				break;
			case IXmlStreamReader.END_ELEMENT:
				if (CompareUtils.compareWithOutNamespace(tag,Kml2_1_Tags.STYLE)){
					endFeature = true;
				}
				else if (CompareUtils.compareWithOutNamespace(tag,Kml2_1_Tags.ICON_STYLE)){
					handler.getContentHandler().addMetadataToMetadata(subData, metadata);
					endIconStyle = true;
				}
				else if (CompareUtils.compareWithOutNamespace(tag,Kml2_1_Tags.LINE_STYLE)){
					handler.getContentHandler().addMetadataToMetadata(subData, metadata);
					endLineStyle = true;
				}
				else if (CompareUtils.compareWithOutNamespace(tag,Kml2_1_Tags.POLY_STYLE)){
					handler.getContentHandler().addMetadataToMetadata(subData, metadata);
					endPolyStyle = true;
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
