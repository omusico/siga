package es.icarto.gvsig.extgia.batch;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import es.icarto.gvsig.extgia.forms.utils.LaunchGIAForms;

public class AddTrabajosBatchListener implements ActionListener {

    private final String element;
    private final String formFileName;
    private final String dbTableName;

    public AddTrabajosBatchListener(String element, String formFileName, String dbTableName) {
	this.element = element;
	this.formFileName = formFileName;
	this.dbTableName = dbTableName;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	LaunchGIAForms.callBatchTrabajosSubFormDependingOfElement(element,
		formFileName, dbTableName);

    }

}
