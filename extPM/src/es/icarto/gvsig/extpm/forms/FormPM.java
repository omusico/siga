package es.icarto.gvsig.extpm.forms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.extpm.forms.filesLink.NavTableComponentsFilesLinkButton;
import es.icarto.gvsig.extpm.forms.reports.NavTableComponentsPrintButton;
import es.icarto.gvsig.extpm.preferences.Preferences;
import es.icarto.gvsig.extpm.utils.managers.ToggleEditingManager;
import es.icarto.gvsig.navtableforms.AbstractForm;

public class FormPM extends AbstractForm {

    private FormPanel form;
    private final FLyrVect layer;
    private final boolean newRegister;

    // WIDGETS
    private JButton editParcelasButton;
    private JComboBox area;
    private JTextField fecha;
    private JTextField numeroPM;

    EditParcelasAfectadasListener editParcelasAfectadasListener;
    CalculatePMNumberListener calculatePMNumberListener;

    public FormPM(FLyrVect layer, boolean newRegister) {
	super(layer);
	this.layer = layer;
	this.newRegister = newRegister;
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

	HashMap<String, JComponent> widgets = getWidgetComponents();

	editParcelasAfectadasListener = new EditParcelasAfectadasListener();
	calculatePMNumberListener = new CalculatePMNumberListener();

	numeroPM = (JTextField) widgets.get("numero_pm");
	numeroPM.setEnabled(false);

	editParcelasButton = (JButton) getFormBody().getComponentByName("num_parcela_audasa_button");
	editParcelasButton.addActionListener(editParcelasAfectadasListener);

	area = (JComboBox) widgets.get("area");
	area.addActionListener(calculatePMNumberListener);

	fecha = (JTextField) widgets.get("fecha");
	fecha.addKeyListener(calculatePMNumberListener);

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
    protected void enableSaveButton(boolean bool) {
	if (!isChangedValues()) {
	    saveB.setEnabled(false);
	} else {
	    saveB.setEnabled(bool);
	}
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

    @Override
    public void windowClosed() {
	super.windowClosed();
	ToggleEditingManager tem = new ToggleEditingManager();
	if (layer.isEditing()) {
	    tem.stopEditing(layer, false);
	}
    }

    /**
     * This method calculates the PM Number. It depends on the selected values in
     * area (JComboBox) and fecha (JTextField)
     * 
     * PM Number is generated like this: "PM.Year.Area.ID(serial)"
     * Ej: "PM.2012.N.001"
     * 
     * @return PM Number
     */
    private String calculatePMNumber() {
	String area;
	String year = "";
	String currentPmNumber;

	String date;
	date = fecha.getText();
	if (date.contains("/")) {
	    String[] dateArray = date.split("/");
	    if (dateArray.length == 3) {
		year = dateArray[2];
	    }
	}

	String areaSelectedValue = this.area.getSelectedItem().toString();
	if (areaSelectedValue.equalsIgnoreCase("Norte")) {
	    area = "N";
	} else if (areaSelectedValue.equalsIgnoreCase("Sur")) {
	    area = "S";
	} else {
	    area = "";
	}

	String id = "";
	currentPmNumber = numeroPM.getText();
	if (!newRegister && currentPmNumber.contains(".")) {
	    String[] pmNumberArray = currentPmNumber.split("\\.");
	    if (pmNumberArray.length ==4 && !year.equals("") &&
		    !area.equals("")) {
		id = pmNumberArray[3];
		return "PM" + "." + year +"." + area + "." + id;
	    }
	    return currentPmNumber;
	}

	return "PM" + "." + year +"." + area + "." + getID();
    }

    private String getID() {
	try {
	    SelectableDataSource recordset = layer.getRecordset();
	    int columnIndex = recordset.getFieldIndexByName("gid");
	    ArrayList<Integer> pmID = new ArrayList<Integer>();
	    for (int rowIndex=0; rowIndex<recordset.getRowCount(); rowIndex++) {
		String id = recordset.getFieldValue(rowIndex, columnIndex).toString();
		if (!id.equals("")) {
		    pmID.add(Integer.parseInt(id));
		}
	    }
	    Arrays.sort(pmID.toArray(new Integer[] {0}));
	    int biggerPmID = pmID.get(pmID.size()-1);
	    return String.format("%1$03d", biggerPmID+1);
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	}
	return null;
    }

    public class CalculatePMNumberListener implements ActionListener, KeyListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
	    numeroPM.setText(calculatePMNumber());

	}

	@Override
	public void keyPressed(KeyEvent arg0) {
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	    // TODO Auto-generated method stub
	    numeroPM.setText(calculatePMNumber());

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}

    }

    public class EditParcelasAfectadasListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
	    SubFormPMParcelasAfectadas subForm = new SubFormPMParcelasAfectadas();
	    PluginServices.getMDIManager().addWindow(subForm);
	}

    }

}
