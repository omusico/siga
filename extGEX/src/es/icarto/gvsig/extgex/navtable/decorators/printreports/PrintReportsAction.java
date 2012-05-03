package es.icarto.gvsig.extgex.navtable.decorators.printreports;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.FileResolver;
import net.sf.jasperreports.engine.util.JRLoader;

import com.iver.andami.PluginServices;

public class PrintReportsAction {

    public void print(String outputFile, String reportFilePathName,
	    PrintReportsData data) {
	try {

	    FileResolver fileResolverForImages = new FileResolver() {
		@Override
		public File resolveFile(String fileName) {
		    java.net.URL imagesPath = PluginServices
			    .getPluginServices("es.icarto.gvsig.extgex")
			    .getClassLoader().getResource(fileName);
		    if (imagesPath != null) {
			return new File(imagesPath.getFile());
		    } else if (fileName != null) {
			return new File(fileName);
		    } else {
			return null;
		    }
		}
	    };

	    String reportPath = PluginServices
		    .getPluginServices("es.icarto.gvsig.extgex")
		    .getClassLoader()
		    .getResource("reports/reversions-subreport.jasper")
		    .getPath();
	    JasperReport reversionsSubreport = (JasperReport) JRLoader
		    .loadObjectFromFile(reportPath);

	    PrintReportsDataSubreportReversions reversionsDataSource = new PrintReportsDataSubreportReversions(
		    data.getIDFinca());

	    Calendar calendar = Calendar.getInstance();
	    String today = String.format("%02d/%02d/%d",
		    calendar.get(Calendar.DAY_OF_MONTH),
		    calendar.get(Calendar.MONTH) + 1, // january is month 0
		    calendar.get(Calendar.YEAR));
	    HashMap<String, Object> parameters = new HashMap<String, Object>();
	    parameters.put("REPORT_FILE_RESOLVER", fileResolverForImages);
	    parameters.put("REVERSIONS_SUBREPORT", reversionsSubreport);
	    parameters.put("FECHA_CONSULTA_EXPEDIENTE", today);
	    // subreport parameters
	    parameters.put("REVERSIONS_DATASOURCE", reversionsDataSource);

	    // Fill the report
	    JasperPrint print = JasperFillManager.fillReport(
		    reportFilePathName, parameters, data);

	    // Create a PDF exporter
	    JRExporter exporter = new JRPdfExporter();

	    // Configure the exporter (set output file name and print object)
	    exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
		    outputFile);
	    exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);

	    // Export the PDF file
	    exporter.exportReport();
	    System.out.println("File PDF created in " + outputFile + " path.");

	} catch (JRException e) {
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	}

    }
}
