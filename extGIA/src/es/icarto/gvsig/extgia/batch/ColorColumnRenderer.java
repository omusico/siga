package es.icarto.gvsig.extgia.batch;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
public class ColorColumnRenderer extends DefaultTableCellRenderer {

    private static final Color notValidColor = Color.RED;
    private final BatchTrabajosTableCalculation validator;

    private final Color nonEditableColumnForegndColor = Color.LIGHT_GRAY;

    public ColorColumnRenderer(BatchTrabajosTableCalculation validator) {
	super();
	this.validator = validator;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
	    boolean isSelected, boolean hasFocus, int row, int column) {
	Component cell = super.getTableCellRendererComponent(table, value,
		isSelected, hasFocus, row, column);

	if (!validator.isValid(row, column)) {
	    cell.setForeground(notValidColor);
	} else if (!table.isCellEditable(row, column)) {
	    cell.setForeground(nonEditableColumnForegndColor);
	} else {
	    cell.setForeground(table.getForeground());
	}

	return cell;
    }
}