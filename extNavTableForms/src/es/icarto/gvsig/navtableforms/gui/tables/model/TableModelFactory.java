package es.icarto.gvsig.navtableforms.gui.tables.model;

import java.util.ArrayList;
import java.util.List;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.cit.gvsig.fmap.edition.IEditableSource;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.navtableforms.gui.tables.filter.IRowFilter;
import es.icarto.gvsig.navtableforms.gui.tables.filter.IRowFilterImplementer;
import es.icarto.gvsig.navtableforms.gui.tables.filter.IRowMultipleOrFilterImplementer;
import es.icarto.gvsig.navtableforms.gui.tables.filter.IRowNotFilterImplementer;
import es.icarto.gvsig.navtableforms.gui.tables.filter.MultipleKeyRowFilter;
import es.icarto.gvsig.navtableforms.utils.TOCLayerManager;
import es.icarto.gvsig.navtableforms.utils.TOCTableManager;

/**
 * TableModelFactory
 * 
 * Factory for creating table models from layers or source tables, with or
 * without filters.
 * 
 * @author Jorge L�pez Fern�ndez <jlopez@cartolab.es>
 */

public class TableModelFactory {

    public static AlphanumericTableModel createFromTable(String sourceTable,
	    String[] columnNames, String[] columnAliases) {

	TOCTableManager toc = new TOCTableManager();
	IEditableSource model = toc.getTableModelByName(sourceTable);
	return new AlphanumericTableModel(model, columnNames, columnAliases);
    }

    public static AlphanumericTableModel createFromTableWithFilter(String sourceTable,
	    String rowFilterName,
	    String rowFilterValue,
	    String[] columnNames,
	    String[] columnAliases)
		    throws ReadDriverException {

	TOCTableManager toc = new TOCTableManager();
	IEditableSource model = toc.getTableModelByName(sourceTable);
	int fieldIndex = model.getRecordset()
		.getFieldIndexByName(rowFilterName);
	IRowFilter filter = new IRowFilterImplementer(
		fieldIndex, rowFilterValue);
	return new AlphanumericTableModel(model, columnNames, columnAliases,
		filter);
    }

    public static AlphanumericTableModel createFromTableWithNotFilter(
	    String sourceTable, String rowFilterName, String rowFilterValue,
	    String[] columnNames, String[] columnAliases)
	    throws ReadDriverException {

	TOCTableManager toc = new TOCTableManager();
	IEditableSource model = toc.getTableModelByName(sourceTable);
	int fieldIndex = model.getRecordset()
		.getFieldIndexByName(rowFilterName);
	IRowFilter filter = new IRowNotFilterImplementer(
		new IRowFilterImplementer(fieldIndex, rowFilterValue));
	return new AlphanumericTableModel(model, columnNames, columnAliases,
		filter);
    }

    public static AlphanumericTableModel createFromTableWithOrFilter(
	    String sourceTable,
	    String rowFilterName, String[] rowFilterValues,
	    String[] columnNames, String[] columnAliases)
	    throws ReadDriverException {

	TOCTableManager toc = new TOCTableManager();
	IEditableSource model = toc.getTableModelByName(sourceTable);
	int fieldIndex = model.getRecordset()
		.getFieldIndexByName(rowFilterName);
	List<IRowFilter> filters = new ArrayList<IRowFilter>();
	for (String rowFilterValue : rowFilterValues) {
	    filters.add(new IRowFilterImplementer(fieldIndex, rowFilterValue));
	}
	return new AlphanumericTableModel(model, columnNames, columnAliases,
		new IRowMultipleOrFilterImplementer(filters));
    }

    public static VectorialTableModel createFromLayer(String layerName,
	    String[] columnNames, String[] columnAliases) {

	FLyrVect layer = new TOCLayerManager().getLayerByName(layerName);
	return new VectorialTableModel(layer, columnNames, columnAliases);
    }

    public static VectorialTableModel createFromLayerWithFilter(
	    String layerName, String[] destinationKey, String[] originKeyValue,
	    String[] colNames, String[] colAliases)
	    throws ReadDriverException {
	FLyrVect layer = new TOCLayerManager().getLayerByName(layerName);
	IRowFilter filter = new MultipleKeyRowFilter(layer, destinationKey, originKeyValue);
	return new VectorialTableModel(layer, colNames, colAliases, filter);
    }

    public static VectorialTableModel createFromLayerWithNotFilter(
	    String layerName, String rowFilterName, String rowFilterValue,
	    String[] columnNames, String[] columnAliases)
	    throws ReadDriverException {

	FLyrVect layer = new TOCLayerManager().getLayerByName(layerName);
	int fieldIndex = layer.getRecordset()
		.getFieldIndexByName(rowFilterName);
	IRowFilter filter = new IRowNotFilterImplementer(
		new IRowFilterImplementer(fieldIndex, rowFilterValue));
	return new VectorialTableModel(layer, columnNames, columnAliases,
		filter);
    }

    public static VectorialTableModel createFromLayerWithOrFilter(
	    String layerName, String rowFilterName, String[] rowFilterValues,
	    String[] columnNames, String[] columnAliases)
	    throws ReadDriverException {

	FLyrVect layer = new TOCLayerManager().getLayerByName(layerName);
	int fieldIndex = layer.getRecordset()
		.getFieldIndexByName(rowFilterName);
	List<IRowFilter> filters = new ArrayList<IRowFilter>();
	for (String rowFilterValue : rowFilterValues) {
	    filters.add(new IRowFilterImplementer(fieldIndex, rowFilterValue));
	}
	return new VectorialTableModel(layer, columnNames, columnAliases,
		new IRowMultipleOrFilterImplementer(filters));
    }

}
