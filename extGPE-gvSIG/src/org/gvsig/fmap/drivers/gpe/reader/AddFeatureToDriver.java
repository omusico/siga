package org.gvsig.fmap.drivers.gpe.reader;

import java.awt.Color;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.gvsig.fmap.drivers.gpe.model.GPEFeature;
import org.gvsig.fmap.drivers.gpe.model.GPEMetadata;
import org.gvsig.fmap.drivers.gpe.utils.GPETypesConversion;
import org.gvsig.symbology.fmap.symbols.PictureMarkerSymbol;
import org.gvsig.xmlschema.som.IXSSchemaDocument;
import org.gvsig.xmlschema.som.IXSTypeDefinition;

import com.hardcode.gdbms.engine.values.StringValue;
import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueFactory;
import com.iver.cit.gvsig.fmap.core.FShape;
import com.iver.cit.gvsig.fmap.core.SymbologyFactory;
import com.iver.cit.gvsig.fmap.core.symbols.AbstractMarkerSymbol;
import com.iver.cit.gvsig.fmap.core.symbols.IMarkerSymbol;
import com.iver.cit.gvsig.fmap.core.symbols.ISymbol;
import com.iver.cit.gvsig.fmap.core.symbols.MultiShapeSymbol;
import com.iver.cit.gvsig.fmap.core.symbols.SimpleFillSymbol;
import com.iver.cit.gvsig.fmap.core.symbols.SimpleLineSymbol;
import com.iver.cit.gvsig.fmap.core.symbols.SimpleMarkerSymbol;
import com.iver.cit.gvsig.fmap.rendering.IVectorLegend;
import com.iver.cit.gvsig.fmap.rendering.LegendFactory;
import com.iver.cit.gvsig.fmap.rendering.SingleSymbolLegend;
import com.iver.cit.gvsig.fmap.rendering.VectorialUniqueValueLegend;
import com.iver.cit.gvsig.project.documents.view.legend.gui.AttrInTableLabeling;

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
 * $Id$
 * $Log$
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class AddFeatureToDriver {
	
	private class Pair {
		Value searchFor = null;
		Value newValue = null;		
	}
	private IXSSchemaDocument schema = null;
	
	protected VectorialUniqueValueLegend legend = null;
	protected AttrInTableLabeling labeling = new AttrInTableLabeling();
	protected ArrayList<Pair> valuesToSubstitute = new ArrayList<Pair>();
		
	public AddFeatureToDriver(){
		legend = LegendFactory.createVectorialUniqueValueLegend(FShape.MULTI);
		String[] fNames = new String[1];
		fNames[0] = "styleUrl";
		legend.setClassifyingFieldNames(fNames);
		legend.useDefaultSymbol(true);
	}

	/**
	 * Add a feature to a layer
	 * @param driver
	 * The driver
	 * @param feature
	 * The feature to add
	 */
	public void addFeatureToLayer(GPEVectorialDriver driver, GPEFeature feature){
		addFeatureToLayer_(driver, feature);		
	}
	
	public void addMetadataToLayer(GPEVectorialDriver driver, GPEMetadata metadata) {
		// Exploramos los metadatos y recuperamos lo necesario para crear la leyenda.
//		System.out.println("A crear una leyenda usando metadatos!! " + metadata.getTagType() 
//				+ "-" + metadata.getTagData());
		String id = metadata.getTagData();
		if (metadata.getTagType().equalsIgnoreCase("STYLEMAP")) {
			// Recuperamos el primer styleId y ese es el símbolo que sustituiremos cuando nos
			// encontremos el styleUrl de este styleMap
			GPEMetadata firstPair = metadata.getElementAt(0);
			for (int i=0; i < firstPair.getDataList().size(); i++) {
				GPEMetadata aux = firstPair.getElementAt(i);
				String type = aux.getTagType();
//				System.out.println("STYLEMAP: " + type);
				if (type.equalsIgnoreCase("STYLEURL")) {
					String styleId = aux.getTagData(); 
//					System.out.println("STYLEMAP: Sustituir " + styleId + " por " + id);
					Value valStyleId = ValueFactory.createValue(styleId);
					Pair pair = new Pair();
					pair.searchFor = valStyleId;
					pair.newValue = ValueFactory.createValue("#" + id);;
					valuesToSubstitute.add(pair);					
				}				
			}
			return;
		}
		
		MultiShapeSymbol multiSymbol = new MultiShapeSymbol();
		boolean bError = false;
		SimpleLineSymbol lineSym = null;
		for (int i=0; i < metadata.getDataList().size(); i++) {
			GPEMetadata style = metadata.getElementAt(i);
			String type = style.getTagType();		
			if (type.equalsIgnoreCase("IconStyle")) {
				IMarkerSymbol pointSym = new SimpleMarkerSymbol(); 
				pointSym.setSize(10);
				pointSym.setUnit(-1); // pixels
				
				for (int j=0; j < style.getDataList().size(); j++) {
					GPEMetadata att = style.getElementAt(j);
					if (att.getTagType().equalsIgnoreCase("COLOR")) {
						// format: OPACITY-GG-BB-RR
						Color color = GPETypesConversion.fromABGRtoColor(att.getTagData());
						pointSym.setColor(color);
					}
					if (att.getTagType().equalsIgnoreCase("ICON")) {
						if (att.getDataList().size() == 0) {
							// TODO: Se adjunta el icono, no es una referencia. Creamos un pointSym por defecto y a por otro.
							continue;
						}
						GPEMetadata iconData = att.getElementAt(0);
						try {
							URI uri = new URI(iconData.getTagData());
							pointSym = new PictureMarkerSymbol(uri.toURL(), uri.toURL());
							pointSym.setSize(12);
							pointSym.setUnit(-1);
						} catch (URISyntaxException e) {
							e.printStackTrace();
							bError = true;
						} catch (MalformedURLException e) {
							e.printStackTrace(); 
							bError = true;
						} catch (IOException e) {
							e.printStackTrace();
							bError = true;
						}						

					}

					if (att.getTagType().equalsIgnoreCase("HEADING")) {
						// Ojo: 0º => N, 90=>E, 180=>S, 270=W
						// TODO: Convertir anguloKmlToRad
						double angleDegree = Double.parseDouble(att.getTagData());
						double rad = GPETypesConversion.kmlDegToRad(angleDegree);
						((AbstractMarkerSymbol)pointSym).setRotation(rad);
					}

				} // for
				multiSymbol.setMarkerSymbol(pointSym);
			} // IconStyle
			
			else if (type.equalsIgnoreCase("LineStyle")) {
				lineSym = new SimpleLineSymbol(); 
				for (int j=0; j < style.getDataList().size(); j++) {
					GPEMetadata att = style.getElementAt(j);
					if (att.getTagType().equalsIgnoreCase("COLOR")) {
						// format: OPACITY-GG-BB-RR
						Color color = GPETypesConversion.fromABGRtoColor(att.getTagData());
						lineSym.setLineColor(color);
					}
					if (att.getTagType().equalsIgnoreCase("WIDTH")) {
						float width = Float.parseFloat(att.getTagData());
						lineSym.setLineWidth(width);
					}
				} // for
				multiSymbol.setLineSymbol(lineSym);
			} // LineStyle
			else if (type.equalsIgnoreCase("PolyStyle")) {
				SimpleFillSymbol polySym = new SimpleFillSymbol(); 
				for (int j=0; j < style.getDataList().size(); j++) {
					GPEMetadata att = style.getElementAt(j);
					if (att.getTagType().equalsIgnoreCase("COLOR")) {
						// format: OPACITY-GG-BB-RR
						Color color = GPETypesConversion.fromABGRtoColor(att.getTagData());
						polySym.setFillColor(color);
					}
					if (att.getTagType().equalsIgnoreCase("FILL")) {
						int fill = Integer.parseInt(att.getTagData());
						if (fill == 1)
							polySym.setHasFill(true);
						else
							polySym.setHasFill(false);

					}
					if (att.getTagType().equalsIgnoreCase("OUTLINE")) {
						int outline = Integer.parseInt(att.getTagData());
						if (outline == 1) {
							polySym.setHasOutline(true);
							if (polySym.getOutline() == null) {
								if (lineSym != null) {
									polySym.setOutline(lineSym);
								}
								else
								{
									polySym.setOutline(new SimpleLineSymbol());
								}
							}
						}
						else
							polySym.setHasOutline(false);
					}

					
				} // for
				multiSymbol.setFillSymbol(polySym);
			} // PolyStyle
		} // for (no podemos fijar el simbolo dentro del for porque no se pueden añadir claves repetidas)
		StringValue key = ValueFactory.createValue("#" + id);
		legend.addSymbol(key, multiSymbol);
		
		if (driver instanceof KMLVectorialDriver) {
			// Tenemos que hacer un chequeo completo cada vez porque puede llegar un styleMap
			// que hace referencia a un símbolo que todavía no se ha creado.
			Object[] values = legend.getValues();
			ISymbol[] auxSymbols = legend.getSymbols();
			for (int iVal = 0; iVal < valuesToSubstitute.size(); iVal++) {
				Pair pair = valuesToSubstitute.get(iVal);
				Value valStyleId = pair.searchFor;
//				System.out.println("Busco " + valStyleId);
				for (int iSym = 0; iSym < values.length; iSym++) {
					Value idSym = (Value) values[iSym];
					if (idSym.doEquals(valStyleId)) {						
						legend.delSymbol(idSym);
						legend.addSymbol(pair.newValue, auxSymbols[iSym]);
					}
				}
			}

			KMLVectorialDriver kmlDriver = (KMLVectorialDriver) driver;
			kmlDriver.setDefaultLegend(legend);
			if (bError) {
				SingleSymbolLegend auxLegend = new SingleSymbolLegend(); 
				kmlDriver.setDefaultLegend(auxLegend);
			}
			if (legend.getSymbols().length ==1) {
				((IVectorLegend) kmlDriver.getDefaultLegend()).setDefaultSymbol(multiSymbol);
			}
			else {
				MultiShapeSymbol multiSymbolAux = new MultiShapeSymbol();
				((IVectorLegend) kmlDriver.getDefaultLegend()).setDefaultSymbol(multiSymbolAux);
			}

		}
		
	}

	/**
	 * Add a feature to a layer
	 * @param layer
	 * The layer
	 * @param feature
	 * The feature to add
	 */
	private void addFeatureToLayer_(GPEVectorialDriver driver, GPEFeature feature){
		IXSTypeDefinition elementType = null;
		//If the feature has a type it will try to retrieve it
//		if (feature.getTypeName() != null){
//			elementType = schema.getTypeByName(feature.getTypeName());
//		}
		//If the type exists in the schema
		if (elementType != null){
			//layer.addFeature(feature, elementType);
			driver.addFeature(feature);
			//If the type doesn't exist in the XML schema
		}else{
			driver.addFeature(feature);
		}		
	}

	/**
	 * @return the schema
	 */
	public IXSSchemaDocument getSchema() {
		return schema;
	}

	/**
	 * @param schema the schema to set
	 */
	public void setSchema(IXSSchemaDocument schema) {
		this.schema = schema;
	}



}
