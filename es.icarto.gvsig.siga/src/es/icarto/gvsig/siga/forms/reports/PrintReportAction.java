package es.icarto.gvsig.siga.forms.reports;

import java.util.HashMap;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class PrintReportAction {

    public void print(String outputFile, String reportFilePath,  HashMap<String, Object> parameters) {
	JasperPrint print;
	try {
	    // Fill report
	    print = JasperFillManager.fillReport(
		    reportFilePath, parameters, DBSession.getCurrentSession().getJavaConnection());

	    // Create a PDF exporter
	    JRExporter exporter = new JRPdfExporter();
	    // Configure the exporter (set output file name and print object)
	    exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
		    outputFile);
	    exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
	    // Export the PDF file
	    exporter.exportReport();
	} catch (JRException e) {
	    e.printStackTrace();
	}
    }

}
