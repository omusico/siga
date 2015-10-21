package es.icarto.gvsig.extgia.batch;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.apache.log4j.Logger;

import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.ValidatorDomain;
import es.udc.cartolab.gvsig.navtable.format.DoubleFormatNT;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class BatchTrabajosTableCalculation implements TableModelListener {

    private static final Logger logger = Logger
	    .getLogger(BatchTrabajosTableCalculation.class);

    private static NumberFormat format = DoubleFormatNT.getDisplayingFormat();

    private final BatchTrabajosTable tTable;
    private final JTable table;

    private int unidadColIdx = -1;
    private int longColIdx = -1;
    private int anchoColIdx = -1;
    private int medicionColIdx = -1;
    private int medicionElementoColIdx = -1;
    private int medicionLastJobColIdx = -1;
    private int fechaColIdx = -1;

    private final String dbTableName;
    private final String idElemento;

    private int obsColIdx;

    public BatchTrabajosTableCalculation(BatchTrabajosTable ttable,
	    String dbTableName, String idElemento) {
	this.tTable = ttable;
	this.table = ttable.getTable();
	this.dbTableName = dbTableName;
	this.idElemento = idElemento;
	initColumnsIdx();
    }

    private void initColumnsIdx() {
	unidadColIdx = table.getColumn("Unidad").getModelIndex();
	medicionColIdx = table.getColumn("Medición").getModelIndex();
	if (!dbTableName.equals("senhalizacion_vertical_trabajos")) {
	    longColIdx = table.getColumn("Longitud").getModelIndex();
	    anchoColIdx = table.getColumn("Ancho").getModelIndex();
	    if (!dbTableName.equals("barrera_rigida_trabajos")) {
		medicionElementoColIdx = table.getColumn("Medición elemento")
			.getModelIndex();
	    }
	    medicionLastJobColIdx = table.getColumn("Medición último trabajo")
		    .getModelIndex();
	}
	fechaColIdx = table.getColumn("Fecha").getModelIndex();
	obsColIdx = table.getColumn("Observaciones").getModelIndex();
    }

    public void updateRow(int row) {
	if (row == -1) {
	    logger.warn("Should never happen");
	    return;
	}

	if (dbTableName.equals("senhalizacion_vertical_trabajos")) {
	    return;
	}

	final String unidad = strValue(row, unidadColIdx);
	String medicionLastJob = getMedicionLastJobValueByUnit(row, unidad);
	String medicion = "";

	if (unidad.equals("Herbicida")) {
	    medicion = calculateMedicionHerbicida(row);
	} else {

	    if (((medicionLastJob == null) || medicionLastJob.isEmpty())
		    && (medicionElementoColIdx != -1)) {
		medicion = strValue(row, medicionElementoColIdx);
	    } else {
		medicion = medicionLastJob;
	    }
	}
	table.setValueAt(medicionLastJob, row, medicionLastJobColIdx);
	table.setValueAt(medicion, row, medicionColIdx);
    }

    public void updateAllRows() {
	for (int row = 0; row < table.getRowCount(); row++) {
	    updateRow(row);
	}
	table.repaint();
    }

    private String strValue(int row, int col) {
	Object o = table.getValueAt(row, col);
	return (o == null) ? "" : o.toString().trim();
    }

    private double doubleValue(int row, int col) throws ParseException {
	Object o = table.getValueAt(row, col);
	if (o != null) {
	    Number s = format.parse(o.toString());
	    return s.doubleValue();
	}
	return 0;
    }

    private String calculateMedicionHerbicida(int row) {
	String medicion = "";
	try {
	    double longitud = doubleValue(row, longColIdx);
	    double ancho = doubleValue(row, anchoColIdx);
	    medicion = format.format(longitud * ancho);
	} catch (ParseException e) {
	}

	return medicion;
    }

    private String getMedicionLastJob(int row) {
	String whereClause = "WHERE " + idElemento + " = " + "'"
		+ strValue(row, 0) + "'";
	try {
	    String[][] table2 = DBSession.getCurrentSession().getTable(
		    dbTableName, DBFieldNames.GIA_SCHEMA,
		    new String[] { DBFieldNames.MEDICION }, whereClause,
		    new String[] { "fecha", "id_trabajo" }, true);
	    if (table2.length > 0) {
		if ((table2[0].length > 0) && (table2[0][0] != null)) {
		    return table2[0][0];
		}
	    }

	} catch (SQLException e) {
	    logger.error(e.getStackTrace(), e);
	}
	return "";
    }

    // TODO: This method is copied from CalculateDBForeignValueLastJob class.
    private String getMedicionLastJobValueByUnit(int row, String unidad) {
	if ((unidad != null) && unidad.isEmpty()) {
	    return "";
	}
	try {
	    String whereClause = "WHERE " + idElemento + " = " + "'"
		    + strValue(row, 0) + "'";
	    if (unidad != null) {
		whereClause += " AND unidad = '" + unidad + "'";
	    }

	    // String orderBy = " ORDER BY fecha DESC, id_trabajo DESC LIMIT 1";

	    String[][] table2 = DBSession.getCurrentSession().getTable(
		    dbTableName, DBFieldNames.GIA_SCHEMA,
		    new String[] { DBFieldNames.MEDICION }, whereClause,
		    new String[] { "fecha", "id_trabajo" }, true);
	    if (table2.length > 0) {
		if ((table2[0].length > 0) && (table2[0][0] != null)) {
		    return table2[0][0];
		}
	    } else {
		return getMedicionLastJob(row);
	    }

	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return "";
    }

    @Override
    public void tableChanged(TableModelEvent e) {
	if (e.getType() != TableModelEvent.UPDATE) {
	    logger.warn("Should never happen: " + e.getType());
	    return;
	}
	final int row = e.getFirstRow();
	if (row != e.getLastRow()) {
	    String msg = String.format(
		    "Should never happen. 1row: %d, 2row:%d", row,
		    e.getLastRow());
	    logger.warn(msg);
	    return;
	}

	if ((e.getColumn() != medicionColIdx) && (e.getColumn() != fechaColIdx)
		&& (e.getColumn() != obsColIdx)) {
	    updateRow(row);
	}
	tTable.getSaveButton().setEnabled(validate());
    }

    public boolean validate() {
	boolean allValid = true;
	for (int row = 0; row < table.getRowCount(); row++) {
	    for (int col = 0; col < table.getColumnCount(); col++) {
		if (table.isCellEditable(row, col)) {
		    allValid = allValid && isValid(row, col);
		}
	    }
	}
	return allValid;
    }

    public boolean isValid(int row, int col) {
	final String column = tTable.getColumnDbNames()[col];
	ValidatorDomain validator = tTable.getOrmLite().getAppDomain()
		.getDomainValidatorForComponent(column);
	if (validator != null) {
	    String value = strValue(row, col);
	    boolean validate = validator.validate(value);
	    if (!validate) {
		return false;
	    }
	}

	return true;
    }

}
