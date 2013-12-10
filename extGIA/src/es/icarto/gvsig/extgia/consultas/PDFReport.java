package es.icarto.gvsig.extgia.consultas;

import java.awt.Font;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.rtf.style.RtfParagraphStyle;

import es.udc.cartolab.gvsig.navtable.format.DateFormatNT;

public abstract class PDFReport {

    protected final com.lowagie.text.Font cellBoldStyle = FontFactory.getFont("arial", 6, Font.BOLD);
    protected final com.lowagie.text.Font bodyBoldStyle = FontFactory.getFont("arial", 8, Font.BOLD);

    private final Locale loc = new Locale("es");

    boolean isFirstPage = true;

    protected Document document;

    protected final ConsultasFilters filters;

    public PDFReport(String element, String fileName,
	    ResultSet resultMap, ConsultasFilters filters, int reportType) {
	this.filters = filters;
	writePdfReport(element, fileName, resultMap, filters, reportType);
    }

    protected abstract String getTitle();

    protected abstract Rectangle setPageSize();

    protected abstract String[] getColumnNames();

    protected abstract float[] getColumnsWidth(int columnCount);

    protected abstract boolean hasEmbebedTable();

    protected abstract PdfPCell writeAditionalColumnName();

    protected abstract PdfPCell writeAditionalColumnValues(String id);

    private Image getHeaderImage() {
	Image image = null;
	try {
	    image = Image
		    .getInstance("gvSIG/extensiones/es.icarto.gvsig.extgex/images/logo_audasa.gif");
	    image.scalePercent((float) 15.00);
	    image.setAlignment(Chunk.ALIGN_RIGHT);
	} catch (BadElementException e) {
	    e.printStackTrace();
	} catch (MalformedURLException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return image;
    }

    private void writeFilters(Document document, ConsultasFilters filters) {
	try {
	    Paragraph amP = null;
	    if (filters.getArea() == null) {
		amP = new Paragraph("Área Mantenimiento: -", bodyBoldStyle);
	    }else {
		amP = new Paragraph("Área Mantenimiento: " + filters.getArea().getValue(),
			bodyBoldStyle);
	    }
	    document.add(amP);

	    Paragraph bcP = null;
	    if (filters.getBaseContratista() == null) {
		bcP = new Paragraph("Base Contratista: -", bodyBoldStyle);
	    }else {
		bcP = new Paragraph("Base Contratista: " + filters.getBaseContratista().getValue(),
			bodyBoldStyle);
	    }
	    document.add(bcP);

	    Paragraph tramoP = null;
	    if (filters.getTramo() == null) {
		tramoP = new Paragraph("Tramo: -", bodyBoldStyle);
	    }else {
		tramoP = new Paragraph("Tramo: "
			+ filters.getTramo().getValue(), bodyBoldStyle);
	    }
	    document.add(tramoP);
	    writeDatesRange(document, filters);
	    document.add(Chunk.NEWLINE);
	} catch (DocumentException e) {
	    e.printStackTrace();
	}
    }

    protected void writeDatesRange(Document document, ConsultasFilters filters)
	    throws DocumentException {
	Paragraph mesP = new Paragraph("Desde: "
		+ filters.getFechaInicioFormatted(), bodyBoldStyle);
	document.add(mesP);
	Paragraph anhoP = new Paragraph("Hasta: "
		+ filters.getFechaFinFormatted(), bodyBoldStyle);
	document.add(anhoP);
    }

    private void writeTitleAndSubtitle(Document document, String title,
	    String subtitle) {

	Paragraph titleP = new Paragraph(title,
		RtfParagraphStyle.STYLE_HEADING_1);
	titleP.setAlignment(Paragraph.ALIGN_CENTER);
	try {
	    document.add(titleP);
	} catch (DocumentException e) {
	    e.printStackTrace();
	}

	Paragraph subtitleP = new Paragraph(subtitle,
		RtfParagraphStyle.STYLE_HEADING_2);
	subtitleP.setAlignment(Paragraph.ALIGN_CENTER);
	try {
	    document.add(subtitleP);
	} catch (DocumentException e) {
	    e.printStackTrace();
	}
    }

    private String getDateFormated() {
	Calendar calendar = Calendar.getInstance();
	DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, loc);
	Date d = calendar.getTime();
	String date = df.format(d);
	return date;
    }

    private void writeDate(Document document) {
	Paragraph dateP = new Paragraph(getDateFormated(), bodyBoldStyle);
	dateP.setAlignment(Paragraph.ALIGN_CENTER);
	try {
	    document.add(dateP);
	} catch (DocumentException e) {
	    e.printStackTrace();
	}
    }

    private void writeNumberOfRows(Document document, int numRows) {
	Paragraph numRowsP = new Paragraph("Número de registros: " + numRows,
		bodyBoldStyle);
	try {
	    document.add(numRowsP);
	} catch (DocumentException e) {
	    e.printStackTrace();
	}
    }

    private void writePdfReportContent(Document document, String element,
	    ResultSet resultMap, ConsultasFilters filters, int reportType) {
	try {
	    // Header
	    Image image = getHeaderImage();
	    document.add(image);

	    // Footer
	    Phrase footerPhrase = new Phrase("Página: ", bodyBoldStyle);
	    HeaderFooter footer = new HeaderFooter(footerPhrase, true);
	    footer.setBorder(Rectangle.NO_BORDER);
	    document.setFooter(footer);

	    // Write title,subtitle and date report
	    String title = getTitle();
	    String subtitle = element;
	    writeTitleAndSubtitle(document, title, subtitle);
	    writeDate(document);
	    document.add(Chunk.NEWLINE);

	    // write filters
	    writeFilters(document, filters);

	    // Column names
	    PdfPTable table = writeColumnNames(document);

	    isFirstPage = false;

	    // Values
	    writeValues(document, resultMap, table, reportType);

	    // Close file
	    document.close();

	} catch (DocumentException e) {
	    e.printStackTrace();
	} catch (SQLException e) {
	    e.printStackTrace();
	}

    }

    protected void writeValues(Document document, ResultSet resultMap,
	    PdfPTable table, int reportType) throws SQLException, DocumentException {
	Paragraph value;
	int numberOfRows = 0;
	resultMap.beforeFirst();
	int startColumn;
	int endColumn;
	if (reportType != 4) {
	    startColumn = 1;
	    endColumn = getColumnNames().length;
	}else {
	    startColumn = 2;
	    endColumn = getColumnNames().length + 1 ;
	}
	while (resultMap.next()) {
	    for (int column = startColumn; column <= endColumn; column++) {
		if (resultMap.getString(column) != null) {
		    if (resultMap.getMetaData().getColumnType(column) == 91) {
			SimpleDateFormat dateFormat = DateFormatNT.getDateFormat();
			Date date = resultMap.getDate(column);
			String dateAsString = dateFormat.format(date);
			value = new Paragraph(dateAsString, cellBoldStyle);
		    }else {
			// Boolean field
			if (resultMap.getString(column).toString().equals("f")) {
			    value = new Paragraph("No",
				    cellBoldStyle);
			}else if (resultMap.getString(column).toString().equals("t")) {
			    value = new Paragraph("Sí",
				    cellBoldStyle);
			}else if (resultMap.getString(column).toString().contains(".")) {
			    value = new Paragraph(
				    resultMap.getString(column).toString().replace(".", ","),
				    cellBoldStyle);

			}else {
			    value = new Paragraph(resultMap
				    .getString(column).toString(),
				    cellBoldStyle);
			}
		    }
		} else {
		    value = new Paragraph("");
		}
		PdfPCell valueCell = new PdfPCell(value);
		valueCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(valueCell);
	    }
	    if (hasEmbebedTable()) {
		table.addCell(writeAditionalColumnValues(resultMap.getString(2)));
	    }
	    numberOfRows = numberOfRows +1;
	}
	document.add(table);
	document.add(Chunk.NEWLINE);
	writeNumberOfRows(document, numberOfRows);
    }

    protected PdfPTable writeColumnNames(Document document)
	    throws DocumentException {
	PdfPTable table = getColumnNames(document);
	return table;
    }

    private PdfPTable getColumnNames(Document document)
	    throws DocumentException {
	int numColumns;
	if (hasEmbebedTable()) {
	    numColumns = getColumnNames().length + 1;
	}else {
	    numColumns = getColumnNames().length;
	}
	PdfPTable table = new PdfPTable(numColumns);
	table.setTotalWidth(document.getPageSize().getWidth() -
		document.leftMargin() - document.rightMargin());
	table.setWidths(getColumnsWidth(numColumns));
	table.setWidthPercentage(100);
	for (int i = 0; i < getColumnNames().length; i++) {
	    Paragraph column = new Paragraph(getColumnNames()[i],
		    bodyBoldStyle);
	    PdfPCell columnCell = new PdfPCell(column);
	    columnCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	    table.addCell(columnCell);
	}
	if (hasEmbebedTable()) {
	    table.addCell(writeAditionalColumnName());
	}
	return table;
    }

    public void writePdfReport(String element, String fileName,
	    ResultSet resultMap, ConsultasFilters filters, int reportType) {
	document = new Document(setPageSize());
	try {
	    PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
	    writer.setPageEvent(new MyPageEvent(writer, document, resultMap));
	    document.open();

	    // Write report into document
	    writePdfReportContent(document, element, resultMap, filters, reportType);
	    // Close file
	    document.close();

	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (DocumentException e) {
	    e.printStackTrace();
	}
    }

    public class MyPageEvent extends PdfPageEventHelper {
	private final PdfWriter pdfWriter;
	private final Document document;
	private final ResultSet resultMap;

	public MyPageEvent(PdfWriter pdfWriter, Document document, ResultSet resultMap) {
	    this.pdfWriter = pdfWriter;
	    this.document = document;
	    this.resultMap = resultMap;
	}
	@Override
	public void onStartPage(PdfWriter pdfWriter, Document document) {
	    if (!isFirstPage && getColumnNames() != null) {
		try {
		    document.add(Chunk.NEWLINE);
		    PdfPTable table = getColumnNames(document);
		    document.add(table);
		}catch (DocumentException e1) {
		    e1.printStackTrace();
		}
	    }
	}

	@Override
	public void onEndPage(PdfWriter pdfWriter, Document document) {

	}
    }


    protected ConsultasFilters getFilters() {
	return filters;
    }

}
