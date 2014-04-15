package org.gvsig.gpe.kml.parser.v22.kml;

import org.gvsig.gpe.containers.Feature;
import org.gvsig.gpe.containers.Layer;
import org.gvsig.gpe.containers.LineString;
import org.gvsig.gpe.kml.parser.v21.kml.KMLBaseTest;



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
public class KMLnucleos_ArcMap_Test extends KMLBaseTest{
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.readers.GPEReaderBaseTest#getFile()
	 */
	public String getFile() {
		return "testdata/nucleos.kml";
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.readers.GPEReaderBaseTest#makeAsserts()
	 */
	public void makeAsserts() {
		Layer[] layers = getLayers();
		assertEquals(layers.length, 1);
		Layer layer = layers[0];
		assertEquals(layer.getName(), "puntosKml");
		assertEquals(2,layer.getFeatures().size());
		Feature f = (Feature) layer.getFeatures().get(0);
		System.out.println(f.getGeometry().getDimension());
		
	}

}
