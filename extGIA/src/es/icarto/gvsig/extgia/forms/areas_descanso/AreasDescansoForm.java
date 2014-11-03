package es.icarto.gvsig.extgia.forms.areas_descanso;

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
import es.icarto.gvsig.extgia.forms.utils.GIAAlphanumericTableHandler;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.extgia.utils.SqlUtils;
import es.udc.cartolab.gvsig.users.utils.DBSession;

@SuppressWarnings("serial")
public class AreasDescansoForm extends AbstractFormWithLocationWidgets {

    public static final String TABLENAME = "areas_descanso";

    JTextField areaDescansoIDWidget;
    CalculateComponentValue areaDescansoid;

    AddReconocimientoListener addReconocimientoListener;
    EditReconocimientoListener editReconocimientoListener;
    DeleteReconocimientoListener deleteReconocimientoListener;

    private final GIAAlphanumericTableHandler trabajosTableHandler;
    private final GIAAlphanumericTableHandler ramalesTableHandler;

    private JButton addRamalButton;
    private JButton editRamalButton;
    private JButton deleteRamalButton;

    public AreasDescansoForm(FLyrVect layer) {
	super(layer);

	// int[] trabajoColumnsSize = { 1, 1, 110, 70, 60 };
	trabajosTableHandler = new GIAAlphanumericTableHandler(
		getTrabajosDBTableName(), getWidgetComponents(),
		getElementID(), AreasDescansoTrabajosSubForm.colNames,
		AreasDescansoTrabajosSubForm.colAlias,
		AreasDescansoTrabajosSubForm.class);
	addTableHandler(trabajosTableHandler);

	ramalesTableHandler = new GIAAlphanumericTableHandler(
		getRamalesDBTableName(), getWidgetComponents(), getElementID(),
		AreasDescansoRamalesSubForm.colNames,
		AreasDescansoRamalesSubForm.colAlias,
		AreasDescansoRamalesSubForm.class);
	addTableHandler(ramalesTableHandler);
    }

    private void addNewButtonsToActionsToolBar() {
	super.addNewButtonsToActionsToolBar(DBFieldNames.Elements.Areas_Descanso);
    }

    @Override
    protected void fillSpecificValues() {
	super.fillSpecificValues();

	if (filesLinkButton == null) {
	    addNewButtonsToActionsToolBar();
	}

	// Embebed Tables
	SqlUtils.createEmbebedTableFromDB(reconocimientoEstado,
		DBFieldNames.GIA_SCHEMA, getReconocimientosDBTableName(),
		DBFieldNames.reconocimientoEstadoFields, null,
		"id_area_descanso", areaDescansoIDWidget.getText(),
		"n_inspeccion");

	repaint();
    }

    @Override
    protected void setListeners() {
	super.setListeners();
	Map<String, JComponent> widgets = getWidgets();

	areaDescansoIDWidget = (JTextField) widgets
		.get(DBFieldNames.ID_AREA_DESCANSO);

	areaDescansoid = new AreasDescansoCalculateIDValue(this, getWidgets(),
		DBFieldNames.ID_AREA_DESCANSO, DBFieldNames.AREA_MANTENIMIENTO,
		DBFieldNames.BASE_CONTRATISTA, DBFieldNames.TRAMO,
		DBFieldNames.TIPO_VIA, DBFieldNames.MUNICIPIO,
		DBFieldNames.SENTIDO);
	areaDescansoid.setListeners();

	addRamalButton = (JButton) super.getFormBody().getComponentByName(
		"add_ramal_button");
	editRamalButton = (JButton) super.getFormBody().getComponentByName(
		"edit_ramal_button");
	deleteRamalButton = (JButton) super.getFormBody().getComponentByName(
		"delete_ramal_button");
	addRamalButton.setAction(ramalesTableHandler.getListener()
		.getCreateAction());
	editRamalButton.setAction(ramalesTableHandler.getListener()
		.getUpdateAction());
	deleteRamalButton.setAction(ramalesTableHandler.getListener()
		.getDeleteAction());

	addReconocimientoListener = new AddReconocimientoListener();
	addReconocimientoButton.addActionListener(addReconocimientoListener);

	addTrabajoButton.setAction(trabajosTableHandler.getListener()
		.getCreateAction());
	editTrabajoButton.setAction(trabajosTableHandler.getListener()
		.getUpdateAction());
	deleteTrabajoButton.setAction(trabajosTableHandler.getListener()
		.getDeleteAction());

	editReconocimientoListener = new EditReconocimientoListener();
	editReconocimientoButton.addActionListener(editReconocimientoListener);

	deleteReconocimientoListener = new DeleteReconocimientoListener();
	deleteReconocimientoButton
		.addActionListener(deleteReconocimientoListener);

    }

    @Override
    protected void removeListeners() {
	addReconocimientoButton.removeActionListener(addReconocimientoListener);
	editReconocimientoButton
		.removeActionListener(editReconocimientoListener);
	deleteReconocimientoButton
		.removeActionListener(deleteReconocimientoListener);

	addTrabajoButton.setAction(null);
	editTrabajoButton.setAction(null);
	deleteTrabajoButton.setAction(null);

	addRamalButton.setAction(null);
	editRamalButton.setAction(null);
	deleteRamalButton.setAction(null);

	super.removeListeners();

    }

    @Override
    protected boolean validationHasErrors() {
	if (this.getFormController().getValuesChanged()
		.containsKey("id_area_descanso")) {
	    if (areaDescansoIDWidget.getText() != "") {
		String query = "SELECT id_area_descanso FROM audasa_extgia.areas_descanso "
			+ " WHERE id_area_descanso = '"
			+ areaDescansoIDWidget.getText() + "';";
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
		    logger.error(e.getStackTrace(), e);
		}
	    }
	}
	return super.validationHasErrors();
    }

    public class AddReconocimientoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    AreasDescansoReconocimientosSubForm subForm = new AreasDescansoReconocimientosSubForm(
		    getReconocimientosFormFileName(),
		    getReconocimientosDBTableName(), reconocimientoEstado,
		    "id_area_descanso", areaDescansoIDWidget.getText(), null,
		    null, false);
	    PluginServices.getMDIManager().addWindow(subForm);
	}
    }

    public class EditReconocimientoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    if (reconocimientoEstado.getSelectedRowCount() != 0) {
		int row = reconocimientoEstado.getSelectedRow();
		AreasDescansoReconocimientosSubForm subForm = new AreasDescansoReconocimientosSubForm(
			getReconocimientosFormFileName(),
			getReconocimientosDBTableName(), reconocimientoEstado,
			"id_area_descanso", areaDescansoIDWidget.getText(),
			"n_inspeccion", reconocimientoEstado.getValueAt(row, 0)
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

    public class DeleteReconocimientoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    deleteElement(reconocimientoEstado,
		    getReconocimientosDBTableName(),
		    getReconocimientosIDField());
	}
    }

    @Override
    protected String getPrimaryKeyValue() {
	return getFormController().getValue(getElementID());
    }

    @Override
    public JTable getReconocimientosJTable() {
	return null;
    }

    @Override
    public JTable getTrabajosJTable() {
	return null;
    }

    @Override
    public boolean isSpecialCase() {
	return false;
    }

    @Override
    protected String getBasicName() {
	return TABLENAME;
    }

    @Override
    public String getElement() {
	return DBFieldNames.Elements.Areas_Descanso.name();
    }

    @Override
    protected boolean hasSentido() {
	return true;
    }

    @Override
    public String getElementID() {
	return "id_area_descanso";
    }

    @Override
    public String getElementIDValue() {
	return areaDescansoIDWidget.getText();
    }

    @Override
    public String getImagesDBTableName() {
	return "areas_descanso_imagenes";
    }

}
