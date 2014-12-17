package es.icarto.gvsig.audasacommons.incidencias;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Types;
import java.text.Collator;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.hardcode.driverManager.DriverLoadException;
import com.hardcode.gdbms.driver.exceptions.InitializeWriterException;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.instruction.FieldNotFoundException;
import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueFactory;
import com.iver.cit.gvsig.exceptions.layers.LegendLayerException;
import com.iver.cit.gvsig.exceptions.visitors.ProcessWriterVisitorException;
import com.iver.cit.gvsig.exceptions.visitors.StartWriterVisitorException;
import com.iver.cit.gvsig.exceptions.visitors.StopWriterVisitorException;
import com.iver.cit.gvsig.fmap.core.DefaultFeature;
import com.iver.cit.gvsig.fmap.core.FShape;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.core.ShapeFactory;
import com.iver.cit.gvsig.fmap.core.symbols.SimpleMarkerSymbol;
import com.iver.cit.gvsig.fmap.drivers.FieldDescription;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.ReadableVectorial;
import com.iver.cit.gvsig.fmap.rendering.VectorialUniqueValueLegend;
import com.iver.cit.gvsig.fmap.rendering.styling.labeling.AttrInTableLabelingStrategy;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.StyleSelector;
import es.icarto.gvsig.commons.datasources.FieldDescriptionFactory;
import es.icarto.gvsig.commons.datasources.SHPFactory;
import es.icarto.gvsig.commons.queries.ConnectionWrapper;
import es.icarto.gvsig.commons.utils.FileNameUtils;
import es.udc.cartolab.gvsig.navtable.format.DateFormatNT;
import es.udc.cartolab.gvsig.navtable.format.DoubleFormatNT;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class IncidenciasParser {

    private static final Logger logger = Logger
	    .getLogger(IncidenciasParser.class);

    private final Sheet sheet;

    private final List<String> header = new ArrayList<String>();

    private int tramoIdx = -1;
    private int tipoViaIdx = -1;
    private int nombreIdx = -1;
    private int pkIdx = -1;
    private int ramalIdx = -1;
    private int sentidoIdx = -1;

    private boolean pushMotivo = false;
    private final Collator collator;

    private final ConnectionWrapper cw;

    private final File file;

    private final List<String> warnings = new ArrayList<String>();
    private final List<IFeature> featureList = new ArrayList<IFeature>();
    private final List<double[]> coordList = new ArrayList<double[]>();

    public IncidenciasParser(File file) throws IOException,
	    InvalidFormatException {
	collator = Collator.getInstance();
	collator.setStrength(Collator.PRIMARY);

	cw = new ConnectionWrapper(DBSession.getCurrentSession()
		.getJavaConnection());

	this.file = file;

	if ((file == null) || (!file.isFile())) {
	    throw new IOException("El fichero no existe");
	}

	Workbook wb = WorkbookFactory.create(file);

	int sheetIdx = wb.getActiveSheetIndex();
	if (sheetIdx != 0) {
	    addWarning("La última hoja empleada no es la primera del libro. Compruebe que esto es correcto");
	}
	sheet = wb.getSheetAt(sheetIdx);
    }

    private void addWarning(String string) {
	warnings.add(string);
    }

    public void parse() {
	initHeader();
	initFeatures();
    }

    private void initHeader() {
	for (Cell cell : sheet.getRow(0)) {
	    if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
		final String value = cell.getStringCellValue();

		final int columnIdx = cell.getColumnIndex();
		if (collator.compare(value, "TRAMO") == 0) {
		    tramoIdx = columnIdx;
		} else if ((collator.compare(value, "CARACTERÍSTICAS VÍA") == 0)
			|| (collator.compare(value, "TIPO VÍA") == 0)) {
		    tipoViaIdx = columnIdx;
		} else if ((collator.compare(value, "LUGAR") == 0)
			|| (collator.compare(value, "NOMBRE") == 0)) {
		    nombreIdx = columnIdx;
		} else if (collator.compare(value, "P.K.") == 0) {
		    pkIdx = columnIdx;
		} else if (collator.compare(value, "RAMAL") == 0) {
		    ramalIdx = columnIdx;
		} else if (collator.compare(value, "SENTIDO") == 0) {
		    sentidoIdx = columnIdx;
		}

		header.add(value);
		setEnumHeaderIdx(value, columnIdx);

		if (collator.compare(value, "Seguimiento") == 0) {
		    break;
		}

	    } else {
		addWarning("La primera fila tiene una cabecera incorrecta o menos valores que otras filas");
	    }
	}

	if (header.indexOf("Motivo") == -1) {
	    header.add("Motivo");
	    pushMotivo = true;
	    setEnumHeaderIdx("Motivo", header.size() - 1);
	}

	if (tramoIdx == -1) {
	    throw new RuntimeException(
		    "Cabecera incorrecta. Es necesario que la columna 'TRAMO' esté presente en la cabecera");
	}
	if (tipoViaIdx == -1) {
	    throw new RuntimeException(
		    "Cabecera incorrecta. Es necesario que la columna 'TIPO VÍA' o 'CARACTERÍSTICAS VÍA' esté presente en la cabecera");
	}
	if (nombreIdx == -1) {
	    throw new RuntimeException(
		    "Cabecera incorrecta. Es necesario que la columna 'LUGAR' o 'NOMBRE' esté presente en la cabecera");
	}
	if (pkIdx == -1) {
	    throw new RuntimeException(
		    "Cabecera incorrecta. Es necesario que la columna 'P.K.' esté presente en la cabecera");
	}
	if (ramalIdx == -1) {
	    throw new RuntimeException(
		    "Cabecera incorrecta. Es necesario que la columna 'RAMAL' esté presente en la cabecera");
	}
	if (sentidoIdx == -1) {
	    throw new RuntimeException(
		    "Cabecera incorrecta. Es necesario que la columna 'SENTIDO' esté presente en la cabecera");
	}
    }

    private void setEnumHeaderIdx(String value, int columnIdx) {
	Header h = Header.fromString(value);
	if (h != null) {
	    h.setIdx(columnIdx);
	}
    }

    private void initFeatures() {
	Iterator<Row> rowIterator = sheet.rowIterator();
	rowIterator.next(); // skip header

	while (rowIterator.hasNext()) {
	    Row row = rowIterator.next();

	    if (notValidRow(row)) {
		continue;
	    }

	    Value[] values = new Value[header.size()];
	    for (int i = 0; i < header.size(); i++) {
		String str = null;
		if ((pushMotivo) && (i == header.size() - 1)) {
		    str = "ACCIDENTES";
		} else {
		    str = getValueAsString(row.getCell(i));
		}
		values[i] = ValueFactory.createValue(str);
	    }

	    TableModel results = calculateGeom(row);
	    if (results != null) {
		double x = (Double) results.getValueAt(0, 0);
		double y = (Double) results.getValueAt(0, 1);
		IGeometry geom = ShapeFactory.createPoint2D(x, y);
		featureList.add(new DefaultFeature(geom, values));

		double lat = (Double) results.getValueAt(0, 2);
		double lon = (Double) results.getValueAt(0, 3);

		coordList.add(new double[] { lon, lat });
	    }
	}
    }

    private boolean notValidRow(Row row) {
	String v = getValueAsString(row.getCell(0));
	if (v.isEmpty() || v.equals(header.get(0))) {
	    return true;
	}
	return false;
    }

    /**
     * call parse first
     */
    public FLyrVect toFLyrVect() {
	String shpPath = FileNameUtils.replaceExtension(file.getAbsolutePath(),
		"shp");
	File outfile = new File(shpPath);

	FieldDescriptionFactory fdf = new FieldDescriptionFactory();
	fdf.setDefaultStringLength(254);
	for (String h : header) {
	    String normalizedColumn = Normalizer
		    .normalize(h, Normalizer.Form.NFD)
		    .replaceAll("[^\\p{ASCII}]", "").replace(" ", "_")
		    .toUpperCase();
	    fdf.addString(normalizedColumn);
	}
	FieldDescription[] fieldsDesc = fdf.getFields();
	int geometryType = FShape.POINT;

	IFeature[] features = featureList.toArray(new IFeature[0]);

	FLyrVect layer = null;
	try {
	    SHPFactory.createSHP(outfile, fieldsDesc, geometryType, features);
	    layer = SHPFactory.getFLyrVectFromSHP(outfile, "EPSG:23029");
	    applySymbology(layer);
	} catch (StartWriterVisitorException e) {
	    logger.error(e.getStackTrace(), e);
	} catch (ProcessWriterVisitorException e) {
	    logger.error(e.getStackTrace(), e);
	} catch (StopWriterVisitorException e) {
	    logger.error(e.getStackTrace(), e);
	} catch (DriverLoadException e) {
	    logger.error(e.getStackTrace(), e);
	} catch (InitializeWriterException e) {
	    logger.error(e.getStackTrace(), e);
	}
	return layer;
    }

    private String kmlAddDescription(Value[] atts, Header h) {
	final int idx = h.getIdx();
	if (idx != -1) {
	    final String template = "<tr><td><strong>%s<strong></td><td>%s</td></tr>";
	    return String.format(template, h.toString(), atts[idx].toString());
	}
	return "";
    }

    public boolean toKml() {

	Kml unmarshal = Kml
		.unmarshal("<?xml version=\"1.0\" encoding=\"UTF-8\"?><kml xmlns=\"http://www.opengis.net/kml/2.2\" xmlns:gx=\"http://www.google.com/kml/ext/2.2\" xmlns:kml=\"http://www.opengis.net/kml/2.2\" xmlns:atom=\"http://www.w3.org/2005/Atom\"><Document>	<name>/tmp/listado de prueba</name>	<open>1</open>	<StyleMap id=\"m_ylw-pushpin\">		<Pair>			<key>normal</key>			<styleUrl>#s_ylw-pushpin</styleUrl>		</Pair>		<Pair>			<key>highlight</key>			<styleUrl>#s_ylw-pushpin_hl</styleUrl>		</Pair>	</StyleMap>	<Style id=\"s_ylw-pushpin\">		<IconStyle>			<scale>0.7</scale>			<Icon>				<href>http://maps.google.com/mapfiles/kml/pushpin/wht-pushpin.png</href>			</Icon>			<hotSpot x=\"20\" y=\"2\" xunits=\"pixels\" yunits=\"pixels\"/>		</IconStyle>		<LabelStyle>			<scale>0.5</scale>		</LabelStyle>		<BalloonStyle>		</BalloonStyle>		<ListStyle>		</ListStyle>	</Style>	<Style id=\"s_ylw-pushpin_hl\">		<IconStyle>			<scale>0.827273</scale>			<Icon>				<href>http://maps.google.com/mapfiles/kml/pushpin/wht-pushpin.png</href>			</Icon>			<hotSpot x=\"20\" y=\"2\" xunits=\"pixels\" yunits=\"pixels\"/>		</IconStyle>		<LabelStyle>			<scale>0.5</scale>		</LabelStyle>		<BalloonStyle>		</BalloonStyle>		<ListStyle>		</ListStyle>	</Style>	<Placemark>		<name>ACCIDENTES</name>		<open>1</open>		<description>prueba</description>		<styleUrl>#m_ylw-pushpin</styleUrl>		<Point>			<coordinates>-8.666041999999999,42.275911,0</coordinates>		</Point>	</Placemark></Document></kml>");
	List<StyleSelector> styleSelector = unmarshal.getFeature()
		.getStyleSelector();

	String kmlNamePath = FileNameUtils.removeExtension(file
		.getAbsolutePath());
	final Kml kml = new Kml();
	Document document = kml.createAndSetDocument().withName(kmlNamePath);
	document.setStyleSelector(styleSelector);

	for (int i = 0; i < coordList.size(); i++) {
	    Value[] atts = featureList.get(i).getAttributes();

	    double[] coord = coordList.get(i);

	    String description = kmlDescription(atts);
	    String name = kmlName(atts);

	    document.createAndAddPlacemark().withName(name)
		    .withDescription(description)
		    .withStyleUrl("#m_ylw-pushpin").withOpen(Boolean.TRUE)
		    .createAndSetPoint().addToCoordinates(coord[0], coord[1]);
	}

	try {
	    kml.marshal(new File(kmlNamePath + ".kml"));

	} catch (FileNotFoundException e) {
	    logger.error(e.getStackTrace(), e);
	    return false;
	}
	return true;

    }

    private String kmlName(Value[] atts) {
	return atts[Header.MOTIVO.getIdx()].toString() + ": "
		+ atts[0].toString();
    }

    private String kmlDescription(Value[] atts) {
	// String description = "<![CDATA[<table>";

	String description = "<table>";
	description += kmlAddDescription(atts, Header.FECHA);
	description += kmlAddDescription(atts, Header.HORA);
	description += kmlAddDescription(atts, Header.PK);
	description += kmlAddDescription(atts, Header.SENTIDO);
	description += kmlAddDescription(atts, Header.DURACION);
	description += kmlAddDescription(atts, Header.ASISTENCIAS_MOVILIZADAS);
	description += kmlAddDescription(atts, Header.CLIMA);
	description += kmlAddDescription(atts, Header.CAUSAS);
	description += kmlAddDescription(atts, Header.N_VEHICULOS);
	description += kmlAddDescription(atts, Header.HERIDOS);
	description += kmlAddDescription(atts, Header.MUERTOS);
	description += kmlAddDescription(atts, Header.ASIST_SANITARIA);
	description += kmlAddDescription(atts, Header.INFORME_ARENA);
	description += "</table>";
	// description += "</table>]]>";
	return description;
    }

    private TableModel calculateGeom(Row row) {
	String tramo = getValueAsString(row.getCell(tramoIdx));
	String tipoVia = getValueAsString(row.getCell(tipoViaIdx));
	String nombre = getValueAsString(row.getCell(nombreIdx));

	String pkStr = getValueAsString(row.getCell(pkIdx));
	pkStr = pkStr.replace("+", ".").replace(",", ".");
	String pkformatted = null;
	try {
	    double pk = Double.parseDouble(pkStr);
	    pkformatted = String.format("%3.3f", pk).replace(",", ".");
	} catch (NumberFormatException e) {
	    pkformatted = null;
	}

	String ramal = getValueAsString(row.getCell(ramalIdx));
	String sentido = getValueAsString(row.getCell(sentidoIdx));
	sentido = sentido.isEmpty() ? "Ambos" : sentido;

	if (tramo.isEmpty() || tipoVia.isEmpty()) {
	    addWarning(String
		    .format("Fila %d no localizada. Tramo y Tipo Via son campos obligatorios. Tramo: %s. Tipo vía: %s. Nombre: %s. PK: %s. Ramal: %s. Sentido: %s",
			    row.getRowNum() + 1, tramo, tipoVia, nombre,
			    pkformatted, ramal, sentido));
	    return null;
	}

	String query = String
		.format("select x_utm, y_utm, latitud, longitud from audasa_aplicaciones.incidencias where tipo_via = '%s' and tramo = '%s' ",
			tipoVia, tramo);

	if ((collator.compare(tipoVia, "A. de Descanso") == 0)
		|| (collator.compare(tipoVia, "A. de Servicio") == 0)
		|| (collator.compare(tipoVia, "A. de Manto.") == 0)
		|| (collator.compare(tipoVia, "Enlace") == 0)) {
	    query += String.format(
		    " and nombre = '%s' and ramal = '%s' and sentido = '%s'",
		    nombre, ramal, sentido);

	} else if (collator.compare(tipoVia, "Estación de Peaje") == 0) {
	    query += String.format(" and nombre = '%s' and sentido = '%s'",
		    nombre, sentido);

	} else if (collator.compare(tipoVia, "Intercambiador") == 0) {
	    query += String.format(
		    " and nombre = '%s' and abs(pk - %s) < 0.01", nombre,
		    pkformatted);
	} else if (collator.compare(tipoVia, "Tronco") == 0) {
	    query += String.format(
		    " and sentido = '%s' and abs(pk - %s) < 0.01", sentido,
		    pkformatted);
	} else if (collator.compare(tipoVia, "Túnel") == 0) {
	    query += String
		    .format(" and nombre = '%s' and sentido = '%s' and abs(pk - %s) < 0.01",
			    nombre, sentido, pkformatted);

	}
	query += " LIMIT 1;";

	DefaultTableModel results = cw.execute(query);

	if (results.getRowCount() == 0) {
	    addWarning(String
		    .format("Fila %d no localizada. Tramo: %s. Tipo vía: %s. Nombre: %s. PK: %s. Ramal: %s. Sentido: %s",
			    row.getRowNum(), tramo, tipoVia, nombre,
			    pkformatted, ramal, sentido));
	    return null;
	}
	return results;
    }

    public List<String> getHeader() {
	return header;
    }

    public String getValueAsString(Cell cell) {
	if (cell == null) {
	    return "";
	}
	switch (cell.getCellType()) {
	case Cell.CELL_TYPE_STRING:
	    return cell.getRichStringCellValue().getString();
	case Cell.CELL_TYPE_NUMERIC:
	    if (DateUtil.isCellDateFormatted(cell)) {
		Date date = cell.getDateCellValue();
		return DateFormatNT.getDateFormat().format(date);
	    } else {
		double numericCellValue = cell.getNumericCellValue();
		return DoubleFormatNT.getEditingFormat().format(
			numericCellValue);
	    }
	case Cell.CELL_TYPE_BOOLEAN:
	    return cell.getBooleanCellValue() ? "Sí" : "No";
	case Cell.CELL_TYPE_FORMULA:
	    return cell.getCellFormula();
	case Cell.CELL_TYPE_BLANK:
	    return "";
	default:
	    return "";
	}
    }

    public List<String> getWarnings() {
	return warnings;
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
	}
	applyLabel(layer);
    }

    private void applyLabel(FLyrVect layer) {
	AttrInTableLabelingStrategy st = new AttrInTableLabelingStrategy();
	st.setFixedColor(new Color(51, 51, 51));
	st.setFixedSize(6);
	st.setTextField("MOTIVO");
	st.setFont(new Font("Arial", Font.BOLD, 6));
	st.setUsesFixedSize(true);
	st.setUsesFixedColor(true);
	st.setLayer(layer);

	layer.setLabelingStrategy(st);
	layer.setIsLabeled(true);
    }

    private void applyLegend(FLyrVect layer) throws ReadDriverException,
	    FieldNotFoundException, LegendLayerException {

	ReadableVectorial source = layer.getSource();
	int motivoIdx = source.getRecordset().getFieldIndexByName("MOTIVO");
	List<Value> list = new ArrayList<Value>();
	for (int i = 0; i < source.getShapeCount(); i++) {
	    Value att = source.getFeature(i).getAttribute(motivoIdx);
	    if (!list.contains(att)) {
		list.add(att);
	    }
	}

	VectorialUniqueValueLegend legend = new VectorialUniqueValueLegend(
		layer.getShapeType());
	legend.setDataSource(source.getRecordset());
	legend.setClassifyingFieldNames(new String[] { "MOTIVO" });
	legend.setClassifyingFieldTypes(new int[] { Types.VARCHAR });
	for (int i = 0; i < list.size(); i++) {
	    Motivo motivo = Motivo.fromString(list.get(i).toString());
	    SimpleMarkerSymbol symbol = new SimpleMarkerSymbol();
	    if (motivo == null) {
		legend.useDefaultSymbol(true);
		symbol.setColor(new Color(255, 0, 0));
		symbol.setSize(6);
		symbol.setDescription("MOTIVO NO IDENTIFICADO");
		// legend.addSymbol(new NullUniqueValue(), symbol);
		// legend.getSymbolByValue(new NullUniqueValue());
		legend.setDefaultSymbol(symbol);
		addWarning(String.format("Motivo desconocido: %s", list.get(i)));
	    } else {
		symbol.setColor(motivo.color());
		symbol.setSize(6);
		symbol.setDescription(motivo.toString());
		legend.addSymbol(list.get(i), symbol);
	    }
	}

	layer.setLegend(legend);
    }
}
