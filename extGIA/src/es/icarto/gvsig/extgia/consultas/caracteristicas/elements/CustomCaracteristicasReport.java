package es.icarto.gvsig.extgia.consultas.caracteristicas.elements;

import java.sql.SQLException;
import java.util.Arrays;

import javax.swing.table.DefaultTableModel;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

import es.icarto.gvsig.commons.queries.Field;
import es.icarto.gvsig.commons.queries.Utils;
import es.icarto.gvsig.extgia.consultas.ConsultasFilters;
import es.icarto.gvsig.extgia.consultas.PDFReport;

public class CustomCaracteristicasReport extends PDFReport {

    public CustomCaracteristicasReport(String[] element, String fileName,
	    DefaultTableModel table, ConsultasFilters<Field> filters,
	    int reportType) {
	super(element, fileName, table, filters, reportType);
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
	String[] columnNames = new String[filters.getFields().size()];
	for (int i = 0; i < columnNames.length; i++) {
	    columnNames[i] = filters.getFields().get(i).getLongName();
	}
	return columnNames;
    }

    @Override
    protected float[] getColumnsWidth(int columnCount) {
	float[] columnsWidth = new float[columnCount];
	float columnWidh = 810f / columnCount;
	Arrays.fill(columnsWidth, columnWidh);
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

    @Override
    protected void writeValues(Document document, DefaultTableModel tableModel,
	    PdfPTable table, int reportType) throws SQLException,
	    DocumentException {
	Paragraph paragraph;

	for (int row = 0; row < tableModel.getRowCount(); row++) {
	    for (int column = 0; column < getColumnNames().length; column++) {
		Object value = tableModel.getValueAt(row, column);
		paragraph = new Paragraph(Utils.formatValue(value),
			cellBoldStyle);
		PdfPCell valueCell = new PdfPCell(paragraph);
		valueCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(valueCell);
	    }
	    if (hasEmbebedTable()) {
		table.addCell(writeAditionalColumnValues(tableModel.getValueAt(
			row, 1).toString()));
	    }
	}

	document.add(table);
	document.add(Chunk.NEWLINE);
	writeNumberOfRows(document, tableModel.getRowCount());
    }

}
