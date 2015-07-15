package es.icarto.gvsig.extgia.batch;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;

import es.icarto.gvsig.extgia.forms.utils.GIASubForm;
import es.icarto.gvsig.navtableforms.gui.tables.model.AlphanumericTableModel;
import es.icarto.gvsig.navtableforms.utils.TOCLayerManager;
import es.udc.cartolab.gvsig.navtable.dataacces.IController;
import es.udc.cartolab.gvsig.navtable.format.ValueFormatNT;

@SuppressWarnings("serial")
public abstract class BatchAbstractSubForm extends GIASubForm {

    private static final ValueFormatNT WRITER = new ValueFormatNT();

    private static final Logger logger = Logger
	    .getLogger(BatchAbstractSubForm.class);

    private final String primaryKey;

    public BatchAbstractSubForm(String formFile, String basicName) {
	super(basicName);
	setForeingKey(new HashMap<String, String>());
	primaryKey = basicName.endsWith("_trabajos") ? "id_trabajo"
		: "n_inspeccion";
    }

    @Override
    public void actionCreateRecord() {
	this.position = -1;
	saveButton.removeActionListener(action);
	action = new BatchCreateAction(this, getFormController(), model);
	saveButton.addActionListener(action);
	setListeners();
	fillEmptyValues();
	PluginServices.getMDIManager().addCentredWindow(this);
    }

    public abstract String getLayerName();

    public abstract String getIdFieldName();

    private final class BatchCreateAction implements ActionListener {

	private final IWindow iWindow;
	private final IController iController;
	private final AlphanumericTableModel model;

	public BatchCreateAction(IWindow iWindow, IController iController,
		AlphanumericTableModel model) {
	    this.iWindow = iWindow;
	    this.iController = iController;
	    this.model = model;
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
		int idFieldIndex = recordset
			.getFieldIndexByName(getIdFieldName());
		if (recordset.getSelection().isEmpty()) {
		    logger.warn("No record selected");
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
		    // TODO: fpuga. 04/02/2014. Istead of this use
		    // selection.nextSetBit. it will performs fast
		    for (int i = 0; i < recordset.getRowCount(); i++) {
			if (recordset.isSelected(i)) {
			    values.put(getIdFieldName(), recordset
				    .getFieldValue(i, idFieldIndex)
				    .getStringValue(WRITER));

			    values.remove(primaryKey);
			    iController.create(values);

			}
		    }
		    JOptionPane.showMessageDialog(
			    null,
			    PluginServices.getText(this, "addedInfo_msg_I")
			    + selectedElements
			    + " "
			    + PluginServices.getText(this,
				    "addedInfo_msg_II"));

		}
		model.dataChanged();
	    } catch (Exception e) {
		iController.clearAll();
		position = -1;
		logger.error(e.getStackTrace());
	    }
	    PluginServices.getMDIManager().closeWindow(iWindow);
	}
    }

}
