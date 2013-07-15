package es.icarto.gvsig.extgia.consultas;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class ConsultasFilters {

    private String area;
    private String baseContratista;
    private String tramo;
    private Date fechaInicio;
    private Date fechaFin;

    private final Locale loc = new Locale("es");
    private final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG, loc);

    public ConsultasFilters(String area, String baseContratista, String tramo,
	    Date fechaInicio, Date fechaFin) {
	this.area = area;
	this.baseContratista = baseContratista;
	this.tramo = tramo;
	this.fechaInicio = fechaInicio;
	this.fechaFin = fechaFin;
    }

    public String getArea() {
	return area;
    }

    public void setArea(String area) {
	this.area = area;
    }

    public String getBaseContratista() {
	return baseContratista;
    }

    public void setBaseContratista(String baseContratista) {
	this.baseContratista = baseContratista;
    }

    public String getTramo() {
	return tramo;
    }

    public void setTramo(String tramo) {
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

    public String getWhereClauseByLocationWidgets() {
	String query = "";
	if (area != null) {
	    query = " WHERE area_mantenimiento =  '" + area + "'";
	}
	if (baseContratista != null) {
	    if (!query.isEmpty()) {
		query = query + " AND base_contratista =  '" + baseContratista + "'";
	    }else {
		query = " WHERE base_contratista =  '" + baseContratista + "'";
	    }
	}
	if (tramo != null) {
	    if (!query.isEmpty()) {
		query = query + " AND tramo =  '" + tramo + "'";
	    }else {
		query = " WHERE tramo =  '" + tramo + "'";
	    }
	}
	return query;
    }

    public String getWhereClauseByDates(String dateField) {
	String query = "";
	if (!getWhereClauseByLocationWidgets().isEmpty()) {
	    query = " ) AND " + dateField + " BETWEEN '" + fechaInicio + "' AND '" + fechaFin + "'";
	}else {
	    query = " WHERE " + dateField + " BETWEEN '" + fechaInicio + "' AND '" + fechaFin + "'";
	}
	return query;
    }

    public String getFechaInicioFormatted() {
	return dateFormat.format(fechaInicio);
    }

    public String getFechaFinFormatted() {
	return dateFormat.format(fechaFin);
    }

}
