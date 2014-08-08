package es.icarto.gvsig.extgex.queries;

import java.io.File;
import java.util.Arrays;

import com.iver.andami.messages.NotificationManager;

import es.icarto.gvsig.extgex.utils.SaveFileDialog;
import es.icarto.gvsig.extgia.consultas.ConsultasFilters;

public class QueriesOuputWidget {

    public static final String PDF = "PDF";
    public static final String CSV = "CSV";
    public static final String SCREEN = "SCREEN";
    public static final String HTML = "HTML";
    public static final String RTF = "RTF";

    private QueriesOuputWidget() {
	throw new AssertionError("only static methods rigth now");
    }

    public static void toPDF(ResultTableModel table, String[] filters) {
	SaveFileDialog sfd = new SaveFileDialog("PDF files", "pdf");
	File f = sfd.showDialog();
	if (f != null) {
	    String filename = f.getAbsolutePath();
	    new Report(Report.PDF, filename, table, filters);
	}
    }

    private static void toCSV(ResultTableModel table, String[] filters) {

	// TODO: fpuga. Workaround. Filters should be a common interfaz between
	// extGIA and extGEX. Here we are reusing CSVReport and using the
	// ConsultasFilters object in a inpropper way to set the filter values
	ConsultasFilters consultasFilters = new ConsultasFilters(null, null,
		null, null, null);
	consultasFilters.setFields(Arrays.asList(filters));

	SaveFileDialog sfd = new SaveFileDialog("CSV files", "csv");
	File f = sfd.showDialog();
	if (f != null) {
	    new CSVReportExpropiations(f.getAbsolutePath(), table,
		    consultasFilters);
	}
    }

    public static void toScreen(ResultTableModel table, String[] filters) {
	QueriesResultPanel resultPanel = new QueriesResultPanel(table, filters);
	resultPanel.open();
    }

    public static void toHtml(ResultTableModel table, String[] filters) {
	SaveFileDialog sfd = new SaveFileDialog("HTML files", "html", "htm");
	File f = sfd.showDialog();
	if (f != null) {
	    if (sfd.writeFileToDisk(table.getHTML(), f)) {
		NotificationManager.showMessageError("error_saving_file", null);
	    }
	}
    }

    public static void toRTF(ResultTableModel table, String[] filters) {
	SaveFileDialog sfd = new SaveFileDialog("RTF files", "rtf");
	File f = sfd.showDialog();
	if (f != null) {
	    String filename = f.getAbsolutePath();
	    new Report(Report.RTF, filename, table, filters);
	}
    }

    public static void to(String sel, ResultTableModel table, String[] filters) {
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
	}
    }

}
