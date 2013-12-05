package es.icarto.gvsig.extgia.consultas.agregados;

import java.sql.ResultSet;

import com.lowagie.text.pdf.PdfPCell;

import es.icarto.gvsig.extgia.consultas.ConsultasFilters;

public class TrabajosAgregadosTaludesReport extends TrabajosAgregadosReport {

    public TrabajosAgregadosTaludesReport(String[] element, String fileName,
	    ResultSet resultMap, ConsultasFilters filters, int reportType) {
	super(element, fileName, resultMap, filters, reportType);
	// TODO Auto-generated constructor stub
    }

    @Override
    protected String getElement() {
	return "Taludes";
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
