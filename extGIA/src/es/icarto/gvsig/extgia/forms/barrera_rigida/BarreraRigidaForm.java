package es.icarto.gvsig.extgia.forms.barrera_rigida;

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
public class BarreraRigidaForm extends AbstractFormWithLocationWidgets {

    public static final String TABLENAME = "barrera_rigida";

    JTextField barreraRigidaIDWidget;
    CalculateComponentValue barreraRigidaid;
    CalculateComponentValue barreraRigidaCodigo;
    private JComboBox tipoVia;
    private DependentComboboxHandler direccionDomainHandler;

    public BarreraRigidaForm(FLyrVect layer) {
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
		BarreraRigidaReconocimientosSubForm.class));
    }

    private void addNewButtonsToActionsToolBar() {
	super.addNewButtonsToActionsToolBar(DBFieldNames.Elements.Barrera_Rigida);
    }

    @Override
    protected void fillSpecificValues() {
	super.fillSpecificValues();

	direccionDomainHandler.updateComboBoxValues();

	if (filesLinkButton == null) {
	    addNewButtonsToActionsToolBar();
	}
    }

    @Override
    protected void setListeners() {
	super.setListeners();
	Map<String, JComponent> widgets = getWidgets();

	barreraRigidaIDWidget = (JTextField) widgets
		.get(DBFieldNames.ID_BARRERA_RIGIDA);

	barreraRigidaid = new BarreraRigidaCalculateIDValue(this,
		getWidgetComponents(), DBFieldNames.ID_BARRERA_RIGIDA,
		DBFieldNames.NUMERO_BARRERA_RIGIDA,
		DBFieldNames.BASE_CONTRATISTA);
	barreraRigidaid.setListeners();

	barreraRigidaCodigo = new BarreraRigidaCalculateCodigo(this,
		getWidgetComponents(), DBFieldNames.CODIGO, DBFieldNames.TIPO,
		DBFieldNames.METODO_CONSTRUCTIVO, DBFieldNames.PERFIL);
	barreraRigidaCodigo.setListeners();

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
		.containsKey("id_barrera_rigida")) {
	    if (barreraRigidaIDWidget.getText() != "") {
		String query = "SELECT id_barrera_rigida FROM audasa_extgia.barrera_rigida "
			+ " WHERE id_barrera_rigida = '"
			+ barreraRigidaIDWidget.getText() + "';";
		PreparedStatement statement = null;
		Connection connection = DBSession.getCurrentSession()
			.getJavaConnection();
		try {
		    statement = connection.prepareStatement(query);
		    statement.execute();
		    ResultSet rs = statement.getResultSet();
		    if (rs.next()) {
			JOptionPane.showMessageDialog(null,
				"El ID est� en uso, por favor, escoja otro.",
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
	return DBFieldNames.Elements.Barrera_Rigida.name();
    }

    @Override
    protected boolean hasSentido() {
	return true;
    }

    @Override
    public String getElementID() {
	return "id_barrera_rigida";
    }

    @Override
    public String getElementIDValue() {
	return barreraRigidaIDWidget.getText();
    }

    @Override
    public String getImagesDBTableName() {
	return "barrera_rigida_imagenes";
    }

}
