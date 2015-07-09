package es.icarto.gvsig.extgia.forms.utils;

import es.icarto.gvsig.audasacommons.PreferencesPage;
import es.icarto.gvsig.navtableforms.gui.tables.AbstractSubForm;

@SuppressWarnings("serial")
public class GIASubForm extends AbstractSubForm {

    public GIASubForm(String basicName) {
	super(basicName);
	initGUIPost();
    }

    public GIASubForm() {
	super();
	initGUIPost();
    }

    protected void initGUIPost() {
	addImageHandler("image", PreferencesPage.AUDASA_ICON);
	getWindowInfo().setTitle("");
    }

}
