package es.icarto.gvsig.extgia.forms.muros;

import javax.swing.JTable;

import com.iver.andami.ui.mdiManager.WindowInfo;

import es.icarto.gvsig.extgia.forms.utils.AbstractSubForm;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class MurosReconocimientosSubForm extends AbstractSubForm {

    public MurosReconocimientosSubForm(String formFile, String dbTableName,
	    JTable embebedTable, String idElementField, String idElementValue,
	    String idField, String idValue, boolean edit) {
	super(formFile, dbTableName, embebedTable, idElementField, idElementValue,
		idField, idValue, edit);

	MurosCalculateIndiceEstado index = new MurosCalculateIndiceEstado(
		this,
		this.getWidgetsVector(),
		DBFieldNames.MUROS_INDEX,
		DBFieldNames.MUROS_A,
		DBFieldNames.MUROS_B,
		DBFieldNames.MUROS_C,
		DBFieldNames.MUROS_D,
		DBFieldNames.MUROS_E,
		DBFieldNames.MUROS_F,
		DBFieldNames.MUROS_G,
		DBFieldNames.MUROS_H,
		DBFieldNames.MUROS_I,
		DBFieldNames.MUROS_J);
	index.setListeners();
    }

    @Override
    public String getXMLPath() {
	return this.getClass().getClassLoader()
		.getResource("rules/muros_reconocimientos_metadata.xml")
		.getPath();
    }

    @Override
    public WindowInfo getWindowInfo() {
	super.getWindowInfo();
	viewInfo.setHeight(540);
	return viewInfo;
    }
}
