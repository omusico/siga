package es.icarto.gvsig.extgia.batch;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;

import es.icarto.gvsig.extgia.forms.utils.GIASubForm;
import es.icarto.gvsig.navtableforms.gui.tables.model.AlphanumericTableModel;
import es.icarto.gvsig.navtableforms.ormlite.ORMLite;
import es.icarto.gvsig.navtableforms.utils.TOCLayerManager;
import es.udc.cartolab.gvsig.navtable.dataacces.IController;
import es.udc.cartolab.gvsig.navtable.format.ValueFormatNT;

@SuppressWarnings("serial")
public abstract class BatchVegetationTrabajosAbstractSubForm extends GIASubForm {

    private static final ValueFormatNT WRITER = new ValueFormatNT();

    private static final Logger logger = Logger
	    .getLogger(BatchAbstractSubForm.class);

    private final ORMLite batchOrmLite;

    public BatchVegetationTrabajosAbstractSubForm(String formFile, String basicName) {
	super(basicName);
	setForeingKey(new HashMap<String, String>());
	batchOrmLite = new ORMLite(getMetadataPath());
	getWindowInfo().setTitle("Añadir Trabajos");
    }

    @Override
    protected String getMetadataPath() {
	return this.getClass().getClassLoader()
		.getResource("rules/" + getDbTableName() + "_metadata.xml")
		.getPath();
    }

    @Override
    public void actionCreateRecord() {
	this.position = -1;
	saveButton.removeActionListener(action);
	action = new BatchCreateAction(this, getFormController(), model, batchOrmLite);
	saveButton.addActionListener(action);
	setListeners();
	fillEmptyValues();
	PluginServices.getMDIManager().addCentredWindow(this);
    }

    public abstract String getLayerName();

    public abstract String getIdFieldName();

    public abstract String getDbTableName();

    public abstract String[] getColumnNames();

    public abstract String[] getColumnDbNames();

    public abstract Integer[] getColumnDbTypes();

    public abstract void getForeignValues(HashMap<String, String> values, String idValue);

    private final class BatchCreateAction implements ActionListener {

	private final IWindow iWindow;
	private final IController iController;
	private final AlphanumericTableModel model;
	private final ORMLite batchOrmLite;

	public BatchCreateAction(IWindow iWindow, IController iController,
		AlphanumericTableModel model, ORMLite batchOrmLite) {
	    this.iWindow = iWindow;
	    this.iController = iController;
	    this.model = model;
	    this.batchOrmLite = batchOrmLite;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
	    HashMap<String, String> values = new HashMap<String, String>();

	    // controller values must be cloned to avoid bugs
	    for (Entry<String, String> f : iController.getValues().entrySet()) {
		values.put(f.getKey(), f.getValue());
	    }

	    final TOCLayerManager toc = new TOCLayerManager();
	    FLyrVect layer = toc.getLayerByName(getLayerName());

	    SelectableDataSource recordset;
	    try {
		recordset = layer.getRecordset();
		int selectedElements = 0;
		if (recordset.getSelection().isEmpty()) {
		    JOptionPane.showMessageDialog(null, PluginServices.getText(
			    this, "unselectedElements_msg"), PluginServices
			    .getText(this, "warning"),
			    JOptionPane.WARNING_MESSAGE);
		    return;
		}
		selectedElements = recordset.getSelection().cardinality();

		Object[] options = {
			PluginServices.getText(this, "optionPane_yes"),
			PluginServices.getText(this, "optionPane_no") };
		int m = JOptionPane.showOptionDialog(
			null,
			PluginServices.getText(this, "addInfo_msg_I")
			+ selectedElements
			+ " "
			+ PluginServices
			.getText(this, "addInfo_msg_II"), null,
			JOptionPane.YES_NO_CANCEL_OPTION,
			JOptionPane.INFORMATION_MESSAGE, null, options,
			options[1]);
		if (m == JOptionPane.OK_OPTION) {
		    String[][] data = getDataForFillingTable(values, recordset,
			    selectedElements);
		    BatchTrabajosTable table = new BatchTrabajosTable(batchOrmLite, getDbTableName(),
			    data, getColumnNames(), getColumnDbNames(),
			    getColumnDbTypes());
		    PluginServices.getMDIManager().addCentredWindow(table);
		}
	    } catch (Exception e) {
		iController.clearAll();
		position = -1;
		logger.error(e.getStackTrace());
	    }
	    PluginServices.getMDIManager().closeWindow(iWindow);
	}

	private String[][] getDataForFillingTable(
		HashMap<String, String> values, SelectableDataSource recordset,
		int selectedElements) throws ReadDriverException {
	    String[][] data = new String[selectedElements][];
	    int idFieldIndex = recordset
		    .getFieldIndexByName(getIdFieldName());
	    for (int i = 0; i < selectedElements; i++) {
		if (recordset.isSelected(i)) {
		    values.put(getIdFieldName(), recordset
			    .getFieldValue(i, idFieldIndex).getStringValue(WRITER));

		    getForeignValues(values, recordset
			    .getFieldValue(i, idFieldIndex).getStringValue(WRITER));

		    String[] row = new String[getColumnNames().length];
		    for (int j = 0; j < getColumnNames().length; j++ ) {
			if (values.containsKey(getColumnDbNames()[j])) {
			    row[j] = values.get(getColumnDbNames()[j]);
			}else {
			    row[j] = "";
			}
		    }
		    data[i] = row;
		}
	    }
	    return data;
	}
    }

}
