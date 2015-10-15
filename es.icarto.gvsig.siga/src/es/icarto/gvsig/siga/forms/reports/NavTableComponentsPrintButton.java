package es.icarto.gvsig.siga.forms.reports;

import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import com.iver.andami.PluginServices;

import es.icarto.gvsig.navtableforms.AbstractForm;
import es.udc.cartolab.gvsig.navtable.AbstractNavTable;

public class NavTableComponentsPrintButton {

    public JButton getPrintButton(String reportPath, String tableName,
	    String idField, AbstractForm form) {
	JButton printReportB = createButton(PluginServices.getText(this,
		"printReportsToolTip"), getIcon("images/print-report.png"),
		new PrintPMReportObserver(reportPath, tableName, idField, form));
	return printReportB;
    }

    public JButton createButton(String toolTipText, ImageIcon icon,
	    ActionListener listener) {
	JButton but = new JButton(icon);
	but.setToolTipText(toolTipText);
	but.removeActionListener(listener);
	but.addActionListener(listener);
	return but;
    }

    public ImageIcon getIcon(String iconName) {
	java.net.URL imgURL = getClass().getClassLoader().getResource(iconName);
	if (imgURL == null) {
	    imgURL = AbstractNavTable.class.getResource(iconName);
	}

	ImageIcon icon = new ImageIcon(imgURL);
	return icon;
    }

}
