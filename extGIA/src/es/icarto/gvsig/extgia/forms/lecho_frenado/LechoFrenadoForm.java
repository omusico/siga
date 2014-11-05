package es.icarto.gvsig.extgia.forms.lecho_frenado;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

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
public class LechoFrenadoForm extends AbstractFormWithLocationWidgets {

    public static final String TABLENAME = "lecho_frenado";

    JTextField lechoFrenadoIDWidget;
    CalculateComponentValue lechoFrenadoid;

    AddReconocimientoListener addReconocimientoListener;
    EditReconocimientoListener editReconocimientoListener;
    DeleteReconocimientoListener deleteReconocimientoListener;

    public LechoFrenadoForm(FLyrVect layer) {
	super(layer);

	// int[] trabajoColumnsSize = { 1, 30, 90, 70, 200 };
	addTableHandler(new GIAAlphanumericTableHandler(
		getTrabajosDBTableName(), getWidgetComponents(),
		getElementID(), DBFieldNames.trabajosColNames,
		DBFieldNames.trabajosColAlias, this));
    }

    private void addNewButtonsToActionsToolBar() {
	super.addNewButtonsToActionsToolBar(DBFieldNames.Elements.Lecho_Frenado);
    }

    @Override
    protected void fillSpecificValues() {
	super.fillSpecificValues();

	if (lechoFrenadoIDWidget.getText().isEmpty()) {
	    lechoFrenadoid = new LechoFrenadoCalculateIDValue(this,
		    getWidgetComponents(), getElementID(), getElementID());
	    lechoFrenadoid.setValue(true);
	}

	if (filesLinkButton == null) {
	    addNewButtonsToActionsToolBar();
	}

	// Embebed Tables
	SqlUtils.createEmbebedTableFromDB(reconocimientoEstado,
		DBFieldNames.GIA_SCHEMA, getReconocimientosDBTableName(),
		DBFieldNames.reconocimientoEstadoFields, null, getElementID(),
		getElementIDValue(), "n_inspeccion");
	repaint();
    }

    @Override
    protected boolean validationHasErrors() {
	if (this.getFormController().getValuesChanged()
		.containsKey(getElementID())) {
	    if (getElementIDValue() != "") {
		String query = "SELECT id_lecho_frenado FROM audasa_extgia.lecho_frenado "
			+ " WHERE id_lecho_frenado = '"
			+ getElementIDValue()
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
    protected void setListeners() {
	super.setListeners();
	Map<String, JComponent> widgets = getWidgets();

	lechoFrenadoIDWidget = (JTextField) widgets.get(getElementID());

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
	addReconocimientoButton.removeActionListener(addReconocimientoListener);
	editReconocimientoButton
		.removeActionListener(editReconocimientoListener);
	deleteReconocimientoButton
		.removeActionListener(deleteReconocimientoListener);

	super.removeListeners();
    }

    public class AddReconocimientoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    LechoFrenadoReconocimientosSubForm subForm = new LechoFrenadoReconocimientosSubForm(
		    getReconocimientosFormFileName(),
		    getReconocimientosDBTableName(), reconocimientoEstado,
		    getElementID(), getElementIDValue(), null, null, false);
	    PluginServices.getMDIManager().addWindow(subForm);
	}
    }

    public class EditReconocimientoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    if (reconocimientoEstado.getSelectedRowCount() != 0) {
		int row = reconocimientoEstado.getSelectedRow();
		LechoFrenadoReconocimientosSubForm subForm = new LechoFrenadoReconocimientosSubForm(
			getReconocimientosFormFileName(),
			getReconocimientosDBTableName(), reconocimientoEstado,
			getElementID(), getElementIDValue(), "n_inspeccion",
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
    public String getElement() {
	return DBFieldNames.Elements.Lecho_Frenado.name();
    }

    @Override
    public String getElementID() {
	return "id_lecho_frenado";
    }

    @Override
    public String getElementIDValue() {
	return lechoFrenadoIDWidget.getText();
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
	return "lecho_frenado_imagenes";
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
    protected boolean hasSentido() {
	return true;
    }

}
