package es.icarto.gvsig.extgia.consultas;

import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.table.DefaultTableModel;

import es.udc.cartolab.gvsig.navtable.format.DateFormatNT;

public class CSVReport {
    private final static SimpleDateFormat DATE_FORMAT = DateFormatNT
	    .getDateFormat();
    private final static NumberFormat NUMBER_FORMAT = NumberFormat
	    .getInstance(Locale.getDefault());

    private static final String CSV_SEPARATOR = "\t";

    public CSVReport(String outputFile, DefaultTableModel tableModel,
	    ConsultasFilters filters) {
	if (outputFile != null) {
	    try {
		FileWriter writer = new FileWriter(outputFile);

		writeFilters(writer, filters);

		writeColumnNames(tableModel, writer);
		writer.append("\n");

		writeRows(tableModel, writer);

		writer.flush();
		writer.close();

	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}

    }

    private void writeFilters(FileWriter writer, ConsultasFilters filters)
	    throws IOException {

	writer.append("Area Mantenimiento");
	writer.append(CSV_SEPARATOR);
	if (filters.getArea() != null) {
	    writer.append(filters.getArea().getValue());
	}
	writer.append("\n");

	writer.append("Base Contratista");
	writer.append(CSV_SEPARATOR);
	if (filters.getBaseContratista() != null) {
	    writer.append(filters.getBaseContratista().getValue());
	}
	writer.append("\n");

	writer.append("Tramo");
	writer.append(CSV_SEPARATOR);
	if (filters.getTramo() != null) {
	    writer.append(filters.getTramo().getValue());
	}
	writer.append("\n");

	writer.append("\n");
    }

    private void writeRows(DefaultTableModel tableModel, FileWriter writer)
	    throws IOException {
	for (int row = 0; row < tableModel.getRowCount(); row++) {
	    for (int column = 0; column < tableModel.getColumnCount(); column++) {
		Object value = tableModel.getValueAt(row, column);
		writer.append(formatValue(value));
		writer.append(CSV_SEPARATOR);
	    }
	    writer.append("\n");
	}
    }

    private String formatValue(Object o) {

	// TODO
	// This is a little 'hack' because of fecha_puesta_servicio
	// is Integer on database instead of Date
	// }else if (rs.getMetaData().getColumnName(column).
	// equalsIgnoreCase("fecha_puesta_servicio")) {
	// valueFormatted = rs.getString(column);
	// }
	if (o == null) {
	    return "";
	} else if (o instanceof Date) {
	    return DATE_FORMAT.format(o);
	} else if (o instanceof Number) {
	    return NUMBER_FORMAT.format(o);
	} else if (o instanceof Boolean) {
	    return ((Boolean) o) ? "Sí" : "No";
	}
	return o.toString();
    }

    private void writeColumnNames(DefaultTableModel tableModel,
	    FileWriter writer) throws IOException {
	for (int i = 0; i < tableModel.getColumnCount(); i++) {
	    writer.append(tableModel.getColumnName(i));
	    writer.append(CSV_SEPARATOR);
	}
    }

}
