package es.icarto.gvsig.extpm.forms;

import java.awt.Color;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.audasacommons.forms.reports.NavTableComponentsPrintButton;
import es.icarto.gvsig.commons.gui.WidgetFactory;
import es.icarto.gvsig.extgex.forms.reversions.OpenWebAction;
import es.icarto.gvsig.extpm.forms.filesLink.NavTableComponentsFilesLinkButton;
import es.icarto.gvsig.extpm.preferences.Preferences;
import es.icarto.gvsig.navtableforms.BasicAbstractForm;
import es.udc.cartolab.gvsig.users.utils.DBSession;

@SuppressWarnings("serial")
public class FormPM extends BasicAbstractForm {

    
    // WIDGETS

    private JTextField numeroPM;
    private JTable fincasAfectadasTable;
    private JButton printReportB;
    String extensionPath;
    URL reportPath;
    boolean firstOpen = true;

    NavTableComponentsFilesLinkButton ntFilesLinkButton;
    NavTableComponentsPrintButton ntPrintButton;
    private JButton openWebBt;

    public FormPM(FLyrVect layer, boolean newRegister, int insertedRow) {
	super(layer);
    }

    private void addNewButtonsToActionsToolBar() {
	reportPath = this.getClass().getClassLoader()
		.getResource("reports/pm_report.jasper");
	extensionPath = reportPath.getPath().replace(
		"reports/pm_report.jasper", "");
	JPanel actionsToolBar = this.getActionsToolBar();
	ntFilesLinkButton = new NavTableComponentsFilesLinkButton();
	ntPrintButton = new NavTableComponentsPrintButton();
	JButton filesLinkB = ntFilesLinkButton.getFilesLinkButton(layer, this);

	printReportB = ntPrintButton.getPrintButton(this, extensionPath,
		reportPath.getPath(), Preferences.PM_TABLENAME,
		Preferences.PM_FIELD_NUMEROPM, numeroPM.getText());
	printReportB.setName("printButton");

	if (openWebBt == null) {
	    openWebBt = new JButton(new OpenWebAction(this, "pm"));
	    actionsToolBar.add(openWebBt);
	}

	if (printReportB != null) {
	    for (int i = 0; i < this.getActionsToolBar().getComponents().length; i++) {
		if (getActionsToolBar().getComponents()[i].getName() != null) {
		    if (getActionsToolBar().getComponents()[i].getName()
			    .equalsIgnoreCase("printButton")) {
			this.getActionsToolBar().remove(
				getActionsToolBar().getComponents()[i]);
			actionsToolBar.add(printReportB);
			break;
		    }
		}
	    }
	    actionsToolBar.add(printReportB);
	}

	if (filesLinkB != null && ntFilesLinkButton == null) {
	    actionsToolBar.add(filesLinkB);
	}

	this.getActionsToolBar().remove(saveB);
	this.getActionsToolBar().remove(removeB);
	this.getActionsToolBar().remove(undoB);
	this.getActionsToolBar().remove(copyPreviousB);
	this.getActionsToolBar().remove(copySelectedB);
    }

    @Override
    protected void setListeners() {
	super.setListeners();

	Map<String, JComponent> widgets = getWidgets();

	numeroPM = (JTextField) widgets.get(PM_FIELD_NUMEROPM);

	fincasAfectadasTable = (JTable) formBody
		.getComponentByName("parcelas_afectadas_table");
    }

    @Override
    protected void fillSpecificValues() {
	super.fillSpecificValues();
	createFincasAfectadasTable();

	for (String finca : getFincasAfectadas()) {
	    String[] fincaValues = getFincaValuesFromID(finca);
	    ((DefaultTableModel) fincasAfectadasTable.getModel())
		    .addRow(fincaValues);
	}
	for (JComponent c : getWidgets().values()) {
	    WidgetFactory.disableComponent(c);
	}
	addNewButtonsToActionsToolBar();
    }

    private String[] getFincaValuesFromID(String idFinca) {
	PreparedStatement statement;
	String[] fincaValues = new String[1];
	try {
	    String query = "SELECT " + Preferences.FINCAS_FIELD_IDFINCA
		    + " FROM " + Preferences.FINCAS_TABLENAME + " a, "
		    + Preferences.TRAMOS_TABLENAME + " b " + " WHERE "
		    + Preferences.FINCAS_FIELD_TRAMO + " = "
		    + Preferences.TRAMOS_FIELD_ID + " AND "
		    + Preferences.FINCAS_FIELD_IDFINCA + "=" + "'" + idFinca
		    + "';";
	    statement = DBSession.getCurrentSession().getJavaConnection()
		    .prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();

	    while (rs.next()) {
		fincaValues[0] = rs.getString(1);
	    }
	    rs.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return fincaValues;
    }

    private void createFincasAfectadasTable() {
	DefaultTableModel model = new DefaultTableModel();
	model.addColumn("ID Finca");

	fincasAfectadasTable.setTableHeader(null);
	fincasAfectadasTable.setModel(model);
	fincasAfectadasTable.setEnabled(false);
    }

    private ArrayList<String> getFincasAfectadas() {
	ArrayList<String> fincasAfectadas = new ArrayList<String>();
	PreparedStatement statement;
	try {
	    String fincasQuery = "SELECT "
		    + Preferences.FINCAS_PM_FIELD_IDFINCA + " FROM "
		    + Preferences.FINCAS_PM_TABLENAME + " WHERE "
		    + Preferences.FINCAS_PM_FIELD_NUMEROPM + " = '"
		    + numeroPM.getText() + "'";
	    statement = DBSession.getCurrentSession().getJavaConnection()
		    .prepareStatement(fincasQuery);
	    statement.execute();
	    ResultSet fincasRs = statement.getResultSet();
	    while (fincasRs.next()) {
		fincasAfectadas.add(fincasRs.getString(1));
	    }
	    return fincasAfectadas;
	} catch (SQLException e) {
	    e.printStackTrace();
	    return new ArrayList<String>();
	}
    }

    @Override
    // As this is a non editable form we should never show the warning
    protected boolean showWarning() {
	return true;
    }

    @Override
    protected String getBasicName() {
	return "exp_pm";
    }

    @Override
    protected String getSchema() {
	return Preferences.PM_SCHEMA;
    }
}
