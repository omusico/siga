package es.icarto.gvsig.extgia;

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
	// enableComponentIfCheckBoxIsSelected("hay_anali", "resultado");
    }

    // private void enableComponentIfCheckBoxIsSelected(String chbName,
    // String cmpName) {
    //
    // if (chb.isSelected()) {
    // cmp.setEnabled(true);
    // } else {
    // cmp.setEnabled(false);
    // }
    // }

    @Override
    protected void setListeners() {
	super.setListeners();
	taludid = new CalculateWidgetValue(this, DBFieldNames.ID_TALUD,
		DBFieldNames.TIPO_TALUD, DBFieldNames.NUMERO_TALUD,
		DBFieldNames.BASE_CONTRATISTA);
	taludid.setListeners();
    }

    @Override
    protected void removeListeners() {
	taludid.removeListeners();
	super.removeListeners();
    }

    // public class ComponentEnablerListener implements ActionListener {
    //
    // public void actionPerformed(ActionEvent e) {
    // enableComponentIfCheckBoxIsSelected("hay_anali", "resultado");
    // }
    //
    // }

}
