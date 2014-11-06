package es.icarto.gvsig.extgia.forms.transformadores;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.extgia.forms.utils.AbstractFormWithLocationWidgets;
import es.icarto.gvsig.extgia.forms.utils.CalculateComponentValue;
import es.icarto.gvsig.extgia.forms.utils.GIAAlphanumericTableHandler;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.listeners.DependentComboboxHandler;
import es.udc.cartolab.gvsig.users.utils.DBSession;

@SuppressWarnings("serial")
public class TransformadoresForm extends AbstractFormWithLocationWidgets {

    public static final String TABLENAME = "transformadores";

    JTextField transformadorIDWidget;
    CalculateComponentValue transformadorid;
    private JComboBox tipoVia;
    private DependentComboboxHandler direccionDomainHandler;

    public TransformadoresForm(FLyrVect layer) {
	super(layer);

	// int[] trabajoColumnsSize = { 1, 30, 90, 70, 200 };
	addTableHandler(new GIAAlphanumericTableHandler(
		getTrabajosDBTableName(), getWidgetComponents(),
		getElementID(), DBFieldNames.trabajosColNames,
		DBFieldNames.trabajosColAlias, this));

	addTableHandler(new GIAAlphanumericTableHandler(
		getReconocimientosDBTableName(), getWidgetComponents(),
		getElementID(),
		DBFieldNames.reconocimientosWhitoutIndexColNames,
		DBFieldNames.reconocimientosWhitoutIndexColAlias, this));
    }

    private void addNewButtonsToActionsToolBar() {
	super.addNewButtonsToActionsToolBar(DBFieldNames.Elements.Transformadores);
    }

    @Override
    protected void fillSpecificValues() {
	super.fillSpecificValues();

	direccionDomainHandler.updateComboBoxValues();

	if (transformadorIDWidget.getText().isEmpty()) {
	    transformadorid = new TransformadoresCalculateIDValue(this,
		    getWidgetComponents(), getElementID(), getElementID());
	    transformadorid.setValue(true);
	}

	if (filesLinkButton == null) {
	    addNewButtonsToActionsToolBar();
	}
    }

    @Override
    protected void setListeners() {
	super.setListeners();
	Map<String, JComponent> widgets = getWidgets();

	transformadorIDWidget = (JTextField) widgets
		.get(DBFieldNames.ID_TRANSFORMADORES);

	transformadorid = new TransformadoresCalculateIDValue(this,
		getWidgetComponents(), getElementID(), getElementID());
	transformadorid.setListeners();

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
    protected boolean validationHasErrors() {
	if (this.getFormController().getValuesChanged()
		.containsKey("id_transformador")) {
	    if (transformadorIDWidget.getText() != "") {
		String query = "SELECT id_transformador FROM audasa_extgia.transformadores "
			+ " WHERE id_transformador = '"
			+ transformadorIDWidget.getText() + "';";
		PreparedStatement statement = null;
		Connection connection = DBSession.getCurrentSession()
			.getJavaConnection();
		try {
		    statement = connection.prepareStatement(query);
		    statement.execute();
		    ResultSet rs = statement.getResultSet();
		    if (rs.next()) {
			JOptionPane.showMessageDialog(null,
				"El ID está en uso, por favor, escoja otro.",
				"ID en uso", JOptionPane.WARNING_MESSAGE);
			return true;
		    }
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	    }
	}
	return super.validationHasErrors();
    }

    @Override
    public String getElement() {
	return DBFieldNames.Elements.Transformadores.name();
    }

    @Override
    public String getElementID() {
	return "id_transformador";
    }

    @Override
    public String getElementIDValue() {
	return transformadorIDWidget.getText();
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
	return "transformadores_imagenes";
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
