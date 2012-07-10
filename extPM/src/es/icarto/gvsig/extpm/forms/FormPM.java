package es.icarto.gvsig.extpm.forms;

import org.apache.log4j.Logger;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.extpm.preferences.PreferencesPage;
import es.icarto.gvsig.navtableforms.AbstractForm;

public class FormPM extends AbstractForm {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private FormPanel form;
    
    public FormPM(FLyrVect layer) {
	super(layer);
	initWindow();
    }

    @Override
    public String getXMLPath() {
	return PluginServices.getPluginServices("es.icarto.gvsig.extpm")
		.getClassLoader()
		.getResource(PreferencesPage.XML_ORMLITE_RELATIVE_PATH).getPath();
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
