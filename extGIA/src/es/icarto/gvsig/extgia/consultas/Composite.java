package es.icarto.gvsig.extgia.consultas;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.iver.andami.PluginServices;

import es.icarto.gvsig.navtableforms.ormlite.domainvalues.KeyValue;

public class Composite implements Component {
    private final List<Component> childs = new ArrayList<Component>();
    private final ConsultasFilters consultasFilters;
    private final KeyValue tipoConsulta;
    private final boolean pdf;

    private File outputPath;

    public Composite(ConsultasFilters consultasFilters, KeyValue tipoConsulta,
	    boolean pdf) {
	this.consultasFilters = consultasFilters;
	this.tipoConsulta = tipoConsulta;
	this.pdf = pdf;
    }

    @Override
    public boolean setOutputPath(File path) {
	if (path == null) {
	    JFileChooser fileChooser = new JFileChooser();
	    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    fileChooser.showSaveDialog(null);
	    outputPath = fileChooser.getSelectedFile();
	} else {
	    outputPath = path;
	}

	for (Component c : childs) {
	    c.setOutputPath(outputPath);
	}
	return outputPath != null;
    }

    @Override
    public void generateReportFile() {
	for (Component child : childs) {
	    child.generateReportFile();
	}
    }

    @Override
    public void finalActions() {
	Object[] reportGeneratedOptions = {
		PluginServices.getText(this, "reportGeneratedOptions_open"),
		PluginServices.getText(this, "reportGeneratedOptions_close") };
	int m = JOptionPane.showOptionDialog(null,
		PluginServices.getText(this, "filesGenerated_msg") + "\""
			+ outputPath.getAbsolutePath() + "\"", null,
		JOptionPane.YES_NO_CANCEL_OPTION,
		JOptionPane.INFORMATION_MESSAGE, null, reportGeneratedOptions,
		reportGeneratedOptions[1]);

	if (m == JOptionPane.OK_OPTION) {
	    Desktop d = Desktop.getDesktop();
	    try {
		d.open(outputPath);
	    } catch (IOException e1) {
		e1.printStackTrace();
	    }
	}
    }

    public void add(List<String[]> list) {
	for (String[] e : list) {
	    Leaf leaf = new Leaf(e, consultasFilters, tipoConsulta, pdf);
	    childs.add(leaf);
	}
    }
}
