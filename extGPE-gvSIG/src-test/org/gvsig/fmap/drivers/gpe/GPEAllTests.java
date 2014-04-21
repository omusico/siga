package org.gvsig.fmap.drivers.gpe;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.gvsig.fmap.drivers.gpe.reader.sfp0.GPEEuroRoadsTest;
import org.gvsig.fmap.drivers.gpe.reader.v2.GPELinesDriverTest;
import org.gvsig.fmap.drivers.gpe.reader.v2.GPEMultipleLayerTest;
import org.gvsig.fmap.drivers.gpe.reader.v2.GPEPointsDriverTest;
import org.gvsig.fmap.drivers.gpe.reader.v2.GPEPolygonsDriverTest;

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
 * $Id$
 * $Log$
 *
 */
/**
 * @author Jorge Piera LLodr� (jorge.piera@iver.es)
 */
public class GPEAllTests {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for gvSIG and GPE");
		//$JUnit-BEGIN$
		suite.addTestSuite(GPEPointsDriverTest.class);
		suite.addTestSuite(GPELinesDriverTest.class);
		suite.addTestSuite(GPEPolygonsDriverTest.class);
		suite.addTestSuite(GPEEuroRoadsTest.class);
		suite.addTestSuite(GPEMultipleLayerTest.class);		
		//$JUnit-END$
		return suite;
	}

}