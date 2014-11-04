package es.icarto.gvsig.extgia.forms.enlaces;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.extgia.forms.utils.AbstractFormWithLocationWidgets;
import es.icarto.gvsig.extgia.forms.utils.CalculateComponentValue;
import es.icarto.gvsig.extgia.forms.utils.RamalesHandler;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.extgia.utils.SqlUtils;
import es.udc.cartolab.gvsig.users.utils.DBSession;

@SuppressWarnings("serial")
public class EnlacesForm extends AbstractFormWithLocationWidgets {

    public static final String ABEILLE_CARRETERAS_FILENAME = "forms/enlaces_carreteras.xml";
    public static final String TABLENAME = "enlaces";

    JTextField enlaceIDWidget;
    CalculateComponentValue enlaceid;

    JTable carreteras;

    JButton addCarreteraButton;
    JButton editCarreteraButton;
    JButton deleteCarreteraButton;

    AddReconocimientoListener addReconocimientoListener;
    EditReconocimientoListener editReconocimientoListener;
    DeleteReconocimientoListener deleteReconocimientoListener;

    AddCarreteraListener addCarreteraListener;
    EditCarreteraListener editCarreteraListener;
    DeleteCarreteraListener deleteCarreteraListener;

    public EnlacesForm(FLyrVect layer) {
	super(layer);
	addTableHandler(new RamalesHandler(getRamalesDBTableName(),
		getWidgetComponents(), getElementID(),
		DBFieldNames.ramalesDireccionColNames,
		DBFieldNames.ramalesDireccionColAlias, this));
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

	// Embebed Tables
	DBFieldNames
		.setReconocimientoEstadoFields(DBFieldNames.enlacesReconocimientoEstadoFields);
	SqlUtils.createEmbebedTableFromDB(reconocimientoEstado,
		"audasa_extgia", getReconocimientosDBTableName(),
		DBFieldNames.reconocimientoEstadoFields, null, "id_enlace",
		enlaceIDWidget.getText(), "n_inspeccion");

	SqlUtils.createEmbebedTableFromDB(carreteras, DBFieldNames.GIA_SCHEMA,
		"enlaces_carreteras_enlazadas",
		DBFieldNames.carreteras_enlazadas, null, "id_enlace",
		enlaceIDWidget.getText(), "id_carretera_enlazada");

	repaint();
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

	carreteras = (JTable) super.getFormBody().getComponentByName(
		"tabla_carreteras");

	addCarreteraButton = (JButton) super.getFormBody().getComponentByName(
		"add_carretera_button");
	editCarreteraButton = (JButton) super.getFormBody().getComponentByName(
		"edit_carretera_button");
	deleteCarreteraButton = (JButton) super.getFormBody()
		.getComponentByName("delete_carretera_button");

	addReconocimientoListener = new AddReconocimientoListener();
	addReconocimientoButton.addActionListener(addReconocimientoListener);
	addCarreteraListener = new AddCarreteraListener();
	addCarreteraButton.addActionListener(addCarreteraListener);
	editReconocimientoListener = new EditReconocimientoListener();
	editReconocimientoButton.addActionListener(editReconocimientoListener);
	editCarreteraListener = new EditCarreteraListener();
	editCarreteraButton.addActionListener(editCarreteraListener);
	deleteReconocimientoListener = new DeleteReconocimientoListener();
	deleteReconocimientoButton
		.addActionListener(deleteReconocimientoListener);
	deleteCarreteraListener = new DeleteCarreteraListener();
	deleteCarreteraButton.addActionListener(deleteCarreteraListener);
    }

    @Override
    protected void removeListeners() {
	addReconocimientoButton.removeActionListener(addReconocimientoListener);
	editReconocimientoButton
		.removeActionListener(editReconocimientoListener);
	deleteReconocimientoButton
		.removeActionListener(deleteReconocimientoListener);

	addCarreteraButton.removeActionListener(addCarreteraListener);
	editCarreteraButton.removeActionListener(editCarreteraListener);
	deleteCarreteraButton.removeActionListener(deleteCarreteraListener);

	super.removeListeners();

	DBFieldNames
		.setReconocimientoEstadoFields(DBFieldNames.genericReconocimientoEstadoFields);
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

    public class AddReconocimientoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    EnlacesReconocimientosSubForm subForm = new EnlacesReconocimientosSubForm(
		    getReconocimientosFormFileName(),
		    getReconocimientosDBTableName(), reconocimientoEstado,
		    "id_enlace", enlaceIDWidget.getText(), null, null, false);
	    PluginServices.getMDIManager().addWindow(subForm);
	}
    }

    public class AddCarreteraListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    EnlacesCarreterasSubForm subForm = new EnlacesCarreterasSubForm(
		    ABEILLE_CARRETERAS_FILENAME,
		    "enlaces_carreteras_enlazadas", carreteras, "id_enlace",
		    enlaceIDWidget.getText(), null, null, false);
	    PluginServices.getMDIManager().addWindow(subForm);
	}
    }

    public class EditReconocimientoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    if (reconocimientoEstado.getSelectedRowCount() != 0) {
		int row = reconocimientoEstado.getSelectedRow();
		EnlacesReconocimientosSubForm subForm = new EnlacesReconocimientosSubForm(
			getReconocimientosFormFileName(),
			getReconocimientosDBTableName(), reconocimientoEstado,
			"id_enlace", enlaceIDWidget.getText(), "n_inspeccion",
			reconocimientoEstado.getValueAt(row, 0).toString(),
			true);
		PluginServices.getMDIManager().addWindow(subForm);
	    } else {
		JOptionPane.showMessageDialog(null,
			"Debe seleccionar una fila para editar los datos.",
			"Ninguna fila seleccionada",
			JOptionPane.INFORMATION_MESSAGE);
	    }
	}
    }

    public class EditCarreteraListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    if (carreteras.getSelectedRowCount() != 0) {
		int row = carreteras.getSelectedRow();
		EnlacesCarreterasSubForm subForm = new EnlacesCarreterasSubForm(
			ABEILLE_CARRETERAS_FILENAME,
			"enlaces_carreteras_enlazadas", carreteras,
			"id_enlace", enlaceIDWidget.getText(),
			"id_carretera_enlazada", carreteras.getValueAt(row, 0)
				.toString(), true);
		PluginServices.getMDIManager().addWindow(subForm);
	    } else {
		JOptionPane.showMessageDialog(null,
			"Debe seleccionar una fila para editar los datos.",
			"Ninguna fila seleccionada",
			JOptionPane.INFORMATION_MESSAGE);
	    }
	}
    }

    public class DeleteCarreteraListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    deleteElement(carreteras, "enlaces_carreteras_enlazadas",
		    "id_carretera_enlazada");
	}
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
