package es.icarto.gvsig.extgia.consultas.firme;

import javax.swing.table.DefaultTableModel;

import es.icarto.gvsig.commons.queries.Field;
import es.icarto.gvsig.extgia.consultas.ConsultasFilters;
import es.icarto.gvsig.extgia.consultas.ReconocimientosReport;

public class FirmeReconocimientosReport extends ReconocimientosReport {

    public FirmeReconocimientosReport(String[] element, String fileName,
	    DefaultTableModel tableModel, ConsultasFilters<Field> filters,
	    int reportType) {
	super(element, fileName, tableModel, filters, reportType);
    }

    @Override
    protected String[] getColumnNames() {
	String[] columnNames = { "ID Elemento", "Tipo Inspección",
		"Nombre Revisor", "Aparato Medición", "Fecha Inspección",
		"Observaciones" };
	return columnNames;
    }

    @Override
    protected float[] getColumnsWidth(int columnCount) {
	float[] columnsWidth = new float[columnCount];

	columnsWidth[0] = 60f;
	columnsWidth[1] = 60f;
	columnsWidth[2] = 100f;
	columnsWidth[3] = 60f;
	columnsWidth[4] = 60f;
	columnsWidth[5] = 258f;

	return columnsWidth;
    }
}
