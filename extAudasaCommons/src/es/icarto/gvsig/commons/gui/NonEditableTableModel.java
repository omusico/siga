package es.icarto.gvsig.commons.gui;

import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class NonEditableTableModel extends DefaultTableModel {

    @Override
    public boolean isCellEditable(int row, int column) {
	return false;
    }

}
