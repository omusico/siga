package es.icarto.gvsig.extgia;

import java.io.File;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.iver.andami.Launcher;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.extgia.preferences.Preferences;
import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.fileslink.FilesLink;
import es.icarto.gvsig.navtableforms.validation.listeners.DependentComboboxesHandler;

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

    public TaludesForm(FLyrVect layer) {
	super(layer);
	initWindow();

	String baseDirectory = null;
	baseDirectory = Launcher.getAppHomeDir() + File.separator
		+ "Inventario" + File.separator + "FILES" + File.separator
		+ layer.getName();

	new FilesLink(this, baseDirectory);
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

    }

    @Override
    protected void setListeners() {
	super.setListeners();
	taludid = new CalculateTaludIDValue(this, DBFieldNames.ID_TALUD,
		DBFieldNames.TIPO_TALUD, DBFieldNames.NUMERO_TALUD,
		DBFieldNames.BASE_CONTRATISTA);
	taludid.setListeners();

	inclinacionMedia = new CalculateInclinacionMediaValue(this,
		DBFieldNames.INCLINACION_MEDIA, DBFieldNames.SECTOR_INCLINACION);
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
    }

    @Override
    protected void removeListeners() {
	taludid.removeListeners();
	inclinacionMedia.removeListeners();
	cunetaCabeza.removeListeners();
	cunetaPie.removeListeners();
	tipoViaPI.removeActionListener(direccionPIDomainHandler);
	tipoViaPF.removeActionListener(direccionPFDomainHandler);
	super.removeListeners();
    }

}
