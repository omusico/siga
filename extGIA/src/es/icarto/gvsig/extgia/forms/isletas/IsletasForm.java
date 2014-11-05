package es.icarto.gvsig.extgia.forms.isletas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.extgia.forms.utils.AbstractFormWithLocationWidgets;
import es.icarto.gvsig.extgia.forms.utils.CalculateComponentValue;
import es.icarto.gvsig.extgia.forms.utils.GIAAlphanumericTableHandler;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.extgia.utils.SqlUtils;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.listeners.DependentComboboxHandler;
import es.udc.cartolab.gvsig.users.utils.DBSession;

@SuppressWarnings("serial")
public class IsletasForm extends AbstractFormWithLocationWidgets {

    public static final String TABLENAME = "isletas";

    JComboBox tipoIsletaWidget;
    JTextField numeroIsletaWidget;
    JComboBox baseContratistaWidget;
    JTextField isletaIDWidget;
    CalculateComponentValue isletaid;
    private JComboBox tipoVia;
    private DependentComboboxHandler direccionDomainHandler;

    AddReconocimientoListener addReconocimientoListener;
    EditReconocimientoListener editReconocimientoListener;
    DeleteReconocimientoListener deleteReconocimientoListener;

    public IsletasForm(FLyrVect layer) {
	super(layer);

	// int[] trabajoColumnsSize = { 1, 30, 90, 70, 200 };
	addTableHandler(new GIAAlphanumericTableHandler(
		getTrabajosDBTableName(), getWidgetComponents(),
		getElementID(), DBFieldNames.trabajosColNames,
		DBFieldNames.trabajosColAlias, this));
    }

    private void addNewButtonsToActionsToolBar() {
	super.addNewButtonsToActionsToolBar(DBFieldNames.Elements.Isletas);
    }

    @Override
    protected void fillSpecificValues() {
	super.fillSpecificValues();

	direccionDomainHandler.updateComboBoxValues();

	if (filesLinkButton == null) {
	    addNewButtonsToActionsToolBar();
	}

	// Embebed Tables
	int[] trabajoColumnsSize = { 1, 30, 90, 70, 200 };
	SqlUtils.createEmbebedTableFromDB(reconocimientoEstado,
		"audasa_extgia", getReconocimientosDBTableName(),
		DBFieldNames.reconocimientoEstadoFields, null, "id_isleta",
		isletaIDWidget.getText(), "n_inspeccion");
	repaint();
    }

    @Override
    protected void setListeners() {
	super.setListeners();
	Map<String, JComponent> widgets = getWidgets();

	isletaIDWidget = (JTextField) widgets.get(DBFieldNames.ID_ISLETA);

	isletaid = new IsletaCalculateIDValue(this, getWidgetComponents(),
		DBFieldNames.ID_ISLETA, DBFieldNames.NUMERO_ISLETA,
		DBFieldNames.BASE_CONTRATISTA);
	isletaid.setListeners();

	JComboBox direccion = (JComboBox) getWidgets().get("direccion");
	tipoVia = (JComboBox) getWidgets().get("tipo_via");
	direccionDomainHandler = new DependentComboboxHandler(this, tipoVia,
		direccion);
	tipoVia.addActionListener(direccionDomainHandler);

	addReconocimientoListener = new AddReconocimientoListener();
	addReconocimientoButton.addActionListener(addReconocimientoListener);
	editReconocimientoListener = new EditReconocimientoListener();
	editReconocimientoButton.addActionListener(editReconocimientoListener);
	deleteReconocimientoListener = new DeleteReconocimientoListener();
	deleteReconocimientoButton
		.addActionListener(deleteReconocimientoListener);

    }

    @Override
    protected void removeListeners() {
	isletaid.removeListeners();
	tipoVia.removeActionListener(direccionDomainHandler);
	addReconocimientoButton.removeActionListener(addReconocimientoListener);
	editReconocimientoButton
		.removeActionListener(editReconocimientoListener);
	deleteReconocimientoButton
		.removeActionListener(deleteReconocimientoListener);

	super.removeListeners();
    }

    @Override
    protected boolean validationHasErrors() {
	if (this.getFormController().getValuesChanged()
		.containsKey("id_isleta")) {
	    if (isletaIDWidget.getText() != "") {
		String query = "SELECT id_isleta FROM audasa_extgia.isletas "
			+ " WHERE id_isleta = '" + isletaIDWidget.getText()
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
	    IsletasReconocimientosSubForm subForm = new IsletasReconocimientosSubForm(
		    getReconocimientosFormFileName(),
		    getReconocimientosDBTableName(), reconocimientoEstado,
		    "id_isleta", isletaIDWidget.getText(), null, null, false);
	    PluginServices.getMDIManager().addWindow(subForm);
	}
    }

    public class EditReconocimientoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    if (reconocimientoEstado.getSelectedRowCount() != 0) {
		int row = reconocimientoEstado.getSelectedRow();
		IsletasReconocimientosSubForm subForm = new IsletasReconocimientosSubForm(
			getReconocimientosFormFileName(),
			getReconocimientosDBTableName(), reconocimientoEstado,
			"id_isleta", isletaIDWidget.getText(), "n_inspeccion",
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
	return DBFieldNames.Elements.Isletas.name();
    }

    @Override
    protected boolean hasSentido() {
	return true;
    }

    @Override
    public String getElementID() {
	return "id_isleta";
    }

    @Override
    public String getElementIDValue() {
	return isletaIDWidget.getText();
    }

    @Override
    public String getImagesDBTableName() {
	return "isletas_imagenes";
    }

}
