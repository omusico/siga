package es.icarto.gvsig.extgia.preferences;

public class DBFieldNames {

    public enum Elements {
	Taludes, Isletas, Enlaces, Barrera_Rigida, Areas_Servicio, Areas_Descanso, Areas_Peaje, Juntas, Pasos_Mediana, Senhalizacion_Vertical, Valla_Cierre, Firme, Obras_Paso, Obras_Desague, Muros, Senhalizacion_Variable, Lecho_Frenado, Areas_Mantenimiento, Lineas_Suministro, Transformadores;
    }

    public static String[] genericReconocimientoEstadoFields = {
	    "n_inspeccion as \"Nº Inspección\"",
	    "nombre_revisor as \"Revisor\"",
	    "fecha_inspeccion as \"Fecha Inspección\"",
	    "indice_estado as \"Índice Estado\"" };

    public static String[] enlacesReconocimientoEstadoFields = {
	    "n_inspeccion as \"Nº Inspección\"",
	    "nombre_revisor as \"Revisor\"",
	    "fecha_inspeccion as \"Fecha Inspección\"" };

    public static String[] reconocimientoEstadoFields = {
	    "n_inspeccion as \"Nº Inspección\"",
	    "nombre_revisor as \"Revisor\"",
	    "fecha_inspeccion as \"Fecha Inspección\"",
	    "indice_estado as \"Índice Estado\"" };

    public static String[] reconocimientoEstadoWhitoutIndexFields = {
	    "n_inspeccion as \"Nº Inspección\"",
	    "nombre_revisor as \"Revisor\"",
	    "fecha_inspeccion as \"Fecha Inspección\"" };

    public static String[] genericTrabajoFields = { "id_trabajo as \"ID\"",
	    "fecha_certificado as \"Fecha cert\"", "unidad as \"Unidad\"",
	    "medicion_audasa as \"Medición AUDASA\"",
	    "observaciones as \"Observaciones\"" };

    public static String[] senhalizacionVerticalTrabajoFields = {
	    "id_trabajo as \"ID\"", "fecha_certificado as \"Fecha cert\"",
	    "unidad as \"Unidad\"", "medicion_audasa as \"Medición AUDASA\"",
	    "observaciones as \"Observaciones\"" };

    public static String[] trabajoFields = { "id_trabajo as \"ID\"",
	    "fecha_certificado as \"Fecha cert\"", "unidad as \"Unidad\"",
	    "medicion_audasa as \"Medición AUDASA\"",
	    "observaciones as \"Observaciones\"" };

    public static String[] carreteras_enlazadas = {
	    "id_carretera_enlazada as \"ID Carretera\"",
	    "clave_carretera as \"Clave\"", "pk as \"PK\"",
	    "titular as \"Titular\"", "tipo_cruce as \"Tipo Cruce\"" };

    public static String[] generic_ramales = { "id_ramal as \"ID Ramal\"",
	    "ramal as \"Nombre Ramal\"", "sentido_ramal as \"Sentido\"",
	    "direccion_ramal as \"Dirección\"", "longitud as \"Longitud\"" };

    public static String[] senhales = { "id_senhal_vertical as \"ID Señal\"",
	    "tipo_senhal as \"Tipo Señal\"",
	    "codigo_senhal as \"Código Señal\"", "leyenda as \"Leyenda\"",
	    "fecha_fabricacion as \"Fabricación\"",
	    "fecha_reposicion as \"Reposición\"" };

    public static String[] firmeReconocimientoEstadoFields = {
	    "n_inspeccion as \"Nº Inspección\"", "tipo_inspeccion as \"Tipo\"",
	    "nombre_revisor as \"Revisor\"", "aparato_medicion as \"Aparato\"",
	    "fecha_inspeccion as \"Fecha Inspección\"" };

    public static String[] firmeTrabajoFields = { "id_trabajo as \"ID\"",
	    "fecha_certificado as \"Fecha cert\"",
	    "pk_inicial as \"PK inicio\"", "pk_final as \"PK fin\"",
	    "sentido as \"Sentido\"", "descripcion as \"Descripción\"" };

    public static String[] viasFields = { "id_via as \"ID\"",
	    "via as \"Nª Vía\"", "via_tipo as \"Tipo Vía\"",
	    "reversible as \"Reversible\"", "cabinas as \"Nº Cabinas\"" };

    public static String[] ramalesColNames = { "id_ramal", "ramal",
	    "sentido_ramal", "longitud" };
    public static String[] ramalesColAlias = { "ID Ramal", "Nombre Ramal",
	    "Sentido", "Longitud" };

    public static String[] ramalesDireccionColNames = { "id_ramal", "ramal",
	    "sentido_ramal", "direccion_ramal", "longitud" };
    public static String[] ramalesDireccionColAlias = { "ID Ramal",
	    "Nombre Ramal", "Sentido", "Dirección", "Longitud" };

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
    public static final String TALUDES_LAYERNAME = "Taludes";
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
    public static final String ISLETAS_LAYERNAME = "Isletas";
    public static final String ID_ISLETA = "id_isleta";
    public static final String NUMERO_ISLETA = "numero_isleta";
    public static final String TIPO_ISLETA = "tipo_isleta";

    // ISLETAS RECONOCIMIENTO
    public static final String ISLETAS_INDEX = "indice_estado";
    public static final String ISLETAS_A = "estado_siega";

    // ENLACES
    public static final String ENLACES_LAYERNAME = "Enlaces";
    public static final String ID_ENLACE = "id_enlace";
    public static final String MUNICIPIO = "municipio";
    public static final String PK = "pk";

    // BARRERA RIGIDA
    public static final String BARRERA_RIGIDA_LAYERNAME = "Barrera_Rigida";
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
    public static final String AREAS_SERVICIO_LAYERNAME = "Areas_Servicio";
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
    public static final String AREAS_DESCANSO_LAYERNAME = "Areas_Descanso";
    public static final String ID_AREA_DESCANSO = "id_area_descanso";

    // AREAS DESCANSO RECONOCIMIENTO ESTADO
    public static final String AREA_DESCANSO_INDEX = "indice_estado";
    public static final String AREA_DESCANSO_A = "sup_pavimentada";
    public static final String AREA_DESCANSO_B = "bordillos";
    public static final String AREA_DESCANSO_C = "servicios";

    // JUNTAS
    public static final String JUNTAS_LAYERNAME = "Juntas";
    public static final String ID_JUNTA = "id_junta";

    // AREAS DESCANSO RECONOCIMIENTO ESTADO
    public static final String JUNTAS_INDEX = "indice_estado";
    public static final String JUNTAS_A = "envejecimiento_deterioro_corrosion";
    public static final String JUNTAS_B = "bloqueo_perdida_movilidad";
    public static final String JUNTAS_C = "fisuras_grietas_deterioros";
    public static final String JUNTAS_D = "falta_elementos_anclaje";
    public static final String JUNTAS_E = "rotura_deformacion";

    // PASOS MEDIANA
    public static final String PASOS_MEDIANA_LAYERNAME = "Pasos_Mediana";
    public static final String ID_PASO_MEDIANA = "id_paso_mediana";

    // PASOS MEDIANA RECONOCIMIENTO ESTADO
    public static final String PASO_MEDIANA_INDEX = "indice_estado";
    public static final String PASO_MEDIANA_A = "aterramientos_vegetacion";
    public static final String PASO_MEDIANA_B = "baches_zonas_cuarteadas";
    public static final String PASO_MEDIANA_C = "deficiencias_sistema_drenaje";
    public static final String PASO_MEDIANA_D = "elementos_contencion_cierre";

    // SENHALIZACION VERTICAL
    public static final String SENHALIZACION_VERTICAL_LAYERNAME = "Senhalizacion_Vertical";
    public static final String ID_ELEMENTO_SENHALIZACION = "id_elemento_senhalizacion";

    // SENHALIZACION VERTICAL RECONOCIMIENTO ESTADO
    public static final String SENHALIZACION_VERTICAL_INDEX = "indice_estado";
    public static final String SENHALIZACION_VERTICAL_A = "visibilidad_senhal";
    public static final String SENHALIZACION_VERTICAL_B = "estado_limpieza";
    public static final String SENHALIZACION_VERTICAL_C = "deterioros_placa_cartel";
    public static final String SENHALIZACION_VERTICAL_D = "estado_cimientos_soporte_anclaje";
    public static final String SENHALIZACION_VERTICAL_E = "visibilidad_nocturna";

    // VALLA CIERRE
    public static final String VALLA_CIERRE_LAYERNAME = "Valla_Cierre";
    public static final String ID_VALLA_CIERRE = "id_valla";
    public static final String VALLA_CIERRE_INDEX = "indice_estado";
    public static final String VALLA_CIERRE_A = "deterioro_superficial_oxido";
    public static final String VALLA_CIERRE_B = "cimiento_soporte_anclaje";
    public static final String VALLA_CIERRE_C = "vegetacion_nociva_suciedad";

    // FIRME
    public static final String FIRME_LAYERNAME = "Firme";
    public static final String ID_FIRME = "id_firme";

    // OBRAS PASO
    public static final String OBRAS_PASO_LAYERNAME = "Obras_Paso";
    public static final String ID_OBRA_PASO = "id_obra_paso";

    // OBRAS DESAGUE
    public static final String OBRAS_DESAGUE_LAYERNAME = "Obras_Desague";
    public static final String ID_OBRA_DESAGUE = "id_obra_desague";

    // AREAS PEAJE
    public static final String AREAS_PEAJE_LAYERNAME = "Areas_Peaje";
    public static final String ID_AREA_PEAJE = "id_area_peaje";

    // SENHALIZACION VARIABLE
    public static final String SENHALIZACION_VARIABLE_LAYERNAME = "Senhalizacion_Variable";
    public static final String ID_SENHAL_VARIABLE = "id_senhal_variable";

    // MUROS
    public static final String MUROS_LAYERNAME = "Muros";
    public static final String ID_MUROS = "id_muro";
    public static final String MUROS_INDEX = "indice_estado";
    public static final String MUROS_A = "descalces_socavaciones";
    public static final String MUROS_B = "asentamientos_giros";
    public static final String MUROS_C = "golpes_roturas";
    public static final String MUROS_D = "fisuras_grietas";
    public static final String MUROS_E = "armaduras_vistas";
    public static final String MUROS_F = "filtraciones_humedades";
    public static final String MUROS_G = "vegetacion_perjudicial";
    public static final String MUROS_H = "coqueras";
    public static final String MUROS_I = "juntas_degradadas";
    public static final String MUROS_J = "drenaje_ineficaz";

    // LECHO FRENADO
    public static final String LECHO_FRENADO_LAYERNAME = "Lecho_Frenado";
    public static final String ID_LECHO_FRENADO = "id_lecho_frenado";
    public static final String LECHO_FRENADO_INDEX = "indice_estado";
    public static final String LECHO_FRENADO_A = "superficie_grava";
    public static final String LECHO_FRENADO_B = "elementos_contencion";
    public static final String LECHO_FRENADO_C = "acceso";

    // LINEAS SUMINISTRO
    public static final String LINEAS_SUMINISTRO_LAYERNAME = "Lineas_Suministro";
    public static final String ID_LINEAS_SUMINISTRO = "id_linea_suministro";

    // LINEAS SUMINISTRO
    public static final String TRANSFORMADORES_LAYERNAME = "Transformadores";
    public static final String ID_TRANSFORMADORES = "id_transformador";

    public static void setReconocimientoEstadoFields(String[] fields) {
	reconocimientoEstadoFields = fields;
    }

    public static void setTrabajosFields(String[] fields) {
	trabajoFields = fields;
    }

    public static String getPrimaryKey(Elements element) {
	String pk = "";
	switch (element) {
	case Taludes:
	    pk = "id_talud";
	    break;
	case Isletas:
	    pk = "id_isleta";
	    break;
	case Enlaces:
	    pk = "id_enlace";
	    break;
	case Barrera_Rigida:
	    pk = "id_barrera_rigida";
	    break;
	case Areas_Servicio:
	    pk = "id_area_servicio";
	    break;
	case Areas_Descanso:
	    pk = "id_area_descanso";
	    break;
	case Areas_Peaje:
	    pk = "id_area_peaje";
	    break;
	case Juntas:
	    pk = "id_junta";
	    break;
	case Pasos_Mediana:
	    pk = "id_paso_mediana";
	    break;
	case Senhalizacion_Vertical:
	    pk = "id_elemento_senhalizacion";
	    break;
	case Firme:
	    pk = "id_firme";
	    break;
	case Valla_Cierre:
	    pk = "id_valla";
	    break;
	case Obras_Desague:
	    pk = "id_obra_desague";
	    break;
	case Obras_Paso:
	    pk = "id_obra_paso";
	    break;
	case Muros:
	    pk = "id_muro";
	    break;
	case Senhalizacion_Variable:
	    pk = "id_senhal_variable";
	    break;
	case Lecho_Frenado:
	    pk = "id_lecho_frenado";
	    break;
	case Areas_Mantenimiento:
	    pk = "id_area_mantenimiento";
	    break;
	case Lineas_Suministro:
	    pk = "id_linea_suministro";
	    break;
	case Transformadores:
	    pk = "id_transformador";
	    break;
	}
	return pk;
    }

}
