package es.icarto.gvsig.extgia.forms;

import es.icarto.gvsig.extgia.forms.obras_desague.ObrasDesagueForm;
import es.icarto.gvsig.extgia.navtableforms.CommonMethodsForTestDBForms;

public class TestObrasDesagueForm extends CommonMethodsForTestDBForms {

    @Override
    protected String getAbeilleForm() {
	return ObrasDesagueForm.ABEILLE_FILENAME;
    }

    @Override
    protected String getXmlFile() {
	return "rules/obras_desague_metadata.xml";
    }

    @Override
    protected String getTable() {
	return "obras_desague";
    }

}
