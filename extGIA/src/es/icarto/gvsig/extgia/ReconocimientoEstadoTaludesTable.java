package es.icarto.gvsig.extgia;

import java.util.ArrayList;

import javax.swing.JTable;

import es.icarto.gvsig.navtableforms.gui.tables.JTableContextualMenu;
import es.icarto.gvsig.navtableforms.gui.tables.TableModelFactory;
import es.icarto.gvsig.navtableforms.model.TableModelAlphanumeric;
import es.icarto.gvsig.navtableforms.view.AlphanumericFormView;

public class ReconocimientoEstadoTaludesTable {

    private AlphanumericFormView formView;
    private JTable table;
    private JTableContextualMenu contextualMenu;

    public ReconocimientoEstadoTaludesTable(JTable table) {
	this.table = table;

	formView = new ReconocimientoEstadoTaludesForm(getTableModel(null));
    }

    private ArrayList<String> getColNames() {
	ArrayList<String> colNames = new ArrayList<String>();
	colNames.add("n_inspeccion");
	colNames.add("fecha_inspeccion");
	colNames.add("indice_estado");
	// colNames.add("nombre_revisor");
	return colNames;
    }

    private ArrayList<String> getColAlias() {
	ArrayList<String> colAliases = new ArrayList<String>();
	colAliases.add("Nº Inspección");
	colAliases.add("Fecha Inspección");
	colAliases.add("Índice Estado");
	// colAliases.add("nombre_revisor");
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
	table.repaint();
    }

    private TableModelAlphanumeric getTableModel(String rowFilterValue) {

	TableModelAlphanumeric model = null;
	try {
	    AlphanumericTableLoader.loadTables();
	    model = TableModelFactory.createFromTable(
		    "taludes_reconocimiento_estado", "id_talud",
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
