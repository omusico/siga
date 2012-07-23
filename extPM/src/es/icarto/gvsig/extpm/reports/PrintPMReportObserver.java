package es.icarto.gvsig.extpm.reports;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.util.HashMap;

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

	if (outputFile != null) {
	    PrintPMReportAction reportAction = new PrintPMReportAction();
	    reportAction.print(outputFile.getAbsolutePath(), getReportPath(), getReportParameters());
	}
    }

    private String getReportPath() {
	 java.net.URL reportPath = PluginServices
		.getPluginServices("es.icarto.gvsig.extpm").getClassLoader()
		.getResource("reports/pm_report.jasper");
	 return reportPath.getPath();
    }
    
    private HashMap<String, Object> getReportParameters() {
	HashMap<String, Object> parameters = new HashMap<String, Object>();
	parameters.put("PM_QUERY_WHERE", Integer.valueOf(getPMFileId()+1));
	return parameters;
    }
    
    private int getPMFileId() {
	long currentPosition = dialog.getPosition();
	return Long.valueOf(currentPosition).intValue();
    }
}