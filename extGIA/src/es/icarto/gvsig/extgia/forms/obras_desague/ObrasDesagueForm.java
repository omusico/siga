package es.icarto.gvsig.extgia.forms.obras_desague;

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
public class ObrasDesagueForm extends AbstractFormWithLocationWidgets {

    public static final String TABLENAME = "obras_desague";

    JTextField obraDesagueIDWidget;
    CalculateComponentValue obraDesagueid;
    private JComboBox tipoVia;
    private DependentComboboxHandler direccionDomainHandler;

    public ObrasDesagueForm(FLyrVect layer) {
	super(layer);

	addTableHandler(new GIAAlphanumericTableHandler(
		getTrabajosDBTableName(), getWidgetComponents(),
		getElementID(), DBFieldNames.trabajosColNames,
		DBFieldNames.trabajosColAlias, DBFieldNames.trabajosColWidths,
		this));
    }

    private void addNewButtonsToActionsToolBar() {
	super.addNewButtonsToActionsToolBar(DBFieldNames.Elements.Obras_Desague);
    }

    @Override
    protected void fillSpecificValues() {
	super.fillSpecificValues();

	direccionDomainHandler.updateComboBoxValues();

	if (obraDesagueIDWidget.getText().isEmpty()) {
	    obraDesagueid = new ObrasDesagueCalculateIDValue(this,
		    getWidgetComponents(), DBFieldNames.ID_OBRA_DESAGUE,
		    DBFieldNames.ID_OBRA_DESAGUE);
	    obraDesagueid.setValue(true);
	}

	if (filesLinkButton == null) {
	    addNewButtonsToActionsToolBar();
	}

    }

    @Override
    protected void setListeners() {
	super.setListeners();
	Map<String, JComponent> widgets = getWidgets();

	obraDesagueIDWidget = (JTextField) widgets
		.get(DBFieldNames.ID_OBRA_DESAGUE);

	JComboBox direccion = (JComboBox) widgets.get("direccion");
	tipoVia = (JComboBox) widgets.get("tipo_via");
	direccionDomainHandler = new DependentComboboxHandler(this, tipoVia,
		direccion);
	tipoVia.addActionListener(direccionDomainHandler);
    }

    @Override
    public String getElement() {
	return DBFieldNames.Elements.Obras_Desague.name();
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
	return "id_obra_desague";
    }

    @Override
    public String getElementIDValue() {
	return obraDesagueIDWidget.getText();
    }

    @Override
    public String getImagesDBTableName() {
	return "obras_desague_imagenes";
    }

}
