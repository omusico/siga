package es.icarto.gvsig.extgia.consultas.agregados;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.WorkbookUtil;

import es.icarto.gvsig.commons.queries.ConnectionWrapper;
import es.icarto.gvsig.commons.utils.Field;
import es.icarto.gvsig.extgia.consultas.ConsultasFilters;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class XLSTrabajosAgregadosReport {

    private static final Logger logger = Logger
	    .getLogger(XLSTrabajosAgregadosReport.class);

    private final String element;
    private final ConsultasFilters<Field> filters;
    private final TrabajosAgregadosReportQueries agregadosReportQueries;
    private Workbook wb;
    private Sheet sheet;
    private final boolean[] columnsStyles;

    public XLSTrabajosAgregadosReport(String element, String outputFile,
	    ConsultasFilters<Field> filters) {
	this.element = element;
	agregadosReportQueries = new TrabajosAgregadosReportQueries(element);
	this.filters = filters;
	columnsStyles = new boolean[getColumnNames().length];
	if (outputFile == null) {
	    return;
	}
	wb = new HSSFWorkbook();
	String safeName = WorkbookUtil.createSafeSheetName("Listado");
	sheet = wb.createSheet(safeName);

	writeFilters();
	writeTables();

	try {
	    writeToDisk(outputFile);
	} catch (IOException e) {
	    logger.error(e.getStackTrace(), e);
	}
    }

    private void writeFilters() {
	short rowIdx = 0;

	for (Field l : filters.getLocation()) {
	    Row row = sheet.createRow(rowIdx++);
	    row.createCell(0).setCellValue(l.getLongName());
	    row.createCell(1).setCellValue(l.getValue().toString());
	}

	Row row = sheet.createRow(rowIdx++);
	row.createCell(0).setCellValue("Desde: ");
	row.createCell(1).setCellValue(filters.getFechaInicioFormatted());

	row = sheet.createRow(rowIdx++);
	row.createCell(0).setCellValue("Hasta: ");
	row.createCell(1).setCellValue(filters.getFechaFinFormatted());
    }

    private int writeTable(String title, String query, String totalQuery,
	    int row) {

	ConnectionWrapper con = new ConnectionWrapper(DBSession
		.getCurrentSession().getJavaConnection());
	String q = query
		+ filters.getWhereClauseFiltersForAgregados(element, false)
		+ " UNION " + totalQuery
		+ filters.getWhereClauseFiltersForAgregados(element, true);
	DefaultTableModel table = con.execute(q);
	if (table.getRowCount() > 1) {
	    writeTitle(title, row);
	    writeColumnNames(row + 2);
	    writeRows(table, row + 3);
	}

	return (row + 3 + table.getRowCount() + 2);
    }

    private void writeTables() {

	int lastRow = writeTable("Desbroce con retroaraña",
		agregadosReportQueries.getDesbroceRetroaranhaQuery(),
		agregadosReportQueries.getDesbroceRetroaranhaSumQuery(), 7);
	lastRow = writeTable("Desbroce mecánico",
		agregadosReportQueries.getDesbroceMecanicoQuery(),
		agregadosReportQueries.getDesbroceMecanicoSumQuery(), lastRow);
	lastRow = writeTable("Tala y desbroce manual",
		agregadosReportQueries.getDesbroceManualQuery(),
		agregadosReportQueries.getDesbroceManualSumQuery(), lastRow);
	writeTotal(
		agregadosReportQueries
			.getDesbroceTotalSumQuery("TOTAL DESBROCES"),
		lastRow);
	lastRow = writeTable("Siega mecánica de isletas",
		agregadosReportQueries.getSiegaMecanicaIsletasQuery(),
		agregadosReportQueries.getSiegaMecanicaIsletasSumQuery(),
		lastRow + 2);
	lastRow = writeTable("Siega mecánica de medianas",
		agregadosReportQueries.getSiegaMecanicaMedianaQuery(),
		agregadosReportQueries.getSiegaMecanicaMedianaSumQuery(),
		lastRow);
	lastRow = writeTable("Siega mecánica de medianas < 1,5 m",
		agregadosReportQueries.getSiegaMecanicaMediana1_5mQuery(),
		agregadosReportQueries.getSiegaMecanicaMediana1_5mSumQuery(),
		lastRow);
	writeTotal(
		agregadosReportQueries
			.getSiegaTotalSumQuery("TOTAL SEGADO DE HIERBAS"),
		lastRow);
	lastRow = writeTable("Herbicida",
		agregadosReportQueries.getHerbicidadQuery(),
		agregadosReportQueries.getHerbicidaSumQuery(), lastRow + 2);
	lastRow = writeTable(
		"Eliminación veg. mediana de HG y transp. a vertedero",
		agregadosReportQueries.getVegetacionQuery(),
		agregadosReportQueries.getVegetacionSumQuery(), lastRow);

	for (int column = 0; column < getColumnNames().length; column++) {
	    sheet.autoSizeColumn(column);
	}

    }

    private String[] getColumnNames() {
	String[] columnNames = { "ID Elemento", "Tramo", "Tipo Vía",
		"Nombre Vía", "PK Inicial", "PK Final", "Sentido",
		"Medición AUDASA" };
	return columnNames;
    }

    private void writeTitle(String title, int rowIdx) {
	Row row0 = sheet.createRow(rowIdx);
	row0.createCell(0).setCellValue(title);

    }

    private void writeColumnNames(int rowIdx) {
	Row row0 = sheet.createRow(rowIdx);

	for (int i = 0; i < getColumnNames().length; i++) {
	    row0.createCell(i).setCellValue(getColumnNames()[i]);
	}
    }

    private void writeRows(DefaultTableModel tableModel, int rowIdx) {
	int totalRow = -1;
	for (int i = 0; i < tableModel.getRowCount(); i++) {
	    String t = tableModel.getValueAt(i, 0).toString();
	    if (t.equalsIgnoreCase("total")) {
		totalRow = i;
		rowIdx--;
		continue;
	    }
	    Row row = sheet.createRow(i + rowIdx);
	    for (int column = 0; column < tableModel.getColumnCount(); column++) {
		Object value = tableModel.getValueAt(i, column);
		createCell(row, column, value);
	    }
	}
	if (totalRow != -1) {
	    Row row = sheet.createRow(tableModel.getRowCount() + rowIdx);
	    for (int column = 0; column < tableModel.getColumnCount(); column++) {
		Object value = tableModel.getValueAt(totalRow, column);
		createCell(row, column, value);
	    }
	}
    }

    private void createCell(Row row, int column, Object value) {
	if (value == null) {
	    row.createCell(column);
	} else if (value instanceof String) {
	    row.createCell(column).setCellValue((String) value);
	} else if (value instanceof Date) {
	    if (!columnsStyles[column]) {
		columnsStyles[column] = true;
		CellStyle cellStyle = wb.createCellStyle();
		CreationHelper creationHelper = wb.getCreationHelper();
		cellStyle.setDataFormat(creationHelper.createDataFormat()
			.getFormat("d/m/yyyy"));
		sheet.setDefaultColumnStyle(column, cellStyle);
	    }
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

    private void writeTotal(String query, int row) {
	ConnectionWrapper con = new ConnectionWrapper(DBSession
		.getCurrentSession().getJavaConnection());
	String q = query
		+ filters.getWhereClauseFiltersForAgregados(element, true);
	DefaultTableModel table = con.execute(q);
	Object total = table.getValueAt(0, getColumnNames().length - 1);
	if ((total != null) && !total.toString().equals("0")) {
	    writeRows(table, row);
	}
    }

    private void writeToDisk(String outputFile) throws IOException {
	FileOutputStream fileOut = new FileOutputStream(outputFile);
	this.wb.write(fileOut);
	fileOut.close();
    }

}
