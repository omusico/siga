package es.icarto.gvsig.extgia.consultas;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import es.icarto.gvsig.extgia.consultas.caracteristicas.elements.AreasDescansoCaracteristicasReport;
import es.icarto.gvsig.extgia.consultas.caracteristicas.elements.AreasMantenimientoCaracteristicasReport;
import es.icarto.gvsig.extgia.consultas.caracteristicas.elements.AreasPeajeCaracteristicasReport;
import es.icarto.gvsig.extgia.consultas.caracteristicas.elements.AreasServicioCaracteristicasReport;
import es.icarto.gvsig.extgia.consultas.caracteristicas.elements.BarreraRigidaCaracteristicasReport;
import es.icarto.gvsig.extgia.consultas.caracteristicas.elements.EnlacesCaracteristicasReport;
import es.icarto.gvsig.extgia.consultas.caracteristicas.elements.FirmeCaracteristicasReport;
import es.icarto.gvsig.extgia.consultas.caracteristicas.elements.IsletasCaracteristicasReport;
import es.icarto.gvsig.extgia.consultas.caracteristicas.elements.JuntasCaracteristicasReport;
import es.icarto.gvsig.extgia.consultas.caracteristicas.elements.LechoFrenadoCaracteristicasReport;
import es.icarto.gvsig.extgia.consultas.caracteristicas.elements.MurosCaracteristicasReport;
import es.icarto.gvsig.extgia.consultas.caracteristicas.elements.ObrasDesagueCaracteristicasReport;
import es.icarto.gvsig.extgia.consultas.caracteristicas.elements.ObrasPasoCaracteristicasReport;
import es.icarto.gvsig.extgia.consultas.caracteristicas.elements.PasosMedianaCaracteristicasReport;
import es.icarto.gvsig.extgia.consultas.caracteristicas.elements.SenhalizacionVariableCaracteristicasReport;
import es.icarto.gvsig.extgia.consultas.caracteristicas.elements.SenhalizacionVerticalCaracteristicasReport;
import es.icarto.gvsig.extgia.consultas.caracteristicas.elements.TaludesCaracteristicasReport;
import es.icarto.gvsig.extgia.consultas.caracteristicas.elements.VallaCierreCaracteristicasReport;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class ConsultasFieldNames {

    public static String getTrabajosFieldNames(String elementId) {
	return elementId + ", fecha, unidad, medicion_contratista, medicion_audasa, " +
		"observaciones, fecha_certificado";
    }

    public static String getFirmeTrabajosFieldNames(String elementId) {
	return elementId + ", fecha, pk_inicial, pk_final, sentido, " +
		"descripcion, fecha_certificado";
    }

    public static String getReconocimientosFieldNames(String elementId) {
	return elementId + ", nombre_revisor, fecha_inspeccion, indice_estado, observaciones";
    }

    public static String getReconocimientosFieldNamesWithoutIndice(String elementId) {
	return elementId + ", nombre_revisor, fecha_inspeccion, observaciones";
    }

    public static String getFirmeReconocimientosFieldNames(String elementId) {
	return elementId + ", tipo_inspeccion, nombre_revisor, aparato_medicion," +
		"fecha_inspeccion, observaciones";
    }

    public static String getPDFCaracteristicasFieldNames(String element) {
	switch (DBFieldNames.Elements.valueOf(element)) {
	case Taludes:
	    return "id_talud, tr.item, pk_inicial, pk_final, tipo_talud, roca, arboles, " +
	    "gunita, escollera, maleza, malla, longitud, altura_max_talud, sup_total_analitica," +
	    "sup_mecanizada_analitica, sup_manual_analitica, sup_restada_analitica";
	case Isletas:
	    return "id_isleta, tr.item, tv.item, nv.item, pk_inicial, pk_final, " +
	    "tipo_isleta, superficie_bajo_bionda, posibilidad_empleo_vehiculos, observaciones";
	case Barrera_Rigida:
	    return "id_barrera_rigida, tr.item, tv.item, nv.item, pk_inicial, pk_final, " +
	    "obstaculo_protegido, longitud, codigo, tipo, metodo_constructivo, perfil, observaciones";
	case Areas_Servicio:
	    return "id_area_servicio, nombre, tr.item, pk, fecha_puesta_servicio, " +
	    "sup_total, riego, cafeteria_rest_bar, aparcamiento_camion_bus, area_picnic, " +
	    "fuentes_potables, observaciones";
	case Areas_Descanso:
	    return "id_area_descanso, nombre, tr.item, pk, fecha_puesta_servicio, " +
	    "sup_total, riego, aparcamiento_camion_bus, area_picnic, fuentes_potables, observaciones";
	case Areas_Peaje:
	    return "id_area_peaje, nombre, tr.item, pk, fecha_puesta_servicio, " +
	    "bordillos, bumpers, tunel_peaje, marquesina_tipo, marquesina_sup, sup_total, " +
	    "observaciones, numero_vias";
	case Enlaces:
	    return "id_enlace, nombre, tr.item, pk, n_salida, tipo_enlace, " +
	    "alumbrado, observaciones";
	case Juntas:
	    return "id_junta, tr.item, tv.item, nv.item, pk, numero_junta, ancho, modulo, " +
	    "elemento, codigo_elemento, descripcion, observaciones";
	case Pasos_Mediana:
	    return "id_paso_mediana, tr.item, tv.item, nv.item, pk, longitud, numero_postes, " +
	    "cierre, longitud_cierre, cuneta_entubada, observaciones";
	case Senhalizacion_Vertical:
	    break;
	case Valla_Cierre:
	    return "id_valla, tr.item, pk_inicial, pk_final, tipo_valla, longitud, " +
	    "altura, n_panhos, n_puertas, n_postes_simples, n_postes_tripode, pastor_electrico, " +
	    "observaciones";
	case Firme:
	    return "id_firme, fecha_inauguracion, fecha_apertura, unidad_constructiva, " +
	    "pk_inicial, pk_final, explanada_cm, zahorra_artificial_cm, suelo_cemento_cm, " +
	    "grava_cemento_cm, mbc_base_cm, mbc_intermedia_cm, mbc_rodadura_cm, observaciones";
	case Obras_Paso:
	    return "id_obra_paso, tr.item, tv.item, nv.item, pk, codigo, nombre," +
	    "tipo_obra, tipologia, longitud, anchura, altura," +
	    "galibo_v_c, galibo_v_d, observaciones";
	case Obras_Desague:
	    return "id_obra_desague, tr.item, tv.item, nv.item, pk, tipologia, material, " +
	    "objeto, fecha_construccion, n_elementos, seccion, longitud, observaciones";
	case Muros:
	    return "id_muro, tr.item, pk_inicial, pk_final, material, longitud, " +
	    "altura_max, observaciones";
	case Senhalizacion_Variable:
	    return "id_senhal_variable, tr.item, tv.item, nv.item, pk, referencia, " +
	    "fecha_instalacion, tipo, tipo_sustentacion, corunha, ferrol, santiago, pontevedra, " +
	    "vigo, tui, observaciones";
	case Lecho_Frenado:
	    return "id_lecho_frenado, tr.item, pk, longitud, anchura, pendiente, " +
	    "observaciones";
	case Areas_Mantenimiento:
	    return "id_area_mantenimiento, nombre, tr.item, pk, centro_operaciones, " +
	    "centro_comunicaciones, control_postes_auxilio, control_tuneles, almacen_fundentes, " +
	    "alumbrado, observaciones";
	}
	return null;
    }

    public static String getCSVCaracteristicasFieldNames(String element) {
	switch (DBFieldNames.Elements.valueOf(element)) {
	case Taludes:
	    return taludesCSVFieldNames();
	case Isletas:
	    return isletasCSVFieldNames();
	case Barrera_Rigida:
	    return barreraRigidaCSVFieldNames();
	case Areas_Servicio:
	    return areasServicioCSVFieldNames();
	case Areas_Descanso:
	    return areasDescansoCSVFieldNames();
	case Areas_Peaje:
	    return areasPeajeCSVFieldNames();
	case Enlaces:
	    return enlacesCSVFieldNames();
	case Juntas:
	    return juntasCSVFieldNames();
	case Pasos_Mediana:
	    return pasosMedianaCSVFieldNames();
	case Senhalizacion_Vertical:
	    return senhalizacionCSVFieldNames();
	case Valla_Cierre:
	    return vallaCierreCSVFieldNames();
	case Firme:
	    return firmeCSVFieldNames();
	case Obras_Paso:
	    return obrasPasoCSVFieldNames();
	case Obras_Desague:
	    return obrasDesagueCSVFieldNames();
	case Muros:
	    return murosCSVFieldNames();
	case Senhalizacion_Variable:
	    return senhalizacionVariableCSVFieldNames();
	case Lecho_Frenado:
	    return lechoFrenadoCSVFieldNames();
	case Areas_Mantenimiento:
	    return areasMantenimientoCSVFieldNames();
	}
	return null;
    }

    private static String localizationCSVFieldNames() {
	return "am.item as \"Area Mantenimiento\"," +
		"bc.item as \"Base Contratista\"," +
		"tr.item as \"Tramo\"," +
		"tv.item as \"Tipo Vía\"," +
		"nv.item as \"Nombre Vía\",";
    }

    private static String taludesCSVFieldNames() {
	return "distinct(gid), " +
		"el.id_talud as \"ID Talud\"," +
		"numero_talud as \"Nº Talud\"," +
		"fecha_actualizacion as \"Fecha Actualización\"," +
		localizationCSVFieldNames() +
		"tvf.item as \"Tipo Vía PF\"," +
		"nvf.item as \"Nombre Vía PF\"," +
		"pk_inicial as \"PK Inicial\"," +
		"pk_final as \"PK Final\"," +
		"ramal_pi as \"Ramal\"," +
		"ramal_pf as \"Ramal PF\"," +
		"direccion_pi as \"Dirección\"," +
		"direccion_pf as \"Dirección PF\"," +
		"st.item as \"Sentido\"," +
		"margen as \"Margen\"," +
		"mu.item as \"Municipio\"," +
		"tipo_talud as \"Tipo Talud\"," +
		"roca as \"Roca\"," +
		"arboles as \"Árboles\"," +
		"gunita as \"Gunita\"," +
		"escollera as \"Escollera\"," +
		"maleza as \"Maleza\"," +
		"malla as \"Malla\"," +
		"observaciones as \"Observaciones\"," +
		"arcen as \"Arcén\"," +
		"barrera_seguridad as \"Barrera Seguridad\"," +
		"cuneta_pie as \"Cuneta Pie\"," +
		"cuneta_pie_revestida as \"Cuneta Pie Revestida\"," +
		"cuneta_cabeza as \"Cuneta Cabezada\"," +
		"cuneta_cabeza_revestida as \"Cuneta Cabeza Revestida\"," +
		"berma as \"Berma\"," +
		"longitud as \"Longitud\"," +
		"sector_inclinacion as \"Sector Inclinación\"," +
		"inclinacion_media as \"Inclinación Media\"," +
		"altura_max_talud as \"Altura Máxima Talud\"," +
		"sup_total_analitica as \"Superficie Total Analítica\"," +
		"sup_mecanizada_analitica as \"Superficie Mecanizada Analítica\"," +
		"sup_manual_analitica as \"Superficie Manual Analítica\"," +
		"sup_restada_analitica as \"Superficie Restada Analítica\"," +
		"sup_total_campo as \"Superficie Total Campo\"," +
		"sup_mecanizada_campo as \"Superficie Mecanizada Campo\"," +
		"sup_restada_campo as \"Superficie Restada Campo\"," +
		"sup_manual_campo as \"Superficie Manual Campo\"," +
		"sup_complementaria as \"Superficie Complementaria\"," +
		"concepto as \"Concepto\"";
    }

    private static String isletasCSVFieldNames() {
	return "distinct(gid), " +
		"el.id_isleta as \"ID Isleta\"," +
		"numero_isleta as \"Nº Isleta\"," +
		"fecha_actualizacion as \"Fecha Actualización\"," +
		localizationCSVFieldNames() +
		"pk_inicial as \"PK Inicial\"," +
		"pk_final as \"PK Final\"," +
		"ramal as \"Ramal\"," +
		"st.item as \"Sentido\"," +
		"direccion as \"Dirección\"," +
		"margen as \"Margen\"," +
		"mu.item as \"Municipio\"," +
		"tipo_isleta as \"Tipo Isleta\"," +
		"superficie_bajo_bionda as \"Superficie Bajo Bionda\"," +
		"posibilidad_empleo_vehiculos as \"Posibilidad Empleo Vehículos\"," +
		"observaciones as \"Observaciones\"";
    }

    private static String barreraRigidaCSVFieldNames() {
	return "distinct(gid), " +
		"el.id_barrera_rigida as \"ID Barrera Rígida\"," +
		"numero_barrera_rigida as \"Nº Barrera Rígida\"," +
		"fecha_actualizacion as \"Fecha Actualización\"," +
		localizationCSVFieldNames() +
		"pk_inicial as \"PK Inicial\"," +
		"pk_final as \"PK Final\"," +
		"ramal as \"Ramal\"," +
		"st.item as \"Sentido\"," +
		"direccion as \"Dirección\"," +
		"margen as \"Margen\"," +
		"mu.item as \"Municipio\"," +
		"obstaculo_protegido as \"Obstáculo Protegido\"," +
		"longitud as \"Longitud\"," +
		"codigo as \"Código\"," +
		"tipo as \"Tipo\"," +
		"metodo_constructivo as \"Método Constructivo\"," +
		"perfil as \"Perfil\"," +
		"observaciones as \"Observaciones\"";
    }

    private static String areasServicioCSVFieldNames() {
	return "distinct(gid), " +
		"el.id_area_servicio as \"ID Área Servicio\"," +
		"nombre as \"Nombre\"," +
		"fecha_actualizacion as \"Fecha Actualización\"," +
		localizationCSVFieldNames() +
		"pk as \"PK\"," +
		"st.item as \"Sentido\"," +
		"mu.item as \"Municipio\"," +
		"fecha_puesta_servicio as \"Fecha Puesta Servicio\"," +
		"sup_total as \"Superficie Total\"," +
		"sup_pavimentada as \"Superficie Pavimentada\"," +
		"aceras as \"Aceras\"," +
		"bordillos as \"Bordillos\"," +
		"zona_siega as \"Zona Siega\"," +
		"zona_ajardinada as \"Zona Ajardinada\"," +
		"riego as \"Riego\"," +
		"cafeteria_rest_bar as \"Cafetería\"," +
		"aparcamiento_camion_bus as \"Aparcamientos\"," +
		"area_picnic as \"Área Picnic\"," +
		"fuentes_potables as \"Fuentes Potables\"," +
		"observaciones as \"Observaciones\"";
    }

    private static String areasDescansoCSVFieldNames() {
	return "distinct(gid), " +
		"el.id_area_descanso as \"ID Área Descanso\"," +
		"nombre as \"Nombre\"," +
		"fecha_actualizacion as \"Fecha Actualización\"," +
		localizationCSVFieldNames() +
		"pk as \"PK\"," +
		"st.item as \"Sentido\"," +
		"mu.item as \"Municipio\"," +
		"fecha_puesta_servicio as \"Fecha Puesta Servicio\"," +
		"sup_total as \"Superficie Total\"," +
		"sup_pavimentada as \"Superficie Pavimentada\"," +
		"aceras as \"Aceras\"," +
		"bordillos as \"Bordillos\"," +
		"zona_siega as \"Zona Siega\"," +
		"zona_ajardinada as \"Zona Ajardinada\"," +
		"riego as \"Riego\"," +
		"aparcamiento_camion_bus as \"Aparcamientos\"," +
		"area_picnic as \"Área Picnic\"," +
		"fuentes_potables as \"Fuentes Potables\"," +
		"observaciones as \"Observaciones\"";
    }

    private static String areasPeajeCSVFieldNames() {
	return "distinct(gid), " +
		"el.id_area_peaje as \"ID Área Peaje\"," +
		"nombre as \"Nombre\"," +
		"fecha_actualizacion as \"Fecha Actualización\"," +
		localizationCSVFieldNames() +
		"pk as \"PK\"," +
		"st.item as \"Sentido\"," +
		"mu.item as \"Municipio\"," +
		"fecha_puesta_servicio as \"Fecha Puesta Servicio\"," +
		"n_salida as \"Número de salida\"," +
		"bordillos as \"Longitud bordillos(m)\"," +
		"bumpers as \"Bumpers(nº)\"," +
		"tunel_peaje as \"Túnel de peaje\"," +
		"long_tunel as \"Longitud Túnel\"," +
		"marquesina_tipo as \"Tipo\"," +
		"marquesina_sup as \"Superficie (m2)\"," +
		"sup_total as \"Superficie total(m2)\"," +
		"sup_trieff as \"Sup. adoquín trieff(m2)\"," +
		"sup_hormigon as \"Sup. adoquín hormigón(m2)\"," +
		"sup_otros as \"Sup. otros(m2)\"," +
		"numero_vias as \"Número vías\"," +
		"observaciones as \"Observaciones\"";
    }

    private static String pasosMedianaCSVFieldNames() {
	return "distinct(gid), " +
		"el.id_paso_mediana as \"ID Paso Mediana\"," +
		"fecha_actualizacion as \"Fecha Actualización\"," +
		localizationCSVFieldNames() +
		"pk as \"PK\"," +
		"mu.item as \"Municipio\"," +
		"longitud as \"Longitud\"," +
		"numero_postes as \"Nº Postes\"," +
		"cierre as \"Cierre\"," +
		"longitud_cierre as \"Longitud Cierre\"," +
		"cuneta_entubada as \"Cuneta Entubada\"," +
		"observaciones as \"Observaciones\"";
    }

    private static String juntasCSVFieldNames() {
	return "distinct(gid), " +
		"el.id_junta as \"ID Junta\"," +
		"fecha_actualizacion as \"Fecha Actualización\"," +
		localizationCSVFieldNames() +
		"pk as \"PK\"," +
		"ramal as \"Ramal\"," +
		"st.item as \"Sentido\"," +
		"direccion as \"Dirección\"," +
		"mu.item as \"Municipio\"," +
		"numero_junta as \"Nº Junta\"," +
		"ancho as \"Ancho\"," +
		"modulo as \"Módulo\"," +
		"elemento as \"Elemento\"," +
		"codigo_elemento as \"Código Elemento\"," +
		"descripcion as \"Descripción\"," +
		"observaciones as \"Observaciones\"";
    }

    private static String enlacesCSVFieldNames() {
	return "distinct(gid), " +
		"el.id_enlace as \"ID Enlace\"," +
		"nombre as \"Nombre\"," +
		"fecha_actualizacion as \"Fecha Actualización\"," +
		localizationCSVFieldNames() +
		"el.pk as \"PK\"," +
		"mu.item as \"Municipio\"," +
		"n_salida as \"Nº Salida\"," +
		"tipo_enlace as \"Tipo Enlace\"," +
		"alumbrado as \"Alumbrado\"," +
		"observaciones as \"Observaciones\"";
    }

    private static String senhalizacionCSVFieldNames() {
	return "distinct(el.id_elemento_senhalizacion) as \"ID Elemento\"," +
		"fecha_actualizacion as \"Fecha Actualización\"," +
		localizationCSVFieldNames() +
		"pk as \"PK\"," +
		"ramal as \"Ramal\"," +
		"st.item as \"Sentido\"," +
		"direccion as \"Dirección\"," +
		"margen_senhal as \"Margen Señal\"," +
		"mu.item as \"Municipio\"," +
		"tipo_sustentacion as \"Tipo Sustentación\"," +
		"material_sustentacion as \"Material Sustentación\"," +
		"tipo_poste as \"Tipo Poste\"," +
		"numero_postes as \"Nº Postes\"," +
		"anclaje as \"Anclaje\"," +
		"cimentacion_especial as \"Cimentación Especial\"," +
		"el.observaciones as \"Observaciones\"," +
		"tipo_senhal as \"Tipo Señal\"," +
		"codigo_senhal as \"Código Señal\"," +
		"leyenda as \"Leyenda\"," +
		"panel_complementario as \"Panel Complementario\"," +
		"codigo_panel as \"Código Panel\"," +
		"texto_panel as \"Texto Panel\"," +
		"reversible as \"Reversible\"," +
		"luminosa as \"Luminosa\"," +
		"tipo_superficie as \"Tipo Superficie\"," +
		"material_superficie as \"Material Superficie\"," +
		"material_retrorreflectante as \"Material Retrorreflectante\"," +
		"nivel_reflectancia as \"Nivel Reflectancia\"," +
		"ancho as \"Ancho\"," +
		"alto as \"Alto\"," +
		"superficie as \"Superficie\"," +
		"altura as \"Altura\"," +
		"fabricante as \"Fabricante\"," +
		"fecha_fabricacion as \"Fecha Fabricación\"," +
		"fecha_instalacion as \"Fecha Instalación\"," +
		"fecha_reposicion as \"Fecha Reposición\"," +
		"marcado_ce as \"Marcado CE\"," +
		"se.observaciones as \"Observaciones Señal\"";
    }

    private static String vallaCierreCSVFieldNames() {
	return "distinct(gid), " +
		"el.id_valla as \"ID Valla\"," +
		"fecha_actualizacion as \"Fecha Actualización\"," +
		localizationCSVFieldNames() +
		"tvf.item as \"Tipo Vía PF\"," +
		"nvf.item as \"Nombre Vía PF\"," +
		"pk_inicial as \"PK Inicial\"," +
		"pk_final as \"PK Final\"," +
		"ramal_pi as \"Ramal\"," +
		"ramal_pf as \"Ramal PF\"," +
		"direccion_pi as \"Dirección\"," +
		"direccion_pf as \"Dirección PF\"," +
		"st.item as \"Sentido\"," +
		"margen as \"Margen\"," +
		"mu.item as \"Municipio\"," +
		"tipo_valla as \"Tipo Valla\"," +
		"longitud as \"Longitud\"," +
		"altura as \"Altura\"," +
		"n_panhos as \"Nº Paños\"," +
		"n_puertas as \"Nº Puertas\"," +
		"n_postes_simples as \"Nº Postes Simples\"," +
		"n_postes_tripode as \"Nº Postes Trípode\"," +
		"pastor_electrico as \"Pastor Eléctrico\"," +
		"observaciones as \"Observaciones\"";
    }

    private static String firmeCSVFieldNames() {
	return "distinct(gid), " +
		"el.id_firme as \"ID Firme\"," +
		"fecha_inauguracion as \"Fecha Inauguración\"," +
		"fecha_apertura as \"Fecha Apertura\"," +
		"unidad_constructiva as \"Unidad Constructiva\"," +
		"am.item as \"Area Mantenimiento\"," +
		"bc.item as \"Base Contratista\"," +
		"tr.item as \"Tramo\"," +
		"pk_inicial as \"PK Inicial\"," +
		"pk_final as \"PK Final\"," +
		"mu.item as \"Municipio\"," +
		"explanada_cm as \"Explanada (cm)\"," +
		"zahorra_artificial as \"Zahorra Artificial (cm)\"," +
		"suelo_cemento_cm as \"Suelo Cemento (cm)\"," +
		"grava_cemento_cm as \"Grava Cemento (cm)\"," +
		"mbc_base_cm as \"MBC Base (cm)\"," +
		"mbc_intermedia_cm as \"MBC Intermedia (cm)\"," +
		"mbc_rodadura_cm as \"MBC Rodadura (cm)\"," +
		"explanada as \"Materiales: Explanada\"," +
		"zahorra_artificial as \"Materiales: Zahorra Artificial\"," +
		"suelo_cemento as \"Materiales: Suelo Cemento\"," +
		"gc_arido_grueso as \"Grava-Cemento: Árido Grueso\"," +
		"gc_arido_fino as \"Grava-Cemento: Árido Fino\"," +
		"gc_cemento as \"Grava-Cemento: Cemento\"," +
		"mbc_bas_huso as \"MBC Base: Huso (cm)\"," +
		"mbc_bas_arido_grueso as \"MBC Base: Árido Grueso (cm)\"," +
		"mbc_bas_arido_fino \"MBC Base: Árido Fino (cm)\"," +
		"mbc_bas_filler as \"MBC Base: Filler\"," +
		"mbc_bas_ligante as \"MBC Base: Ligante\"," +
		"mbc_rod_huso as \"MBC Rodadura: Huso (cm)\"," +
		"mbc_rod_arido_grueso as \"MBC Rodadura: Árido Grueso (cm)\"," +
		"mbc_rod_arido_fino \"MBC Rodadura: Árido Fino (cm)\"," +
		"mbc_rod_filler as \"MBC Rodadura: Filler\"," +
		"mbc_rod_ligante as \"MBC Rodadura: Ligante\"," +
		"observaciones as \"Observaciones\"";
    }

    private static String obrasPasoCSVFieldNames() {
	return "distinct(gid), " +
		"el.id_obra_paso as \"ID Obra Paso\"," +
		"fecha_actualizacion as \"Fecha Actualización\"," +
		localizationCSVFieldNames() +
		"pk as \"PK\"," +
		"ramal as \"Ramal\"," +
		"st.item as \"Sentido\"," +
		"direccion as \"Dirección\"," +
		"mu.item as \"Municipio\"," +
		"codigo as \"Código\"," +
		"nombre as \"Nombre\"," +
		"fecha_construccion as \"Fecha construcción\"," +
		"tipo_obra as \"Tipo\"," +
		"tipologia as \"Tipología\"," +
		"utm_x as \"Coordenada X (UTM)\"," +
		"utm_y as \"Coordenada Y (UTM)\"," +
		"utm_z as \"Cota (UTM)\"," +
		"longitud as \"Longitud (m)\"," +
		"anchura as \"Anchura (m)\"," +
		"altura as \"Altura (m)\"," +
		"luz_maxima as \"Luz Máxima (m)\"," +
		"n_vanos as \"Nº vanos\"," +
		"n_pilas as \"Nº pilas\"," +
		"n_carriles as \"Nº carriles\"," +
		"galibo_v_c as \"Gálibo V Cre. (m)\"," +
		"galibo_v_d as \"Gálibo V Dec. (m)\"," +
		"observaciones as \"Observaciones\"";
    }

    private static String obrasDesagueCSVFieldNames() {
	return "distinct(gid), " +
		"el.id_obra_desague as \"ID Obra Desagüe\"," +
		"fecha_actualizacion as \"Fecha Actualización\"," +
		localizationCSVFieldNames() +
		"pk as \"PK\"," +
		"ramal as \"Ramal\"," +
		"st.item as \"Sentido\"," +
		"direccion as \"Dirección\"," +
		"mu.item as \"Municipio\"," +
		"tipologia as \"Tipología\"," +
		"material as \"Material\"," +
		"objeto as \"Objeto\"," +
		"fecha_construccion as \"Fecha construcción\"," +
		"n_elementos as \"Nº elementos\"," +
		"seccion as \"Sección (cm)\"," +
		"longitud as \"Longitud (m)\"," +
		"observaciones as \"Observaciones\"";
    }

    private static String murosCSVFieldNames() {
	return "distinct(gid), " +
		"el.id_muro as \"ID Muro\"," +
		"fecha_actualizacion as \"Fecha Actualización\"," +
		localizationCSVFieldNames() +
		"tvf.item as \"Tipo Vía PF\"," +
		"nvf.item as \"Nombre Vía PF\"," +
		"pk_inicial as \"PK Inicial\"," +
		"pk_final as \"PK Final\"," +
		"ramal_pi as \"Ramal\"," +
		"ramal_pf as \"Ramal PF\"," +
		"direccion_pi as \"Dirección\"," +
		"direccion_pf as \"Dirección PF\"," +
		"st.item as \"Sentido\"," +
		"margen as \"Margen\"," +
		"mu.item as \"Municipio\"," +
		"material as \"Material\"," +
		"longitud as \"Longitud\"," +
		"altura_max as \"Altura Máxima\"," +
		"observaciones as \"Observaciones\"";
    }

    private static String lechoFrenadoCSVFieldNames() {
	return "distinct(gid), " +
		"el.id_lecho_frenado as \"ID Lecho Frenado\"," +
		"fecha_actualizacion as \"Fecha Actualización\"," +
		localizationCSVFieldNames() +
		"pk as \"PK\"," +
		"st.item as \"Sentido\"," +
		"mu.item as \"Municipio\"," +
		"longitud as \"Longitud(m)\"," +
		"anchura as \"Anchura(m)\"," +
		"pendiente as \"Pendiente máxima(%)\"," +
		"observaciones as \"Observaciones\"";
    }

    private static String areasMantenimientoCSVFieldNames() {
	return "distinct(gid), " +
		"el.id_area_mantenimiento as \"ID Área Mantenimiento\"," +
		"nombre as \"Nombre\"," +
		"fecha_actualizacion as \"Fecha Actualización\"," +
		localizationCSVFieldNames() +
		"pk as \"PK\"," +
		"st.item as \"Sentido\"," +
		"mu.item as \"Municipio\"," +
		"centro_operaciones as \"Centro de operaciones\"," +
		"centro_comunicaciones as \"Centro de comunicaciones\"," +
		"control_postes_auxilio as \"Control de postes de auxilio\"," +
		"control_tuneles as \"Control de túneles\"," +
		"almacen_fundentes as \"Almacén de fundentes\"," +
		"alumbrado as \"Alumbrado\"," +
		"observaciones as \"Observaciones\"";
    }

    private static String senhalizacionVariableCSVFieldNames() {
	return "distinct(gid), " +
		"el.id_senhal_variable as \"ID Señal\"," +
		"fecha_actualizacion as \"Fecha Actualización\"," +
		localizationCSVFieldNames() +
		"pk as \"PK\"," +
		"ramal as \"Ramal\"," +
		"st.item as \"Sentido\"," +
		"direccion as \"Dirección\"," +
		"margen_senhal as \"Margen Señal\"," +
		"mu.item as \"Municipio\"," +
		"referencia as \"Nº Referencia\"," +
		"fecha_instalacion as \"Fecha Instalación\"," +
		"tipo as \"Tipo\"," +
		"tipo_sustentacion as \"Tipo Sustentación\"," +
		"corunha as \"A Coruña\"," +
		"ferrol as \"Ferrol\"," +
		"santiago as \"Santiago\"," +
		"pontevedra as \"Pontevedra\"," +
		"vigo as \"Vigo\"," +
		"tui as \"Tui\"," +
		"observaciones as \"Observaciones\"";
    }

    public static void createCaracteristicasReport(String[] element, String outputFile,
	    ResultSet rs, ConsultasFilters filters, int reportType) {
	switch (DBFieldNames.Elements.valueOf(element[0])) {
	case Taludes:
	    new TaludesCaracteristicasReport(element[1], outputFile, rs, filters, reportType);
	    break;
	case Isletas:
	    new IsletasCaracteristicasReport(element[1], outputFile, rs, filters, reportType);
	    break;
	case Barrera_Rigida:
	    new BarreraRigidaCaracteristicasReport(element[1], outputFile, rs, filters, reportType);
	    break;
	case Areas_Servicio:
	    new AreasServicioCaracteristicasReport(element[1], outputFile, rs, filters, reportType);
	    break;
	case Areas_Descanso:
	    new AreasDescansoCaracteristicasReport(element[1], outputFile, rs, filters, reportType);
	    break;
	case Areas_Peaje:
	    new AreasPeajeCaracteristicasReport(element[1], outputFile, rs, filters, reportType);
	    break;
	case Enlaces:
	    new EnlacesCaracteristicasReport(element[1], outputFile, rs, filters, reportType);
	    break;
	case Juntas:
	    new JuntasCaracteristicasReport(element[1], outputFile, rs, filters, reportType);
	    break;
	case Pasos_Mediana:
	    new PasosMedianaCaracteristicasReport(element[1], outputFile, rs, filters, reportType);
	    break;
	case Senhalizacion_Vertical:
	    new SenhalizacionVerticalCaracteristicasReport(element[1], outputFile, rs, filters, reportType);
	    break;
	case Valla_Cierre:
	    new VallaCierreCaracteristicasReport(element[1], outputFile, rs, filters, reportType);
	    break;
	case Firme:
	    new FirmeCaracteristicasReport(element[1], outputFile, rs, filters, reportType);
	    break;
	case Obras_Paso:
	    new ObrasPasoCaracteristicasReport(element[1], outputFile, rs, filters, reportType);
	    break;
	case Obras_Desague:
	    new ObrasDesagueCaracteristicasReport(element[1], outputFile, rs, filters, reportType);
	    break;
	case Muros:
	    new MurosCaracteristicasReport(element[1], outputFile, rs, filters, reportType);
	    break;
	case Senhalizacion_Variable:
	    new SenhalizacionVariableCaracteristicasReport(element[1], outputFile, rs, filters, reportType);
	    break;
	case Lecho_Frenado:
	    new LechoFrenadoCaracteristicasReport(element[1], outputFile, rs, filters, reportType);
	    break;
	case Areas_Mantenimiento:
	    new AreasMantenimientoCaracteristicasReport(element[1], outputFile, rs, filters, reportType);
	    break;
	}
    }

    public static String getElementId(String element) {
	PreparedStatement statement;
	String query = "SELECT id_fieldname FROM audasa_extgia_dominios.elemento " +
		"WHERE id = '" + element + "';";
	try {
	    statement = DBSession.getCurrentSession().getJavaConnection().prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();
	    rs.next();
	    return rs.getString(1);
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return null;
    }

    public static boolean hasIndiceFieldOnReconocimientos(String element) {
	Vector<String> elements = new Vector<String>();
	elements.add("Areas_Peaje");
	elements.add("Senhalizacion_Variable");
	if (elements.contains(element)){
	    return false;
	}else {
	    return true;
	}
    }

}
