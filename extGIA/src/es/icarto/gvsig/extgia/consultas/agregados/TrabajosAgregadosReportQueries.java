package es.icarto.gvsig.extgia.consultas.agregados;

import es.icarto.gvsig.extgia.consultas.ConsultasFieldNames;

public class TrabajosAgregadosReportQueries {

    private final String element;

    public TrabajosAgregadosReportQueries(String element) {
	this.element = element;

    }

    public String getBaseQuery() {
	String elementId = ConsultasFieldNames.getElementId(element);

	return "SELECT distinct(a."
		+ elementId
		+ "), tr.item, tv.item, nv.item, pk_inicial, pk_final, c.item, "
		+ "medicion_audasa "
		+ "FROM audasa_extgia."
		+ element
		+ "_trabajos a, audasa_extgia."
		+ element
		+ " b, "
		+ "audasa_extgia_dominios.sentido c, "
		+ "audasa_extgia_dominios.tramo tr, "
		+ "audasa_extgia_dominios.tipo_via tv, "
		+ "audasa_extgia_dominios.nombre_via nv "
		+ "WHERE a."
		+ elementId
		+ " = b."
		+ elementId
		+ " AND b.sentido = c.id AND b.tramo = tr.id "
		+ "AND b.tipo_via = tv.id AND b.nombre_via = cast (nv.id as text) "
		+ "AND unidad = '";
    }

    public String getBaseSumQuery() {
	return getBaseSumQuery("TOTAL");
    }

    public String getBaseSumQuery(String firstField) {
	return "SELECT '" + firstField
		+ "', '', null, null, null, null, null, sum(medicion_audasa) "
		+ "FROM audasa_extgia." + element + "_trabajos "
		+ "WHERE (unidad = '";
    }

    public String getBaseSumQuery2() {
	return "SELECT sum(medicion_audasa) " + "FROM audasa_extgia." + element
		+ "_trabajos " + "WHERE (unidad = '";
    }

    public String getDesbroceRetroaranhaQuery() {
	return getBaseQuery() + "Desbroce con retroaraña'";
    }

    public String getDesbroceRetroaranhaSumQuery() {
	return getBaseSumQuery() + "Desbroce con retroaraña')";
    }

    public String getDesbroceMecanicoQuery() {
	return getBaseQuery() + "Desbroce mecánico'";
    }

    public String getDesbroceMecanicoSumQuery() {
	return getBaseSumQuery() + "Desbroce mecánico')";
    }

    public String getDesbroceManualQuery() {
	return getBaseQuery() + "Tala y desbroce manual'";
    }

    public String getDesbroceManualSumQuery() {
	return getBaseSumQuery() + "Tala y desbroce manual')";
    }

    public String getDesbroceTotalSumQuery(String string) {
	return getBaseSumQuery(string)
		+ "Desbroce con retroaraña'"
		+ " OR unidad = 'Desbroce mecánico' OR unidad = 'Tala y desbroce manual')";
    }

    public String getDesbroceTotalSumQuery() {
	return getBaseSumQuery2()
		+ "Desbroce con retroaraña'"
		+ " OR unidad = 'Desbroce mecánico' OR unidad = 'Tala y desbroce manual')";
    }

    public String getSiegaMecanicaIsletasQuery() {
	return getBaseQuery() + "Siega mecánica de isletas'";
    }

    public String getSiegaMecanicaIsletasSumQuery() {
	return getBaseSumQuery() + "Siega mecánica de isletas')";
    }

    public String getSiegaMecanicaMedianaQuery() {
	return getBaseQuery() + "Siega mecánica de medianas'";
    }

    public String getSiegaMecanicaMedianaSumQuery() {
	return getBaseSumQuery() + "Siega mecánica de medianas')";
    }

    public String getSiegaMecanicaMediana1_5mQuery() {
	return getBaseQuery() + "Siega mecánica de medianas < 1,5 m'";
    }

    public String getSiegaMecanicaMediana1_5mSumQuery() {
	return getBaseSumQuery() + "Siega mecánica de medianas < 1,5 m')";
    }

    public String getSiegaTotalSumQuery(String string) {
	return getBaseSumQuery(string) + "Siega mecánica de isletas'"
		+ " OR unidad = 'Siega mecánica de medianas'"
		+ " OR unidad = 'Siega mecánica de medianas < 1,5 m')";
    }

    public String getSiegaTotalSumQuery() {
	return getBaseSumQuery2() + "Siega mecánica de isletas'"
		+ " OR unidad = 'Siega mecánica de medianas'"
		+ " OR unidad = 'Siega mecánica de medianas < 1,5 m')";
    }

    public String getHerbicidadQuery() {
	return getBaseQuery() + "Herbicida'";
    }

    public String getHerbicidaSumQuery() {
	return getBaseSumQuery() + "Herbicida')";
    }

    public String getVegetacionQuery() {
	return getBaseQuery()
		+ "Eliminación veg. mediana de HG y transp. a vertedero'";
    }

    public String getVegetacionSumQuery() {
	return getBaseSumQuery()
		+ "Eliminación veg. mediana de HG y transp. a vertedero')";
    }

}
