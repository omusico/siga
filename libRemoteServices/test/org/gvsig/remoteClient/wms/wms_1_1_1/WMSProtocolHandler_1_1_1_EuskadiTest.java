/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.remoteClient.wms.wms_1_1_1;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.gvsig.remoteClient.wms.WMSProtocolHandlerFactory;

import junit.framework.TestCase;

/**
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 */
public class WMSProtocolHandler_1_1_1_EuskadiTest extends TestCase {
	WMSProtocolHandler1_1_1 handler1, handler2, handler3, handler4;

	public void setUp() {
		System.out.println("Setting up test..");
		handler1 = new WMSProtocolHandler1_1_1();
	}



	public void testParsing() {
		long t1 = System.currentTimeMillis();
		handler1.parse(new File("testdata/wms/getCapabilities_Euskadi.xml"));
		long t2 = System.currentTimeMillis();
		System.out.println("Test parsing done with apparently no errors in "+ (t2-(float)t1)/1000+" seconds");
	}
	
	public void testConnect() {
    	StringReader reader = null;
    	DataInputStream dis = null;
    	String request = "http://www1.euskadi.net/wmsconnector/com.esri.wms.Esrimap?ServiceName=WMS_Euskadi_CBase" +
    			"&REQUEST=GetCapabilities&SERVICE=WMS&VERSION=1.3.0&EXCEPTIONS=XML";
		URL url;
		try {
			WMSProtocolHandlerFactory.negotiate(request);
			
			url = new URL(request);
			byte[] buffer = new byte[1024];//new byte[1024*256];
			dis = new DataInputStream(url.openStream());
	        dis.readFully(buffer);
	        String string = new String(buffer);
	        System.out.println(string);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

