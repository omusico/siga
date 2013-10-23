package es.icarto.gvsig.extgia.forms.firme;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.image.ImageComponent;

import es.icarto.gvsig.audasacommons.PreferencesPage;
import es.icarto.gvsig.extgia.forms.images.AddImageListener;
import es.icarto.gvsig.extgia.forms.images.DeleteImageListener;
import es.icarto.gvsig.extgia.forms.utils.AbstractFormWithLocationWidgets;
import es.icarto.gvsig.extgia.forms.utils.CalculateComponentValue;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.extgia.utils.SqlUtils;

@SuppressWarnings("serial")
public class FirmeForm extends AbstractFormWithLocationWidgets {

    public static final String ABEILLE_FILENAME = "forms/firme.xml";
    public static final String ABEILLE_RECONOCIMIENTOS_FILENAME = "forms/firme_reconocimiento_estado.xml";
    public static final String ABEILLE_TRABAJOS_FILENAME = "forms/firme_trabajos.xml";

    JTextField firmeIDWidget;
    CalculateComponentValue firmeid;

    AddReconocimientoListener addReconocimientoListener;
    EditReconocimientoListener editReconocimientoListener;
    AddTrabajoListener addTrabajoListener;
    EditTrabajoListener editTrabajoListener;
    DeleteReconocimientoListener deleteReconocimientoListener;
    DeleteTrabajoListener deleteTrabajoListener;

    public FirmeForm(FLyrVect layer) {
	super(layer);
	initListeners();
    }

    private void addNewButtonsToActionsToolBar() {
	super.addNewButtonsToActionsToolBar(DBFieldNames.Elements.Firme);
    }

    @Override
    protected void fillSpecificValues() {
	updateBaseContratistaCombo();
	selectBaseContratistaOption();
	updateTramoCombo();
	selectTramoOption();

	if (firmeIDWidget.getText().isEmpty()) {
	    firmeid = new FirmeCalculateIDValue(this, getWidgetComponents(),
		    DBFieldNames.ID_FIRME, DBFieldNames.ID_FIRME);
	    firmeid.setValue(true);
	}

	if (filesLinkButton == null) {
	    addNewButtonsToActionsToolBar();
	}

	// Embebed Tables
	int[] trabajoColumnsSize = {1, 1, 1, 1, 30, 250};
	SqlUtils.createEmbebedTableFromDB(reconocimientoEstado,
		"audasa_extgia", getReconocimientosDBTableName(),
		DBFieldNames.firmeReconocimientoEstadoFields, null, "id_firme",
		firmeIDWidget.getText(), "n_inspeccion");
	SqlUtils.createEmbebedTableFromDB(trabajos,
		"audasa_extgia", getTrabajosDBTableName(),
		DBFieldNames.firmeTrabajoFields, trabajoColumnsSize, "id_firme",
		firmeIDWidget.getText(), "id_trabajo");
	repaint();
    }

    @Override
    protected void setListeners() {
	super.setListeners();

	ImageComponent image = (ImageComponent) form.getComponentByName("image");
	ImageIcon icon = new ImageIcon (PreferencesPage.AUDASA_ICON);
	image.setIcon(icon);

	HashMap<String, JComponent> widgets = getWidgetComponents();

	imageComponent = (ImageComponent) form.getComponentByName("element_image");
	addImageButton = (JButton) form.getComponentByName("add_image_button");
	deleteImageButton = (JButton) form.getComponentByName("delete_image_button");

	areaMantenimientoWidget = (JComboBox) widgets.get(AREA_MANTENIMIENTO);
	baseContratistaWidget = (JComboBox) widgets.get(BASE_CONTRATISTA);
	tramoWidget = (JComboBox) widgets.get(TRAMO);

	updateBaseContratistaListener = new UpdateBaseContratistaListener();
	updateTramoListener = new UpdateTramoListener();

	areaMantenimientoWidget.addActionListener(updateBaseContratistaListener);
	baseContratistaWidget.addActionListener(updateTramoListener);

	reconocimientoEstado = (JTable) widgets.get("reconocimiento_estado_firme");
	trabajos = (JTable) widgets.get("trabajos_firme");
	addReconocimientoButton = (JButton) form.getComponentByName("add_reconocimiento_button");
	editReconocimientoButton = (JButton) form.getComponentByName("edit_reconocimiento_button");
	addTrabajoButton = (JButton) form.getComponentByName("add_trabajo_button");
	editTrabajoButton = (JButton) form.getComponentByName("edit_trabajo_button");
	deleteReconocimientoButton = (JButton) form.getComponentByName("delete_reconocimiento_button");
	deleteTrabajoButton = (JButton) form.getComponentByName("delete_trabajo_button");

	if (addImageListener == null) {
	    addImageListener = new AddImageListener(imageComponent, addImageButton,
		    getImagesDBTableName(), getElementID());
	    addImageButton.addActionListener(addImageListener);
	}

	if (deleteImageListener == null) {
	    deleteImageListener = new DeleteImageListener(imageComponent, addImageButton,
		    getImagesDBTableName(), getElementID());
	    deleteImageButton.addActionListener(deleteImageListener);
	}
    }

    protected void initListeners() {

	HashMap<String, JComponent> widgets = getWidgetComponents();

	firmeIDWidget = (JTextField) widgets.get(DBFieldNames.ID_FIRME);

	addReconocimientoListener = new AddReconocimientoListener();
	addReconocimientoButton.addActionListener(addReconocimientoListener);
	editReconocimientoListener = new EditReconocimientoListener();
	editReconocimientoButton.addActionListener(editReconocimientoListener);
	addTrabajoListener = new AddTrabajoListener();
	addTrabajoButton.addActionListener(addTrabajoListener);
	editTrabajoListener = new EditTrabajoListener();
	editTrabajoButton.addActionListener(editTrabajoListener);
	deleteReconocimientoListener = new DeleteReconocimientoListener();
	deleteReconocimientoButton.addActionListener(deleteReconocimientoListener);
	deleteTrabajoListener = new DeleteTrabajoListener();
	deleteTrabajoButton.addActionListener(deleteTrabajoListener);
    }

    @Override
    protected void removeListeners() {
	areaMantenimientoWidget.removeActionListener(updateBaseContratistaListener);
	baseContratistaWidget.removeActionListener(updateTramoListener);

	addReconocimientoButton.removeActionListener(addReconocimientoListener);
	editReconocimientoButton.removeActionListener(editReconocimientoListener);
	addTrabajoButton.removeActionListener(addTrabajoListener);
	editTrabajoButton.removeActionListener(editTrabajoListener);
	deleteReconocimientoButton.removeActionListener(deleteReconocimientoListener);
	deleteTrabajoButton.removeActionListener(deleteTrabajoListener);
    }

    public class AddReconocimientoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    FirmeReconocimientosSubForm subForm =
		    new FirmeReconocimientosSubForm(
			    ABEILLE_RECONOCIMIENTOS_FILENAME,
			    getReconocimientosDBTableName(),
			    reconocimientoEstado,
			    "id_firme",
			    firmeIDWidget.getText(),
			    null,
			    null,
			    false);
	    PluginServices.getMDIManager().addWindow(subForm);
	}
    }

    public class AddTrabajoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    FirmeTrabajosSubForm subForm = new FirmeTrabajosSubForm(
		    ABEILLE_TRABAJOS_FILENAME,
		    getTrabajosDBTableName(),
		    trabajos,
		    "id_firme",
		    firmeIDWidget.getText(),
		    null,
		    null,
		    false);
	    PluginServices.getMDIManager().addWindow(subForm);
	}
    }

    public class EditReconocimientoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    if (reconocimientoEstado.getSelectedRowCount() != 0) {
		int row = reconocimientoEstado.getSelectedRow();
		FirmeReconocimientosSubForm subForm =
			new FirmeReconocimientosSubForm(
				ABEILLE_RECONOCIMIENTOS_FILENAME,
				getReconocimientosDBTableName(),
				reconocimientoEstado,
				"id_firme",
				firmeIDWidget.getText(),
				"n_inspeccion",
				reconocimientoEstado.getValueAt(row, 0).toString(),
				true);
		PluginServices.getMDIManager().addWindow(subForm);
	    }else {
		JOptionPane.showMessageDialog(null,
			"Debe seleccionar una fila para editar los datos.",
			"Ninguna fila seleccionada",
			JOptionPane.INFORMATION_MESSAGE);
	    }
	}
    }

    public class EditTrabajoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    if (trabajos.getSelectedRowCount() != 0) {
		int row = trabajos.getSelectedRow();
		FirmeTrabajosSubForm subForm = new FirmeTrabajosSubForm(
			ABEILLE_TRABAJOS_FILENAME,
			getTrabajosDBTableName(),
			trabajos,
			"id_firme",
			firmeIDWidget.getText(),
			"id_trabajo",
			trabajos.getValueAt(row, 0).toString(),
			true);
		PluginServices.getMDIManager().addWindow(subForm);
	    }else {
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
	    deleteElement(reconocimientoEstado, getReconocimientosDBTableName(),
		    getReconocimientosIDField());
	}
    }

    public class DeleteTrabajoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    deleteElement(trabajos, getTrabajosDBTableName(), getTrabajosIDField());
	}
    }

    @Override
    public String getFormBodyPath() {
	return ABEILLE_FILENAME;
    }

    @Override
    public Logger getLoggerName() {
	return Logger.getLogger(this.getClass().getName());
    }

    @Override
    public String getXMLPath() {
	return this.getClass().getClassLoader()
		.getResource("rules/firme_metadata.xml")
		.getPath();
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
    public String getReconocimientosDBTableName() {
	return "firme_reconocimiento_estado";
    }

    @Override
    public String getTrabajosDBTableName() {
	return "firme_trabajos";
    }

    @Override
    public boolean isSpecialCase() {
	return true;
    }

    @Override
    protected String getBasicName() {
	return "Firme";
    }

    @Override
    public String getElement() {
	return DBFieldNames.Elements.Firme.name();
    }

    @Override
    public String getReconocimientosFormFileName() {
	return ABEILLE_RECONOCIMIENTOS_FILENAME;
    }

    @Override
    public String getTrabajosFormFileName() {
	return ABEILLE_TRABAJOS_FILENAME;
    }

    @Override
    protected boolean hasSentido() {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public String getElementID() {
	return "id_firme";
    }

    @Override
    public String getElementIDValue() {
	return firmeIDWidget.getText();
    }

    @Override
    public String getImagesDBTableName() {
	return "firme_imagenes";
    }

}
