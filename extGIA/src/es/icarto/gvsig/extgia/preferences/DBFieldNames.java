package es.icarto.gvsig.extgia.preferences;

import es.icarto.gvsig.extgia.batch.BatchAbstractSubForm;
import es.icarto.gvsig.extgia.batch.elements.BatchBarreraRigidaTrabajos;
import es.icarto.gvsig.extgia.batch.elements.BatchIsletasTrabajos;
import es.icarto.gvsig.extgia.batch.elements.BatchSenhalizacionVerticalTrabajos;
import es.icarto.gvsig.extgia.batch.elements.BatchTaludesTrabajos;
import es.icarto.gvsig.extgia.forms.areas_descanso.AreasDescansoForm;
import es.icarto.gvsig.extgia.forms.areas_mantenimiento.AreasMantenimientoForm;
import es.icarto.gvsig.extgia.forms.areas_peaje.AreasPeajeForm;
import es.icarto.gvsig.extgia.forms.areas_servicio.AreasServicioForm;
import es.icarto.gvsig.extgia.forms.barrera_rigida.BarreraRigidaForm;
import es.icarto.gvsig.extgia.forms.competencias.CompetenciasForm;
import es.icarto.gvsig.extgia.forms.enlaces.EnlacesForm;
import es.icarto.gvsig.extgia.forms.firme.FirmeForm;
import es.icarto.gvsig.extgia.forms.isletas.IsletasForm;
import es.icarto.gvsig.extgia.forms.juntas.JuntasForm;
import es.icarto.gvsig.extgia.forms.lecho_frenado.LechoFrenadoForm;
import es.icarto.gvsig.extgia.forms.lineas_suministro.LineasSuministroForm;
import es.icarto.gvsig.extgia.forms.muros.MurosForm;
import es.icarto.gvsig.extgia.forms.obras_desague.ObrasDesagueForm;
import es.icarto.gvsig.extgia.forms.obras_paso.ObrasPasoForm;
import es.icarto.gvsig.extgia.forms.pasos_mediana.PasosMedianaForm;
import es.icarto.gvsig.extgia.forms.ramales.RamalesForm;
import es.icarto.gvsig.extgia.forms.senhalizacion_variable.SenhalizacionVariableForm;
import es.icarto.gvsig.extgia.forms.senhalizacion_vertical.SenhalizacionVerticalForm;
import es.icarto.gvsig.extgia.forms.taludes.TaludesForm;
import es.icarto.gvsig.extgia.forms.transformadores.TransformadoresForm;
import es.icarto.gvsig.extgia.forms.valla_cierre.VallaCierreForm;
import es.icarto.gvsig.navtableforms.AbstractForm;

public class DBFieldNames {

    public enum Elements {
	Areas_Descanso("id_area_descanso", AreasDescansoForm.class, BatchAbstractSubForm.class),
	Areas_Mantenimiento("id_area_mantenimiento", AreasMantenimientoForm.class, null),
	Areas_Peaje("id_area_peaje", AreasPeajeForm.class, BatchAbstractSubForm.class),
	Areas_Servicio("id_area_servicio", AreasServicioForm.class, BatchAbstractSubForm.class),
	Barrera_Rigida("id_barrera_rigida", BarreraRigidaForm.class, BatchBarreraRigidaTrabajos.class),
	Enlaces("id_enlace", EnlacesForm.class, null),
	Firme("id_firme", FirmeForm.class, BatchAbstractSubForm.class),
	Isletas("id_isleta", IsletasForm.class, BatchIsletasTrabajos.class),
	Juntas("id_junta", JuntasForm.class, BatchAbstractSubForm.class),
	Lecho_Frenado("id_lecho_frenado", LechoFrenadoForm.class, BatchAbstractSubForm.class),
	Lineas_Suministro("id_linea_suministro", LineasSuministroForm.class, BatchAbstractSubForm.class),
	Muros("id_muro", MurosForm.class, BatchAbstractSubForm.class),
	Obras_Desague("id_obra_desague", ObrasDesagueForm.class, BatchAbstractSubForm.class),
	Obras_Paso("id_obra_paso", ObrasPasoForm.class, BatchAbstractSubForm.class),
	Pasos_Mediana("id_paso_mediana", PasosMedianaForm.class, BatchAbstractSubForm.class),
	Senhalizacion_Variable("id_senhal_variable", SenhalizacionVariableForm.class, BatchAbstractSubForm.class),
	Senhalizacion_Vertical("id_elemento_senhalizacion", SenhalizacionVerticalForm.class, BatchSenhalizacionVerticalTrabajos.class),
	Taludes("id_talud", TaludesForm.class, BatchTaludesTrabajos.class),
	Transformadores("id_transformador", TransformadoresForm.class, BatchAbstractSubForm.class),
	Valla_Cierre("id_valla", VallaCierreForm.class, BatchAbstractSubForm.class),
	Ramales("gid", RamalesForm.class, null),
	Competencias("gid", CompetenciasForm.class, null);

	public final String pk;
	public final Class<? extends AbstractForm> form;
	public final Class<? extends BatchAbstractSubForm> batchForm;
	public final String layerName;

	private Elements(String pk, Class<? extends AbstractForm> form,
		Class<? extends BatchAbstractSubForm> batchForm) {
	    this.pk = pk;
	    this.form = form;
	    this.batchForm = batchForm;
	    this.layerName = this.toString();
	}
    }

    public static String[] reconocimientosWhitoutIndexColNames = {
	"n_inspeccion", "nombre_revisor", "fecha_inspeccion" };
    public static String[] reconocimientosWhitoutIndexColAlias = {
	"Nº Inspección", "Revisor", "Fecha Inspección" };

    public static final String[] reconocimientosColNames = { "n_inspeccion",
	"nombre_revisor", "fecha_inspeccion", "indice_estado" };
    public static final String[] reconocimientosColAlias = { "Nº Inspección",
	"Revisor", "Fecha Inspección", "Índice Estado" };

    public static String[] trabajosColNames = { "id_trabajo",
	"fecha_certificado", "unidad", "medicion_audasa", "observaciones" };

    public static String[] trabajosColAlias = { "ID", "Fecha cert", "Unidad",
	"Medición AUDASA", "Observaciones" };
    public static int[] trabajosColWidths = { 10, 45, 90, 75, 190 };

    public static String[] trabajosVegetacionColNames = { "id_trabajo",
	"fecha", "unidad", "medicion", "observaciones" };

    public static String[] trabajosVegetacionColAlias = { "ID", "Fecha",
	"Unidad", "Medición", "Observaciones" };

    public static String[] trabajosVegetacionTableEditableCells = { "Fecha",
	"Unidad", "Medición", "Observaciones" };

    public static final String GIA_SCHEMA = "audasa_extgia";

    // LOCATION
    public static final String AREA_MANTENIMIENTO = "area_mantenimiento";
    public static final String BASE_CONTRATISTA = "base_contratista";
    public static final String TRAMO = "tramo";
    public static final String TIPO_VIA = "tipo_via";
    public static final String TIPO_VIA_PF = "tipo_via_pf";
    public static final String NOMBRE_VIA = "nombre_via";
    public static final String NOMBRE_VIA_PF = "nombre_via_pf";
    public static final String PK = "pk";
    public static final String PK_INICIAL = "pk_inicial";
    public static final String PK_FINAL = "pk_final";
    public static final String DIRECCION = "direccion";
    public static final String DIRECCION_PI = "direccion_pi";
    public static final String DIRECCION_PF = "direccion_pf";
    public static final String SENTIDO = "sentido";

    // TALUDES
    public static final String TALUDES_LAYERNAME = "Taludes";
    public static final String TALUDES_DBTABLENAME = "taludes";
    public static final String ID_TALUD = "id_talud";
    public static final String NUMERO_TALUD = "numero_talud";
    public static final String TIPO_TALUD = "tipo_talud";
    public static final String INCLINACION_MEDIA = "inclinacion_media";
    public static final String SECTOR_INCLINACION = "sector_inclinacion";
    public static final String SUP_TOTAL_ANALITICA = "sup_total_analitica";
    public static final String SUP_COMPLEMENTARIA = "sup_complementaria";
    public static final String TALUDES_LONGITUD = "longitud";

    // TALUDES TRABAJOS
    public static final String TALUDES_TRABAJOS_DBTABLENAME = "taludes_trabajos";
    public static final String UNIDAD = "unidad";

    // TALUDES RECONOCIMIENTO
    public static final String TALUDES_INDEX = "indice_estado";
    public static final String TALUDES_A = "existencia_deformaciones_o_grietas";
    public static final String TALUDES_B = "peligro_caida_materiales";
    public static final String TALUDES_C = "bajante_deteriorada";
    public static final String TALUDES_D = "elementos_proteccion_talud";

    // ISLETAS
    public static final String ISLETAS_LAYERNAME = "Isletas";
    public static final String ISLETAS_DBTABLENAME = "isletas";
    public static final String SUPERFICIE_BAJO_BIONDA = "superficie_bajo_bionda";
    public static final String ID_ISLETA = "id_isleta";
    public static final String NUMERO_ISLETA = "numero_isleta";
    public static final String TIPO_ISLETA = "tipo_isleta";

    // ISLETAS TRABAJOS
    public static final String ISLETAS_TRABAJOS_DBTABLENAME = "isletas_trabajos";

    // ISLETAS RECONOCIMIENTO
    public static final String ISLETAS_INDEX = "indice_estado";
    public static final String ISLETAS_A = "estado_siega";

    // ENLACES
    public static final String ENLACES_LAYERNAME = "Enlaces";
    public static final String ID_ENLACE = "id_enlace";
    public static final String MUNICIPIO = "municipio";

    // BARRERA RIGIDA
    public static final String BARRERA_RIGIDA_LAYERNAME = "Barrera_Rigida";
    public static final String BARRERA_RIGIDA_DBTABLENAME = "barrera_rigida";
    public static final String ID_BARRERA_RIGIDA = "id_barrera_rigida";
    public static final String NUMERO_BARRERA_RIGIDA = "numero_barrera_rigida";
    public static final String CODIGO = "codigo";
    public static final String TIPO = "tipo";
    public static final String METODO_CONSTRUCTIVO = "metodo_constructivo";
    public static final String PERFIL = "perfil";
    public static final String BARRERA_RIGIDA_LONGITUD = "longitud";

    // BARRERA RIGIDA TRABAJOS
    public static final String BARRERA_RIGIDA_TRABAJOS_DBTABLENAME = "barrera_rigida_trabajos";

    // BARRERA RIGIDA RECONOCIMIENTO ESTADO
    public static final String BARRERA_RIGIDA_INDEX = "indice_estado";
    public static final String BARRERA_RIGIDA_A = "deficiencias_aplicacion_normativa";
    public static final String BARRERA_RIGIDA_B = "limpieza_elemento_contencion";
    public static final String BARRERA_RIGIDA_C = "deterioros_superficiales";
    public static final String BARRERA_RIGIDA_D = "cimiento_soporte_anclaje";

    // AREAS SERVICIO
    public static final String AREAS_SERVICIO_LAYERNAME = "Areas_Servicio";
    public static final String ID_AREA_SERVICIO = "id_area_servicio";

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
    public static final String PASOS_MEDIANA_INDEX = "indice_estado";
    public static final String PASOS_MEDIANA_A = "aterramientos_vegetacion";
    public static final String PASOS_MEDIANA_B = "baches_zonas_cuarteadas";
    public static final String PASOS_MEDIANA_C = "deficiencias_sistema_drenaje";
    public static final String PASOS_MEDIANA_D = "elementos_contencion_cierre";

    // SENHALIZACION VERTICAL
    public static final String SENHALIZACION_VERTICAL_LAYERNAME = "Senhalizacion_Vertical";
    public static final String ID_ELEMENTO_SENHALIZACION = "id_elemento_senhalizacion";
    public static final String SENHALIZACION_VERTICAL_SENHALES_LAYERNAME = "Senhales";

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

    // VEGETACION TRABAJOS (Barrera Rígida, Isletas y Taludes)
    public static final String MEDICION = "medicion";
    public static final String MEDICION_ELEMENTO = "medicion_elemento";
    public static final String MEDICION_COMPLEMENTARIA = "medicion_complementaria";
    public static final String MEDICION_ULTIMO_TRABAJO = "medicion_ultimo_trabajo";
    public static final String LONGITUD = "longitud";
    public static final String ANCHO = "ancho";
    public static final String FECHA = "fecha";
}
