package es.icarto.gvsig.extgia.consultas;

import java.sql.ResultSet;

import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;

public class ReconocimientosReport extends PDFReport {

    public ReconocimientosReport(String element, String fileName,
	    ResultSet resultMap, ConsultasFilters filters, int reportType) {
	super(element, fileName, resultMap, filters, reportType);
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
		"Fecha Inspecci�n",
		"�ndice Estado",
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

    @Override
    protected Rectangle setPageSize() {
	return PageSize.A4;
    }

    @Override
    protected boolean hasEmbebedTable() {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    protected PdfPCell writeAditionalColumnName() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    protected PdfPCell writeAditionalColumnValues(String id) {
	// TODO Auto-generated method stub
	return null;
    }
}
