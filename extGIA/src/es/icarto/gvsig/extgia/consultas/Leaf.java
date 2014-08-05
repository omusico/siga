package es.icarto.gvsig.extgia.consultas;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import com.iver.andami.PluginServices;

import es.icarto.gvsig.audasacommons.forms.reports.SaveFileDialog;
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

    private static final int TYPE_NOT_SET = -1;
    private static final int TRABAJOS = 0;
    private static final int RECONOCIMIENTOS = 1;
    private static final int TRABAJOS_FIRME = 2;
    private static final int RECONOCIMIENTOS_FIRME = 3;
    private static final int CARACTERISTICAS = 4;
    private static final int TRABAJOS_AGREGADOS = 5;

    private final String[] element;
    private final ConsultasFilters consultasFilters;
    private final KeyValue tipoConsulta;
    private final boolean pdf;

    private File outputFile;
    private boolean emptyQuery = false;

    public Leaf(String[] element, ConsultasFilters consultasFilters,
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
	    extension = "csv";
	    extensionDescription = PluginServices.getText(this, "csvFiles");
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
	if (tipoConsulta.equals("Trabajos Agrupados")) {
	    if (pdf) {
		createPdfReportAgregados(outputFile.getAbsolutePath(), element,
			consultasFilters);
	    } else {
		createCsvReportAgregados(outputFile.getAbsolutePath(), element,
			consultasFilters);
	    }

	} else {
	    int tipo = getTipo();
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
	    ConsultasFilters filters) {
	new TrabajosAgregadosReport(element, outputFile, null, filters,
		TYPE_NOT_SET); // TODO
    }

    private void createCsvReportAgregados(String outputFile, String[] element,
	    ConsultasFilters filters) {
	new CSVTrabajosAgregadosReport(element[0], outputFile, consultasFilters);
    }

    private int getTipo() {
	int tipo = TYPE_NOT_SET;
	if (tipoConsulta.equals("Trabajos") && element[1].equals("Firme")) {
	    tipo = TRABAJOS_FIRME;
	} else if (tipoConsulta.equals("Trabajos")) {
	    tipo = TRABAJOS;
	} else if (tipoConsulta.equals("Inspecciones")
		&& element[1].equals("Firme")) {
	    tipo = RECONOCIMIENTOS_FIRME;
	} else if (tipoConsulta.equals("Inspecciones")) {
	    tipo = RECONOCIMIENTOS;
	} else if (tipoConsulta.equals("Características")) {
	    tipo = CARACTERISTICAS;
	} else if (tipoConsulta.equals("Trabajos Agrupados")) {
	    tipo = TRABAJOS_AGREGADOS;
	}

	return tipo;
    }

    private String getFields(int tipo, String elementId) {
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
	}
	return "";
    }

    private String getReportQuery(int tipo, ConsultasFilters filters,
	    String element) {
	String query;

	if (tipo == CARACTERISTICAS) {
	    query = getReportQueryForCaracteristicas(filters, element);
	} else {
	    query = getReportQueryForNoCaracteristicas(tipo, filters, element);
	}
	return query;
    }

    private String getReportQueryForCaracteristicas(ConsultasFilters filters,
	    String element) {
	String query;
	if (pdf) {
	    query = PDFCaracteristicasQueries.getPDFCaracteristicasQuery(
		    element, filters);
	} else {
	    query = CSVCaracteristicasQueries.getCSVCaracteristicasQuery(
		    element, filters);
	    String subquery = query;
	    if (filters.getQueryType().equals("CUSTOM")) {
		if (filters.getFields().size() > 0) {
		    subquery = query.substring(query.indexOf(" FROM"));
		    String select = "SELECT ";
		    for (String field : filters.getFields()) {
			select = select + "el." + field + ", ";
		    }
		    subquery = select.substring(0, select.length() - 2)
			    + subquery;
		}
		if (filters.getOrderBy().size() > 0) {

		    int indexOf = subquery.indexOf("ORDER BY ");
		    if (indexOf != -1) {
			subquery = subquery.substring(0, indexOf + 9);
		    } else {
			if (subquery.endsWith(";")) {
			    subquery = subquery.substring(0,
				    subquery.length() - 1);
			}

			subquery = subquery + " ORDER BY ";
		    }

		    for (String field : filters.getOrderBy()) {
			subquery = subquery + field + ", ";
		    }
		    subquery = subquery.substring(0, subquery.length() - 2);
		}

	    }
	    query = subquery;
	}

	return query;
    }

    private String getReportQueryForNoCaracteristicas(int tipo,
	    ConsultasFilters filters, String element) {
	String elementId = ConsultasFieldNames.getElementId(element);
	String fields = getFields(tipo, elementId);

	String query = "SELECT " + fields + " FROM " + DBFieldNames.GIA_SCHEMA
		+ "." + element + "_" + tipoConsulta.getKey();

	if (!consultasFilters.getWhereClauseByLocationWidgets(false).isEmpty()) {
	    query = query + " WHERE " + elementId + " IN (SELECT " + elementId
		    + " FROM " + DBFieldNames.GIA_SCHEMA + "." + element
		    + filters.getWhereClauseByLocationWidgets(false);
	}

	if (tipo == TRABAJOS || tipo == TRABAJOS_FIRME) {
	    query = query + filters.getWhereClauseByDates("fecha_certificado");
	} else {
	    query = query + filters.getWhereClauseByDates("fecha_inspeccion");
	}
	return query;
    }

    private void createPdfReport(int tipo, String outputFile, String[] element,
	    ConsultasFilters filters, DefaultTableModel table) {

	if (tipo == TRABAJOS) {
	    new TrabajosReport(element, outputFile, table, filters, tipo);
	} else if (tipo == TRABAJOS_FIRME) {
	    new FirmeTrabajosReport(element, outputFile, table, filters, tipo);
	} else if (tipo == RECONOCIMIENTOS_FIRME) {
	    new FirmeReconocimientosReport(element, outputFile, table, filters,
		    tipo);
	} else if (tipo == CARACTERISTICAS) {
	    ConsultasFieldNames.createCaracteristicasReport(element,
		    outputFile, table, filters, tipo);
	} else {
	    new ReconocimientosReport(element, outputFile, table, filters, tipo);
	}
    }

    private void createCsvReport(String outputFile, DefaultTableModel table,
	    ConsultasFilters filters) {
	new CSVReport(outputFile, table, filters);
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
