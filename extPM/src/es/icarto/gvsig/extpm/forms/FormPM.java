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
import javax.swing.JTextField;

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
import es.icarto.gvsig.extpm.utils.managers.ToggleEditingManager;
import es.icarto.gvsig.navtableforms.AbstractForm;
import es.udc.cartolab.gvsig.users.utils.DBSession;

@SuppressWarnings("serial")
public class FormPM extends AbstractForm {

    private FormPanel form;
    private final FLyrVect layer;

    private static ArrayList<String> parcelasAfectadas;

    // WIDGETS
    private JTextField numParcelaCatastro;
    private JTextField poligonoCatastro;
    private JTextField numeroPM;

    NavTableComponentsFilesLinkButton ntFilesLinkButton;
    NavTableComponentsPrintButton ntPrintButton;

    public FormPM(FLyrVect layer, boolean newRegister, int insertedRow) {
	super(layer);
	this.layer = layer;
	initWindow();
	initWidgets();
	parcelasAfectadas = new ArrayList<String>();
    }

    public static ArrayList<String> getParcelasAfectadas() {
	return parcelasAfectadas;
    }

    public static void setParcelasAfectadas (ArrayList<String> parcelasAfectadasByPM) {
	if (parcelasAfectadas == null) {
	    parcelasAfectadas = new ArrayList<String>();
	}
	parcelasAfectadas = parcelasAfectadasByPM;
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
	parcelasAfectadas.clear();
	PreparedStatement statement;
	try {
	    // Parcelas afected by this PM File
	    String parcelasQuery = "SELECT " + Preferences.FINCAS_PM_FIELD_IDFINCA +
		    " FROM " + Preferences.FINCAS_PM_TABLENAME +
		    " WHERE " + Preferences.FINCAS_PM_FIELD_NUMEROPM + " = '" + numeroPM.getText() + "'";
	    statement = DBSession.getCurrentSession().getJavaConnection().prepareStatement(parcelasQuery);
	    statement.execute();
	    ResultSet parcelasRs = statement.getResultSet();
	    while (parcelasRs.next()) {
		parcelasAfectadas.add(parcelasRs.getString(1));
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}

	if (ntFilesLinkButton == null) {
	    addNewButtonsToActionsToolBar();
	}
    }

    @Override
    public boolean saveRecord() {
	PreparedStatement statement;
	String query = null;
	try {
	    for (String idParcela : parcelasAfectadas) {
		query = "INSERT INTO audasa_pm.fincas_pm "
			+ "VALUES (" + "'" + idParcela + "', '" + numeroPM.getText() + "')";

		statement = DBSession.getCurrentSession().getJavaConnection().prepareStatement(query);
		statement.execute();
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return super.saveRecord();
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

    @Override
    public void windowClosed() {
	super.windowClosed();
	ToggleEditingManager tem = new ToggleEditingManager();
	if (layer.isEditing()) {
	    tem.stopEditing(layer, false);
	}
    }
}
