package es.icarto.gvsig.extgia.consultas.caracteristicas.elements;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

import es.icarto.gvsig.extgia.consultas.ConsultasFilters;
import es.icarto.gvsig.extgia.consultas.PDFReport;
import es.icarto.gvsig.extgia.consultas.caracteristicas.PDFCaracteristicasQueries;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class SenhalizacionVerticalCaracteristicasReport extends PDFReport {

    public SenhalizacionVerticalCaracteristicasReport(String element,
	    String fileName, ResultSet resultMap, ConsultasFilters filters) {
	super(element, fileName, resultMap, filters);
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
		"ID Elemento",
		"Tipo Vía",
		"Nombre Vía",
		"PK",
		"Tipo Sustentación",
		"Material Sustentación",
		"Tipo Poste",
		"Nº Postes",
		"Anclaje",
		"Cimentación Especial",
		"Observaciones",
		"Tipo Señal",
		"Código Señal",
		"Leyenda",
		"Panel Complementario",
		"Código Panel",
		"Texto Panel",
		"Reversible",
		"Luminosa",
		"Tipo Superficie",
		"Material Superficie",
		"Material Retrorreflectante",
		"Nivel Reflectancia",
		"Ancho",
		"Alto",
		"Superficie",
		"Altura",
		"Fabricante",
		"Fecha Fabricación",
		"Fecha Instalación",
		"Fecha Reposición",
		"Marcado CE",
		"Observaciones Señal"
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
	columnsWidth[18] = 60f;
	columnsWidth[19] = 60f;
	columnsWidth[20] = 60f;
	columnsWidth[21] = 60f;
	columnsWidth[22] = 60f;
	columnsWidth[23] = 60f;
	columnsWidth[24] = 60f;
	columnsWidth[25] = 60f;
	columnsWidth[26] = 60f;
	columnsWidth[27] = 60f;
	columnsWidth[28] = 60f;
	columnsWidth[29] = 60f;
	columnsWidth[30] = 60f;
	columnsWidth[31] = 60f;
	columnsWidth[32] = 60f;

	return columnsWidth;
    }

    @Override
    protected void writeValues(Document document, ResultSet resultMap,
	    PdfPTable table) throws SQLException, DocumentException {
	Statement st = DBSession.getCurrentSession().getJavaConnection().createStatement();
	String query = PDFCaracteristicasQueries.getSenhalizacionVerticalQuery(filters);
	ResultSet rs = st.executeQuery(query);
	super.writeValues(document, rs, table);
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
