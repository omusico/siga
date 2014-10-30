package es.icarto.gvsig.extgia.forms.firme;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.image.ImageComponent;

import es.icarto.gvsig.audasacommons.PreferencesPage;
import es.icarto.gvsig.extgia.forms.images.AddImageListener;
import es.icarto.gvsig.extgia.forms.images.DeleteImageListener;
import es.icarto.gvsig.extgia.forms.images.ShowImageAction;
import es.icarto.gvsig.extgia.forms.utils.AbstractFormWithLocationWidgets;
import es.icarto.gvsig.extgia.forms.utils.CalculateComponentValue;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.extgia.utils.SqlUtils;

@SuppressWarnings("serial")
public class FirmeForm extends AbstractFormWithLocationWidgets {

    public static final String TABLENAME = "firme";

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
    }

    private void addNewButtonsToActionsToolBar() {
	super.addNewButtonsToActionsToolBar(DBFieldNames.Elements.Firme);
    }

    @Override
    protected void fillSpecificValues() {
	if (firmeIDWidget.getText().isEmpty()) {
	    firmeid = new FirmeCalculateIDValue(this, getWidgets(),
		    DBFieldNames.ID_FIRME, DBFieldNames.ID_FIRME);
	    firmeid.setValue(true);
	}

	if (filesLinkButton == null) {
	    addNewButtonsToActionsToolBar();
	}

	if (addImageListener != null) {
	    addImageListener.setPkValue(getElementIDValue());
	}

	if (deleteImageListener != null) {
	    deleteImageListener.setPkValue(getElementIDValue());
	}

	// Element image
	new ShowImageAction(imageComponent, addImageButton,
		getImagesDBTableName(), getElementID(), getElementIDValue());

	// Embebed Tables
	int[] trabajoColumnsSize = { 1, 1, 1, 1, 30, 250 };
	SqlUtils.createEmbebedTableFromDB(reconocimientoEstado,
		"audasa_extgia", getReconocimientosDBTableName(),
		DBFieldNames.firmeReconocimientoEstadoFields, null, "id_firme",
		firmeIDWidget.getText(), "n_inspeccion");
	SqlUtils.createEmbebedTableFromDB(trabajos, "audasa_extgia",
		getTrabajosDBTableName(), DBFieldNames.firmeTrabajoFields,
		trabajoColumnsSize, "id_firme", firmeIDWidget.getText(),
		"id_trabajo");
	repaint();
    }

    @Override
    protected void setListeners() {
	super.setListeners();

	ImageComponent image = (ImageComponent) formBody
		.getComponentByName("image");
	ImageIcon icon = new ImageIcon(PreferencesPage.AUDASA_ICON);
	image.setIcon(icon);

	Map<String, JComponent> widgets = getWidgets();

	imageComponent = (ImageComponent) formBody
		.getComponentByName("element_image");
	addImageButton = (JButton) formBody
		.getComponentByName("add_image_button");
	deleteImageButton = (JButton) formBody
		.getComponentByName("delete_image_button");

	reconocimientoEstado = (JTable) widgets
		.get("reconocimiento_estado_firme");
	trabajos = (JTable) widgets.get("trabajos_firme");
	addReconocimientoButton = (JButton) formBody
		.getComponentByName("add_reconocimiento_button");
	editReconocimientoButton = (JButton) formBody
		.getComponentByName("edit_reconocimiento_button");
	addTrabajoButton = (JButton) formBody
		.getComponentByName("add_trabajo_button");
	editTrabajoButton = (JButton) formBody
		.getComponentByName("edit_trabajo_button");
	deleteReconocimientoButton = (JButton) formBody
		.getComponentByName("delete_reconocimiento_button");
	deleteTrabajoButton = (JButton) formBody
		.getComponentByName("delete_trabajo_button");

	if (addImageListener == null) {
	    addImageListener = new AddImageListener(imageComponent,
		    addImageButton, getImagesDBTableName(), getElementID());
	    addImageButton.addActionListener(addImageListener);
	}

	if (deleteImageListener == null) {
	    deleteImageListener = new DeleteImageListener(imageComponent,
		    addImageButton, getImagesDBTableName(), getElementID());
	    deleteImageButton.addActionListener(deleteImageListener);
	}
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
	deleteReconocimientoButton
		.addActionListener(deleteReconocimientoListener);
	deleteTrabajoListener = new DeleteTrabajoListener();
	deleteTrabajoButton.addActionListener(deleteTrabajoListener);
    }

    @Override
    protected void removeListeners() {
	addReconocimientoButton.removeActionListener(addReconocimientoListener);
	editReconocimientoButton
		.removeActionListener(editReconocimientoListener);
	addTrabajoButton.removeActionListener(addTrabajoListener);
	editTrabajoButton.removeActionListener(editTrabajoListener);
	deleteReconocimientoButton
		.removeActionListener(deleteReconocimientoListener);
	deleteTrabajoButton.removeActionListener(deleteTrabajoListener);
    }

    public class AddReconocimientoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    FirmeReconocimientosSubForm subForm = new FirmeReconocimientosSubForm(
		    getReconocimientosFormFileName(),
		    getReconocimientosDBTableName(), reconocimientoEstado,
		    "id_firme", firmeIDWidget.getText(), null, null, false);
	    PluginServices.getMDIManager().addWindow(subForm);
	}
    }

    public class AddTrabajoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    FirmeTrabajosSubForm subForm = new FirmeTrabajosSubForm(
		    getTrabajosFormFileName(), getTrabajosDBTableName(),
		    trabajos, "id_firme", firmeIDWidget.getText(), null, null,
		    false);
	    PluginServices.getMDIManager().addWindow(subForm);
	}
    }

    public class EditReconocimientoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    if (reconocimientoEstado.getSelectedRowCount() != 0) {
		int row = reconocimientoEstado.getSelectedRow();
		FirmeReconocimientosSubForm subForm = new FirmeReconocimientosSubForm(
			getReconocimientosFormFileName(),
			getReconocimientosDBTableName(), reconocimientoEstado,
			"id_firme", firmeIDWidget.getText(), "n_inspeccion",
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

    public class EditTrabajoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    if (trabajos.getSelectedRowCount() != 0) {
		int row = trabajos.getSelectedRow();
		FirmeTrabajosSubForm subForm = new FirmeTrabajosSubForm(
			getTrabajosFormFileName(), getTrabajosDBTableName(),
			trabajos, "id_firme", firmeIDWidget.getText(),
			"id_trabajo", trabajos.getValueAt(row, 0).toString(),
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

    public class DeleteReconocimientoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    deleteElement(reconocimientoEstado,
		    getReconocimientosDBTableName(),
		    getReconocimientosIDField());
	}
    }

    public class DeleteTrabajoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    deleteElement(trabajos, getTrabajosDBTableName(),
		    getTrabajosIDField());
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
    public String getReconocimientosDBTableName() {
	return "firme_reconocimientos";
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
	return TABLENAME;
    }

    @Override
    public String getElement() {
	return DBFieldNames.Elements.Firme.name();
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
