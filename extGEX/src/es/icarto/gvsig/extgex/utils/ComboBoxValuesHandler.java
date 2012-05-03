package es.icarto.gvsig.extgex.utils;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JComboBox;

import es.icarto.gvsig.navtableforms.ormlite.ORMLite;
import es.icarto.gvsig.navtableforms.ormlite.domain.DomainValues;
import es.icarto.gvsig.navtableforms.ormlite.domain.KeyValue;

public class ComboBoxValuesHandler implements ItemListener {

    private ArrayList<JComboBox> cbParents;
    private JComboBox cbToFill;
    private String xmlFilePath;

    public ComboBoxValuesHandler(String xmlFilePath, JComboBox cbToFill, ArrayList<JComboBox> cbParents) {
	this.xmlFilePath = xmlFilePath;
	this.cbParents = cbParents;
	this.cbToFill = cbToFill;
    }

    public ComboBoxValuesHandler(String xmlFilePath, JComboBox cbToFill, JComboBox cbParent) {
	this.xmlFilePath = xmlFilePath;
	this.cbToFill = cbToFill;
	this.cbParents = new ArrayList<JComboBox>();
	this.cbParents.add(cbParent);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
	if((e.getStateChange() == ItemEvent.SELECTED) &&
		allParentsHaveProperValue()) {
	    DomainValues dv = ORMLite.getAplicationDomainObject(xmlFilePath)
		    .getDomainValuesForComponent(cbToFill.getName());
	    ArrayList<String> fk = getForeignKeys();
	    if(dv.getValuesFilteredBy(fk).size() > 0) {
		cbToFill.setEnabled(true);
		cbToFill.removeAllItems();
		Collections.sort((List<KeyValue>) dv.getValues(),
			new Comparator<KeyValue>() {
		    public int compare(KeyValue a, KeyValue b) {
			int aInt = Integer.parseInt(a.getKey());
			int bInt = Integer.parseInt(b.getKey());
			return (aInt >= bInt) ? 1 : -1;
		    }
		});
		for (KeyValue kv : dv.getValuesFilteredBy(fk)) {
		    cbToFill.addItem(kv);
		}
		cbToFill.setSelectedIndex(0);
	    } else {
		cbToFill.removeAllItems();
		cbToFill.addItem("");
		cbToFill.setSelectedIndex(0);
		cbToFill.setEnabled(false);
	    }
	}
    }

    private ArrayList<String> getForeignKeys(){
	ArrayList<String> fks = new ArrayList<String>();
	for (JComboBox cb : cbParents) {
	    KeyValue valueSelected = (KeyValue) cb.getSelectedItem();
	    fks.add(valueSelected.getKey());
	}
	return fks;
    }

    private boolean allParentsHaveProperValue(){
	for(JComboBox cb : cbParents) {
	    if(!(cb.getSelectedItem() instanceof KeyValue)) {
		return false;
	    }
	}
	return true;
    }

}
