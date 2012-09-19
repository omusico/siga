package es.icarto.gvsig.extgex.queries;

import javax.swing.table.DefaultTableModel;

public class QueriesTableModel extends DefaultTableModel {

    @Override
    public boolean isCellEditable(int row, int col) {
	if (col == 0) {
	    return true;
	} else {
	    return false;
	}
    }

}
