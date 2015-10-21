package es.icarto.gvsig.extgia.batch;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import org.apache.log4j.Logger;

import com.toedter.calendar.JDateChooser;

@SuppressWarnings("serial")
public class DateCellEditor extends AbstractCellEditor implements
TableCellEditor {

    private static final Logger logger = Logger.getLogger(DateCellEditor.class);

    final JDateChooser dateChooser = new JDateChooser();
    final SimpleDateFormat dateFormat;

    public DateCellEditor(SimpleDateFormat dateFormat) {
	this.dateFormat = dateFormat;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
	    boolean isSelected, int row, int column) {
	dateChooser.setDateFormatString(dateFormat.toPattern());
	dateChooser.getDateEditor().setEnabled(false);
	final JComponent uiComponent = dateChooser.getDateEditor()
		.getUiComponent();
	uiComponent.setBackground(new Color(255, 255, 255));
	uiComponent.setFont(new Font("Arial", Font.PLAIN, 11));
	uiComponent.setToolTipText(null);
	if (!value.toString().isEmpty()) {
	    try {
		Date date = dateFormat.parse(value.toString());
		dateChooser.setDate(date);
	    } catch (ParseException e) {
		logger.error(e.getStackTrace(), e);
	    }
	}
	return dateChooser;
    }

    @Override
    public Object getCellEditorValue() {
	final Date date = dateChooser.getDate();
	if (date != null) {
	    return dateFormat.format(date);
	}
	return "";
    }
}