package es.icarto.gvsig.extgex.locators.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.extgex.forms.FormExpropiations;
import es.icarto.gvsig.extgex.preferences.DBNames;
import es.icarto.gvsig.extgex.utils.managers.TOCLayerManager;

public class FormOpener implements ActionListener {

    private final IPositionRetriever retriever;

    public FormOpener(IPositionRetriever retriever) {
	this.retriever = retriever;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
	TOCLayerManager toc = new TOCLayerManager();
	FLyrVect layer = toc.getLayerByName(DBNames.LAYER_FINCAS);
	//	if(AlphanumericTableLoader.loadTables() &&
	//		(layer != null)) {
	if (layer != null) {
	    FormExpropiations form = new FormExpropiations(layer, null);
	    if(form.init()) {
		form.setPosition(retriever.getPosition());
		PluginServices.getMDIManager().addWindow(form);
	    }
	} else {
	    JOptionPane.showMessageDialog(null, PluginServices.getText(this,
		    "alphanumeric_table_no_loaded"));
	}
    }

}
