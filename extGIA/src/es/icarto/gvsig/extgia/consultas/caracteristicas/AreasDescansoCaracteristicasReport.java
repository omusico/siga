package es.icarto.gvsig.extgia.consultas.caracteristicas;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;

import es.icarto.gvsig.extgia.consultas.ConsultasFilters;
import es.icarto.gvsig.extgia.consultas.PDFReport;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class AreasDescansoCaracteristicasReport extends PDFReport {

    public AreasDescansoCaracteristicasReport(String element, String fileName,
	    ResultSet resultMap, ConsultasFilters filters) {
	super(element, fileName, resultMap, filters);
    }

    @Override
    protected String getTitle() {
	return "Listado de Características";
    }

    @Override
    protected String[] getColumnNames() {
	String[] columnNames = {
		"ID Área",
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
	columnsWidth[17] = 60f;
	//aditionalColumn
	columnsWidth[18] = 60f;

	return columnsWidth;
    }

    @Override
    protected Rectangle setPageSize() {
	return PageSize.A4.rotate();
    }

    @Override
    protected void writeDatesRange(Document document, ConsultasFilters filters) {

    }

    @Override
    protected boolean hasEmbebedTable() {
	return true;
    }

    @Override
    protected PdfPCell writeAditionalColumnName() {
	PdfPCell aditionalCell = new PdfPCell(new Paragraph("Nº Ramales", bodyBoldStyle));
	aditionalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	return aditionalCell;
    }

    @Override
    protected PdfPCell writeAditionalColumnValues(String id) {
	try {
	    Statement st = DBSession.getCurrentSession().getJavaConnection().createStatement();
	    String query = "SELECT count(id_ramal) FROM audasa_extgia.areas_descanso_ramales" +
		    " WHERE id_area_descanso = '" + id + "';";
	    ResultSet rs = st.executeQuery(query);
	    rs.next();
	    PdfPCell aditionalCell = new PdfPCell(new Paragraph(rs.getString(1), cellBoldStyle));
	    aditionalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    return aditionalCell;
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return new PdfPCell();
    }

}
