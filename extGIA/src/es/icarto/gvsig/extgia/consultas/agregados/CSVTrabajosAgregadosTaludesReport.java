package es.icarto.gvsig.extgia.consultas.agregados;

import es.icarto.gvsig.extgia.consultas.ConsultasFilters;

public class CSVTrabajosAgregadosTaludesReport extends
CSVTrabajosAgregadosReport {

    public CSVTrabajosAgregadosTaludesReport(String outputFile, ConsultasFilters filters) {
	super(outputFile, filters);
	// TODO Auto-generated constructor stub
    }

    @Override
    protected String getElement() {
	return "Taludes";
    }

}
