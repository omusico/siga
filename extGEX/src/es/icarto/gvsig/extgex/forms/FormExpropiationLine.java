package es.icarto.gvsig.extgex.forms;

import java.io.InputStream;

import javax.swing.ImageIcon;

import org.apache.log4j.Logger;

import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.image.ImageComponent;
import com.jeta.forms.components.panel.FormPanel;
import com.jeta.forms.gui.common.FormException;

import es.icarto.gvsig.audasacommons.PreferencesPage;
import es.icarto.gvsig.navtableforms.AbstractForm;

@SuppressWarnings("serial")
public class FormExpropiationLine extends AbstractForm {

    private static final Logger logger = Logger
	    .getLogger(FormExpropiationLine.class);

    public static final String LAYER_TOC_NAME = "Linea_Expropiacion";
    public static final String NAME = "linea_expropiacion";

    public FormExpropiationLine(FLyrVect layer) {
	super(layer);
    }

    @Override
    public FormPanel getFormBody() {
	if (formBody == null) {
	    InputStream stream = getClass().getClassLoader()
		    .getResourceAsStream(getBasicName() + ".xml");
	    try {
		formBody = new FormPanel(stream);
		ImageComponent image = (ImageComponent) formBody
			.getComponentByName("image");
		ImageIcon icon = new ImageIcon(PreferencesPage.AUDASA_ICON);
		image.setIcon(icon);
	    } catch (FormException e) {
		logger.error(e.getStackTrace(), e);
	    }
	}
	return formBody;
    }

    @Override
    public String getXMLPath() {
	return this.getClass().getClassLoader()
		.getResource("data/" + getBasicName() + ".xml").getPath();
    }

    public String getBasicName() {
	return NAME;
    }

}
