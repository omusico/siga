package es.icarto.gvsig.audasacommons.forms.reports;

import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import com.iver.andami.PluginServices;

import es.udc.cartolab.gvsig.navtable.AbstractNavTable;

public class NavTableComponentsPrintButton {

    public JButton getPrintButton(AbstractNavTable dialog) {
	JButton printReportB = createButton(PluginServices.getText(this,
		"printReportsToolTip"), getIcon("images/print-report.jpg"),
		new PrintPMReportObserver(dialog));
	return printReportB;
    }

    public JButton createButton(String toolTipText, ImageIcon icon,
	    ActionListener listener) {
	JButton but = new JButton(icon);
	but.setToolTipText(toolTipText);
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
