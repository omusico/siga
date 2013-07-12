package es.icarto.gvsig.extgia.consultas.agregados;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import es.udc.cartolab.gvsig.users.utils.DBSession;


public abstract class CSVTrabajosAgregadosReport {

    private static final CharSequence CSV_SEPARATOR = "\t";
    private final TrabajosAgregadosReportQueries agregadosReportQueries;

    public CSVTrabajosAgregadosReport(String outputFile) {
	agregadosReportQueries = new TrabajosAgregadosReportQueries(getElement());
	writeCSVFile(outputFile);
    }

    protected String[] getColumnNames() {
	String[] columnNames = {
		"ID Elemento",
		"PK Inicial",
		"PK Final",
		"Sentido",
		"Medici�n Contratista",
		"Medici�n AUDASA"
	};
	return columnNames;
    }

    private void writeCSVFile(String outputFile) {
	if (outputFile != null) {
	    try {
		FileWriter writer = new FileWriter(outputFile);

		writeTable(writer,
			"Desbroce con retroara�a\n\n",
			agregadosReportQueries.getDesbroceRetroaranhaQuery(),
			agregadosReportQueries.getDesbroceRetroaranhaSumQuery());
		//		writer.append("\n");
		writeTable(writer,
			"\nDesbroce mec�nico\n\n",
			agregadosReportQueries.getDesbroceMecanicoQuery(),
			agregadosReportQueries.getDesbroceMecanicoSumQuery());
		//		writer.append("\n");
		writeTable(writer,
			"\nTala y desbroce manual\n\n",
			agregadosReportQueries.getDesbroceManualQuery(),
			agregadosReportQueries.getDesbroceManualSumQuery());
		//		writer.append("\n");
		writeTotal(writer,
			"TOTAL DESBROCES",
			agregadosReportQueries.getDesbroceTotalSumQuery());
		writeTable(writer,
			"\nSiega mec�nica isletas\n\n",
			agregadosReportQueries.getSiegaMecanicaIsletasQuery(),
			agregadosReportQueries.getSiegaMecanicaIsletasSumQuery());
		//		writer.append("\n");
		writeTable(writer,
			"\nSiega mec�nica medianas\n\n",
			agregadosReportQueries.getSiegaMecanicaMedianaQuery(),
			agregadosReportQueries.getSiegaMecanicaMedianaSumQuery());
		//		writer.append("\n");
		writeTotal(writer,
			"TOTAL SEGADO DE HIERBAS",
			agregadosReportQueries.getSiegaTotalSumQuery());
		//		writer.append("\n");
		writeTable(writer,
			"\nHerbicida\n\n",
			agregadosReportQueries.getHerbicidadQuery(),
			agregadosReportQueries.getHerbicidaSumQuery());
		//		writer.append("\n");
		writeTable(writer,
			"\nVegeraci�n mediana de hormig�n\n\n",
			agregadosReportQueries.getVegeracionQuery(),
			agregadosReportQueries.getVegeracionSumQuery());

		writer.flush();
		writer.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    }

	}
    }

    protected abstract String getElement();

    private void writeColumnsNames(FileWriter writer) {
	try {
	    for (int i=0; i<getColumnNames().length; i++) {

		writer.append(getColumnNames()[i]);
		writer.append(CSV_SEPARATOR);
	    }
	    writer.append("\n");
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private void writeTable(FileWriter writer, String title, String query, String totalQuery) {
	PreparedStatement statement;
	try {
	    statement = DBSession.getCurrentSession().getJavaConnection().prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();
	    if (rs.next()) {
		writer.append(title);
		writeColumnsNames(writer);
		rs.beforeFirst();
		while (rs.next()) {
		    for (int i=0; i<getColumnNames().length; i++) {
			writer.append(rs.getString(i+1));
			writer.append(CSV_SEPARATOR);
		    }
		    writer.append("\n");
		}
		rs = statement.executeQuery(totalQuery);
		writer.append("TOTAL");
		for (int i=0; i<=3; i++) {
		    writer.append(CSV_SEPARATOR);
		}
		if (rs.next()) {
		    for (int i=0; i<rs.getMetaData().getColumnCount(); i++) {
			writer.append(rs.getString(i+1));
			writer.append(CSV_SEPARATOR);
		    }
		}
		writer.append("\n");
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private void writeTotal(FileWriter writer, String title, String query) {
	PreparedStatement statement;
	try {
	    statement = DBSession.getCurrentSession().getJavaConnection().prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();
	    rs.next();
	    if (rs.getString(1)!=null) {
		rs.beforeFirst();
		writer.append("\n");
		writer.append(title);
		for (int i=0; i<=3; i++) {
		    writer.append(CSV_SEPARATOR);
		}
		rs.next();
		for (int i=0; i<rs.getMetaData().getColumnCount(); i++) {
		    writer.append(rs.getString(i+1));
		    writer.append(CSV_SEPARATOR);
		}
		writer.append("\n");
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

}