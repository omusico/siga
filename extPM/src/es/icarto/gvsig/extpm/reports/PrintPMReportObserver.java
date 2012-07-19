package es.icarto.gvsig.extpm.reports;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.extgex.navtable.decorators.printreports.PrintReportsAction;
import es.icarto.gvsig.extgex.utils.SaveFileDialog;
import es.udc.cartolab.gvsig.navtable.AbstractNavTable;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class PrintPMReportObserver implements ActionListener {
    
    private FLyrVect layer;
    private AbstractNavTable dialog;
    
    private File outputFile;
    
    public PrintPMReportObserver(FLyrVect layer, AbstractNavTable dialog) {
	this.layer = layer;
	this.dialog = dialog;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
	SaveFileDialog sfd = new SaveFileDialog("PDF files", "pdf");
	outputFile = sfd.showDialog();
	
	
	Connection conn = null;
	try {
	    JasperPrint print = JasperFillManager.fillReport(
	    	    "/home/psanxiao/workspace/audasa3/_fwAndami/gvSIG/extensiones/es.icarto.gvsig.extpm/reports/pm_report.jasper", null, conn);
	    // Create a PDF exporter
	    JRExporter exporter = new JRPdfExporter();

	    // Configure the exporter (set output file name and print object)
	    exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
		    outputFile);
	   // exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);

	    // Export the PDF file
	    exporter.exportReport();
	} catch (JRException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	

    }

}
