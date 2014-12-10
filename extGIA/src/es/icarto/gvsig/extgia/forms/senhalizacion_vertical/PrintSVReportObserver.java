package es.icarto.gvsig.extgia.forms.senhalizacion_vertical;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.NumericValue;
import com.hardcode.gdbms.engine.values.Value;
import com.iver.cit.gvsig.fmap.layers.FBitSet;

import es.icarto.gvsig.audasacommons.forms.reports.PrintReportAction;
import es.icarto.gvsig.audasacommons.forms.reports.SaveFileDialog;
import es.icarto.gvsig.commons.queries.FinalActions;
import es.udc.cartolab.gvsig.navtable.AbstractNavTable;

public class PrintSVReportObserver implements ActionListener {

    private static final Logger logger = Logger
	    .getLogger(PrintSVReportObserver.class);

    private final AbstractNavTable dialog;
    private File outputFile;
    private final String reportPath;
    private final String extensionPath;
    private final String idField;
    private final String idValue;

    private int idIdx;

    public PrintSVReportObserver(AbstractNavTable dialog, String extensionPath,
	    String reportPath, String idField, String idValue) {
	this.extensionPath = extensionPath;
	this.dialog = dialog;
	this.reportPath = reportPath;
	this.idField = idField;
	this.idValue = idValue;

    }

    @Override
    public void actionPerformed(ActionEvent arg0) {

	try {
	    idIdx = dialog.getRecordset().getFieldIndexByName(idField);
	} catch (ReadDriverException e) {
	    logger.error(e.getStackTrace(), e);
	}

	FBitSet selection = dialog.getRecordset().getSelection();
	int size = selection.cardinality();
	String msg = "";
	if (size == 0) {
	    msg = "Se va a imprimir la ficha actual";

	    int showConfirmDialog = JOptionPane.showConfirmDialog(dialog, msg);
	    if (showConfirmDialog != JOptionPane.YES_OPTION) {
		return;
	    }

	    SaveFileDialog sfd = new SaveFileDialog("PDF files", "pdf");
	    outputFile = sfd.showDialog();

	    if (outputFile != null) {

		PrintReportAction reportAction = new PrintReportAction();
		reportAction.print(outputFile.getAbsolutePath(),
			getReportPath(), getReportParameters());

		Object[] reportGeneratedOptions = { "Ver informe", "Cerrar" };
		int m = JOptionPane.showOptionDialog(
			null,
			"Informe generado con �xito en: \n" + "\""
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

	} else {
	    msg = "Se van a imprimir las " + size + " fichas seleccionadas";
	    int showConfirmDialog = JOptionPane.showConfirmDialog(dialog, msg);
	    if (showConfirmDialog != JOptionPane.YES_OPTION) {
		return;
	    }
	    JFileChooser jFileChooser = new JFileChooser(
		    System.getenv("user.dir"));
	    jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    int showDialog = jFileChooser.showDialog(dialog, "Aceptar");
	    if (showDialog == JFileChooser.APPROVE_OPTION) {
		final File selectedFile = jFileChooser.getSelectedFile();
		if (selectedFile != null) {
		    for (int i = selection.nextSetBit(0); i >= 0; i = selection
			    .nextSetBit(i + 1)) {
			HashMap<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("EXTENSION_PATH", extensionPath);

			try {
			    Value fieldValue = dialog.getRecordset()
				    .getFieldValue(i, idIdx);
			    int intValue = ((NumericValue) fieldValue)
				    .intValue();
			    parameters.put("SENHALIZACION_VERTICAL_QUERY",
				    intValue);
			    PrintReportAction reportAction = new PrintReportAction();
			    reportAction.print(selectedFile.getAbsolutePath()
				    + "/se�alizacion_vertical_" + intValue,
				    getReportPath(), parameters);
			} catch (ReadDriverException e) {
			    logger.error(e.getStackTrace(), e);
			}
		    }
		    FinalActions fa = new FinalActions(false, selectedFile);
		    fa.openFolder();
		}
	    }

	}

    }

    private String getReportPath() {
	return this.reportPath;
    }

    private HashMap<String, Object> getReportParameters() {
	HashMap<String, Object> parameters = new HashMap<String, Object>();
	parameters.put("SENHALIZACION_VERTICAL_QUERY",
		Integer.parseInt(idValue));
	parameters.put("EXTENSION_PATH", extensionPath);
	return parameters;
    }
}