package es.icarto.gvsig.extgia.consultas.agregados;

import java.sql.ResultSet;

import es.icarto.gvsig.extgia.consultas.ConsultasFilters;

public class TrabajosAgregadosTaludesReport extends TrabajosAgregadosReport {

    public TrabajosAgregadosTaludesReport(String[] element, String fileName,
	    ResultSet resultMap, ConsultasFilters filters) {
	super(element, fileName, resultMap, filters);
	// TODO Auto-generated constructor stub
    }

    @Override
    protected String getElement() {
	return "taludes";
    }

}
