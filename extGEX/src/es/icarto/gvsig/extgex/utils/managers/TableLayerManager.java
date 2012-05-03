package es.icarto.gvsig.extgex.utils.managers;

import java.util.ArrayList;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.project.documents.table.gui.Table;

public class TableLayerManager {

    private ArrayList<Table> tables;

    public TableLayerManager() {
	tables = initTables();
    }

    private ArrayList<Table> initTables(){
	tables = new ArrayList<Table>();
	IWindow[] windows = PluginServices.getMDIManager().getAllWindows();
	for (IWindow w : windows) {
	    if(w instanceof Table) {
		tables.add((Table) w);
	    }
	}
	return tables;
    }

    public Table getTableByName(String tableName){
	for (Table t : tables) {
	    if (t.getModel().getName().equalsIgnoreCase(tableName)) {
		return t;
	    }
	}
	return null;
    }
}
