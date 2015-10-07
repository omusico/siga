package es.icarto.gvsig.extgia.batch;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import es.icarto.gvsig.extgia.forms.LaunchGIAForms;
import es.icarto.gvsig.extgia.preferences.DBFieldNames.Elements;
import es.icarto.gvsig.navtableforms.gui.tables.handler.BaseTableHandler;

public class AddTrabajosBatchListener implements ActionListener {

    private final String element;
    private final String formFileName;
    private final String dbTableName;
    private final BaseTableHandler trabajosTableHandler;

    public AddTrabajosBatchListener(Elements element, String formFileName,
	    String dbTableName, BaseTableHandler trabajosTableHandler) {
	this.element = element.name();
	this.formFileName = formFileName;
	this.dbTableName = dbTableName;
	this.trabajosTableHandler = trabajosTableHandler;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	LaunchGIAForms.callBatchTrabajosSubFormDependingOfElement(element,
		formFileName, dbTableName, trabajosTableHandler);

    }

}
