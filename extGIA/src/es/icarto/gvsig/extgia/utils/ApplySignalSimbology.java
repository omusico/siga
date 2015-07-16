package es.icarto.gvsig.extgia.utils;

import java.awt.Point;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.gvsig.symbology.fmap.symbols.PictureMarkerSymbol;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.instruction.FieldNotFoundException;
import com.hardcode.gdbms.engine.values.StringValue;
import com.hardcode.gdbms.engine.values.Value;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.exceptions.layers.LegendLayerException;
import com.iver.cit.gvsig.fmap.core.symbols.ISymbol;
import com.iver.cit.gvsig.fmap.core.symbols.MultiLayerMarkerSymbol;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.ReadableVectorial;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.iver.cit.gvsig.fmap.rendering.VectorialUniqueValueLegend;

import es.icarto.gvsig.commons.utils.FileNameUtils;

public class ApplySignalSimbology {

    private static final int PICTURE_SIZE = 20;
    private static final int CARTEL_PICTURE_SIZE = 50;
    private static final Point NOFFSET = new Point(0, 0);

    private static final Logger logger = Logger
	    .getLogger(ApplySignalSimbology.class);

    private final FLyrVect senhales;

    private final String extension = ".gif";

    private final String emptyImagePath = "0_placa.png";

    public ApplySignalSimbology(FLyrVect postes, FLyrVect signals) {
	this.senhales = signals;
	applySymbology(postes);
    }

    private void applySymbology(FLyrVect layer) {
	try {
	    applyLegend(layer);
	} catch (LegendLayerException e) {
	    logger.error(e.getStackTrace(), e);
	} catch (ReadDriverException e) {
	    logger.error(e.getStackTrace(), e);
	} catch (FieldNotFoundException e) {
	    logger.error(e.getStackTrace(), e);
	} catch (RuntimeException e) {
	    logger.error(e.getStackTrace(), e);
	    NotificationManager.addWarning("Error aplicando simbolog�a", e);
	}
    }

    private void applyLegend(FLyrVect poste) throws ReadDriverException,
    FieldNotFoundException, LegendLayerException {

	final String fieldName = "id_elemento_senhalizacion";
	final String[] classifyingFieldNames = new String[] { fieldName };
	final int[] classifyingFieldTypes = new int[] { Types.INTEGER };

	ReadableVectorial source = poste.getSource();

	VectorialUniqueValueLegend legend = new VectorialUniqueValueLegend(
		poste.getShapeType());
	legend.setDataSource(source.getRecordset());
	legend.setClassifyingFieldNames(classifyingFieldNames);
	legend.setClassifyingFieldTypes(classifyingFieldTypes);

	createLegend(legend, senhales);

	poste.setLegend(legend);
	poste.setMinScale(0);
	poste.setMaxScale(10000);
    }

    private int getIdx(FLyrVect layer, String fieldName) {
	try {
	    SelectableDataSource recordset = layer.getRecordset();
	    return recordset.getFieldIndexByName(fieldName);
	} catch (ReadDriverException e) {
	    throw new RuntimeException(e);
	}
    }

    private void createLegend(VectorialUniqueValueLegend legend,
	    FLyrVect senhales) throws ReadDriverException {

	int idIndex = getIdx(senhales, "id_elemento_senhalizacion");
	int tipoIdx = getIdx(senhales, "tipo_senhal");
	int codigoIdx = getIdx(senhales, "codigo_senhal");

	ReadableVectorial source = senhales.getSource();

	Map<Value, Integer> list = new HashMap<Value, Integer>();

	for (int i = 0; i < source.getShapeCount(); i++) {
	    Value[] attributes = source.getFeature(i).getAttributes();
	    Value tipo = attributes[tipoIdx];
	    Value codigo = attributes[codigoIdx];
	    Value id = attributes[idIndex];
	    final Integer count = list.get(id);
	    if (count == null) {
		ISymbol symbol = setPictureSymbol(0, tipo, codigo);
		legend.addSymbol(id, symbol);
		list.put(id, 0);
	    } else {
		MultiLayerMarkerSymbol multi = new MultiLayerMarkerSymbol();
		final ISymbol symbolByValue = legend.getSymbolByValue(id);
		if (symbolByValue instanceof MultiLayerMarkerSymbol) {
		    MultiLayerMarkerSymbol s = (MultiLayerMarkerSymbol) symbolByValue;
		    for (int j = 0; j < s.getLayerCount(); j++) {
			multi.addLayer(s.getLayer(j));
		    }
		} else {
		    multi.addLayer(symbolByValue);
		}

		ISymbol symbol = setPictureSymbol((count + 1), tipo, codigo);
		multi.addLayer(symbol);
		legend.delSymbol(id);
		legend.addSymbol(id, multi);
		list.put(id, count + 1);
	    }
	}
    }

    private ISymbol setPictureSymbol(int offset, Value tipoValue,
	    Value codigoValue) {
	String file = emptyImagePath;
	int size = PICTURE_SIZE;

	String tipo = stringValue(tipoValue);
	String codigo = stringValue(codigoValue);

	if (tipo.equals("Cartel")) {
	    if (codigo.isEmpty() || (codigo.equals("Otro"))) {
		file = "0_cartel.png";
	    } else {
		file = codigo + extension;
		size = CARTEL_PICTURE_SIZE;
	    }

	} else if (tipo.equals("Placa")) {
	    if (codigo.isEmpty() || (codigo.equals("Otro"))) {
		file = "0_placa.png";
	    } else {
		file = codigo + extension;
	    }
	} else {
	    // This condition is the same as "Placa" but this approach is more
	    // readable
	    if (codigo.isEmpty() || (codigo.equals("Otro"))) {
		file = "0_placa.png";
	    } else {
		file = codigo + extension;
	    }
	}

	PictureMarkerSymbol symbol = new PictureMarkerSymbol();
	symbol.setDescription(FileNameUtils.removeExtension(file));
	URL imageUrl;
	try {
	    imageUrl = new URL("file:senhales/" + file);
	    symbol.setImage(imageUrl);
	    symbol.setSelImage(imageUrl);
	} catch (MalformedURLException e) {
	    logger.error(e.getStackTrace(), e);
	} catch (IOException e) {
	    logger.error(e.getStackTrace(), e);
	}
	symbol.setOffset(offset == 0 ? NOFFSET : new Point(0, size * offset));
	symbol.setSize(size);

	// symbol.setUnit(unitIndex);
	// symbol.setRotation(r);
	// Por defecto est� a true
	// symbol.setIsShapeVisible(isShapeVisible);
	// S�lo vale para CharacterMarkerSymbol
	// symbol.setMask(mask);
	return symbol;
    }

    private String stringValue(Value v) {
	if (v instanceof StringValue) {
	    return v.toString().trim();
	}
	return "";

    }

}