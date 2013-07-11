package es.icarto.gvsig.extgia.consultas;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class CSVReport {
    private static final String CSV_SEPARATOR = "\t";

    public CSVReport(String outputFile, ResultSetMetaData rsMetaData, ResultSet rs) {
	if (outputFile != null) {
	    try {
		FileWriter writer = new FileWriter(outputFile);

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

    private void writeRows(ResultSet rs, ResultSetMetaData rsMetaData,
	    FileWriter writer) throws SQLException, IOException {
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
