package es.icarto.gvsig.extgia.consultas.firme;

import java.sql.ResultSet;

import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;

import es.icarto.gvsig.extgia.consultas.Report;

public class FirmeReconocimientosReport extends Report {

    public FirmeReconocimientosReport(String element, String fileName,
	    ResultSet resultMap, String[] filters) {
	super(element, fileName, resultMap, filters);
	// TODO Auto-generated constructor stub
    }

    @Override
    protected String getTitle() {
	return "Listado de Reconocimientos de Estado";
    }

    @Override
    protected String[] getColumnNames() {
	String[] columnNames = {
		"ID Elemento",
		"Tipo Inspección",
		"Nombre Revisor",
		"Aparato Medición",
		"Fecha Inspección",
		"Observaciones"
	};
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

    @Override
    protected Rectangle setPageSize() {
	return PageSize.A4;
    }

}
