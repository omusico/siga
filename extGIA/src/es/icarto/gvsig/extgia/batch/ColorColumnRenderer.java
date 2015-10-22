package es.icarto.gvsig.extgia.batch;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
public class ColorColumnRenderer extends DefaultTableCellRenderer {

    private static final Color notValidColor = Color.RED;
    private final BatchTrabajosTableCalculation validator;

    private final Color nonEditableColumnForegndColor = Color.gray;

    public ColorColumnRenderer(BatchTrabajosTableCalculation validator) {
	super();
	this.validator = validator;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
	    boolean isSelected, boolean hasFocus, int row, int column) {
	// Forces to recalculate the default background and foregrounds value of
	// the cells
	setBackground(null);
	setForeground(null);

	super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
		row, column);

	if (!validator.isValid(row, column)) {
	    setBackground(notValidColor);
	} else if (!table.isCellEditable(row, column)) {
	    // default font is always recalculated in super
	    setFont(getFont().deriveFont(Font.ITALIC));
	    setForeground(nonEditableColumnForegndColor);
	}

	return this;
    }
}