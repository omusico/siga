package es.icarto.gvsig.extgia.consultas.caracteristicas.elements;

import java.sql.ResultSet;

import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;

import es.icarto.gvsig.extgia.consultas.ConsultasFilters;
import es.icarto.gvsig.extgia.consultas.PDFReport;

public class TaludesCaracteristicasReport extends PDFReport {

    public TaludesCaracteristicasReport(String element, String fileName,
	    ResultSet resultMap, ConsultasFilters filters) {
	super(element, fileName, resultMap, filters);
    }

    @Override
    protected String getTitle() {
	return "Listado de Caracter�sticas";
    }

    @Override
    protected Rectangle setPageSize() {
	return PageSize.A4.rotate();
    }

    @Override
    protected String[] getColumnNames() {
	String[] columnNames = {
		"ID Talud",
		"Tramo",
		"PK Inicial",
		"PK Final",
		"Tipo Talud",
		"Roca",
		"�rboles",
		"Gunita",
		"Escollera",
		"Maleza",
		"Malla",
		"Longitud",
		"Altura M�xima",
		"Superficie Total",
		"Superficie Mecanizada",
		"Superficie Manual",
		"Superficie Restada"
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
	columnsWidth[14] = 90f;
	columnsWidth[15] = 60f;
	columnsWidth[16] = 60f;

	return columnsWidth;
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
