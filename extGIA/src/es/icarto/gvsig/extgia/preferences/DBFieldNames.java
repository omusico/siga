package es.icarto.gvsig.extgia.preferences;

public class DBFieldNames {

    public static String[] genericReconocimientoEstadoFields = {"n_inspeccion as \"Nº Inspección\"",
	"nombre_revisor as \"Revisor\"",
	"fecha_inspeccion as \"Fecha Inspección\"",
    "indice_estado as \"Indice Estado\""};

    public static String[] enlacesReconocimientoEstadoFields = {"n_inspeccion as \"Nº Inspección\"",
	"nombre_revisor as \"Revisor\"",
    "fecha_inspeccion as \"Fecha Inspección\""};

    public static String[] reconocimientoEstadoFields = {"n_inspeccion as \"Nº Inspección\"",
	"nombre_revisor as \"Revisor\"",
	"fecha_inspeccion as \"Fecha Inspección\"",
    "indice_estado as \"Indice Estado\""};

    public static String[] trabajoFields = {"id_trabajo as \"ID\"",
	"fecha as \"Fecha\"",
	"unidad as \"Unidad\"",
	"medicion_contratista as \"Medida Contratista\"",
    "medicion_audasa as \"Medida AUDASA\""};

    public static String[] carreteras_enlazadas = {"id_carretera_enlazada as \"ID Carretera\"",
	"clave_carretera as \"Clave\"",
	"pk as \"PK\"",
	"titular as \"Titular\"",
    "tipo_cruce as \"Tipo Cruce\""};

    public static String[] generic_ramales = {"id_ramal as \"ID Ramal\"",
	"ramal as \"Nombre Ramal\"",
	"sentido_ramal as \"Sentido\"",
	"direccion_ramal as \"Dirección\"",
	"longitud as \"Longitud\""
    };

    public static String[] ramales = {"id_ramal as \"ID Ramal\"",
	"ramal as \"Nombre Ramal\"",
	"sentido_ramal as \"Sentido\"",
	"direccion_ramal as \"Dirección\"",
	"longitud as \"Longitud\""
    };

    public static String[] ramales_area_descanso = {"id_ramal as \"ID Ramal\"",
	"ramal as \"Nombre Ramal\"",
	"sentido_ramal as \"Sentido\"",
	"longitud as \"Longitud\""
    };

    public static final String GIA_SCHEMA = "audasa_extgia";

    // LOCATION
    public static final String AREA_MANTENIMIENTO = "area_mantenimiento";
    public static final String BASE_CONTRATISTA = "base_contratista";
    public static final String TRAMO = "tramo";
    public static final String TIPO_VIA = "tipo_via";

    // TABLE NAMES
    public static final String TALUDES_TABLENAME = "audasa_extgia.taludes";
    public static final String ISLETAS_TABLENAME = "audasa_extgia.isletas";

    // TALUDES
    public static final String ID_TALUD = "id_talud";
    public static final String NUMERO_TALUD = "numero_talud";
    public static final String TIPO_TALUD = "tipo_talud";
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

    // ISLETAS RECONOCIMIENTO
    public static final String ISLETAS_INDEX = "indice_estado";
    public static final String ISLETAS_A = "estado_siega";

    // ENLACES
    public static final String ID_ENLACE = "id_enlace";
    public static final String MUNICIPIO = "municipio";
    public static final String PK = "pk";

    // BARRERA RIGIDA
    public static final String ID_BARRERA_RIGIDA = "id_barrera_rigida";
    public static final String NUMERO_BARRERA_RIGIDA = "numero_barrera_rigida";
    public static final String CODIGO = "codigo";
    public static final String TIPO = "tipo";
    public static final String METODO_CONSTRUCTIVO = "metodo_constructivo";
    public static final String PERFIL = "perfil";

    // BARRERA RIGIDA RECONOCIMIENTO ESTADO
    public static final String BARRERA_RIGIDA_INDEX = "indice_estado";
    public static final String BARRERA_RIGIDA_A = "deficiencias_aplicacion_normativa";
    public static final String BARRERA_RIGIDA_B = "limpieza_elemento_contencion";
    public static final String BARRERA_RIGIDA_C = "deterioros_superficiales";
    public static final String BARRERA_RIGIDA_D = "cimiento_soporte_anclaje";

    // AREAS SERVICIO
    public static final String ID_AREA_SERVICIO = "id_area_servicio";
    public static final String SENTIDO = "sentido";

    // AREAS SERVICIO RECONOCIMIENTO ESTADO
    public static final String AREA_SERVICIO_INDEX = "indice_estado";
    public static final String AREA_SERVICIO_A = "sup_pavimentada";
    public static final String AREA_SERVICIO_B = "aceras";
    public static final String AREA_SERVICIO_C = "bordillos";
    public static final String AREA_SERVICIO_D = "zona_ajardinada";
    public static final String AREA_SERVICIO_E = "servicios";

    // AREAS DESCANSO
    public static final String ID_AREA_DESCANSO = "id_area_descanso";

    // AREAS DESCANSO RECONOCIMIENTO ESTADO
    public static final String AREA_DESCANSO_INDEX = "indice_estado";
    public static final String AREA_DESCANSO_A = "sup_pavimentada";
    public static final String AREA_DESCANSO_B = "bordillos";
    public static final String AREA_DESCANSO_C = "servicios";

    // JUNTAS
    public static final String ID_JUNTA = "id_junta";

    // AREAS DESCANSO RECONOCIMIENTO ESTADO
    public static final String JUNTAS_INDEX = "indice_estado";
    public static final String JUNTAS_A = "envejecimiento_deterioro_corrosion";
    public static final String JUNTAS_B = "bloqueo_perdida_movilidad";
    public static final String JUNTAS_C = "fisuras_grietas_deterioros";
    public static final String JUNTAS_D = "falta_elementos_anclaje";
    public static final String JUNTAS_E = "rotura_deformacion";

    public static void setReconocimientoEstadoFields(String[] fields) {
	reconocimientoEstadoFields = fields;
    }

    public static void setRamalesFields(String[] fields) {
	ramales = fields;
    }

}
