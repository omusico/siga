package es.icarto.gvsig.extpm.reports;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.extgex.navtable.NavTableComponentsFactory;
import es.udc.cartolab.gvsig.navtable.AbstractNavTable;

public class NavTableComponentsPrintButton extends NavTableComponentsFactory {
    
    public JButton getPrintButton(AbstractNavTable dialog) {
	NavTableComponentsFactory ntFactory = new NavTableComponentsFactory();
	JButton printReportB = ntFactory.createButton(PluginServices.getText(
		this, "printReportsToolTip"), getIcon("/print-report.jpg"),
		new PrintPMReportObserver(dialog));
	return printReportB;
    }

    private ImageIcon getIcon(String iconName) {
	java.net.URL imgURL = getClass().getResource(iconName);
	if (imgURL == null) {
	    imgURL = AbstractNavTable.class.getResource(iconName);
	}

	ImageIcon icon = new ImageIcon(imgURL);
	return icon;
    }
}
