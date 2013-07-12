package es.icarto.gvsig.extgia.consultas.agregados;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

import es.icarto.gvsig.extgia.consultas.PDFReport;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public abstract class TrabajosAgregadosReport extends PDFReport {

    private final TrabajosAgregadosReportQueries agregadosReportQueries;

    public TrabajosAgregadosReport(String[] element, String fileName,
	    ResultSet resultMap, String[] filters) {
	super(element[1], fileName, null, filters);
	agregadosReportQueries = new TrabajosAgregadosReportQueries(getElement());
    }

    protected abstract String getElement();

    @Override
    protected String getTitle() {
	return "Mediciones Vegetaci�n";
    }

    @Override
    protected Rectangle setPageSize() {
	return PageSize.A4;
    }

    @Override
    protected String[] getColumnNames() {
	String[] columnNames = {
		"ID Elemento",
		"PK Inicial",
		"PK Final",
		"Sentido",
		"Medici�n Contratista",
		"Medici�n AUDASA"
	};
	return columnNames;
    }

    @Override
    protected float[] getColumnsWidth(int columnCount) {
	float[] columnsWidth = new float[columnCount];

	columnsWidth[0] = 60f;
	columnsWidth[1] = 60f;
	columnsWidth[2] = 60f;
	columnsWidth[3] = 60f;
	columnsWidth[4] = 60f;
	columnsWidth[5] = 60f;

	return columnsWidth;
    }

    @Override
    protected PdfPTable writeColumnNames(Document document) {
	return null;

    }

    @Override
    protected void writeValues (Document document, ResultSet resultMap, PdfPTable table) {
	writeTable("Desbroce con retroara�a\n\n",
		agregadosReportQueries.getDesbroceRetroaranhaQuery(),
		agregadosReportQueries.getDesbroceRetroaranhaSumQuery());
	writeTable("\nDesbroce mec�nico\n\n",
		agregadosReportQueries.getDesbroceMecanicoQuery(),
		agregadosReportQueries.getDesbroceMecanicoSumQuery());
	writeTable("\nTala y desbroce manual\n\n",
		agregadosReportQueries.getDesbroceManualQuery(),
		agregadosReportQueries.getDesbroceManualSumQuery());

	writeTotal(document, "TOTAL DESBROCES",
		agregadosReportQueries.getDesbroceTotalSumQuery());

	writeTable("\nSiega mec�nica isletas\n\n",
		agregadosReportQueries.getSiegaMecanicaIsletasQuery(),
		agregadosReportQueries.getSiegaMecanicaIsletasSumQuery());

	writeTable("\nSiega mec�nica medianas\n\n",
		agregadosReportQueries.getSiegaMecanicaMedianaQuery(),
		agregadosReportQueries.getSiegaMecanicaMedianaSumQuery());

	writeTotal(document, "TOTAL SEGADO DE HIERBAS",
		agregadosReportQueries.getSiegaTotalSumQuery());

	writeTable("\nHerbicida\n\n",
		agregadosReportQueries.getHerbicidadQuery(),
		agregadosReportQueries.getHerbicidaSumQuery());

	writeTable("\nVegeraci�n mediana de hormig�n\n\n",
		agregadosReportQueries.getVegeracionQuery(),
		agregadosReportQueries.getVegeracionSumQuery());
    }

    private void writeTotal(Document document, String title, String query) {
	ResultSet rs = getValuesFromQuery(query);
	try {
	    rs.next();
	    if (rs.getString(1)!=null) {
		PdfPTable totalTable = new PdfPTable(getColumnNames().length);
		totalTable.setTotalWidth(document.getPageSize().getWidth() -
			document.leftMargin() - document.rightMargin());
		totalTable.setWidths(getColumnsWidth(getColumnNames().length));
		totalTable.setWidthPercentage(100);

		document.add(Chunk.NEWLINE);

		PdfPCell totalCell = new PdfPCell(new Paragraph(title, bodyBoldStyle));
		totalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		totalCell.setBorder(Rectangle.NO_BORDER);
		totalTable.addCell(totalCell);

		for (int i=0; i<3; i++) {
		    PdfPCell cell = new PdfPCell();
		    cell.setBorder(Rectangle.NO_BORDER);
		    totalTable.addCell(cell);
		}

		rs.beforeFirst();
		while (rs.next()) {
		    PdfPCell medicionContratistaCell =
			    new PdfPCell(new Paragraph(rs.getString(1), cellBoldStyle));
		    medicionContratistaCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		    totalTable.addCell(medicionContratistaCell);
		    PdfPCell medicionAudasaCell =
			    new PdfPCell(new Paragraph(rs.getString(2), cellBoldStyle));
		    medicionAudasaCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		    totalTable.addCell(medicionAudasaCell);
		}
		document.add(totalTable);
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} catch (DocumentException e) {
	    e.printStackTrace();
	}
    }

    private void writeTable (String tableTittle, String contentQuery, String totalQuery) {
	try {
	    ResultSet resultMap;

	    resultMap = getValuesFromQuery(contentQuery);
	    if (resultMap.next()) {
		document.add(new Paragraph(tableTittle, bodyBoldStyle));
		PdfPTable table = super.writeColumnNames(document);
		writeTableContent(table, resultMap);
		if (totalQuery != null) {
		    resultMap = getValuesFromQuery(totalQuery);
		    PdfPCell totalCell = new PdfPCell(new Paragraph("TOTAL", bodyBoldStyle));
		    totalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		    table.addCell(totalCell);

		    for (int i=0; i<3; i++) {
			PdfPCell cell = new PdfPCell();
			table.addCell(cell);
		    }

		    while (resultMap.next()) {
			PdfPCell medicionContratistaCell =
				new PdfPCell(new Paragraph(resultMap.getString(1), cellBoldStyle));
			medicionContratistaCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(medicionContratistaCell);
			PdfPCell medicionAudasaCell =
				new PdfPCell(new Paragraph(resultMap.getString(2), cellBoldStyle));
			medicionAudasaCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(medicionAudasaCell);
		    }
		    document.add(table);
		}
	    }
	}catch (DocumentException e) {
	    e.printStackTrace();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    private ResultSet getValuesFromQuery (String query) {
	PreparedStatement statement;
	try {
	    statement = DBSession.getCurrentSession().getJavaConnection().prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();
	    return rs;
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return null;
    }

    private void writeTableContent(PdfPTable table, ResultSet resultMap)
	    throws DocumentException, SQLException {
	Paragraph value;
	resultMap.beforeFirst();
	while (resultMap.next()) {
	    for (int column = 1; column <= getColumnNames().length; column++) {
		if (resultMap.getString(column) != null) {
		    value = new Paragraph(resultMap
			    .getString(column).toString(),
			    cellBoldStyle);
		} else {
		    value = new Paragraph("");
		}
		PdfPCell valueCell = new PdfPCell(value);
		valueCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(valueCell);
	    }
	}
    }

}