package es.icarto.gvsig.extgia.consultas;

import java.awt.Font;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.rtf.style.RtfParagraphStyle;

public class TrabajosReport {

    String[] trabajosColumnNames = {
	    "ID Elemento",
	    "Fecha",
	    "Unidad",
	    "Medición Contratista",
	    "Medición AUDASA",
	    "Observaciones",
	    "Fecha Certificado"
    };

    private final com.lowagie.text.Font cellBoldStyle = FontFactory.getFont("arial", 6, Font.BOLD);
    private final com.lowagie.text.Font bodyBoldStyle = FontFactory.getFont("arial", 8, Font.BOLD);

    private final Locale loc = new Locale("es");

    boolean isFirstPage = true;

    public TrabajosReport(String element, String fileName,
	    ResultSet resultMap, String[] filters) {
	writePdfReport(element, fileName, resultMap, filters);
    }

    private Image getHeaderImage() {
	Image image = null;
	try {
	    image = Image
		    .getInstance("gvSIG/extensiones/es.icarto.gvsig.extgex/images/logo_audasa.gif");
	    image.scalePercent((float) 15.00);
	    //	    image.setAbsolutePosition(0, 0);
	    image.setAlignment(Chunk.ALIGN_RIGHT);
	} catch (BadElementException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (MalformedURLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return image;
    }

    private void writeFilters(Document document, String[] filters) {
	try {
	    Paragraph amP = new Paragraph("Area Mantenimiento: " + filters[0],
		    bodyBoldStyle);
	    document.add(amP);
	    Paragraph bcP = new Paragraph("Base Contratista: " + filters[1], bodyBoldStyle);
	    document.add(bcP);
	    Paragraph tramoP = new Paragraph("Tramo: "
		    + filters[2], bodyBoldStyle);
	    document.add(tramoP);
	    Paragraph mesP = new Paragraph("Mes Certificado: "
		    + filters[3], bodyBoldStyle);
	    document.add(mesP);
	    Paragraph anhoP = new Paragraph("Año Certificado: "
		    + filters[4], bodyBoldStyle);
	    document.add(anhoP);
	    document.add(Chunk.NEWLINE);
	} catch (DocumentException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    private void writeTitleAndSubtitle(Document document, String title,
	    String subtitle) {

	Paragraph titleP = new Paragraph(title,
		RtfParagraphStyle.STYLE_HEADING_1);
	titleP.setAlignment(Paragraph.ALIGN_CENTER);
	try {
	    document.add(titleP);
	} catch (DocumentException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	Paragraph subtitleP = new Paragraph(subtitle,
		RtfParagraphStyle.STYLE_HEADING_2);
	subtitleP.setAlignment(Paragraph.ALIGN_CENTER);
	try {
	    document.add(subtitleP);
	} catch (DocumentException e) {
	    // TODO Auto-generated catch block
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
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    private void writeNumberOfTrabajos(Document document, int numTrabajos) {
	Paragraph numFincasP = new Paragraph("Número de trabajos: " + numTrabajos,
		bodyBoldStyle);
	try {
	    document.add(numFincasP);
	} catch (DocumentException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    private float[] getColumnsWidth(int columnCount) {
	float[] columnsWidth = new float[columnCount];

	columnsWidth[0] = 60f;
	columnsWidth[1] = 60f;
	columnsWidth[2] = 100f;
	columnsWidth[3] = 63f;
	columnsWidth[4] = 60f;
	columnsWidth[5] = 155f;
	columnsWidth[6] = 70f;

	return columnsWidth;
    }

    private void writePdfReportContent(Document document, String element,
	    ResultSet resultMap, String[] filters) {
	try {
	    // Header
	    Image image = getHeaderImage();
	    document.add(image);

	    // Write title,subtitle and date report
	    String title = "Listado de Trabajos";
	    String subtitle = element;
	    writeTitleAndSubtitle(document, title, subtitle);
	    writeDate(document);
	    document.add(Chunk.NEWLINE);

	    // write filters
	    writeFilters(document, filters);

	    // Column names
	    PdfPTable table = new PdfPTable(trabajosColumnNames.length);
	    table.setTotalWidth(document.getPageSize().getWidth() -
		    document.leftMargin() - document.rightMargin());
	    table.setWidths(getColumnsWidth(trabajosColumnNames.length));
	    table.setWidthPercentage(100);
	    for (int i = 0; i < trabajosColumnNames.length; i++) {
		Paragraph column = new Paragraph(trabajosColumnNames[i],
			bodyBoldStyle);
		PdfPCell columnCell = new PdfPCell(column);
		columnCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(columnCell);
	    }

	    isFirstPage = false;

	    // Values
	    Paragraph value;
	    int numberOfRows = 0;
	    while (resultMap.next()) {
		for (int column = 1; column <= trabajosColumnNames.length; column++) {
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
		numberOfRows = numberOfRows +1;
	    }
	    document.add(table);
	    document.add(Chunk.NEWLINE);
	    writeNumberOfTrabajos(document, numberOfRows);

	    // Close file
	    document.close();

	} catch (DocumentException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    public void writePdfReport(String element, String fileName,
	    ResultSet resultMap, String[] filters) {
	Document document = new Document();
	try {
	    PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
	    writer.setPageEvent(new MyPageEvent(writer, document, resultMap));
	    document.open();

	    // Write report into document
	    writePdfReportContent(document, element, resultMap, filters);
	    // Close file
	    document.close();

	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (DocumentException e) {
	    // TODO Auto-generated catch block
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
	    if (!isFirstPage) {
		try {
		    document.add(Chunk.NEWLINE);
		    float width = document.getPageSize().getWidth();
		    PdfPTable table = new PdfPTable(trabajosColumnNames.length);
		    table.setWidthPercentage(100);
		    table.setWidths(getColumnsWidth(trabajosColumnNames.length));
		    for (int i = 0; i < trabajosColumnNames.length; i++) {
			Paragraph column = new Paragraph(trabajosColumnNames[i],
				bodyBoldStyle);
			PdfPCell columnCell = new PdfPCell(column);
			columnCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(columnCell);
		    }
		    document.add(table);
		}catch (DocumentException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}
	    }
	}
    }
    public void onEndPage(PdfWriter pdfWriter, Document document) {
	//you do what you want here
    }

}
