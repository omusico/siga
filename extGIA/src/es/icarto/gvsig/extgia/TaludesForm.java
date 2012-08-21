package es.icarto.gvsig.extgia;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.extgia.preferences.Preferences;
import es.icarto.gvsig.navtableforms.AbstractForm;

@SuppressWarnings("serial")
public class TaludesForm extends AbstractForm {

    public static final String ABEILLE_FILENAME = "forms/taludes.xml";
    private FormPanel form;
    JComboBox tipoTaludWidget;
    JTextField numeroTaludWidget;
    JComboBox baseContratistaWidget;
    JTextField taludIDWidget;
    CalculateWidgetValue taludid;
    private EnableComponentBasedOnCheckBox cunetaPie;
    private EnableComponentBasedOnCheckBox cunetaCabeza;

    public TaludesForm(FLyrVect layer) {
	super(layer);
	initWindow();
    }

    private void initWindow() {
	this.viewInfo.setHeight(625);
	this.viewInfo.setWidth(800);
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

    }

    @Override
    protected void setListeners() {
	super.setListeners();
	taludid = new CalculateWidgetValue(this, DBFieldNames.ID_TALUD,
		DBFieldNames.TIPO_TALUD, DBFieldNames.NUMERO_TALUD,
		DBFieldNames.BASE_CONTRATISTA);
	taludid.setListeners();

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

    }

    @Override
    protected void removeListeners() {
	taludid.removeListeners();
	cunetaCabeza.removeListeners();
	cunetaPie.removeListeners();
	super.removeListeners();
    }

}
