/*
 * Copyright (c) 2010. Cartolab (Universidade da Coruña)
 * 
 * This file is part of EIEL Validation
 * 
 * EIEL Validation is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or any later version.
 * 
 * EIEL Validation is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with EIEL Validation
 * If not, see <http://www.gnu.org/licenses/>.
 */

package es.icarto.gvsig.extgex.queries;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;

import com.iver.andami.messages.NotificationManager;

import es.icarto.gvsig.audasacommons.gui.gvWindow;
import es.icarto.gvsig.extgex.utils.SaveFileDialog;

public class QueriesResultPanel extends gvWindow implements ActionListener {

    private JEditorPane resultTA;
    private JButton exportB;
    private JComboBox fileTypeCB;
    String[] fileFormats = { "HTML", "RTF", "PDF" };
    private ArrayList<ResultTableModel> resultsMap;
    private String[] filters;

    public QueriesResultPanel() {
	this(null);
    }

    public QueriesResultPanel(String council) {

	super(800, 500, false);
	if (council != null && !council.equals("")) {
	    setTitle("Resultado de la consulta de " + council);
	} else {
	    setTitle("Resultado de la consulta");
	}

	MigLayout layout = new MigLayout("inset 0, align center", "[grow]",
		"[grow][]");

	setLayout(layout);

	resultTA = new JEditorPane();
	resultTA.setEditable(false);
	resultTA.setContentType("text/html");
	JScrollPane scrollPane = new JScrollPane(resultTA);
	JPanel panel = new JPanel();
	exportB = new JButton("Exportar");
	exportB.addActionListener(this);
	panel.add(exportB);
	fileTypeCB = new JComboBox(fileFormats);
	panel.add(fileTypeCB);

	add(scrollPane, "growx, growy, wrap");
	add(panel, "shrink, align right");

    }

    public void setResult(String result) {
	resultTA.setText(result);
    }

    public void setResultMap(ArrayList<ResultTableModel> resultMap) {
	this.resultsMap = resultMap;
    }

    public void setFilters(String[] filters) {
	this.filters = filters;
    }

    public void actionPerformed(ActionEvent e) {
	if (e.getSource() == exportB) {
	    if (fileTypeCB.getSelectedIndex() == 0) {
		SaveFileDialog sfd = new SaveFileDialog("HTML files", "html",
			"htm");
		File f = sfd.showDialog();
		if (f != null) {
		    if (sfd.writeFileToDisk(resultTA.getText(), f)) {
			NotificationManager.showMessageError(
				"error_saving_file", null);
		    }
		}
	    } else if (fileTypeCB.getSelectedIndex() == 1) {
		SaveFileDialog sfd = new SaveFileDialog("RTF files", "rtf");
		File f = sfd.showDialog();
		if (f != null) {
		    String fileName = f.getAbsolutePath();
		    ResultTableModel.writeResultTableToRtfReport(fileName,
			    resultsMap, filters);
		}
	    } else {
		SaveFileDialog sfd = new SaveFileDialog("PDF files", "pdf");
		File f = sfd.showDialog();
		if (f != null) {
		    String fileName = f.getAbsolutePath();
		    ResultTableModel.writeResultTableToPdfReport(fileName,
			    resultsMap, filters);
		}
	    }
	}
    }

}// Class
