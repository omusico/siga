package es.icarto.gvsig.extgia.forms.areas_descanso;

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
import es.icarto.gvsig.extgia.forms.utils.RamalesHandler;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.udc.cartolab.gvsig.users.utils.DBSession;

@SuppressWarnings("serial")
public class AreasDescansoForm extends AbstractFormWithLocationWidgets {

    public static final String TABLENAME = "areas_descanso";

    JTextField areaDescansoIDWidget;
    CalculateComponentValue areaDescansoid;

    private final GIAAlphanumericTableHandler trabajosTableHandler;
    private final GIAAlphanumericTableHandler reconocimientosTableHandler;

    public AreasDescansoForm(FLyrVect layer) {
	super(layer);

	// int[] trabajoColumnsSize = { 1, 1, 110, 70, 60 };
	trabajosTableHandler = new GIAAlphanumericTableHandler(
		getTrabajosDBTableName(), getWidgetComponents(),
		getElementID(), AreasDescansoTrabajosSubForm.colNames,
		AreasDescansoTrabajosSubForm.colAlias,
		AreasDescansoTrabajosSubForm.class);
	addTableHandler(trabajosTableHandler);

	addTableHandler(new RamalesHandler(getRamalesDBTableName(),
		getWidgetComponents(), getElementID(),
		DBFieldNames.ramalesColNames, DBFieldNames.ramalesColAlias,
		this));

	reconocimientosTableHandler = new GIAAlphanumericTableHandler(
		getReconocimientosDBTableName(), getWidgetComponents(),
		getElementID(), AreasDescansoReconocimientosSubForm.colNames,
		AreasDescansoReconocimientosSubForm.colAlias,
		AreasDescansoReconocimientosSubForm.class);
	addTableHandler(reconocimientosTableHandler);
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

	addReconocimientoButton.setAction(reconocimientosTableHandler
		.getListener().getCreateAction());
	editReconocimientoButton.setAction(reconocimientosTableHandler
		.getListener().getUpdateAction());
	deleteReconocimientoButton.setAction(reconocimientosTableHandler
		.getListener().getDeleteAction());

	//

	addTrabajoButton.setAction(trabajosTableHandler.getListener()
		.getCreateAction());
	editTrabajoButton.setAction(trabajosTableHandler.getListener()
		.getUpdateAction());
	deleteTrabajoButton.setAction(trabajosTableHandler.getListener()
		.getDeleteAction());
    }

    @Override
    protected void removeListeners() {
	addReconocimientoButton.setAction(null);
	editReconocimientoButton.setAction(null);
	deleteReconocimientoButton.setAction(null);

	addTrabajoButton.setAction(null);
	editTrabajoButton.setAction(null);
	deleteTrabajoButton.setAction(null);

	//

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
