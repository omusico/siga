package es.icarto.gvsig.extgia.consultas;

import java.sql.ResultSet;

public class TrabajosAgregadosTaludesReport extends TrabajosAgregadosReport {

    public TrabajosAgregadosTaludesReport(String[] element, String fileName,
	    ResultSet resultMap, String[] filters) {
	super(element, fileName, resultMap, filters);
	// TODO Auto-generated constructor stub
    }

    @Override
    protected String getElement() {
	return "taludes";
    }

}
