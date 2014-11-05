package es.icarto.gvsig.extgia.forms.muros;

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
public class MurosForm extends AbstractFormWithLocationWidgets {

    public static final String TABLENAME = "muros";

    JTextField murosIDWidget;
    CalculateComponentValue murosid;

    private JComboBox tipoViaPI;
    private JComboBox tipoViaPF;
    private DependentComboboxHandler direccionPIDomainHandler;
    private DependentComboboxHandler direccionPFDomainHandler;

    AddReconocimientoListener addReconocimientoListener;
    EditReconocimientoListener editReconocimientoListener;
    DeleteReconocimientoListener deleteReconocimientoListener;

    public MurosForm(FLyrVect layer) {
	super(layer);

	// int[] trabajoColumnsSize = { 1, 30, 90, 70, 200 };
	addTableHandler(new GIAAlphanumericTableHandler(
		getTrabajosDBTableName(), getWidgetComponents(),
		getElementID(), DBFieldNames.trabajosColNames,
		DBFieldNames.trabajosColAlias, this));
    }

    private void addNewButtonsToActionsToolBar() {
	super.addNewButtonsToActionsToolBar(DBFieldNames.Elements.Muros);
    }

    @Override
    protected void fillSpecificValues() {
	super.fillSpecificValues();

	direccionPIDomainHandler.updateComboBoxValues();
	direccionPFDomainHandler.updateComboBoxValues();

	if (murosIDWidget.getText().isEmpty()) {
	    murosid = new MurosCalculateIDValue(this, getWidgetComponents(),
		    getElementID(), getElementID());
	    murosid.setValue(true);
	}

	if (filesLinkButton == null) {
	    addNewButtonsToActionsToolBar();
	}

	// Embebed Tables

	SqlUtils.createEmbebedTableFromDB(reconocimientoEstado,
		"audasa_extgia", getReconocimientosDBTableName(),
		DBFieldNames.reconocimientoEstadoFields, null, getElementID(),
		murosIDWidget.getText(), "n_inspeccion");

	repaint();
    }

    @Override
    protected void setListeners() {
	super.setListeners();
	Map<String, JComponent> widgets = getWidgets();

	murosIDWidget = (JTextField) widgets.get(getElementID());

	JComboBox direccionPI = (JComboBox) widgets.get("direccion_pi");
	tipoViaPI = (JComboBox) widgets.get("tipo_via");
	direccionPIDomainHandler = new DependentComboboxHandler(this,
		tipoViaPI, direccionPI);
	tipoViaPI.addActionListener(direccionPIDomainHandler);

	JComboBox direccionPF = (JComboBox) widgets.get("direccion_pf");
	tipoViaPF = (JComboBox) widgets.get("tipo_via_pf");
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
	    MurosReconocimientosSubForm subForm = new MurosReconocimientosSubForm(
		    getReconocimientosFormFileName(),
		    getReconocimientosDBTableName(), reconocimientoEstado,
		    getElementID(), murosIDWidget.getText(), null, null, false);
	    PluginServices.getMDIManager().addWindow(subForm);
	}
    }

    public class EditReconocimientoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    if (reconocimientoEstado.getSelectedRowCount() != 0) {
		int row = reconocimientoEstado.getSelectedRow();
		MurosReconocimientosSubForm subForm = new MurosReconocimientosSubForm(
			getReconocimientosFormFileName(),
			getReconocimientosDBTableName(), reconocimientoEstado,
			getElementID(), murosIDWidget.getText(),
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
    public String getElement() {
	return DBFieldNames.Elements.Muros.name();
    }

    @Override
    public String getElementID() {
	return "id_muro";
    }

    @Override
    public String getElementIDValue() {
	return murosIDWidget.getText();
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
	return "muros_imagenes";
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
