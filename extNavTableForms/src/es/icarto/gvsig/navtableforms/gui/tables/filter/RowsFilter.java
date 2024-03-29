package es.icarto.gvsig.navtableforms.gui.tables.filter;

import java.util.ArrayList;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.Value;
import com.iver.cit.gvsig.fmap.edition.IEditableSource;
import com.iver.cit.gvsig.fmap.edition.IRowEdited;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;

public class RowsFilter {

    public static Object[][] getRowsFromSource(IEditableSource source,
	    String fieldFilterName, String fieldFilterValue)
		    throws ReadDriverException {

	ArrayList<Object[]> rows = new ArrayList<Object[]>();
	int fieldFilterIndex;
	fieldFilterIndex = source.getRecordset().getFieldIndexByName(
		fieldFilterName);
	for (int index = 0; index < source.getRowCount(); index++) {
	    IRowEdited row = source.getRow(index);
	    String value = row.getAttribute(fieldFilterIndex).toString();
	    if (value.equalsIgnoreCase(fieldFilterValue)) {
		rows.add(row.getAttributes());
	    }
	}
	return rows.toArray(new Object[1][1]);
    }

    public static Object[][] getRowsFromSource(IEditableSource source,
	    String fieldFilterName, String fieldFilterValue,
	    ArrayList<String> columnNames) throws ReadDriverException {

	ArrayList<Object[]> rows = new ArrayList<Object[]>();
	int fieldFilterIndex;
	fieldFilterIndex = source.getRecordset().getFieldIndexByName(
		fieldFilterName);
	ArrayList<Integer> columnIndexes = getIndexesOfColumns(
		source.getRecordset(), columnNames);
	ArrayList<Value> attributes = new ArrayList<Value>();
	for (int index = 0; index < source.getRowCount(); index++) {
	    IRowEdited row = source.getRow(index);
	    String value = row.getAttribute(fieldFilterIndex).toString();
	    if (value.equalsIgnoreCase(fieldFilterValue)) {
		attributes.clear();
		for (Integer idx : columnIndexes) {
		    attributes.add(row.getAttribute(idx));
		}
		rows.add(attributes.toArray());
	    }
	}
	return rows.toArray(new Object[0][0]);
    }

    public static Object[][] getRowsFromSource(SelectableDataSource source,
	    String rowFilterName, String rowFilterValue) {

	ArrayList<Object[]> rows = new ArrayList<Object[]>();
	int fieldIndex;
	try {
	    fieldIndex = source.getFieldIndexByName(rowFilterName);
	    for (int index = 0; index < source.getRowCount(); index++) {
		Value[] row = source.getRow(index);
		String indexValue = row[fieldIndex].toString();
		if (indexValue.equalsIgnoreCase(rowFilterValue)) {
		    rows.add(row);
		}
	    }
	    return rows.toArray(new Object[1][1]);
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    private static ArrayList<Integer> getIndexesOfColumns(
	    SelectableDataSource sds, ArrayList<String> columnNames) {

	ArrayList<Integer> indexes = new ArrayList<Integer>();
	for (int i = 0; i < columnNames.size(); i++) {
	    int idx;
	    try {
		idx = sds.getFieldIndexByName(columnNames.get(i));
		indexes.add(new Integer(idx));
	    } catch (ReadDriverException e) {
		e.printStackTrace();
	    }
	}
	return indexes;
    }

}
