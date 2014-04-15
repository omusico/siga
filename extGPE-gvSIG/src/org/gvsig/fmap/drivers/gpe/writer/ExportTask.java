package org.gvsig.fmap.drivers.gpe.writer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.drivers.gpe.reader.GPEDriverFactory;
import org.gvsig.fmap.drivers.gpe.reader.GPEVectorialDriver;
import org.gvsig.fmap.drivers.gpe.utils.GPETypesConversion;
import org.gvsig.gpe.GPEDefaults;
import org.gvsig.gpe.GPERegister;
import org.gvsig.gpe.exceptions.ParserCreationException;
import org.gvsig.gpe.gml.utils.GMLUtilsParser;
import org.gvsig.gpe.kml.utils.KmlCompoundStyle;
import org.gvsig.gpe.kml.utils.KmlIconStyle;
import org.gvsig.gpe.kml.utils.KmlLabelStyle;
import org.gvsig.gpe.kml.utils.KmlLineStyle;
import org.gvsig.gpe.kml.utils.KmlPolygonStyle;
import org.gvsig.gpe.kml.utils.KmlStyle;
import org.gvsig.gpe.kml.writer.GPEKmlWriterHandlerImplementor;
import org.gvsig.gpe.parser.GPEParser;
import org.gvsig.gpe.utils.StringUtils;
import org.gvsig.gpe.writer.GPEWriterHandler;
import org.gvsig.symbology.fmap.symbols.PictureMarkerSymbol;

import com.hardcode.gdbms.driver.exceptions.InitializeDriverException;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.data.driver.DriverException;
import com.hardcode.gdbms.engine.values.Value;
import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.ExportTo;
import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.core.symbols.IFillSymbol;
import com.iver.cit.gvsig.fmap.core.symbols.ILineSymbol;
import com.iver.cit.gvsig.fmap.core.symbols.IMarkerSymbol;
import com.iver.cit.gvsig.fmap.core.symbols.ISymbol;
import com.iver.cit.gvsig.fmap.crs.CRSFactory;
import com.iver.cit.gvsig.fmap.drivers.DriverIOException;
import com.iver.cit.gvsig.fmap.layers.FBitSet;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.LayerFactory;
import com.iver.cit.gvsig.fmap.layers.ReadableVectorial;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.iver.cit.gvsig.fmap.layers.layerOperations.LayerCollection;
import com.iver.cit.gvsig.fmap.rendering.IClassifiedVectorLegend;
import com.iver.cit.gvsig.fmap.rendering.ILegend;
import com.iver.cit.gvsig.fmap.rendering.IVectorLegend;
import com.iver.cit.gvsig.fmap.rendering.styling.labeling.AttrInTableLabelingStrategy;
import com.iver.cit.gvsig.fmap.rendering.styling.labeling.ILabelingStrategy;
import com.iver.cit.gvsig.fmap.rendering.styling.labeling.LabelClass;
import com.iver.cit.gvsig.fmap.rendering.styling.labeling.LabelingFactory;
import com.iver.cit.gvsig.project.documents.view.gui.IView;
import com.iver.utiles.swing.threads.AbstractMonitorableTask;

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
 * This class writes a gvSIG layer and its children
 * (if the driver supports a layer with children)
 * @author Jorge Piera LLodr� (jorge.piera@iver.es)
 */
public class ExportTask extends AbstractMonitorableTask{
	private FLayer rootLayer = null;
	private GPEWriterHandler writer = null;
	private MapContext mapContext = null;
	private ExportGeometry eGeometry = null;	
	private File file = null;
	
	private Hashtable<ISymbol, KmlStyle> symbols = null;
	private KmlLabelStyle defaultLabelStyle;
	
	public ExportTask(FLayer layer, 
			GPEWriterHandler writer,
			MapContext mapContext,
			File file){
		this.rootLayer = layer;
		this.writer = writer;
		this.mapContext = mapContext;
		this.eGeometry = new ExportGeometry(writer);
		this.file = file;
		eGeometry.setProjOrig(layer.getProjection());
		if (writer.getFormat().equals("text/xml; subtype=kml/2.1")){
			eGeometry.setProjDest(CRSFactory.getCRS("EPSG:4326"));	
		}else{
			eGeometry.setProjDest(layer.getProjection());
		}
		setInitialStep(0);
		setDeterminatedProcess(true);
		setStatusMessage(PluginServices.getText(this, "gpe_exporting"));
	}

	public void run() throws Exception {
		writer.initialize();
		exportLayer(rootLayer);
		writer.close();
		if (isFileLoaded()){
			loadExportedFile();
		}
	}
	
	/**
	 * Load the exported file
	 * @throws GPEParserCreationException 
	 * @throws IOException 
	 */
	private void loadExportedFile() throws ParserCreationException, IOException{
		GPEParser parser = GPERegister.createParser(file.toURI());
		GPEVectorialDriver driver = GPEDriverFactory.createDriver(parser);
		driver.open(file);
		IProjection proj = driver.getProjection();
		if(proj == null)
			proj = eGeometry.getProjDest();
		FLayer layer = LayerFactory.createLayer(file.getAbsolutePath(),
				driver, proj);
		((IView)PluginServices.getMDIManager().getActiveWindow()).
		getMapControl().getMapContext().getLayers().addLayer(layer);
	}
	
	/**
	 * @return true if the exported files have to be loaded
	 */
	private boolean isFileLoaded(){
		int load = JOptionPane.showConfirmDialog(
				(JComponent) PluginServices.getMDIManager().getActiveWindow()
				, PluginServices.getText(this, "insertar_en_la_vista_la_capa_creada"),
				PluginServices.getText(this,"insertar_capa"),
				JOptionPane.YES_NO_OPTION);
		if (load == JOptionPane.YES_OPTION){
			return true;
		}
		return false;
	}

	/**
	 * It writes a layer and its children recursively
	 * @param layer
	 * The layer to write
	 */
	private void exportLayer(FLayer layer){
		String projection = eGeometry.getProjDest().getAbrev();
		
		writer.startLayer(null, null, layer.getName(), null, projection);
		
		if (writer.getWriterHandlerImplementor() instanceof GPEKmlWriterHandlerImplementor) {
			GPEKmlWriterHandlerImplementor kmlWriterHandler = (GPEKmlWriterHandlerImplementor) writer.getWriterHandlerImplementor();
			KmlStyle[] styles = convertSymbols(layer);
			kmlWriterHandler.writeStyles(styles);
		}
		
		//Sets the extent
		try {
			if (layer.getProjection().getAbrev().compareTo(mapContext.getViewPort().getProjection().getAbrev())==0)
				writer.startBbox(null,new CoordinatesSequenceBbox(eGeometry.getExtent(layer.getFullExtent())), eGeometry.getProjDest().getAbrev());
			else
				writer.startBbox(null,new CoordinatesSequenceBbox(layer.getFullExtent()),eGeometry.getProjDest().getAbrev());
			writer.endBbox();
		} catch (Exception e) {
			writer.getErrorHandler().addWarning(new ExtentExportWarning(layer,e));
		} 
		//Export the layer information
		exportLayerInfo(layer);
		//If the layer has children...
		if (layer instanceof LayerCollection){
			LayerCollection layers = (LayerCollection)layer;
			for (int i=0 ; i<layers.getLayersCount() ; i++){
				exportLayer(layers.getLayer(i));
			}
		}
		
		writer.endLayer();
	}

	private KmlStyle[] convertSymbols(FLayer layer) {
		FLyrVect lyrVect = (FLyrVect) layer;
		IVectorLegend legend = (IVectorLegend) lyrVect.getLegend();
		ISymbol defaultSym = legend.getDefaultSymbol();
		ArrayList<KmlStyle> aux = new ArrayList<KmlStyle>();
		KmlStyle defaultStyle = convertSymbol(defaultSym);
		defaultStyle.setId("defaultStyle");
		aux.add(defaultStyle);
		symbols = new Hashtable<ISymbol, KmlStyle>();
		symbols.put(defaultSym, defaultStyle);
		
		// we only support a default labelStyle for all texts
		defaultLabelStyle = new KmlLabelStyle();
		
		if (lyrVect.isLabeled()) {
			ILabelingStrategy labelStrategy = lyrVect.getLabelingStrategy();
			if (labelStrategy instanceof AttrInTableLabelingStrategy) { 
				// we only support simple labels
				AttrInTableLabelingStrategy lbs = (AttrInTableLabelingStrategy) labelStrategy;
				defaultLabelStyle.setColor(lbs.getFixedColor());
				defaultLabelStyle.setId("labelStyle");
				aux.add(defaultLabelStyle);

			}
		}
		if (legend instanceof IClassifiedVectorLegend) {
			IClassifiedVectorLegend cLeg = (IClassifiedVectorLegend) legend;
			ISymbol[] symbolsAux = cLeg.getSymbols();
			for (int i=0; i < symbolsAux.length; i++) {
				ISymbol s = symbolsAux[i];
				KmlStyle style = convertSymbol(s);
				style.setId(i + "");
				aux.add(style);
				symbols.put(s, style);
			}
		}

		return aux.toArray(new KmlStyle[0]);
	}

	private KmlStyle convertSymbol(ISymbol s) {
		KmlStyle style = null;
		
		if (s instanceof IMarkerSymbol) {
			KmlIconStyle icoStyle = new KmlIconStyle();
			icoStyle.setColor(((IMarkerSymbol) s).getColor());
	
			if (s instanceof PictureMarkerSymbol) {
				float angKml = (float) GPETypesConversion.RadTokmlDeg(((IMarkerSymbol) s).getRotation());
				icoStyle.setHeading(angKml);
			
				//TODO: CONVERT IMAGES TO HREF... but I don't know how to do it.
			}
			
			style = icoStyle;
		}
		
		if (s instanceof ILineSymbol) {
			ILineSymbol lSym = (ILineSymbol) s;
			KmlLineStyle lStyle = new KmlLineStyle();
			lStyle.setColor(lSym.getColor());
			lStyle.setWidth((float) lSym.getLineWidth());
			
			style = lStyle;
	
		}

		if (s instanceof IFillSymbol) {
			IFillSymbol fSym = (IFillSymbol) s;
			KmlCompoundStyle cStyle = new KmlCompoundStyle();
			KmlLineStyle lStyle = new KmlLineStyle();
			ILineSymbol lSym = fSym.getOutline();
			lStyle.setColor(lSym.getColor());
			lStyle.setWidth((float) lSym.getLineWidth());

			KmlPolygonStyle fStyle = new KmlPolygonStyle();
			fStyle.setColor(fSym.getFillColor());
			fStyle.setOutline(fSym.hasOutline());
			fStyle.setFill(fSym.hasFill());
			
			cStyle.setLineStyle(lStyle);
			cStyle.setPolygonStyle(fStyle);
			
			style = cStyle;
	
		}

		
		return style;
	}

	/**
	 * It exports the layer information. Geometries if is
	 * a vectorial layer, images if is a raster layer...
	 * @param layer
	 */
	private void exportLayerInfo(FLayer layer){
		try {
			if (layer instanceof FLyrVect){
				exportVectorialLayer((FLyrVect)layer);
			}
		} catch (Exception e) {
			writer.getErrorHandler().addError(e);
		}
	}	

	/**
	 * Export the geometries of a vectorial layer
	 * @param layer
	 * @throws DriverException 
	 * @throws DriverIOException 
	 * @throws ReadDriverException 
	 * @throws InitializeDriverException 
	 * @throws ReadDriverException 
	 */
	private void exportVectorialLayer(FLyrVect layer) throws DriverException, DriverIOException, InitializeDriverException, ReadDriverException {
		System.out.println(layer.getName());
		ReadableVectorial rv = layer.getSource();
		SelectableDataSource sds = layer.getRecordset();
		rv.start();
		//If there is a selection the rows to export have to be 
		//the selected rows
		FBitSet bitSet = sds.getSelection();
		int rowCount;		
		if (bitSet.cardinality() == 0){
			rowCount = rv.getShapeCount();
			for (int i = 0; i < rowCount; i++) {
				exportFeature(sds, rv, i, layer);
			}
		}else{
			rowCount = bitSet.cardinality();
			for (int i = bitSet.nextSetBit(0); i >= 0; i = bitSet
			.nextSetBit(i + 1)) {
				exportFeature(sds, rv, i, layer);
			}
		}	
		rv.stop();
	}

	/**
	 * It writes a feature (geometry + attributes)
	 * @param sds
	 * The selectable datasource to get the attributes
	 * @param rv
	 * The readable vectorial to get the geoemtries
	 * @param index
	 * The feature index
	 * @param layer
	 * Only to personalize the exceptions
	 * @throws ReadDriverException
	 */
	private void exportFeature(SelectableDataSource sds, ReadableVectorial rv, int index, FLayer layer){
		try {

			int fieldName = sds.getFieldIndexByName("name");
			String name = null;
			if (fieldName != -1)
				name = sds.getFieldValue(index, fieldName).toString();
			if (writer.getWriterHandlerImplementor() instanceof GPEKmlWriterHandlerImplementor) {
				GPEKmlWriterHandlerImplementor kmlWriterHandler = (GPEKmlWriterHandlerImplementor) writer.getWriterHandlerImplementor();
				FLyrVect lyrVect = (FLyrVect) layer;
				if (lyrVect.getLabelingStrategy() != null) {
					ILabelingStrategy labelStrategy = lyrVect.getLabelingStrategy();
					String fieldLabel = labelStrategy.getUsedFields()[0];
					int idfieldlabel = sds.getFieldIndexByName(fieldLabel);
					name = sds.getFieldValue(index, idfieldlabel).toString();
				}
			}			
			writer.startFeature(String.valueOf(index), "FEATURE", name);
			
			//Add the attributes
			if (writer.getWriterHandlerImplementor() instanceof GPEKmlWriterHandlerImplementor) {
				GPEKmlWriterHandlerImplementor kmlWriterHandler = (GPEKmlWriterHandlerImplementor) writer.getWriterHandlerImplementor();
				// If KML, we add a fieldName styleUrl in order to export simbology
				// If the layer is labelled, we can use the text label as <name> in order to
				// allow Google Earth to put labels.
				FLyrVect lyrVect = (FLyrVect) layer;
				IVectorLegend legend = (IVectorLegend) lyrVect.getLegend();
				ISymbol s = legend.getSymbol(index);
				KmlStyle style = symbols.get(s);
				if (lyrVect.isLabeled()) {
					writer.startElement("", "styleUrl",	"#" + defaultLabelStyle.getId());
					writer.endElement();					
				}
				else
				{
					writer.startElement("", "styleUrl",	"#" + style.getId());
					writer.endElement();
				}
				
			}

			//Add the geoemtry
			IGeometry geom = rv.getShape(index);			
			eGeometry.writeGeometry(geom);
			
			Value[] values = sds.getRow(index);
			for (int i=0 ; i<values.length ; i++){	
				String fldName = StringUtils.replaceAllString(sds.getFieldName(i), " ", "_"); 
				writer.startElement("", 
						fldName,
						values[i].toString());
				writer.endElement();
			}
			writer.endFeature();
		} catch (Exception e) {
			writer.getErrorHandler().addError(new FeatureExportException(layer,index,e));
		}		
	}

	/* (non-Javadoc)
	 * @see com.iver.utiles.swing.threads.IMonitorableTask#finished()
	 */
	public void finished() {		
		try {
			ExportTo.executeCommand((FLyrVect)rootLayer);
		} catch (Exception e) {
			NotificationManager.addError(e);
		}
	}
}
