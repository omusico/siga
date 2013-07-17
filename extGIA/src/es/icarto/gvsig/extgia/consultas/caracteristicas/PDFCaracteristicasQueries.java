package es.icarto.gvsig.extgia.consultas.caracteristicas;

import es.icarto.gvsig.extgia.consultas.ConsultasFilters;

public class PDFCaracteristicasQueries {

    public static final String getSenhalizacionVerticalQuery(ConsultasFilters filters) {
	return "SELECT sv.id_elemento_senhalizacion, tv.item, nv.item, pk, tipo_sustentacion," +
		"material_sustentacion, tipo_poste, numero_postes, anclaje, " +
		"cimentacion_especial, sv.observaciones, tipo_senhal, codigo_senhal, leyenda," +
		"panel_complementario, codigo_panel, texto_panel, reversible, luminosa," +
		"tipo_superficie, material_superficie, material_retrorreflectante," +
		"nivel_reflectancia, ancho, alto, superficie, altura, fabricante," +
		"fecha_fabricacion, fecha_instalacion, fecha_reposicion, marcado_ce," +
		"se.observaciones" +
		" FROM audasa_extgia.senhalizacion_vertical sv," +
		" audasa_extgia.senhalizacion_vertical_senhales se," +
		" audasa_extgia_dominios.tipo_via tv," +
		" audasa_extgia_dominios.nombre_via nv" +
		" WHERE sv.id_elemento_senhalizacion = se.id_elemento_senhalizacion" +
		" AND sv.tipo_via = tv.id AND sv.nombre_via = cast (nv.id as text)" +
		filters.getWhereClauseByLocationWidgets(true) +
		" ORDER BY sv.id_elemento_senhalizacion";
    }

}
