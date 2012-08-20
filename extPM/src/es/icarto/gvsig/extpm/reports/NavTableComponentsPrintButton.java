package es.icarto.gvsig.extpm.reports;

import javax.swing.JButton;

import com.iver.andami.PluginServices;

import es.icarto.gvsig.extgex.navtable.NavTableComponentsFactory;
import es.udc.cartolab.gvsig.navtable.AbstractNavTable;

public class NavTableComponentsPrintButton extends NavTableComponentsFactory {

    public JButton getPrintButton(AbstractNavTable dialog) {
	JButton printReportB = createButton(PluginServices.getText(
		this, "printReportsToolTip"), getIcon("/print-report.jpg"),
		new PrintPMReportObserver(dialog));
	return printReportB;
    }
}
