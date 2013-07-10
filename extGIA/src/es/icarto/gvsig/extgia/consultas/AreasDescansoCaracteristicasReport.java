package es.icarto.gvsig.extgia.consultas;

import java.sql.ResultSet;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;

public class AreasDescansoCaracteristicasReport extends Report {

    public AreasDescansoCaracteristicasReport(String element, String fileName,
	    ResultSet resultMap, String[] filters) {
	super(element, fileName, resultMap, filters);
    }

    @Override
    protected String getTitle() {
	return "Listado de Características";
    }

    @Override
    protected String[] getColumnNames() {
	String[] columnNames = {
		"Nombre",
		"Tipo Vía",
		"Nombre Vía",
		"Municipio",
		"PK",
		"Puesta en Servicio",
		"Superficie Total",
		"Superpicie Pavimentada",
		"Aceras",
		"Bordillos",
		"Zona Siega",
		"Zona Ajardinada",
		"Riego",
		"Aparcamiento",
		"Area Picnic",
		"Fuentes Potables",
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
	columnsWidth[11] = 60f;
	columnsWidth[12] = 60f;
	columnsWidth[13] = 60f;
	columnsWidth[14] = 60f;
	columnsWidth[15] = 60f;
	columnsWidth[16] = 60f;

	return columnsWidth;
    }

    @Override
    protected Rectangle setPageSize() {
	return PageSize.A4.rotate();
    }

    @Override
    protected void writeDatesRange(Document document, String[] filters) {

    }

}
