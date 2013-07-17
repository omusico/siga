package es.icarto.gvsig.extgia.consultas;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class CSVReport {
    private static final String CSV_SEPARATOR = "\t";

    public CSVReport(String outputFile, ResultSetMetaData rsMetaData, ResultSet rs,
	    ConsultasFilters filters) {
	if (outputFile != null) {
	    try {
		FileWriter writer = new FileWriter(outputFile);

		writeFilters(writer, filters);

		writeColumnNames(rsMetaData, writer);
		writer.append("\n");

		writeRows(rs, rsMetaData, writer);

		writer.flush();
		writer.close();

	    } catch (IOException e) {
		e.printStackTrace();
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}

    }

    private void writeFilters(FileWriter writer, ConsultasFilters filters) throws IOException {

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

    private void writeRows(ResultSet rs, ResultSetMetaData rsMetaData,
	    FileWriter writer) throws SQLException, IOException {
	rs.beforeFirst();
	while (rs.next()) {
	    for (int i=0; i<rsMetaData.getColumnCount(); i++) {
		writer.append(rs.getString(i+1));
		writer.append(CSV_SEPARATOR);
	    }
	    writer.append("\n");
	}
    }

    private void writeColumnNames(ResultSetMetaData rsMetaData,
	    FileWriter writer) throws SQLException, IOException {
	for (int i=0; i<rsMetaData.getColumnCount(); i++) {
	    writer.append(rsMetaData.getColumnName(i+1));
	    writer.append(CSV_SEPARATOR);
	}
    }

}
