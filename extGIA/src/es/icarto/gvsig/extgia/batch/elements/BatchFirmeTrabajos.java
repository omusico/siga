package es.icarto.gvsig.extgia.batch.elements;

import com.iver.andami.ui.mdiManager.WindowInfo;

import es.icarto.gvsig.extgia.batch.BatchAbstractSubForm;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class BatchFirmeTrabajos extends BatchAbstractSubForm {

    public BatchFirmeTrabajos(String formFile, String dbTableName) {
	super(formFile, dbTableName);
	// TODO Auto-generated constructor stub
    }

    @Override
    public String getLayerName() {
	return DBFieldNames.FIRME_LAYERNAME;
    }

    @Override
    public String getIdFieldName() {
	return DBFieldNames.ID_FIRME;
    }

    @Override
    public String getXMLPath() {
	return this.getClass().getClassLoader()
		.getResource("rules/firme_trabajos_metadata.xml")
		.getPath();
    }

    @Override
    public WindowInfo getWindowInfo() {
	viewInfo = new WindowInfo(WindowInfo.MODALDIALOG);
	viewInfo.setWidth(700);
	viewInfo.setHeight(800);
	return viewInfo;
    }
}
