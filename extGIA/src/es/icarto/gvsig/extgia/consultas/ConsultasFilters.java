package es.icarto.gvsig.extgia.consultas;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.KeyValue;

public class ConsultasFilters {

    private KeyValue area;
    private KeyValue baseContratista;
    private KeyValue tramo;
    private Date fechaInicio;
    private Date fechaFin;

    private final Locale loc = new Locale("es");
    private final DateFormat dateFormat = DateFormat.getDateInstance(
	    DateFormat.LONG, loc);
    private String queryType;
    private List<String> fields;
    private List<String> orderBy;

    public ConsultasFilters(KeyValue area, KeyValue baseContratista,
	    KeyValue tramo, Date fechaInicio, Date fechaFin) {
	this.area = area;
	this.baseContratista = baseContratista;
	this.tramo = tramo;
	this.fechaInicio = fechaInicio;
	this.fechaFin = fechaFin;
    }

    public KeyValue getArea() {
	return area;
    }

    public void setArea(KeyValue area) {
	this.area = area;
    }

    public KeyValue getBaseContratista() {
	return baseContratista;
    }

    public void setBaseContratista(KeyValue baseContratista) {
	this.baseContratista = baseContratista;
    }

    public KeyValue getTramo() {
	return tramo;
    }

    public void setTramo(KeyValue tramo) {
	this.tramo = tramo;
    }

    public Date getFechaInicio() {
	return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
	this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
	return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
	this.fechaFin = fechaFin;
    }

    public String getWhereClauseFiltersForAgregados(String element,
	    boolean isSum) {
	String query = "";
	if (area != null || baseContratista != null || tramo != null) {
	    if (isSum) {
		query = " AND ";
	    } else {
		query = " AND a.";
	    }
	    query = query + ConsultasFieldNames.getElementId(element)
		    + " IN (SELECT "
		    + ConsultasFieldNames.getElementId(element) + " FROM "
		    + DBFieldNames.GIA_SCHEMA + "." + element;
	}
	if (area != null) {
	    query = query + " WHERE area_mantenimiento =  '" + area.getKey()
		    + "'";
	}
	if (baseContratista != null) {
	    if (!query.isEmpty()) {
		query = query + " AND base_contratista =  '"
			+ baseContratista.getKey() + "'";
	    } else {
		query = " WHERE base_contratista =  '"
			+ baseContratista.getKey() + "'";
	    }
	}
	if (tramo != null) {
	    if (!query.isEmpty()) {
		query = query + " AND tramo =  '" + tramo.getKey() + "'";
	    } else {
		query = " WHERE tramo =  '" + tramo.getKey() + "'";
	    }
	}

	if (fechaInicio != null && fechaFin != null) {
	    if (area != null || baseContratista != null || tramo != null) {
		query = query + ") AND fecha_certificado BETWEEN '"
			+ fechaInicio + "' AND '" + fechaFin + "'";
	    } else {
		query = query + " AND fecha_certificado BETWEEN '"
			+ fechaInicio + "' AND '" + fechaFin + "'";
	    }
	}

	return query;
    }

    public String getWhereClauseByLocationWidgets(boolean queryHasWhere) {
	String query = "";
	if (area != null) {
	    if (!queryHasWhere) {
		query = " WHERE area_mantenimiento =  '" + area.getKey() + "'";
	    } else {
		query = " AND area_mantenimiento =  '" + area.getKey() + "'";
	    }
	}
	if (baseContratista != null) {
	    if (!query.isEmpty()) {
		query = query + " AND base_contratista =  '"
			+ baseContratista.getKey() + "'";
	    } else {
		query = " WHERE base_contratista =  '"
			+ baseContratista.getKey() + "'";
	    }
	}
	if (tramo != null) {
	    if (!query.isEmpty()) {
		query = query + " AND tramo =  '" + tramo.getKey() + "'";
	    } else {
		query = " WHERE tramo =  '" + tramo.getKey() + "'";
	    }
	}
	return query;
    }

    public String getWhereClauseByDates(String dateField) {
	String query = "";
	if (!getWhereClauseByLocationWidgets(false).isEmpty()) {
	    query = " ) AND " + dateField + " BETWEEN '" + fechaInicio
		    + "' AND '" + fechaFin + "'";
	} else {
	    query = " WHERE " + dateField + " BETWEEN '" + fechaInicio
		    + "' AND '" + fechaFin + "'";
	}
	return query;
    }

    public String getFechaInicioFormatted() {
	return dateFormat.format(fechaInicio);
    }

    public String getFechaFinFormatted() {
	return dateFormat.format(fechaFin);
    }

    public void setQueryType(String queryType) {
	this.queryType = queryType;
    }

    public String getQueryType() {
	return this.queryType;
    }

    public void setFields(List<String> fields) {
	this.fields = fields;
    }

    public List<String> getFields() {
	return this.fields;
    }

    public void setOrderBy(List<String> orderBy) {
	this.orderBy = orderBy;
    }

    public List<String> getOrderBy() {
	return this.orderBy;
    }

}
