package es.udc.cartolab.gvsig.elle.gui.wizard.load;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListModel;

import org.apache.log4j.Logger;

import com.iver.andami.PluginServices;
import com.jeta.forms.components.panel.FormPanel;
import com.jeta.forms.gui.common.FormException;

import es.udc.cartolab.gvsig.elle.constants.ConstantUtils;
import es.udc.cartolab.gvsig.elle.utils.ELLEMap;

@SuppressWarnings("serial")
public class ConstantsPanel extends JPanel {

    private static final Logger logger = Logger.getLogger(ConstantsPanel.class);
    private JList valuesList;

    public ConstantsPanel() {
	setUpUI();
	setUpData();
	preselectConstants();
    }

    private void setUpUI() {
	FormPanel form = null;
	try {
	    InputStream stream = getClass().getClassLoader()
		    .getResourceAsStream("forms/loadConstants.jfrm");
	    form = new FormPanel(stream);
	} catch (FormException e) {
	    logger.error(e.getStackTrace(), e);
	    return;
	}

	JLabel constantsLabel = form.getLabel("constantsLabel");
	constantsLabel.setText(PluginServices.getText(this, "constants_load"));

	valuesList = form.getList("valuesList");
	this.add(form);
    }

    private void setUpData() {
	final String[] valueListData = ConstantUtils
		.getValuesFromConstantByQuery();
	valuesList.setListData(valueListData);
    }

    private void preselectConstants() {
	List<String> constants = ELLEMap.getConstantValuesSelected();
	if (constants.isEmpty()) {
	    return;
	}

	ListModel model = valuesList.getModel();
	List<Integer> indexes = new ArrayList<Integer>();
	for (int i = 0; i < model.getSize(); i++) {
	    final String displayedText = (String) model.getElementAt(i);
	    String ineCode = ConstantUtils.getIdByConstantTag(displayedText);
	    if (constants.contains(ineCode)) {
		indexes.add(i);
	    }
	}

	if (!indexes.isEmpty()) {
	    int[] idx = new int[indexes.size()];
	    for (int j = 0; j < indexes.size(); j++) {
		idx[j] = indexes.get(j).intValue();
	    }
	    valuesList.setSelectedIndices(idx);
	}
    }

    public List<String> getCouncils() {
	List<String> values = new ArrayList<String>();
	for (Object o : valuesList.getSelectedValues()) {
	    values.add(ConstantUtils.getIdByConstantTag(o.toString()));
	}
	return values;
    }

}
