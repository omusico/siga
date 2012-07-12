package es.icarto.gvsig.extpm.forms;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.extgex.navtable.NavTableComponentsFactory;
import es.icarto.gvsig.extpm.preferences.Preferences;
import es.icarto.gvsig.navtableforms.AbstractForm;
import es.udc.cartolab.gvsig.navtable.AbstractNavTable;

public class FormPM extends AbstractForm {
    
    private FormPanel form;
    private FLyrVect layer;
    
    public FormPM(FLyrVect layer) {
	super(layer);
	this.layer = layer;
	initWindow();
	addNewButtonsToActionsToolBar();
    }

    private void addNewButtonsToActionsToolBar() {
	JPanel actionsToolBar = this.getActionsToolBar();
	NavTableComponentsFactory ntFactory = new NavTableComponentsFactory();
	JButton filesLinkB = ntFactory.getFilesLinkButton(layer,
		(AbstractNavTable) this);
	if (filesLinkB != null) {
	    actionsToolBar.add(filesLinkB);
	}
    }
    
    @Override
    public String getXMLPath() {
	return PluginServices.getPluginServices("es.icarto.gvsig.extpm")
		.getClassLoader()
		.getResource(Preferences.XML_ORMLITE_RELATIVE_PATH).getPath();
    }
    
    private void initWindow() {
	viewInfo.setHeight(830);
	viewInfo.setWidth(700);
	viewInfo.setTitle("Policía de Márgenes");
    }

    @Override
    protected void fillSpecificValues() {
	// TODO Auto-generated method stub
	
    }

    @Override
    public FormPanel getFormBody() {
	if (form == null) {
	    return new FormPanel("pm.xml");
	}
	return form;
    }

    @Override
    public Logger getLoggerName() {
	// TODO Auto-generated method stub
	return null;
    }

}
