package es.icarto.gvsig.extgex.queries;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import es.icarto.gvsig.extgia.consultas.CSVReport;
import es.icarto.gvsig.extgia.consultas.ConsultasFilters;

public class CSVReportExpropiations extends CSVReport {

    public CSVReportExpropiations(String outputFile,
	    DefaultTableModel tableModel, ConsultasFilters filters) {
	super(outputFile, tableModel, filters);
    }

    @Override
    protected void writeFilters(FileWriter writer, ConsultasFilters filters)
	    throws IOException {

	List<String> fields = filters.getFields();

	writer.append("Tramo");
	writer.append(CSV_SEPARATOR);
	writer.append(fields.get(0) == null ? "" : fields.get(0));
	writer.append("\n");

	writer.append("UC");
	writer.append(CSV_SEPARATOR);
	writer.append(fields.get(1) == null ? "" : fields.get(1));

	writer.append("\n");

	writer.append("Ayuntamiento");
	writer.append(CSV_SEPARATOR);
	writer.append(fields.get(2) == null ? "" : fields.get(2));

	writer.append("\n");

	writer.append("Parroquia/Subtramo");
	writer.append(CSV_SEPARATOR);
	writer.append(fields.get(3) == null ? "" : fields.get(3));

	writer.append("\n");

	writer.append("\n");
    }
}
