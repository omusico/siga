package es.icarto.gvsig.extgia.forms.areas_descanso;

import es.icarto.gvsig.extgia.forms.utils.BasicAbstractSubForm;

@SuppressWarnings("serial")
public class AreasDescansoTrabajosSubForm extends BasicAbstractSubForm {

    public static String[] colNames = { "id_trabajo", "fecha_certificado",
	    "unidad", "medicion_audasa", "observaciones" };

    public static String[] colAlias = { "ID", "Fecha cert", "Unidad",
	    "Medición AUDASA", "Observaciones" };

    @Override
    protected String getBasicName() {
	return "areas_descanso_trabajos";
    }

}
