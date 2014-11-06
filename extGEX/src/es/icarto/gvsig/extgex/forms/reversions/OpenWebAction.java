package es.icarto.gvsig.extgex.forms.reversions;

import java.awt.event.ActionEvent;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.apache.log4j.Logger;

import com.iver.andami.PluginServices;

import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.utils.DesktopApi;
import es.udc.cartolab.gvsig.navtable.AbstractNavTable;

@SuppressWarnings("serial")
public class OpenWebAction extends AbstractAction {

    private static final Logger logger = Logger.getLogger(OpenWebAction.class);
    private final AbstractNavTable nt;
    private final String type;

    public OpenWebAction(AbstractNavTable nt, String type) {
	super();
	this.nt = nt;
	this.type = type;
	putValue(SHORT_DESCRIPTION, "Abrir expediente");
	ImageIcon icon = new ImageIcon(getClass().getResource(
		"/open_web_form.png"));
	putValue(SMALL_ICON, icon);
	putValue(ACTION_COMMAND_KEY, "OPEN_WEB_FORM");
	// putValue(NAME, "buttonText");
	setEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	String expId = ((AbstractForm) nt).getFormController().getValue(
		"exp_id");
	try {
	    String ip = PluginServices.getPluginServices(
		    "es.icarto.gvsig.audasacommons").getText("internal_ip");
	    URI uri = new URI(ip + type + "/" + expId);
	    DesktopApi.browse(uri);
	} catch (URISyntaxException e1) {
	    logger.error(e1.getStackTrace(), e1);
	}
    }

}
