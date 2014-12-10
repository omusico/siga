package es.icarto.gvsig.extgia.forms.muros;

import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.extgia.forms.utils.AbstractFormWithLocationWidgets;
import es.icarto.gvsig.extgia.forms.utils.CalculateComponentValue;
import es.icarto.gvsig.extgia.forms.utils.GIAAlphanumericTableHandler;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.listeners.DependentComboboxHandler;

@SuppressWarnings("serial")
public class MurosForm extends AbstractFormWithLocationWidgets {

    public static final String TABLENAME = "muros";

    JTextField murosIDWidget;
    CalculateComponentValue murosid;

    private JComboBox tipoViaPI;
    private JComboBox tipoViaPF;
    private DependentComboboxHandler direccionPIDomainHandler;
    private DependentComboboxHandler direccionPFDomainHandler;

    public MurosForm(FLyrVect layer) {
	super(layer);

	addTableHandler(new GIAAlphanumericTableHandler(
		getTrabajosDBTableName(), getWidgetComponents(),
		getElementID(), DBFieldNames.trabajosColNames,
		DBFieldNames.trabajosColAlias, DBFieldNames.trabajosColWidths,
		this));

	addTableHandler(new GIAAlphanumericTableHandler(
		getReconocimientosDBTableName(), getWidgetComponents(),
		getElementID(), DBFieldNames.reconocimientosColNames,
		DBFieldNames.reconocimientosColAlias, null, this,
		MurosReconocimientosSubForm.class));
    }

    private void addNewButtonsToActionsToolBar() {
	super.addNewButtonsToActionsToolBar(DBFieldNames.Elements.Muros);
    }

    @Override
    protected void fillSpecificValues() {
	super.fillSpecificValues();

	direccionPIDomainHandler.updateComboBoxValues();
	direccionPFDomainHandler.updateComboBoxValues();

	if (murosIDWidget.getText().isEmpty()) {
	    murosid = new MurosCalculateIDValue(this, getWidgetComponents(),
		    getElementID(), getElementID());
	    murosid.setValue(true);
	}

	if (filesLinkButton == null) {
	    addNewButtonsToActionsToolBar();
	}
    }

    @Override
    protected void setListeners() {
	super.setListeners();
	Map<String, JComponent> widgets = getWidgets();

	murosIDWidget = (JTextField) widgets.get(getElementID());

	JComboBox direccionPI = (JComboBox) widgets.get("direccion_pi");
	tipoViaPI = (JComboBox) widgets.get("tipo_via");
	direccionPIDomainHandler = new DependentComboboxHandler(this,
		tipoViaPI, direccionPI);
	tipoViaPI.addActionListener(direccionPIDomainHandler);

	JComboBox direccionPF = (JComboBox) widgets.get("direccion_pf");
	tipoViaPF = (JComboBox) widgets.get("tipo_via_pf");
	direccionPFDomainHandler = new DependentComboboxHandler(this,
		tipoViaPF, direccionPF);
	tipoViaPF.addActionListener(direccionPFDomainHandler);
    }

    @Override
    protected void removeListeners() {
	tipoViaPI.removeActionListener(direccionPIDomainHandler);
	tipoViaPF.removeActionListener(direccionPFDomainHandler);

	super.removeListeners();
    }

    @Override
    public String getElement() {
	return DBFieldNames.Elements.Muros.name();
    }

    @Override
    public String getElementID() {
	return "id_muro";
    }

    @Override
    public String getElementIDValue() {
	return murosIDWidget.getText();
    }

    @Override
    public JTable getReconocimientosJTable() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public JTable getTrabajosJTable() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String getImagesDBTableName() {
	return "muros_imagenes";
    }

    @Override
    protected String getBasicName() {
	return TABLENAME;
    }

    @Override
    protected boolean hasSentido() {
	return true;
    }

}
