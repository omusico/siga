package es.icarto.gvsig.extgia.consultas.caracteristicas;

import java.sql.ResultSet;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;

import es.icarto.gvsig.extgia.consultas.Report;

public class JuntasCaracteristicasReport extends Report {

    public JuntasCaracteristicasReport(String element, String fileName,
	    ResultSet resultMap, String[] filters) {
	super(element, fileName, resultMap, filters);
	// TODO Auto-generated constructor stub
    }

    @Override
    protected String getTitle() {
	return "Listado de Características";
    }

    @Override
    protected Rectangle setPageSize() {
	return PageSize.A4.rotate();
    }

    @Override
    protected String[] getColumnNames() {
	String[] columnNames = {
		"Tramo",
		"Tipo Vía",
		"Nombre Vía",
		"PK",
		"Número Junta",
		"Ancho",
		"Módulo",
		"Elemento",
		"Código Elemento",
		"Descripción",
		"Observaciones"
	};
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
	columnsWidth[5] = 60f;
	columnsWidth[6] = 60f;
	columnsWidth[7] = 60f;
	columnsWidth[8] = 60f;
	columnsWidth[9] = 60f;
	columnsWidth[10] = 60f;

	return columnsWidth;
    }

    @Override
    protected void writeDatesRange(Document document, String[] filters) {

    }

}
