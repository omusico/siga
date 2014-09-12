package es.icarto.gvsig.commons.queries;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.swing.table.DefaultTableModel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.WorkbookUtil;

public class XLSReport {

    private Workbook wb;
    private Sheet sheet;
    private final QueryFiltersI filters;
    private final int colNamesRowIdx;

    public XLSReport(String outputFile, DefaultTableModel table,
	    QueryFiltersI filters) {
	this.filters = filters;

	colNamesRowIdx = filters.getLocation().size() + 2;
	if (outputFile != null) {
	    try {
		wb = new HSSFWorkbook(); // xls
		String safeName = WorkbookUtil.createSafeSheetName("Listado");
		sheet = wb.createSheet(safeName);

		sheet.setAutoFilter(new CellRangeAddress(colNamesRowIdx,
			colNamesRowIdx, 0, table.getColumnCount() - 1));

		writeFilters();
		writeColumnNames(table);
		writeRows(table);

		writeToDisk(outputFile);
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }

    protected void writeFilters() {

	short rowIdx = 0;

	for (Field l : filters.getLocation()) {
	    Row row = sheet.createRow(rowIdx++);
	    row.createCell(0).setCellValue(l.getLongName());
	    row.createCell(1).setCellValue(l.getValue().toString());
	}

    }

    private void writeColumnNames(DefaultTableModel table) {
	Row row0 = sheet.createRow(colNamesRowIdx);

	for (int i = 0; i < table.getColumnCount(); i++) {
	    row0.createCell(i).setCellValue(table.getColumnName(i));
	}
    }

    private void writeRows(DefaultTableModel tableModel) throws IOException {

	for (int rowIdx = 0; rowIdx < tableModel.getRowCount(); rowIdx++) {
	    Row row = sheet.createRow(rowIdx + colNamesRowIdx + 1);
	    for (int column = 0; column < tableModel.getColumnCount(); column++) {
		Object value = tableModel.getValueAt(rowIdx, column);
		createCell(row, column, value);
	    }
	}

	for (int column = 0; column < tableModel.getColumnCount(); column++) {
	    sheet.autoSizeColumn(column);
	}
    }

    private void createCell(Row row, int column, Object value) {
	if (value == null) {
	    row.createCell(column);
	} else if (value instanceof String) {
	    row.createCell(column).setCellValue((String) value);
	} else if (value instanceof Date) {
	    CellStyle cellStyle = wb.createCellStyle();
	    CreationHelper creationHelper = wb.getCreationHelper();
	    cellStyle.setDataFormat(creationHelper.createDataFormat()
		    .getFormat("d/m/yyyy"));
	    sheet.setDefaultColumnStyle(column, cellStyle);
	    row.createCell(column).setCellValue((Date) value);
	} else if (value instanceof Number) {
	    Number doubleValue = (Number) value;
	    row.createCell(column).setCellValue(doubleValue.doubleValue());
	} else if (value instanceof Boolean) {
	    row.createCell(column).setCellValue((Boolean) value ? "Sí" : "No");
	} else {
	    throw new AssertionError("This should never happen");
	}
    }

    private void writeToDisk(String outputFile) throws IOException {
	FileOutputStream fileOut = new FileOutputStream(outputFile);
	this.wb.write(fileOut);
	fileOut.close();
    }

}
