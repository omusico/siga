package es.icarto.gvsig.extgex.queries;

import javax.swing.table.DefaultTableModel;

public class QueriesTableModel extends DefaultTableModel {

    public Class getColumnClass(int index) {
	if (index == 0) {
	    return Boolean.class;
	} else {
	    return super.getColumnClass(index);
	}
    }

    public boolean isCellEditable(int row, int col) {
	if (col == 0) {
	    return true;
	} else {
	    return false;
	}
    }

}
