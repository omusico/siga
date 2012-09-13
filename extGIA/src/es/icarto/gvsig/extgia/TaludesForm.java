package es.icarto.gvsig.extgia;

import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.iver.andami.Launcher;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.audasacommons.PreferencesPage;
import es.icarto.gvsig.extgia.forms.reports.NavTableComponentsPrintButton;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.extgia.preferences.Preferences;
import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.fileslink.FilesLink;
import es.icarto.gvsig.navtableforms.validation.listeners.DependentComboboxesHandler;
import es.udc.cartolab.gvsig.navtable.listeners.PositionEvent;

@SuppressWarnings("serial")
public class TaludesForm extends AbstractForm {

    public static final String ABEILLE_FILENAME = "forms/taludes.xml";
    private FormPanel form;
    JComboBox tipoTaludWidget;
    JTextField numeroTaludWidget;
    JComboBox baseContratistaWidget;
    JTextField taludIDWidget;
    CalculateComponentValue taludid;
    private CalculateComponentValue inclinacionMedia;
    private EnableComponentBasedOnCheckBox cunetaPie;
    private EnableComponentBasedOnCheckBox cunetaCabeza;
    private JComboBox tipoViaPI;
    private JComboBox tipoViaPF;
    private DependentComboboxesHandler direccionPIDomainHandler;
    private DependentComboboxesHandler direccionPFDomainHandler;

    private ReconocimientoEstadoTaludesTable reconocimientoEstadoTaludesTable;
    private TrabajosTaludesTable trabajosTaludesTable;

    public TaludesForm(FLyrVect layer) {
	super(layer);
	initWindow();

	String baseDirectory = null;
	try {
	    baseDirectory = PreferencesPage.getBaseDirectory();
	} catch (Exception e) {
	}

	if (baseDirectory == null || baseDirectory.isEmpty()) {
	    baseDirectory = Launcher.getAppHomeDir();
	}

	baseDirectory = baseDirectory + File.separator + "FILES"
		+ File.separator + "inventario" + File.separator
		+ layer.getName();

	new FilesLink(this, baseDirectory);

	addNewButtonsToActionsToolBar();
    }

    private void addNewButtonsToActionsToolBar() {
	JPanel actionsToolBar = this.getActionsToolBar();
	NavTableComponentsPrintButton ntPrintButton = new NavTableComponentsPrintButton();
	JButton printReportB = ntPrintButton.getPrintButton(this);
	if (printReportB != null) {
	    actionsToolBar.add(printReportB);
	}
    }

    private void initWindow() {
	this.viewInfo.setHeight(625);
	this.viewInfo.setWidth(775);
	this.viewInfo.setTitle("Taludes");
    }

    @Override
    public FormPanel getFormBody() {
	if (this.form == null) {
	    this.form = new FormPanel(TaludesForm.ABEILLE_FILENAME);
	}
	return this.form;
    }

    @Override
    public String getXMLPath() {
	return Preferences.getPreferences().getXMLFilePath();
    }

    @Override
    public Logger getLoggerName() {
	return Logger.getLogger(this.getClass().getName());
    }

    @Override
    protected void fillSpecificValues() {
	cunetaCabeza.fillSpecificValues();
	cunetaPie.fillSpecificValues();
	direccionPIDomainHandler.updateComboBoxValues();
	direccionPFDomainHandler.updateComboBoxValues();
	reconocimientoEstadoTaludesTable.updateTable(getFormController()
		.getValue("id_talud"));
	trabajosTaludesTable.updateTable(getFormController().getValue(
		"id_talud"));
    }

    @Override
    protected void setListeners() {
	super.setListeners();
	taludid = new CalculateTaludIDValue(controller, getWidgetsVector(),
		DBFieldNames.ID_TALUD, DBFieldNames.TIPO_TALUD,
		DBFieldNames.NUMERO_TALUD, DBFieldNames.BASE_CONTRATISTA);
	taludid.setListeners();

	inclinacionMedia = new CalculateInclinacionMediaValue(controller,
		getWidgetsVector(), DBFieldNames.INCLINACION_MEDIA,
		DBFieldNames.SECTOR_INCLINACION);
	inclinacionMedia.setListeners();

	cunetaCabeza = new EnableComponentBasedOnCheckBox(
		(JCheckBox) getWidgetComponents().get("cuneta_cabeza"),
		getWidgetComponents().get("cuneta_cabeza_revestida"));
	cunetaCabeza.setRemoveDependentValues(true);
	cunetaCabeza.setListeners();
	cunetaPie = new EnableComponentBasedOnCheckBox(
		(JCheckBox) getWidgetComponents().get("cuneta_pie"),
		getWidgetComponents().get("cuneta_pie_revestida"));
	cunetaPie.setRemoveDependentValues(true);
	cunetaPie.setListeners();

	JComboBox direccionPI = (JComboBox) getWidgetComponents().get(
		"direccion_pi");
	tipoViaPI = (JComboBox) getWidgetComponents().get("tipo_via_pi");
	direccionPIDomainHandler = new DependentComboboxesHandler(this,
		tipoViaPI, direccionPI);
	tipoViaPI.addActionListener(direccionPIDomainHandler);

	JComboBox direccionPF = (JComboBox) getWidgetComponents().get(
		"direccion_pf");
	tipoViaPF = (JComboBox) getWidgetComponents().get("tipo_via_pf");
	direccionPFDomainHandler = new DependentComboboxesHandler(this,
		tipoViaPF, direccionPF);
	tipoViaPF.addActionListener(direccionPFDomainHandler);

	reconocimientoEstadoTaludesTable = new ReconocimientoEstadoTaludesTable(
		(JTable) this.getWidgetComponents().get(
			"taludes_reconocimiento_estado"), this);
	reconocimientoEstadoTaludesTable.setListeners();

	trabajosTaludesTable = new TrabajosTaludesTable((JTable) this
		.getWidgetComponents().get("taludes_trabajos"), this);
	trabajosTaludesTable.setListeners();

    }

    @Override
    protected void removeListeners() {
	taludid.removeListeners();
	inclinacionMedia.removeListeners();
	cunetaCabeza.removeListeners();
	cunetaPie.removeListeners();
	tipoViaPI.removeActionListener(direccionPIDomainHandler);
	tipoViaPF.removeActionListener(direccionPFDomainHandler);
	reconocimientoEstadoTaludesTable.removeListeners();
	trabajosTaludesTable.removeListeners();
	super.removeListeners();
    }

    @Override
    public void onPositionChange(PositionEvent e) {
	super.onPositionChange(e);
	reconocimientoEstadoTaludesTable.updateTable(getFormController()
		.getValue("id_talud"));
	trabajosTaludesTable.updateTable(getFormController().getValue(
		"id_talud"));

	// It should be better to repaint only the table but it don't seem to
	// work. More research is needed
    }

}
