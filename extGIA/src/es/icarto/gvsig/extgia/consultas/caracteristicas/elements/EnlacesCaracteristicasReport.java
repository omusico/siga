package es.icarto.gvsig.extgia.consultas.caracteristicas.elements;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.table.DefaultTableModel;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;

import es.icarto.gvsig.commons.queries.Field;
import es.icarto.gvsig.extgia.consultas.ConsultasFilters;
import es.icarto.gvsig.extgia.consultas.PDFReport;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class EnlacesCaracteristicasReport extends PDFReport {

    public EnlacesCaracteristicasReport(String element[], String fileName,
	    DefaultTableModel tableModel, ConsultasFilters<Field> filters,
	    int reportType) {
	super(element, fileName, tableModel, filters, reportType);
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
	String[] columnNames = { "ID Enlace", "Nombre", "Tramo", "PK",
		"Número Salida", "Tipo Enlace", "Alumbrado", "Observaciones" };
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
	// aditional column
	columnsWidth[8] = 90f;

	return columnsWidth;
    }

    @Override
    protected void writeDatesRange(Document document,
	    ConsultasFilters<Field> filters) {

    }

    @Override
    protected boolean hasEmbebedTable() {
	return true;
    }

    @Override
    protected PdfPCell writeAditionalColumnName() {
	PdfPCell aditionalCell = new PdfPCell(new Paragraph(
		"Nº Ramales | Carreteras Enlazadas", bodyBoldStyle));
	aditionalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	return aditionalCell;
    }

    @Override
    protected PdfPCell writeAditionalColumnValues(String id) {
	try {
	    Statement st = DBSession.getCurrentSession().getJavaConnection()
		    .createStatement();
	    String query = "SELECT count(id_ramal) FROM audasa_extgia.enlaces_ramales"
		    + " WHERE id_enlace = '" + id + "';";
	    ResultSet rs = st.executeQuery(query);
	    rs.next();
	    String data = rs.getString(1);

	    query = "SELECT clave_carretera FROM audasa_extgia.enlaces_carreteras_enlazadas"
		    + " WHERE id_enlace = '" + id + "';";
	    rs = st.executeQuery(query);
	    data = data + " | ";
	    while (rs.next()) {
		data = data + rs.getString(1) + ";";
	    }

	    PdfPCell aditionalCell = new PdfPCell(new Paragraph(data,
		    cellBoldStyle));
	    aditionalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    return aditionalCell;
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return new PdfPCell();
    }

}
