package es.icarto.gvsig.extgia.consultas.agregados;

public class CSVTrabajosAgregadosTaludesReport extends
CSVTrabajosAgregadosReport {

    public CSVTrabajosAgregadosTaludesReport(String outputFile) {
	super(outputFile);
	// TODO Auto-generated constructor stub
    }

    @Override
    protected String getElement() {
	return "Taludes";
    }

}
