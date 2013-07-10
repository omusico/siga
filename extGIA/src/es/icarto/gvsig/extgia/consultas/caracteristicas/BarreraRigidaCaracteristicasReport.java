package es.icarto.gvsig.extgia.consultas.caracteristicas;

import java.sql.ResultSet;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;

import es.icarto.gvsig.extgia.consultas.Report;

public class BarreraRigidaCaracteristicasReport extends Report {

    public BarreraRigidaCaracteristicasReport(String element, String fileName,
	    ResultSet resultMap, String[] filters) {
	super(element, fileName, resultMap, filters);
	// TODO Auto-generated constructor stub
    }

    @Override
    protected String getTitle() {
	return "Listado de Caracter�sticas";
    }

    @Override
    protected Rectangle setPageSize() {
	return PageSize.A4.rotate();
    }

    @Override
    protected String[] getColumnNames() {
	String[] columnNames = {
		"ID Barrera R�gida",
		"Tipo V�a",
		"Nombre V�a",
		"PK Inicial",
		"PK Final",
		"Obstaculo Protegido",
		"Longitud",
		"Codigo",
		"Tipo",
		"M�todo Constructivo",
		"Perfil",
		"Observaciones"
	};
	return columnNames;
    }

    @Override
    protected float[] getColumnsWidth(int columnCount) {
	float[] columnsWidth = new float[columnCount];

	columnsWidth[0] = 60f;
	columnsWidth[1] = 60f;
	columnsWidth[2] = 90f;
	columnsWidth[3] = 60f;
	columnsWidth[4] = 60f;
	columnsWidth[5] = 60f;
	columnsWidth[6] = 60f;
	columnsWidth[7] = 60f;
	columnsWidth[8] = 60f;
	columnsWidth[9] = 60f;
	columnsWidth[10] = 60f;
	columnsWidth[11] = 120f;

	return columnsWidth;
    }

    @Override
    protected void writeDatesRange(Document document, String[] filters) {

    }
}
