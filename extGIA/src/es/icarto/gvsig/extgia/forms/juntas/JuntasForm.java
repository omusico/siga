package es.icarto.gvsig.extgia.forms.juntas;

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
public class JuntasForm extends AbstractFormWithLocationWidgets {

    public static final String TABLENAME = "juntas";

    JTextField juntaIDWidget;
    CalculateComponentValue juntaid;
    private JComboBox tipoVia;
    private DependentComboboxHandler direccionDomainHandler;

    public JuntasForm(FLyrVect layer) {
	super(layer);

	// int[] trabajoColumnsSize = { 1, 30, 90, 70, 200 };
	addTableHandler(new GIAAlphanumericTableHandler(
		getTrabajosDBTableName(), getWidgetComponents(),
		getElementID(), DBFieldNames.trabajosColNames,
		DBFieldNames.trabajosColAlias, this));

	addTableHandler(new GIAAlphanumericTableHandler(
		getReconocimientosDBTableName(), getWidgetComponents(),
		getElementID(), DBFieldNames.reconocimientosColNames,
		DBFieldNames.reconocimientosColAlias, this,
		JuntasReconocimientosSubForm.class));
    }

    private void addNewButtonsToActionsToolBar() {
	super.addNewButtonsToActionsToolBar(DBFieldNames.Elements.Juntas);
    }

    @Override
    protected void fillSpecificValues() {
	super.fillSpecificValues();

	direccionDomainHandler.updateComboBoxValues();

	if (juntaIDWidget.getText().isEmpty()) {
	    juntaid = new JuntasCalculateIDValue(this, getWidgetComponents(),
		    DBFieldNames.ID_JUNTA, DBFieldNames.ID_JUNTA);
	    juntaid.setValue(true);
	}

	if (filesLinkButton == null) {
	    addNewButtonsToActionsToolBar();
	}
    }

    @Override
    protected void setListeners() {
	super.setListeners();
	Map<String, JComponent> widgets = getWidgets();

	juntaIDWidget = (JTextField) widgets.get(DBFieldNames.ID_JUNTA);

	JComboBox direccion = (JComboBox) widgets.get("direccion");
	tipoVia = (JComboBox) widgets.get("tipo_via");
	direccionDomainHandler = new DependentComboboxHandler(this, tipoVia,
		direccion);
	tipoVia.addActionListener(direccionDomainHandler);

    }

    @Override
    protected void removeListeners() {
	tipoVia.removeActionListener(direccionDomainHandler);

	super.removeListeners();
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
    public boolean isSpecialCase() {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    protected String getBasicName() {
	return TABLENAME;
    }

    @Override
    public String getElement() {
	return DBFieldNames.Elements.Juntas.name();
    }

    @Override
    protected boolean hasSentido() {
	return true;
    }

    @Override
    public String getElementID() {
	return "id_junta";
    }

    @Override
    public String getElementIDValue() {
	return juntaIDWidget.getText();
    }

    @Override
    public String getImagesDBTableName() {
	return "juntas_imagenes";
    }
}
