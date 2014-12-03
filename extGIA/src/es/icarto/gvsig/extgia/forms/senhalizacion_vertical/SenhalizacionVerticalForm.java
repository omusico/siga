package es.icarto.gvsig.extgia.forms.senhalizacion_vertical;

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
public class SenhalizacionVerticalForm extends AbstractFormWithLocationWidgets {

    public static final String TABLENAME = "senhalizacion_vertical";

    JTextField elementoSenhalizacionIDWidget;
    CalculateComponentValue elementoSenhalizacionid;
    private JComboBox tipoVia;
    private DependentComboboxHandler direccionDomainHandler;

    public static String[] senhalesColNames = { "id_senhal_vertical",
	    "tipo_senhal", "codigo_senhal", "leyenda", "fecha_fabricacion",
	    "fecha_reposicion" };

    public static String[] senhalesColAlias = { "ID Señal", "Tipo Señal",
	    "Código Señal", "Leyenda", "Fabricación", "Reposición" };

    public SenhalizacionVerticalForm(FLyrVect layer) {
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
		SenhalizacionVerticalReconocimientosSubForm.class));

	addTableHandler(new GIAAlphanumericTableHandler(
		"senhalizacion_vertical_senhales", getWidgetComponents(),
		getElementID(), senhalesColNames, senhalesColAlias, new int[] {
			20, 45, 45, 180, 40, 40 }, this));
    }

    private void addNewButtonsToActionsToolBar() {
	super.addNewButtonsToActionsToolBar(DBFieldNames.Elements.Senhalizacion_Vertical);
    }

    @Override
    protected void fillSpecificValues() {
	super.fillSpecificValues();

	direccionDomainHandler.updateComboBoxValues();

	if (elementoSenhalizacionIDWidget.getText().isEmpty()) {
	    elementoSenhalizacionid = new SenhalizacionVerticalCalculateIDValue(
		    this, getWidgetComponents(),
		    DBFieldNames.ID_ELEMENTO_SENHALIZACION,
		    DBFieldNames.ID_ELEMENTO_SENHALIZACION);
	    elementoSenhalizacionid.setValue(true);
	}

	if (filesLinkButton == null) {
	    addNewButtonsToActionsToolBar();
	}
    }

    @Override
    protected void setListeners() {
	super.setListeners();
	Map<String, JComponent> widgets = getWidgets();

	elementoSenhalizacionIDWidget = (JTextField) widgets
		.get(DBFieldNames.ID_ELEMENTO_SENHALIZACION);

	JComboBox direccion = (JComboBox) widgets.get("direccion");
	tipoVia = (JComboBox) getWidgets().get("tipo_via");
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
    protected String getBasicName() {
	return TABLENAME;
    }

    @Override
    public String getElement() {
	return DBFieldNames.Elements.Senhalizacion_Vertical.name();
    }

    @Override
    protected boolean hasSentido() {
	return true;
    }

    @Override
    public String getElementID() {
	return "id_elemento_senhalizacion";
    }

    @Override
    public String getElementIDValue() {
	return elementoSenhalizacionIDWidget.getText();
    }

    @Override
    public String getImagesDBTableName() {
	return "senhalizacion_vertical_imagenes";
    }

}
