package es.icarto.gvsig.extpm.forms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.extpm.forms.filesLink.NavTableComponentsFilesLinkButton;
import es.icarto.gvsig.extpm.forms.reports.NavTableComponentsPrintButton;
import es.icarto.gvsig.extpm.preferences.Preferences;
import es.icarto.gvsig.navtableforms.AbstractForm;

public class FormPM extends AbstractForm {

    private FormPanel form;
    private final FLyrVect layer;

    // WIDGETS
    private JButton editParcelasButton;

    EditParcelasAfectadasListener editParcelasAfectadasListener;

    public FormPM(FLyrVect layer) {
	super(layer);
	this.layer = layer;
	initWindow();
	addNewButtonsToActionsToolBar();
    }

    private void addNewButtonsToActionsToolBar() {
	JPanel actionsToolBar = this.getActionsToolBar();
	NavTableComponentsFilesLinkButton ntFilesLinkButton = new NavTableComponentsFilesLinkButton();
	NavTableComponentsPrintButton ntPrintButton = new NavTableComponentsPrintButton();
	JButton filesLinkB = ntFilesLinkButton.getFilesLinkButton(layer,
		this);
	JButton printReportB = ntPrintButton.getPrintButton(this);
	if (filesLinkB != null && printReportB != null) {
	    actionsToolBar.add(filesLinkB);
	    actionsToolBar.add(printReportB);
	}
    }

    @Override
    protected void setListeners() {
	super.setListeners();
	editParcelasAfectadasListener = new EditParcelasAfectadasListener();
	editParcelasButton = (JButton) getFormBody().getComponentByName("num_parcela_audasa_button");
	editParcelasButton.addActionListener(editParcelasAfectadasListener);
    }

    @Override
    public String getXMLPath() {
	return PluginServices.getPluginServices("es.icarto.gvsig.extpm")
	.getClassLoader()
	.getResource(Preferences.XML_ORMLITE_RELATIVE_PATH).getPath();
    }

    private void initWindow() {
	viewInfo.setHeight(830);
	viewInfo.setWidth(700);
	viewInfo.setTitle("Policía de Márgenes");
    }

    @Override
    protected void fillSpecificValues() {
	// TODO Auto-generated method stub

    }

    @Override
    public FormPanel getFormBody() {
	if (form == null) {
	    return new FormPanel("pm.xml");
	}
	return form;
    }

    @Override
    public Logger getLoggerName() {
	// TODO Auto-generated method stub
	return null;
    }

    public class EditParcelasAfectadasListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
	    System.out.println("=====LISTENER");
	    SubFormPMParcelasAfectadas subForm = new SubFormPMParcelasAfectadas();
	    PluginServices.getMDIManager().addWindow(subForm);
	}

    }

}
