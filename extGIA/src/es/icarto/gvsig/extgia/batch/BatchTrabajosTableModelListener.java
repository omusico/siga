package es.icarto.gvsig.extgia.batch;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;

import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.ValidatorDomain;

public class BatchTrabajosTableModelListener implements TableModelListener {

    private static final Logger logger = Logger
	    .getLogger(BatchTrabajosTableModelListener.class);
    private final BatchTrabajosTable trabajosTable;

    public BatchTrabajosTableModelListener(BatchTrabajosTable trabajosTable) {
	this.trabajosTable = trabajosTable;
    }

    // SelectModel -> SingleRow
    // Hacer más ancha la ventana y autoajustar las columnas
    @Override
    public void tableChanged(TableModelEvent e) {
	if (e.getType() != TableModelEvent.UPDATE) {
	    logger.warn("Should never happen: " + e.getType());
	    return;
	}

	trabajosTable.getSaveButton().setEnabled(validate());
    }

    public boolean validate() {
	TableModel model = trabajosTable.getTable().getModel();

	boolean allValid = true;
	for (int row = 0; row < model.getRowCount(); row++) {
	    for (int col = 0; col < model.getColumnCount(); col++) {
		if (model.isCellEditable(row, col)) {
		    allValid = allValid && isValid(model, row, col);

		}
	    }
	}
	return allValid;
    }

    private boolean isValid(TableModel model, int row, int col) {
	Object valueObj = model.getValueAt(row, col);
	String value = (valueObj == null) ? "" : valueObj.toString().trim();
	final String column = trabajosTable.getColumnDbNames()[col];
	ValidatorDomain validator = trabajosTable.getOrmLite().getAppDomain()
		.getDomainValidatorForComponent(column);
	if (validator != null) {
	    boolean validate = validator.validate(value);
	    if (!validate) {
		return false;
	    }
	}

	return true;
    }

}
