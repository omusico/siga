package es.icarto.gvsig.extgia.forms.enlaces;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.extgia.forms.utils.AbstractFormWithLocationWidgets;
import es.icarto.gvsig.extgia.forms.utils.CalculateComponentValue;
import es.icarto.gvsig.extgia.forms.utils.GIAAlphanumericTableHandler;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.udc.cartolab.gvsig.users.utils.DBSession;

@SuppressWarnings("serial")
public class EnlacesForm extends AbstractFormWithLocationWidgets {

    public static final String TABLENAME = "enlaces";

    JTextField enlaceIDWidget;
    CalculateComponentValue enlaceid;

    public static String[] carreterasColNames = { "id_carretera_enlazada",
	    "clave_carretera", "pk", "titular", "tipo_cruce" };

    public static String[] carreterasColAlias = { "ID Carretera", "Clave",
	    "PK", "Titular", "Tipo Cruce" };

    public EnlacesForm(FLyrVect layer) {
	super(layer);
	addTableHandler(new GIAAlphanumericTableHandler(
		getRamalesDBTableName(), getWidgetComponents(), getElementID(),
		DBFieldNames.ramalesDireccionColNames,
		DBFieldNames.ramalesDireccionColAlias, this));

	addTableHandler(new GIAAlphanumericTableHandler(
		getReconocimientosDBTableName(), getWidgetComponents(),
		getElementID(),
		DBFieldNames.reconocimientosWhitoutIndexColNames,
		DBFieldNames.reconocimientosWhitoutIndexColAlias, this));

	addTableHandler(new GIAAlphanumericTableHandler(
		"enlaces_carreteras_enlazadas", getWidgetComponents(),
		getElementID(), carreterasColNames, carreterasColAlias, this));
    }

    private void addNewButtonsToActionsToolBar() {
	super.addNewButtonsToActionsToolBar(DBFieldNames.Elements.Enlaces);
    }

    @Override
    protected void fillSpecificValues() {
	super.fillSpecificValues();

	if (filesLinkButton == null) {
	    addNewButtonsToActionsToolBar();
	}
    }

    @Override
    protected void setListeners() {
	super.setListeners();
	Map<String, JComponent> widgets = getWidgets();

	enlaceIDWidget = (JTextField) widgets.get(DBFieldNames.ID_ENLACE);

	enlaceid = new EnlacesCalculateIDValue(this, getWidgetComponents(),
		DBFieldNames.ID_ENLACE, DBFieldNames.AREA_MANTENIMIENTO,
		DBFieldNames.BASE_CONTRATISTA, DBFieldNames.TRAMO,
		DBFieldNames.TIPO_VIA, DBFieldNames.MUNICIPIO, DBFieldNames.PK);
	enlaceid.setListeners();
    }

    @Override
    protected boolean validationHasErrors() {
	if (this.getFormController().getValuesChanged()
		.containsKey("id_enlace")) {
	    if (enlaceIDWidget.getText() != "") {
		String query = "SELECT id_enlace FROM audasa_extgia.enlaces "
			+ " WHERE id_enlace = '" + enlaceIDWidget.getText()
			+ "';";
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
	return DBFieldNames.Elements.Enlaces.name();
    }

    @Override
    protected boolean hasSentido() {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public String getElementID() {
	return "id_enlace";
    }

    @Override
    public String getElementIDValue() {
	return enlaceIDWidget.getText();
    }

    @Override
    public String getImagesDBTableName() {
	return "enlaces_imagenes";
    }

}
