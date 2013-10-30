package es.icarto.gvsig.extgia.consultas.caracteristicas.elements;

import java.sql.ResultSet;

import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;

import es.icarto.gvsig.extgia.consultas.ConsultasFilters;
import es.icarto.gvsig.extgia.consultas.PDFReport;

public class MurosCaracteristicasReport extends PDFReport {

    public MurosCaracteristicasReport(String element, String fileName,
	    ResultSet resultMap, ConsultasFilters filters) {
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
		"ID Muro",
		"Tramo",
		"PK Inicial",
		"PK Final",
		"Material",
		"Longitud (m)",
		"Altura Máxima (m)",
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
	columnsWidth[7] = 90f;

	return columnsWidth;
    }

    @Override
    protected boolean hasEmbebedTable() {
	return false;
    }

    @Override
    protected PdfPCell writeAditionalColumnName() {
	return null;
    }

    @Override
    protected PdfPCell writeAditionalColumnValues(String id) {
	return null;
    }

}
