package es.icarto.gvsig.extgia.batch;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.Value;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.jeta.forms.components.image.ImageComponent;

import es.icarto.gvsig.audasacommons.PreferencesPage;
import es.icarto.gvsig.extgia.forms.utils.BasicAbstractSubForm;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.extgia.utils.SqlUtils;
import es.icarto.gvsig.navtableforms.utils.AbeilleParser;
import es.icarto.gvsig.navtableforms.utils.TOCLayerManager;

@SuppressWarnings("serial")
public abstract class BatchAbstractSubForm extends BasicAbstractSubForm implements ActionListener {

    public BatchAbstractSubForm(String formFile, String dbTableName) {
	super(formFile, dbTableName, null, null, null, null, null, false);
    }

    @Override
    protected void initWidgets() {
	widgetsVector = AbeilleParser.getWidgetsFromContainer(getForm());
	widgetsVector.size();

	ImageComponent image = (ImageComponent) getForm().getComponentByName("image");
	ImageIcon icon = new ImageIcon (PreferencesPage.AUDASA_ICON);
	image.setIcon(icon);

	addButton = (JButton) form.getComponentByName("add_subform_button");
	addButton.addActionListener(this);
    }

    public abstract String getLayerName();
    public abstract String getIdFieldName();

    public void actionPerformed(ActionEvent arg0) {
	HashMap<String, Value> formData = getFormData();
	formData.remove(getIdFieldName());

	final TOCLayerManager toc = new TOCLayerManager();
	FLyrVect layer = toc.getLayerByName(getLayerName());

	SelectableDataSource recordset;
	try {
	    recordset = layer.getRecordset();
	    int selectedElements = 0;
	    int idFieldIndex = recordset.getFieldIndexByName(getIdFieldName());
	    if (recordset.getSelection().isEmpty()) {
		JOptionPane.showMessageDialog(null,
			PluginServices.getText(this, "unselectedElements_msg"),
			PluginServices.getText(this, "warning"),
			JOptionPane.WARNING_MESSAGE);
		return;
	    }
	    selectedElements = recordset.getSelection().cardinality();

	    Object[] options = {PluginServices.getText(this, "optionPane_yes"),
		    PluginServices.getText(this, "optionPane_no")};
	    int m = JOptionPane.showOptionDialog(
		    null,
		    PluginServices.getText(this, "addInfo_msg_I" ) + selectedElements + " " +
			    PluginServices.getText(this, "addInfo_msg_II") ,
			    null,
			    JOptionPane.YES_NO_CANCEL_OPTION,
			    JOptionPane.INFORMATION_MESSAGE, null,
			    options, options[1]);
	    if (m == JOptionPane.OK_OPTION) {
		for (int i=0; i<recordset.getRowCount();i++) {
		    if (recordset.isSelected(i)) {
			formData.put(getIdFieldName(), recordset.getFieldValue(i, idFieldIndex));
			SqlUtils.insert(DBFieldNames.GIA_SCHEMA, dbTableName, formData);
		    }
		}
		JOptionPane.showMessageDialog(null,
			PluginServices.getText(this, "addedInfo_msg_I")
			+ selectedElements + " "
			+ PluginServices.getText(this, "addedInfo_msg_II"));
		this.closeWindow();
	    }
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	}

    }

}
