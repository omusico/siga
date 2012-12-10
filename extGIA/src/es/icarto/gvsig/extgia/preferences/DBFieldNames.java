package es.icarto.gvsig.extgia.preferences;

public class DBFieldNames {
    public static String[] reconocimientoEstadoFields = {"n_inspeccion as \"Nº Inspección\"",
	"nombre_revisor as \"Revisor\"",
	"fecha_inspeccion as \"Fecha Inspección\"",
    "indice_estado as \"Indice Estado\""};

    public static String[] trabajoFields = {"id_trabajo as \"ID\"",
	"fecha as \"Fecha\"",
	"unidad as \"Unidad\"",
	"medicion_contratista as \"Medida Contratista\"",
    "medicion_audasa as \"Medida AUDASA\""};


    public static final String GIA_SCHEMA = "audasa_extgia";

    // TABLE NAMES
    public static final String TALUDES_TABLENAME = "audasa_extgia.taludes";
    public static final String ISLETAS_TABLENAME = "audasa_extgia.isletas";

    // TALUDES
    public static final String ID_TALUD = "id_talud";
    public static final String NUMERO_TALUD = "numero_talud";
    public static final String TIPO_TALUD = "tipo_talud";
    public static final String BASE_CONTRATISTA = "base_contratista";
    public static final String INCLINACION_MEDIA = "inclinacion_media";
    public static final String SECTOR_INCLINACION = "sector_inclinacion";

    // TALUDES RECONOCIMIENTO
    public static final String TALUDES_INDEX = "indice_estado";
    public static final String TALUDES_A = "existencia_deformaciones_o_grietas";
    public static final String TALUDES_B = "peligro_caida_materiales";
    public static final String TALUDES_C = "bajante_deteriorada";
    public static final String TALUDES_D = "elementos_proteccion_talud";

    // ISLETAS
    public static final String ID_ISLETA = "id_isleta";
    public static final String NUMERO_ISLETA = "numero_isleta";
    public static final String TIPO_ISLETA = "tipo_isleta";

}
