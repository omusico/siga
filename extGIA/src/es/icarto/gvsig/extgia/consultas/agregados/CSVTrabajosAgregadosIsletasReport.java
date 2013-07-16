package es.icarto.gvsig.extgia.consultas.agregados;

import es.icarto.gvsig.extgia.consultas.ConsultasFilters;

public class CSVTrabajosAgregadosIsletasReport extends CSVTrabajosAgregadosReport {

    public CSVTrabajosAgregadosIsletasReport(String outputFile,
	    ConsultasFilters filters) {
	super(outputFile, filters);
	// TODO Auto-generated constructor stub
    }

    @Override
    protected String getElement() {
	return "Isletas";
    }

}
