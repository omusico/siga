package es.icarto.gvsig.extgia.batch;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import es.icarto.gvsig.extgia.forms.AbstractFormWithLocationWidgets;
import es.icarto.gvsig.extgia.forms.LaunchGIAForms;
import es.icarto.gvsig.navtableforms.gui.tables.handler.BaseTableHandler;

public class AddTrabajosBatchListener implements ActionListener {

    private final String element;
    private final String formFileName;
    private final String dbTableName;
    private final BaseTableHandler trabajosTableHandler;
    private final AbstractFormWithLocationWidgets form;

    public AddTrabajosBatchListener(AbstractFormWithLocationWidgets form) {
	this.form = form;
	this.element = form.getElement().name();
	this.formFileName = "forms/" + form.getBasicName() + "_trabajos.jfrm";
	this.dbTableName = form.getTrabajosDBTableName();
	this.trabajosTableHandler = getTrabajosTableHandler();
    }

    private BaseTableHandler getTrabajosTableHandler() {
	BaseTableHandler trabajosTableHandler = null;
	for (BaseTableHandler th : form.getTableHandlers()) {
	    if (th.getJTable().getName().equals(form.getTrabajosDBTableName())) {
		trabajosTableHandler = th;
		break;
	    }
	}
	return trabajosTableHandler;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	LaunchGIAForms.callBatchTrabajosSubFormDependingOfElement(element,
		formFileName, dbTableName, trabajosTableHandler);

    }

}
