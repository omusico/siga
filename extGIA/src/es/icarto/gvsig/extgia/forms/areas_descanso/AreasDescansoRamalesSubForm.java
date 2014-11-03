package es.icarto.gvsig.extgia.forms.areas_descanso;

import es.icarto.gvsig.extgia.forms.utils.BasicAbstractSubForm;

@SuppressWarnings("serial")
public class AreasDescansoRamalesSubForm extends BasicAbstractSubForm {

    public static String[] colNames = { "id_ramal", "ramal", "sentido_ramal",
	    "longitud" };

    public static String[] colAlias = { "ID Ramal", "Nombre Ramal", "Sentido",
	    "Longitud" };

    @Override
    protected String getBasicName() {
	return "areas_descanso_ramales";
    }

}
