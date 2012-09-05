package es.icarto.gvsig.extgia;

import java.util.ArrayList;

import javax.swing.JTable;

import es.icarto.gvsig.navtableforms.gui.tables.JTableContextualMenu;
import es.icarto.gvsig.navtableforms.gui.tables.TableModelFactory;
import es.icarto.gvsig.navtableforms.model.TableModelAlphanumeric;
import es.icarto.gvsig.navtableforms.view.AlphanumericFormView;

public class TrabajosTaludesTable {

    private static final String TABLE_NAME = "taludes_trabajos";
    private AlphanumericFormView formView;
    private JTable table;
    private JTableContextualMenu contextualMenu;
    private TaludesForm parentPanel;

    public TrabajosTaludesTable(JTable table, TaludesForm parentPanel) {
	this.table = table;
	this.parentPanel = parentPanel;
	formView = new TrabajosTaludesForm(getTableModel(null));
    }

    private ArrayList<String> getColNames() {
	ArrayList<String> colNames = new ArrayList<String>();
	colNames.add("id_trabajo");
	colNames.add("fecha");
	return colNames;
    }

    private ArrayList<String> getColAlias() {
	ArrayList<String> colAliases = new ArrayList<String>();
	colAliases.add("ID de trabajo");
	colAliases.add("Fecha");
	return colAliases;
    }

    public void setListeners() {
	contextualMenu = new JTableContextualMenu(formView);
	table.addMouseListener(contextualMenu);
    }

    public void updateTable(String rowFilterValue) {
	TableModelAlphanumeric model = getTableModel(rowFilterValue);
	table.setModel(model);
	formView.setModel(model);
	formView.setForeignKey(rowFilterValue);
	parentPanel.repaint();
    }

    private TableModelAlphanumeric getTableModel(String rowFilterValue) {

	TableModelAlphanumeric model = null;
	try {
	    AlphanumericTableLoader.loadTables();
	    model = TableModelFactory.createFromTable(
		    TrabajosTaludesTable.TABLE_NAME, "id_talud",
		    rowFilterValue, getColNames(), getColAlias());
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return model;
    }

    public void removeListeners() {
	table.removeMouseListener(contextualMenu);
    }
}
