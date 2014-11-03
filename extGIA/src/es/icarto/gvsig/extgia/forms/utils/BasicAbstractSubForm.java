package es.icarto.gvsig.extgia.forms.utils;

import javax.swing.ImageIcon;

import com.iver.andami.PluginServices;
import com.jeta.forms.components.image.ImageComponent;

import es.icarto.gvsig.audasacommons.PreferencesPage;
import es.icarto.gvsig.navtableforms.gui.tables.AbstractSubForm;

@SuppressWarnings("serial")
public abstract class BasicAbstractSubForm extends AbstractSubForm {

    public BasicAbstractSubForm() {
	super();
	initGUIPost();
    }

    protected void initGUIPost() {

	ImageComponent image = (ImageComponent) getFormPanel()
		.getComponentByName("image");
	ImageIcon icon = new ImageIcon(PreferencesPage.AUDASA_ICON);
	image.setIcon(icon);
    }

    public void closeWindow() {
	PluginServices.getMDIManager().closeWindow(this);
    }
}
