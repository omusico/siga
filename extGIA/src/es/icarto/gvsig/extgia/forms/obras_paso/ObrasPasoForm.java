package es.icarto.gvsig.extgia.forms.obras_paso;

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
public class ObrasPasoForm extends AbstractFormWithLocationWidgets {

    public static final String TABLENAME = "obras_paso";

    JTextField obraPasoIDWidget;
    CalculateComponentValue obraPasoid;
    private JComboBox tipoVia;
    private DependentComboboxHandler direccionDomainHandler;

    public ObrasPasoForm(FLyrVect layer) {
	super(layer);

	addTableHandler(new GIAAlphanumericTableHandler(
		getTrabajosDBTableName(), getWidgetComponents(),
		getElementID(), DBFieldNames.trabajosColNames,
		DBFieldNames.trabajosColAlias, DBFieldNames.trabajosColWidths,
		this));
    }

    private void addNewButtonsToActionsToolBar() {
	super.addNewButtonsToActionsToolBar(DBFieldNames.Elements.Obras_Paso);
    }

    @Override
    protected void fillSpecificValues() {
	super.fillSpecificValues();

	direccionDomainHandler.updateComboBoxValues();

	if (obraPasoIDWidget.getText().isEmpty()) {
	    obraPasoid = new ObrasPasoCalculateIDValue(this,
		    getWidgetComponents(), DBFieldNames.ID_OBRA_PASO,
		    DBFieldNames.ID_OBRA_PASO);
	    obraPasoid.setValue(true);
	}

	if (filesLinkButton == null) {
	    addNewButtonsToActionsToolBar();
	}

	repaint();
    }

    @Override
    protected void setListeners() {
	super.setListeners();
	Map<String, JComponent> widgets = getWidgets();

	obraPasoIDWidget = (JTextField) widgets.get(DBFieldNames.ID_OBRA_PASO);

	JComboBox direccion = (JComboBox) getWidgets().get("direccion");
	tipoVia = (JComboBox) getWidgets().get("tipo_via");
	direccionDomainHandler = new DependentComboboxHandler(this, tipoVia,
		direccion);
	tipoVia.addActionListener(direccionDomainHandler);
    }

    @Override
    public String getElement() {
	return DBFieldNames.Elements.Obras_Paso.name();
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
    protected boolean hasSentido() {
	return true;
    }

    @Override
    public String getElementID() {
	return "id_obra_paso";
    }

    @Override
    public String getElementIDValue() {
	return obraPasoIDWidget.getText();
    }

    @Override
    public String getImagesDBTableName() {
	return "obras_paso_imagenes";
    }

}
