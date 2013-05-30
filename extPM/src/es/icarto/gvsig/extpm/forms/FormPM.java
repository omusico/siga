package es.icarto.gvsig.extpm.forms;

import java.io.InputStream;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.image.ImageComponent;
import com.jeta.forms.components.panel.FormPanel;
import com.jeta.forms.gui.common.FormException;

import es.icarto.gvsig.audasacommons.PreferencesPage;
import es.icarto.gvsig.audasacommons.forms.reports.NavTableComponentsPrintButton;
import es.icarto.gvsig.extpm.forms.filesLink.NavTableComponentsFilesLinkButton;
import es.icarto.gvsig.extpm.preferences.Preferences;
import es.icarto.gvsig.navtableforms.AbstractForm;
import es.udc.cartolab.gvsig.users.utils.DBSession;

@SuppressWarnings("serial")
public class FormPM extends AbstractForm {

    private FormPanel form;
    private final FLyrVect layer;

    // WIDGETS
    private JTextField numParcelaCatastro;
    private JTextField poligonoCatastro;
    private JTextField numeroPM;
    private JTable fincasAfectadasTable;

    NavTableComponentsFilesLinkButton ntFilesLinkButton;
    NavTableComponentsPrintButton ntPrintButton;

    public FormPM(FLyrVect layer, boolean newRegister, int insertedRow) {
	super(layer);
	this.layer = layer;
	initWindow();
	initWidgets();
    }

    private void addNewButtonsToActionsToolBar() {
	URL reportPath = this.getClass().getClassLoader()
		.getResource("reports/pm_report.jasper");
	String extensionPath = reportPath.getPath().replace("reports/pm_report.jasper", "");
	JPanel actionsToolBar = this.getActionsToolBar();
	ntFilesLinkButton = new NavTableComponentsFilesLinkButton();
	ntPrintButton = new NavTableComponentsPrintButton();
	JButton filesLinkB = ntFilesLinkButton.getFilesLinkButton(layer,
		this);
	JButton printReportB = null;
	if (!layer.isEditing()) {
	    printReportB = ntPrintButton.getPrintButton(this, extensionPath, reportPath.getPath(),
		    Preferences.PM_TABLENAME, Preferences.PM_FIELD_NUMEROPM, numeroPM.getText());
	}
	if (filesLinkB != null && printReportB != null) {
	    actionsToolBar.add(filesLinkB);
	    actionsToolBar.add(printReportB);
	}

	this.getActionsToolBar().remove(saveB);
    }

    @Override
    protected void setListeners() {
	super.setListeners();

	HashMap<String, JComponent> widgets = getWidgetComponents();

	ImageComponent image = (ImageComponent) form.getComponentByName("image");
	ImageIcon icon = new ImageIcon (PreferencesPage.AUDASA_ICON);
	image.setIcon(icon);

	numeroPM = (JTextField) widgets.get(Preferences.PM_FIELD_NUMEROPM);

	numParcelaCatastro = (JTextField) widgets.get(Preferences.PM_FORM_WIDGETS_NUM_PARCELA_CATASTRO);
	numParcelaCatastro.setToolTipText("Si hay varias parcelas separar con guión (-)");
	poligonoCatastro = (JTextField) widgets.get(Preferences.PM_FORM_WIDGETS_POLIGONO_CATASTRO);
	poligonoCatastro.setToolTipText("Si hay varios polígonos separar con guión (-)");

	fincasAfectadasTable = (JTable)form.getComponentByName("parcelas_afectadas_table");
    }

    @Override
    protected void removeListeners() {
	super.removeListeners();
    }

    @Override
    public String getXMLPath() {
	return PluginServices.getPluginServices("es.icarto.gvsig.extpm")
		.getClassLoader()
		.getResource(Preferences.XML_ORMLITE_RELATIVE_PATH).getPath();
    }

    private void initWindow() {
	//	viewInfo.setHeight(700);
	//	viewInfo.setWidth(690);
	viewInfo.setTitle(Preferences.PM_FORM_TITLE);
    }

    @Override
    protected void fillSpecificValues() {
	createFincasAfectadasTable();

	for (String finca : getFincasAfectadas()) {
	    String[] fincaValues = getFincaValuesFromID(finca);
	    ((DefaultTableModel) fincasAfectadasTable.getModel()).addRow(fincaValues);
	}

	if (ntFilesLinkButton == null) {
	    addNewButtonsToActionsToolBar();
	}

	repaint();
    }

    private String[] getFincaValuesFromID(String idFinca) {
	PreparedStatement statement;
	String[] fincaValues = new String[2];
	try {
	    String query = "SELECT " + Preferences.TRAMOS_FIELD_NOMBRE + ", "
		    + Preferences.FINCAS_FIELD_IDFINCA +
		    " FROM " + Preferences.FINCAS_TABLENAME + " a, " +
		    Preferences.TRAMOS_TABLENAME + " b " +
		    " WHERE " + Preferences.FINCAS_FIELD_TRAMO + " = " + Preferences.TRAMOS_FIELD_ID +
		    " AND " + Preferences.FINCAS_FIELD_IDFINCA + "=" + "'" + idFinca + "';";
	    statement = DBSession.getCurrentSession().getJavaConnection().prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();

	    while (rs.next()) {
		fincaValues[0] = rs.getString(1);
		fincaValues[1] = rs.getString(2);
	    }
	    rs.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return fincaValues;
    }

    private void createFincasAfectadasTable() {
	ArrayList<String> columnNames = new ArrayList<String>();
	columnNames.add("Tramo");
	columnNames.add("ID Finca");
	DefaultTableModel model = new DefaultTableModel();
	for (String columnName : columnNames) {
	    model.addColumn(columnName);
	}
	fincasAfectadasTable.setModel(model);
	fincasAfectadasTable.setEnabled(false);
    }

    private ArrayList<String> getFincasAfectadas() {
	ArrayList<String> fincasAfectadas = new ArrayList<String>();
	PreparedStatement statement;
	try {
	    String fincasQuery = "SELECT " + Preferences.FINCAS_PM_FIELD_IDFINCA +
		    " FROM " + Preferences.FINCAS_PM_TABLENAME +
		    " WHERE " + Preferences.FINCAS_PM_FIELD_NUMEROPM + " = '" + numeroPM.getText() + "'";
	    statement = DBSession.getCurrentSession().getJavaConnection().prepareStatement(fincasQuery);
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
    public FormPanel getFormBody() {
	if (form == null) {
	    InputStream stream = getClass().getClassLoader().getResourceAsStream(Preferences.PM_FORM_FILE);
	    FormPanel result = null;
	    try {
		result = new FormPanel(stream);
	    } catch (FormException e) {
		e.printStackTrace();
	    }
	    form = result;
	}
	return form;
    }

    @Override
    public Logger getLoggerName() {
	// TODO Auto-generated method stub
	return null;
    }

}
