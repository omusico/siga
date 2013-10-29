package es.icarto.gvsig.extgia.batch.elements;

import com.iver.andami.ui.mdiManager.WindowInfo;

import es.icarto.gvsig.extgia.batch.BatchAbstractSubForm;
import es.icarto.gvsig.extgia.forms.muros.MurosCalculateIndiceEstado;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class BatchMurosReconocimientos extends BatchAbstractSubForm {

    public BatchMurosReconocimientos(String formFile, String dbTableName) {
	super(formFile, dbTableName);

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
    public String getLayerName() {
	return DBFieldNames.MUROS_LAYERNAME;
    }

    @Override
    public String getIdFieldName() {
	return DBFieldNames.ID_MUROS;
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
