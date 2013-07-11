package es.icarto.gvsig.extgia.consultas;

import java.sql.ResultSet;

import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;


public class TrabajosReport extends PDFReport {

    public TrabajosReport(String element, String fileName, ResultSet resultMap,
	    String[] filters) {
	super(element, fileName, resultMap, filters);
    }

    @Override
    protected String getTitle() {
	return "Listado de Trabajos";
    }

    @Override
    protected String[] getColumnNames() {
	String[] columnNames = {
		"ID Elemento",
		"Fecha",
		"Unidad",
		"Medición Contratista",
		"Medición AUDASA",
		"Observaciones",
		"Fecha Certificado"
	};
	return columnNames;
    }

    @Override
    protected float[] getColumnsWidth(int columnCount) {
	float[] columnsWidth = new float[columnCount];

	columnsWidth[0] = 60f;
	columnsWidth[1] = 60f;
	columnsWidth[2] = 100f;
	columnsWidth[3] = 63f;
	columnsWidth[4] = 60f;
	columnsWidth[5] = 155f;
	columnsWidth[6] = 70f;

	return columnsWidth;
    }

    @Override
    protected Rectangle setPageSize() {
	return PageSize.A4;
    }


}
