package es.icarto.gvsig.extgex.queries;

import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

import com.iver.andami.messages.NotificationManager;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.commons.queries.CSVReport;
import es.icarto.gvsig.commons.queries.XLSReport;
import es.icarto.gvsig.extgex.utils.SaveFileDialog;

public class QueriesOuputWidget {

    public static final String PDF = "PDF";
    public static final String CSV = "CSV";
    public static final String SCREEN = "SCREEN";
    public static final String HTML = "HTML";
    public static final String RTF = "RTF";
    public static final String EXCEL = "EXCEL";
    private final JRadioButton pdfRB;
    private final JRadioButton excelRB;
    private final ButtonGroup buttonGroup;

    public QueriesOuputWidget(FormPanel formPanel, String... formats) {
	pdfRB = (JRadioButton) formPanel.getComponentByName("pdf");
	pdfRB.setActionCommand(QueriesOuputWidget.PDF);
	pdfRB.setSelected(true);
	excelRB = (JRadioButton) formPanel.getComponentByName("excel");
	excelRB.setActionCommand(QueriesOuputWidget.EXCEL);

	buttonGroup = new ButtonGroup();
	buttonGroup.add(excelRB);
	buttonGroup.add(pdfRB);
    }

    public QueriesOuputWidget() {
	pdfRB = null;
	excelRB = null;
	buttonGroup = null;
    }

    public void toPDF(ResultTableModel table, String[] filters) {
	SaveFileDialog sfd = new SaveFileDialog("Archivos PDF", "pdf");
	File f = sfd.showDialog();
	if (f != null) {
	    String filename = f.getAbsolutePath();
	    new Report(Report.PDF, filename, table, filters);
	}
    }

    private void toCSV(ResultTableModel table, final String[] filters) {

	SaveFileDialog sfd = new SaveFileDialog("CSV files", "csv");
	File f = sfd.showDialog();
	if (f != null) {
	    new CSVReport(f.getAbsolutePath(), table, table.getQueryFilters());
	}
    }

    public void toScreen(ResultTableModel table, String[] filters) {
	QueriesResultPanel resultPanel = new QueriesResultPanel(table, filters);
	resultPanel.open();
    }

    public void toHtml(ResultTableModel table, String[] filters) {
	SaveFileDialog sfd = new SaveFileDialog("HTML files", "html", "htm");
	File f = sfd.showDialog();
	if (f != null) {
	    if (sfd.writeFileToDisk(table.getHTML(), f)) {
		NotificationManager.showMessageError("error_saving_file", null);
	    }
	}
    }

    public void toRTF(ResultTableModel table, String[] filters) {
	SaveFileDialog sfd = new SaveFileDialog("RTF files", "rtf");
	File f = sfd.showDialog();
	if (f != null) {
	    String filename = f.getAbsolutePath();
	    new Report(Report.RTF, filename, table, filters);
	}
    }

    public void toXLSX(ResultTableModel table, final String[] filters) {
	SaveFileDialog sfd = new SaveFileDialog("Archivos Excel", "xls");
	File f = sfd.showDialog();
	if (f != null) {
	    String filename = f.getAbsolutePath();
	    new XLSReport(filename, table, table.getQueryFilters());
	}
    }

    public void to(ResultTableModel table, String[] filters) {
	String sel = buttonGroup.getSelection().getActionCommand();
	to(sel, table, filters);
    }

    public void to(String sel, ResultTableModel table, String[] filters) {
	if (sel.equals(PDF)) {
	    toPDF(table, filters);
	} else if (sel.equals(CSV)) {
	    toCSV(table, filters);
	} else if (sel.equals(SCREEN)) {
	    toScreen(table, filters);
	} else if (sel.equals(HTML)) {
	    toHtml(table, filters);
	} else if (sel.equals(RTF)) {
	    toRTF(table, filters);
	} else if (sel.equals(EXCEL)) {
	    toXLSX(table, filters);
	}
    }
}
