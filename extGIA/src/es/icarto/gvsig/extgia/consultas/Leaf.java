package es.icarto.gvsig.extgia.consultas;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import com.iver.andami.PluginServices;

import es.icarto.gvsig.audasacommons.forms.reports.SaveFileDialog;
import es.icarto.gvsig.commons.queries.Component;
import es.icarto.gvsig.commons.queries.ConnectionWrapper;
import es.icarto.gvsig.commons.queries.Field;
import es.icarto.gvsig.commons.queries.XLSReport;
import es.icarto.gvsig.extgia.consultas.agregados.CSVTrabajosAgregadosReport;
import es.icarto.gvsig.extgia.consultas.agregados.TrabajosAgregadosReport;
import es.icarto.gvsig.extgia.consultas.caracteristicas.CSVCaracteristicasQueries;
import es.icarto.gvsig.extgia.consultas.caracteristicas.PDFCaracteristicasQueries;
import es.icarto.gvsig.extgia.consultas.firme.FirmeReconocimientosReport;
import es.icarto.gvsig.extgia.consultas.firme.FirmeTrabajosReport;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.KeyValue;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class Leaf implements Component {

    private final String[] element;
    private final ConsultasFilters<Field> consultasFilters;
    private final KeyValue tipoConsulta;
    private final boolean pdf;

    private File outputFile;
    private boolean emptyQuery = false;

    public Leaf(String[] element, ConsultasFilters<Field> consultasFilters,
	    KeyValue tipoConsulta, boolean pdf) {
	this.element = element;
	this.consultasFilters = consultasFilters;
	this.outputFile = new File("./" + element[0]);
	this.tipoConsulta = tipoConsulta;
	this.pdf = pdf;
    }

    @Override
    public boolean setOutputPath(File path) {
	String extension;
	String extensionDescription;
	if (pdf) {
	    extension = "pdf";
	    extensionDescription = PluginServices.getText(this, "pdfFiles");
	} else {
	    extension = "xls";
	    extensionDescription = PluginServices.getText(this, "excelFiles");
	}
	if (path == null) {
	    SaveFileDialog sfd = new SaveFileDialog(extensionDescription,
		    extension);
	    outputFile = sfd.showDialog();
	} else {
	    if (path.isDirectory()) {
		outputFile = new File(path.getAbsolutePath() + File.separator
			+ element[0] + "." + extension);
	    } else {
		outputFile = path;
	    }
	}

	return outputFile != null;
    }

    @Override
    public void generateReportFile() {
	QueryType tipo = getTipo();
	if (tipo == QueryType.TRABAJOS_AGREGADOS) {
	    if (pdf) {
		createPdfReportAgregados(outputFile.getAbsolutePath(), element,
			consultasFilters);
	    } else {
		createCsvReportAgregados(outputFile.getAbsolutePath(), element,
			consultasFilters);
	    }

	} else {

	    String query = getReportQuery(tipo, consultasFilters, element[0]);
	    ConnectionWrapper con = new ConnectionWrapper(DBSession
		    .getCurrentSession().getJavaConnection());
	    DefaultTableModel table = con.execute(query);
	    if (table.getRowCount() == 0) {
		emptyQuery = true;
		return;
	    }

	    if (pdf) {
		createPdfReport(tipo, outputFile.getAbsolutePath(), element,
			consultasFilters, table);
	    } else {
		createCsvReport(outputFile.getAbsolutePath(), table,
			consultasFilters);
	    }
	}
    }

    private void createPdfReportAgregados(String outputFile, String[] element,
	    ConsultasFilters<Field> filters) {
	new TrabajosAgregadosReport(element, outputFile, null, filters,
		QueryType.TYPE_NOT_SET); // TODO
    }

    private void createCsvReportAgregados(String outputFile, String[] element,
	    ConsultasFilters<Field> filters) {
	new CSVTrabajosAgregadosReport(element[0], outputFile, consultasFilters);
    }

    private QueryType getTipo() {
	QueryType tipo = QueryType.TYPE_NOT_SET;
	if (tipoConsulta.equals("Trabajos") && element[1].equals("Firme")) {
	    tipo = QueryType.TRABAJOS_FIRME;
	} else if (tipoConsulta.equals("Trabajos")) {
	    tipo = QueryType.TRABAJOS;
	} else if (tipoConsulta.equals("Inspecciones")
		&& element[1].equals("Firme")) {
	    tipo = QueryType.RECONOCIMIENTOS_FIRME;
	} else if (tipoConsulta.equals("Inspecciones")) {
	    tipo = QueryType.RECONOCIMIENTOS;
	} else if (tipoConsulta.equals("Características")) {
	    tipo = QueryType.CARACTERISTICAS;
	} else if (tipoConsulta.equals("Trabajos Agrupados")) {
	    tipo = QueryType.TRABAJOS_AGREGADOS;
	}

	return tipo;
    }

    private String getFields(QueryType tipo, String elementId) {
	switch (tipo) {
	case TRABAJOS_FIRME:
	    return ConsultasFieldNames.getFirmeTrabajosFieldNames(elementId);
	case TRABAJOS:
	    return ConsultasFieldNames.getTrabajosFieldNames(elementId);
	case RECONOCIMIENTOS_FIRME:
	    return ConsultasFieldNames
		    .getFirmeReconocimientosFieldNames(elementId);
	case RECONOCIMIENTOS:
	    return ConsultasFieldNames.getReconocimientosFieldNames(elementId);
	case CARACTERISTICAS:
	    return ConsultasFieldNames
		    .getPDFCaracteristicasFieldNames(element[0]);
	default:
	    return "";
	}
    }

    private String getReportQuery(QueryType tipo,
	    ConsultasFilters<Field> filters, String element) {
	String query;

	if (tipo == QueryType.CARACTERISTICAS) {
	    if (filters.getQueryType().equals("CUSTOM")) {
		query = getCustomCaracteristicasQuery(filters, element);
	    } else if (pdf) {
		query = PDFCaracteristicasQueries.getPDFCaracteristicasQuery(
			element, filters);
	    } else {
		query = CSVCaracteristicasQueries.getCSVCaracteristicasQuery(
			element, filters);
	    }
	} else {
	    String elementId = ConsultasFieldNames.getElementId(element);
	    String fields;
	    if (filters.getQueryType().equals("CUSTOM")) {
		fields = buildFields(filters, "");
	    } else {
		fields = getFields(tipo, elementId);
	    }

	    query = "SELECT " + fields + " FROM " + DBFieldNames.GIA_SCHEMA
		    + "." + element + "_" + tipoConsulta.getKey()
		    + " AS sub JOIN " + DBFieldNames.GIA_SCHEMA + "." + element
		    + " AS el ON sub." + elementId + "= el." + elementId
		    + CSVCaracteristicasQueries.get(element);

	    if (!consultasFilters.getWhereClauseByLocationWidgets(false)
		    .isEmpty()) {
		query = query + " WHERE el." + elementId + " IN (SELECT "
			+ elementId + " FROM " + DBFieldNames.GIA_SCHEMA + "."
			+ element
			+ filters.getWhereClauseByLocationWidgets(false);
	    }

	    if (tipo == QueryType.TRABAJOS || tipo == QueryType.TRABAJOS_FIRME) {
		query += filters.getWhereClauseByDates("fecha_certificado");
	    } else {
		query += filters.getWhereClauseByDates("fecha_inspeccion");
	    }
	}
	return query;
    }

    private String getCustomCaracteristicasQuery(
	    ConsultasFilters<Field> filters, String element) {
	String query = CSVCaracteristicasQueries.getCSVCaracteristicasQuery(
		element, filters);
	String subquery = query;
	if (filters.getFields().size() > 0) {
	    subquery = query.substring(query.indexOf(" FROM"));
	    subquery = buildFields(filters, "SELECT ") + subquery;
	}
	if (filters.getOrderBy().size() > 0) {

	    int indexOf = subquery.indexOf("ORDER BY ");
	    if (indexOf != -1) {
		subquery = subquery.substring(0, indexOf + 9);
	    } else {
		if (subquery.endsWith(";")) {
		    subquery = subquery.substring(0, subquery.length() - 1);
		}

		subquery = subquery + " ORDER BY ";
	    }

	    for (Field field : filters.getOrderBy()) {
		subquery = subquery + field.getKey() + ", ";
	    }
	    subquery = subquery.substring(0, subquery.length() - 2);
	}
	return subquery;
    }

    private String buildFields(ConsultasFilters<Field> filters, String select) {
	for (Field field : filters.getFields()) {
	    if (field.getKey().endsWith("area_mantenimiento")) {
		select += "am.item AS  \"Área Mantenimiento\", ";
	    } else if (field.getKey().endsWith("base_contratista")) {
		select += "bc.item AS  \"Base Contratista\", ";
	    } else if (field.getKey().endsWith("tramo")) {
		select += "tr.item AS  \"Tramo\", ";
	    } else if (field.getKey().endsWith("tipo_via")) {
		select += "tv.item AS  \"Tipo Vía\", ";
	    } else if (field.getKey().endsWith("nombre_via")) {
		select += "nv.item AS  \"Nombre Vía\", ";
	    } else if (field.getKey().endsWith("municipio")) {
		select += "mu.item AS  \"Municipio\", ";
	    } else if (field.getKey().endsWith("sentido")) {
		select += "st.item AS  \"Sentido\", ";
	    } else {
		select = select
			+ field.getKey()
			+ String.format(" AS \"%s\"", field.getLongName()
				.replace("\"", "'")) + ", ";
	    }
	}
	return select.substring(0, select.length() - 2);
    }

    private void createPdfReport(QueryType tipo, String outputFile,
	    String[] element, ConsultasFilters<Field> filters,
	    DefaultTableModel table) {

	if (filters.getQueryType().equals("CUSTOM")) {
	    new CustomPDFReport(element, outputFile, table, filters, tipo);
	    return;
	}

	switch (tipo) {
	case TRABAJOS:
	    new TrabajosReport(element, outputFile, table, filters, tipo);
	    break;
	case TRABAJOS_FIRME:
	    new FirmeTrabajosReport(element, outputFile, table, filters, tipo);
	    break;
	case CARACTERISTICAS:
	    ConsultasFieldNames.createCaracteristicasReport(element,
		    outputFile, table, filters, tipo);
	    break;
	case RECONOCIMIENTOS_FIRME:
	    new FirmeReconocimientosReport(element, outputFile, table, filters,
		    tipo);
	    break;
	case RECONOCIMIENTOS:
	    new ReconocimientosReport(element, outputFile, table, filters, tipo);
	    break;
	default:
	    break;
	}

    }

    private void createCsvReport(String outputFile, DefaultTableModel table,
	    ConsultasFilters<Field> filters) {
	new XLSReport(outputFile, table, filters);

    }

    @Override
    public void finalActions() {

	if (emptyQuery) {
	    JOptionPane.showMessageDialog(null,
		    PluginServices.getText(this, "queryWithoutResults_msg"));
	} else {

	    if (pdf) {
		showOpenSingleReportDialog(outputFile.toString());
	    } else {
		JOptionPane.showMessageDialog(null,
			PluginServices.getText(this, "csvReportGenerated_msg")
				+ outputFile);
	    }
	}
    }

    private void showOpenSingleReportDialog(String outputFile) {
	Object[] reportGeneratedOptions = {
		PluginServices.getText(this,
			"singleReportGeneratedOptions_open"),
		PluginServices.getText(this,
			"singleReportGeneratedOptions_close") };
	int m = JOptionPane.showOptionDialog(null,
		PluginServices.getText(this, "reportGenerated_msg") + "\""
			+ outputFile + "\"", null,
		JOptionPane.YES_NO_CANCEL_OPTION,
		JOptionPane.INFORMATION_MESSAGE, null, reportGeneratedOptions,
		reportGeneratedOptions[1]);

	if (m == JOptionPane.OK_OPTION) {
	    Desktop d = Desktop.getDesktop();
	    try {
		d.open(new File(outputFile));
	    } catch (IOException e1) {
		e1.printStackTrace();
	    }
	}
    }

}
