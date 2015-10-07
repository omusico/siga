package es.icarto.gvsig.extgia.batch;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import es.icarto.gvsig.extgia.forms.LaunchGIAForms;

public class AddReconocimientosBatchListener implements ActionListener {

    private final String element;
    private final String formFileName;
    private final String dbTableName;

    public AddReconocimientosBatchListener(String element, String formFileName, String dbTableName) {
	this.element = element;
	this.formFileName = formFileName;
	this.dbTableName = dbTableName;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	LaunchGIAForms.callBatchReconocimientosSubFormDependingOfElement(element,
		formFileName, dbTableName);
    }

}
