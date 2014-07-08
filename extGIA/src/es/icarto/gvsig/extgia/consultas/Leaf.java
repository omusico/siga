package es.icarto.gvsig.extgia.consultas;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;

import javax.swing.JOptionPane;

import com.iver.andami.PluginServices;

import es.icarto.gvsig.audasacommons.forms.reports.SaveFileDialog;
import es.icarto.gvsig.extgia.consultas.agregados.CSVTrabajosAgregadosIsletasReport;
import es.icarto.gvsig.extgia.consultas.agregados.CSVTrabajosAgregadosTaludesReport;
import es.icarto.gvsig.extgia.consultas.agregados.TrabajosAgregadosIsletasReport;
import es.icarto.gvsig.extgia.consultas.agregados.TrabajosAgregadosTaludesReport;
import es.icarto.gvsig.extgia.consultas.caracteristicas.CSVCaracteristicasQueries;
import es.icarto.gvsig.extgia.consultas.caracteristicas.PDFCaracteristicasQueries;
import es.icarto.gvsig.extgia.consultas.firme.FirmeReconocimientosReport;
import es.icarto.gvsig.extgia.consultas.firme.FirmeTrabajosReport;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.KeyValue;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class Leaf implements Component {

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
    public boolean lookUp() {

	// TODO: Hacer la query

	// return queryHasResults(rs);
	return true;
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

    @Override
    public void generateReportFile() {
	generateReportFile(element, outputFile.getAbsolutePath(),
		consultasFilters.getFechaInicio(),
		consultasFilters.getFechaFin(), consultasFilters);
    }

    private void generateReportFile(String[] element, String outputFile,
	    Date fechaInicial, Date fechaFinal, ConsultasFilters filters) {

	int tipo = -1;
	String elementId = ConsultasFieldNames.getElementId(element[0]);
	String fields = "";

	if (tipoConsulta.equals("Trabajos")) {
	    if (element[1].equals("Firme")) {
		fields = ConsultasFieldNames
			.getFirmeTrabajosFieldNames(elementId);
		tipo = TRABAJOS_FIRME;
	    } else {
		fields = ConsultasFieldNames.getTrabajosFieldNames(elementId);
		tipo = TRABAJOS;
	    }
	} else if (tipoConsulta.equals("Inspecciones")) {
	    if (element[1].equals("Firme")) {
		fields = ConsultasFieldNames
			.getFirmeReconocimientosFieldNames(elementId);
		tipo = RECONOCIMIENTOS_FIRME;
	    } else if (!ConsultasFieldNames
		    .hasIndiceFieldOnReconocimientos(element[0])) {
		fields = ConsultasFieldNames
			.getReconocimientosFieldNamesWithoutIndice(elementId);
		tipo = RECONOCIMIENTOS;
	    } else {
		fields = ConsultasFieldNames
			.getReconocimientosFieldNames(elementId);
		tipo = RECONOCIMIENTOS;
	    }
	} else if (tipoConsulta.equals("Características")) {
	    fields = ConsultasFieldNames
		    .getPDFCaracteristicasFieldNames(element[0]);
	    tipo = CARACTERISTICAS;
	} else if (tipoConsulta.equals("Trabajos Agrupados")) {
	    tipo = TRABAJOS_AGREGADOS;
	}

	String query = getReportQuery(tipo, fechaInicial, fechaFinal,
		element[0], elementId, fields);

	if (pdf) {
	    if (tipo == TRABAJOS_AGREGADOS) {
		createPdfReportAgregados(outputFile, element, filters, tipo);
	    } else {
		createPdfReport(tipo, outputFile, element, filters, query);
	    }
	} else {
	    if (tipo == TRABAJOS_AGREGADOS) {
		createCsvReportAgregados(outputFile, element, filters);
	    } else {
		createCsvReport(outputFile, query, filters);
	    }
	}
    }

    private String getReportQuery(int tipo, Date fechaInicial, Date fechaFinal,
	    String element, String elementId, String fields) {
	String query;

	if (tipo == TRABAJOS_AGREGADOS) {
	    query = "SELECT 1=1";
	    return query;
	}
	if (tipo == CARACTERISTICAS) {
	    if (pdf) {
		query = PDFCaracteristicasQueries.getPDFCaracteristicasQuery(
			element, consultasFilters);
	    } else {
		query = CSVCaracteristicasQueries.getCSVCaracteristicasQuery(
			element, consultasFilters);
	    }
	} else {
	    query = "SELECT " + fields + " FROM " + DBFieldNames.GIA_SCHEMA
		    + "." + element + "_" + tipoConsulta.getKey();
	}

	if (!consultasFilters.getWhereClauseByLocationWidgets(false).isEmpty()) {
	    if (tipo == CARACTERISTICAS) {
		// query = query + " WHERE " + elementId + " IN (SELECT " +
		// elementId +
		// " FROM " + DBFieldNames.GIA_SCHEMA + "." + element +
		// consultasFilters.getWhereClauseByLocationWidgets(false) +
		// ");";
	    } else {
		query = query
			+ " WHERE "
			+ elementId
			+ " IN (SELECT "
			+ elementId
			+ " FROM "
			+ DBFieldNames.GIA_SCHEMA
			+ "."
			+ element
			+ consultasFilters
				.getWhereClauseByLocationWidgets(false);
	    }
	}

	if (tipo == CARACTERISTICAS) {
	    return query;
	} else if (tipo == TRABAJOS || tipo == TRABAJOS_FIRME) {
	    query = query
		    + consultasFilters
			    .getWhereClauseByDates("fecha_certificado");
	} else {
	    query = query
		    + consultasFilters
			    .getWhereClauseByDates("fecha_inspeccion");
	}
	return query;
    }

    private void createPdfReportAgregados(String outputFile, String[] element,
	    ConsultasFilters filters, int reportType) {
	if (element[0].equals("Taludes")) {
	    new TrabajosAgregadosTaludesReport(element, outputFile, null,
		    filters, reportType);
	} else if (element[0].equals("Isletas")) {
	    new TrabajosAgregadosIsletasReport(element, outputFile, null,
		    filters, reportType);
	}
    }

    private void createCsvReportAgregados(String outputFile, String[] element,
	    ConsultasFilters filters) {
	if (element[0].equals("Taludes")) {
	    new CSVTrabajosAgregadosTaludesReport(outputFile, consultasFilters);
	} else if (element[0].equals("Isletas")) {
	    new CSVTrabajosAgregadosIsletasReport(outputFile, consultasFilters);
	}
    }

    private void createPdfReport(int tipo, String outputFile, String[] element,
	    ConsultasFilters filters, String query) {
	if (outputFile != null) {

	    PreparedStatement statement;
	    try {
		Connection connection = DBSession.getCurrentSession()
			.getJavaConnection();
		statement = connection.prepareStatement(query);
		statement.execute();
		ResultSet rs = statement.getResultSet();

		if (!queryHasResults(rs)) {
		    emptyQuery = true;
		    return;
		}

		if (tipo == TRABAJOS) {
		    new TrabajosReport(element, outputFile, rs, filters, tipo);
		} else if (tipo == TRABAJOS_FIRME) {
		    new FirmeTrabajosReport(element, outputFile, rs, filters,
			    tipo);
		} else if (tipo == RECONOCIMIENTOS_FIRME) {
		    new FirmeReconocimientosReport(element, outputFile, rs,
			    filters, tipo);
		} else if (tipo == CARACTERISTICAS) {
		    ConsultasFieldNames.createCaracteristicasReport(element,
			    outputFile, rs, filters, tipo);
		} else {
		    new ReconocimientosReport(element, outputFile, rs, filters,
			    tipo);
		}

	    } catch (SQLException e1) {
		e1.printStackTrace();
		return;
	    }
	}
    }

    private void createCsvReport(String outputFile, String query,
	    ConsultasFilters filters) {
	PreparedStatement statement;

	if (outputFile != null) {
	    try {
		Connection connection = DBSession.getCurrentSession()
			.getJavaConnection();
		statement = connection.prepareStatement(query);
		statement.execute();
		ResultSet rs = statement.getResultSet();
		ResultSetMetaData rsMetaData = rs.getMetaData();

		if (!queryHasResults(rs)) {
		    emptyQuery = true;
		    return;
		}

		new CSVReport(outputFile, rsMetaData, rs, filters);

	    } catch (SQLException e) {
		e.printStackTrace();
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

    private boolean queryHasResults(ResultSet rs) throws SQLException {
	if (rs.next()) {
	    return true;
	}
	return false;
    }

}
