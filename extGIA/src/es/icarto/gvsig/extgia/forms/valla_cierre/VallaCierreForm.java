package es.icarto.gvsig.extgia.forms.valla_cierre;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

@SuppressWarnings("serial")
public class VallaCierreForm extends AbstractFormWithLocationWidgets {

    public static final String TABLENAME = "valla_cierre";

    JTextField vallaCierreIDWidget;
    CalculateComponentValue vallaCierreid;

    private JComboBox tipoViaPI;
    private JComboBox tipoViaPF;
    private DependentComboboxHandler direccionPIDomainHandler;
    private DependentComboboxHandler direccionPFDomainHandler;

    AddReconocimientoListener addReconocimientoListener;
    EditReconocimientoListener editReconocimientoListener;
    DeleteReconocimientoListener deleteReconocimientoListener;

    public VallaCierreForm(FLyrVect layer) {
	super(layer);

	// int[] trabajoColumnsSize = { 1, 30, 90, 70, 200 };
	addTableHandler(new GIAAlphanumericTableHandler(
		getTrabajosDBTableName(), getWidgetComponents(),
		getElementID(), DBFieldNames.trabajosColNames,
		DBFieldNames.trabajosColAlias, this));
    }

    private void addNewButtonsToActionsToolBar() {
	super.addNewButtonsToActionsToolBar(DBFieldNames.Elements.Valla_Cierre);
    }

    @Override
    protected void fillSpecificValues() {
	super.fillSpecificValues();

	direccionPIDomainHandler.updateComboBoxValues();
	direccionPFDomainHandler.updateComboBoxValues();

	if (vallaCierreIDWidget.getText().isEmpty()) {
	    vallaCierreid = new VallaCierreCalculateIDValue(this,
		    getWidgetComponents(), DBFieldNames.ID_VALLA_CIERRE,
		    DBFieldNames.ID_VALLA_CIERRE);
	    vallaCierreid.setValue(true);
	}

	if (filesLinkButton == null) {
	    addNewButtonsToActionsToolBar();
	}

	// Embebed Tables
	SqlUtils.createEmbebedTableFromDB(reconocimientoEstado,
		"audasa_extgia", getReconocimientosDBTableName(),
		DBFieldNames.reconocimientoEstadoFields, null, "id_valla",
		vallaCierreIDWidget.getText(), "n_inspeccion");

	repaint();
    }

    @Override
    protected void setListeners() {
	super.setListeners();
	Map<String, JComponent> widgets = getWidgets();

	vallaCierreIDWidget = (JTextField) widgets
		.get(DBFieldNames.ID_VALLA_CIERRE);

	JComboBox direccionPI = (JComboBox) widgets.get("direccion_pi");
	tipoViaPI = (JComboBox) widgets.get("tipo_via");
	direccionPIDomainHandler = new DependentComboboxHandler(this,
		tipoViaPI, direccionPI);
	tipoViaPI.addActionListener(direccionPIDomainHandler);

	JComboBox direccionPF = (JComboBox) getWidgets().get("direccion_pf");
	tipoViaPF = (JComboBox) getWidgets().get("tipo_via_pf");
	direccionPFDomainHandler = new DependentComboboxHandler(this,
		tipoViaPF, direccionPF);
	tipoViaPF.addActionListener(direccionPFDomainHandler);

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
	tipoViaPI.removeActionListener(direccionPIDomainHandler);
	tipoViaPF.removeActionListener(direccionPFDomainHandler);
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
	    VallaCierreReconocimientosSubForm subForm = new VallaCierreReconocimientosSubForm(
		    getReconocimientosFormFileName(),
		    getReconocimientosDBTableName(), reconocimientoEstado,
		    "id_valla", vallaCierreIDWidget.getText(), null, null,
		    false);
	    PluginServices.getMDIManager().addWindow(subForm);
	}
    }

    public class EditReconocimientoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    if (reconocimientoEstado.getSelectedRowCount() != 0) {
		int row = reconocimientoEstado.getSelectedRow();
		VallaCierreReconocimientosSubForm subForm = new VallaCierreReconocimientosSubForm(
			getReconocimientosFormFileName(),
			getReconocimientosDBTableName(), reconocimientoEstado,
			"id_valla", vallaCierreIDWidget.getText(),
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
	return DBFieldNames.Elements.Valla_Cierre.name();
    }

    @Override
    protected boolean hasSentido() {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public String getElementID() {
	return "id_valla";
    }

    @Override
    public String getElementIDValue() {
	return vallaCierreIDWidget.getText();
    }

    @Override
    public String getImagesDBTableName() {
	return "valla_cierre_imagenes";
    }
}
