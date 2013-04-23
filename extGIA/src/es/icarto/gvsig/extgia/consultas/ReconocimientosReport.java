package es.icarto.gvsig.extgia.consultas;

import java.sql.ResultSet;

public class ReconocimientosReport extends Report {

    public ReconocimientosReport(String element, String fileName,
	    ResultSet resultMap, String[] filters) {
	super(element, fileName, resultMap, filters);
    }

    @Override
    protected String getTitle() {
	return "Listado de Reconocimientos de Estado";
    }

    @Override
    protected String[] getColumnNames() {
	String[] columnNames = {
		"ID Elemento",
		"Nombre Revisor",
		"Fecha Inspección",
		"Índice Estado",
		"Observaciones"
	};
	return columnNames;
    }

    @Override
    protected float[] getColumnsWidth(int columnCount) {
	float[] columnsWidth = new float[columnCount];

	columnsWidth[0] = 70f;
	columnsWidth[1] = 170f;
	columnsWidth[2] = 70f;
	columnsWidth[3] = 70f;
	columnsWidth[4] = 215f;

	return columnsWidth;
    }
}
