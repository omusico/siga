package org.gvsig.gpe.kml.writer.v21.kml;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.gvsig.gpe.GPEFactory;
import org.gvsig.gpe.containers.CoordinatesSequence;
import org.gvsig.gpe.containers.Feature;
import org.gvsig.gpe.containers.GeometryAsserts;
import org.gvsig.gpe.containers.Layer;
import org.gvsig.gpe.containers.Point;
import org.gvsig.gpe.kml.utils.KmlCompoundStyle;
import org.gvsig.gpe.kml.utils.KmlIconStyle;
import org.gvsig.gpe.kml.utils.KmlLineStyle;
import org.gvsig.gpe.kml.utils.KmlPolygonStyle;
import org.gvsig.gpe.kml.utils.KmlStyle;
import org.gvsig.gpe.kml.writer.GPEKmlWriterHandlerImplementor;
import org.gvsig.gpe.parser.GPEContentHandlerTest;
import org.gvsig.gpe.parser.GPEParser;
import org.gvsig.gpe.writer.GPEPointsLayerTest;

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

public class KMLWriteLegendTest extends GPEPointsLayerTest{
	
	private String layerId = "l1";
	private String layerName = "Points Layer";
	private String layerDescription = "This is a test of a points layer";
	private String srs = "EPSG:23030";
	private String bboxId = "bboxID";
	private double[] bboxX = generateRandomBBox();
	private double[] bboxY = generateRandomBBox();
	private double[] bboxZ = generateRandomBBox();
	private String feature1Name = "New York";
	private String feature1Id = "f1";
	private String point1Id = "p1";
	private double point1X = generateRandomPoint();
	private double point1Y = generateRandomPoint();
	private double point1Z = generateRandomPoint();
	private String feature2Name = "Los Angeles";
	private String feature2Id = "f2";
	private String point2Id = "p2";
	private double point2X = generateRandomPoint();
	private double point2Y = generateRandomPoint();
	private double point2Z = generateRandomPoint();
	private File outputFile;
	private GPEParser parser;

	
	public void setUp() throws Exception{
		outputFile = new File(this.getClass().getName());
	}
	
	public Layer[] getLayers(){
		ArrayList layers = ((GPEContentHandlerTest)parser.getContentHandler()).getLayers();
		Layer[] aLayers = new Layer[layers.size()];
		for (int i=0 ; i<layers.size() ; i++){
			aLayers[i] = (Layer)layers.get(i);
		}
		return aLayers;
	}


	public void testWriter() throws Exception{
		OutputStream os = createOutputStream(outputFile);
		getWriterHandler().setOutputStream(os);
		writeObjects();		
		
		parser = GPEFactory.createParserByClass(getGPEParserClass().getName());
		InputStream is = createInputStream(outputFile);
		parser.parse(getContenHandler(),getErrorHandler() ,is);
		readObjects();
		
		outputFile.delete();		
	}

	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterBaseTest#getGPEParserClass()
	 */
	public Class getGPEParserClass() {
		return org.gvsig.gpe.kml.parser.GPEKml2_1_Parser.class;
	}
	
	public void readObjects() {
		Layer[] layers = getLayers();
		assertEquals(layers.length, 1);		
		Layer layer = layers[0];
	
		assertEquals(layer.getFeatures().size(), 2);
		//FEATURE 1
		Feature feature1 = (Feature)layer.getFeatures().get(0);
		GeometryAsserts.point((Point)feature1.getGeometry(), point1X, point1Y, point1Z);
		
		//FEATURE 2
		Feature feature2 = (Feature)layer.getFeatures().get(1);
		GeometryAsserts.point((Point)feature2.getGeometry(), point2X, point2Y, point2Z);		
	}


	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterBaseTest#getGPEWriterHandlerClass()
	 */
	public Class getGPEWriterHandlerClass() {
		return org.gvsig.gpe.kml.writer.GPEKml21WriterHandlerImplementor.class;
	}	

	public  GPEKmlWriterHandlerImplementor getWriter() {
		return (GPEKmlWriterHandlerImplementor) getWriterHandler().getWriterHandlerImplementor();
	}
	
	public void writeObjects() {
		KmlStyle[] styles = new KmlStyle[2];
		KmlLineStyle lineStyle = new KmlLineStyle();
		
		KmlIconStyle icon = new KmlIconStyle();
		icon.setHref("http://maps.google.com/mapfiles/kml/paddle/red-stars.png");
		icon.setId("iconStyle");
		
		KmlPolygonStyle polyStyle = new KmlPolygonStyle();
		
		KmlCompoundStyle cStyle = new KmlCompoundStyle();
		cStyle.setLineStyle(lineStyle);
		cStyle.setPolygonStyle(polyStyle);
		cStyle.setId("compoundStyle");
		
		styles[0] = cStyle;
		styles[1] = icon;
		
		getWriter().initialize();
		getWriter().startLayer(layerId, null, layerName, layerDescription, srs);
		
		getWriter().writeStyles(styles);
		
		getWriter().startBbox(bboxId, new CoordinatesSequence(bboxX,	bboxY, bboxZ), srs);
		getWriter().endBbox();
		getWriter().startFeature(feature1Id, null, feature1Name);
		getWriter().startElement(null, "styleUrl", "#compoundStyle");
		getWriter().endElement();
		getWriter().startPoint(point1Id, new CoordinatesSequence(point1X, point1Y, point1Z), srs);
		getWriter().endPoint();		
		getWriter().endFeature();
		getWriter().startFeature(feature2Id, null, feature2Name);
		getWriter().startPoint(point2Id, new CoordinatesSequence(point2X, point2Y, point2Z), srs);
		getWriter().endPoint();		
		getWriter().endFeature();
		getWriter().endLayer();
		getWriter().close();		
		
	}

}
