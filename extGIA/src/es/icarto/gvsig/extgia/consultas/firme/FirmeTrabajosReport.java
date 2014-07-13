package es.icarto.gvsig.extgia.consultas.firme;

import javax.swing.table.DefaultTableModel;

import es.icarto.gvsig.extgia.consultas.ConsultasFilters;
import es.icarto.gvsig.extgia.consultas.TrabajosReport;

public class FirmeTrabajosReport extends TrabajosReport {

    public FirmeTrabajosReport(String[] element, String fileName,
	    DefaultTableModel table, ConsultasFilters filters, int reportType) {
	super(element, fileName, table, filters, reportType);

    }

    @Override
    protected String[] getColumnNames() {
	String[] columnNames = { "ID Elemento", "Fecha", "PK inicio",
		"PK final", "Sentido", "Descripción", "Fecha Certificado" };
	return columnNames;
    }

    @Override
    protected float[] getColumnsWidth(int columnCount) {
	float[] columnsWidth = new float[columnCount];

	columnsWidth[0] = 60f;
	columnsWidth[1] = 60f;
	columnsWidth[2] = 60f;
	columnsWidth[3] = 60f;
	columnsWidth[4] = 60f;
	columnsWidth[5] = 198f;
	columnsWidth[6] = 70f;

	return columnsWidth;
    }

}
