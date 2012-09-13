package es.icarto.gvsig.audasacommons.forms.reports;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JOptionPane;

import com.iver.andami.PluginServices;

import es.icarto.gvsig.audasacommons.forms.reports.imagefilechooser.ImageFileChooser;
import es.udc.cartolab.gvsig.navtable.AbstractNavTable;

public class PrintPMReportObserver implements ActionListener {

    private final AbstractNavTable dialog;
    private File inputImageFile;
    private File outputFile;

    public PrintPMReportObserver(AbstractNavTable dialog) {
	this.dialog = dialog;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
	SaveFileDialog sfd = new SaveFileDialog("PDF files", "pdf");
	outputFile = sfd.showDialog();

	if (outputFile != null) {
	    Object[] selectImageOptions = { "Sí, seleccionar imagen", "No" };
	    int n = JOptionPane.showOptionDialog(null,
		    "¿Desea incluir una imagen en el informe?",
		    "Incluir imagen", JOptionPane.YES_NO_CANCEL_OPTION,
		    JOptionPane.QUESTION_MESSAGE, null, selectImageOptions,
		    selectImageOptions[1]);

	    if (n == JOptionPane.YES_OPTION) {
		ImageFileChooser ifs = new ImageFileChooser();
		inputImageFile = ifs.showDialog();
	    }

	    PrintPMReportAction reportAction = new PrintPMReportAction();
	    reportAction.print(outputFile.getAbsolutePath(), getReportPath(),
		    getReportParameters());

	    Object[] reportGeneratedOptions = { "Ver informe", "Cerrar" };
	    int m = JOptionPane.showOptionDialog(
		    null,
		    "Informe generado con éxito en: \n" + "\""
			    + outputFile.getAbsolutePath() + "\"", null,
		    JOptionPane.YES_NO_CANCEL_OPTION,
		    JOptionPane.INFORMATION_MESSAGE, null,
		    reportGeneratedOptions, reportGeneratedOptions[1]);

	    if (m == JOptionPane.OK_OPTION) {
		Desktop d = Desktop.getDesktop();
		try {
		    d.open(outputFile);
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	}
    }

    private String getReportPath() {
	java.net.URL reportPath = PluginServices
		.getPluginServices("es.icarto.gvsig.extgia").getClassLoader()
		.getResource("reports/taludes.jasper");
	return reportPath.getPath();
    }

    private HashMap<String, Object> getReportParameters() {
	HashMap<String, Object> parameters = new HashMap<String, Object>();
	parameters.put("PM_QUERY_WHERE", Integer.valueOf(getPMFileId() + 1));
	if (inputImageFile != null) {
	    parameters.put("MAIN_IMAGE_PATH", inputImageFile.getAbsolutePath());
	}
	return parameters;
    }

    private int getPMFileId() {
	long currentPosition = dialog.getPosition();
	return Long.valueOf(currentPosition).intValue();
    }
}