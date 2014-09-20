package es.icarto.gvsig.navtableforms.gui;

import es.icarto.gvsig.commons.gui.NonEditableTableModel;
import es.icarto.gvsig.commons.queries.Field;

@SuppressWarnings("serial")
public class CustomTableModel extends NonEditableTableModel {

    public String getColumnKey(int colIndex) {
	return ((Field) columnIdentifiers.get(colIndex)).getKey();
    }

}
