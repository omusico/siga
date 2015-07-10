package es.icarto.gvsig.extgia.forms.senhalizacion_vertical;

import java.awt.Dimension;
import java.io.File;

import javax.swing.ImageIcon;

import com.iver.cit.gvsig.fmap.core.SymbologyFactory;
import com.iver.cit.gvsig.fmap.edition.IEditableSource;

import es.icarto.gvsig.audasacommons.PreferencesPage;
import es.icarto.gvsig.commons.utils.ImageUtils;
import es.icarto.gvsig.navtableforms.gui.tables.filter.IRowFilter;
import es.icarto.gvsig.navtableforms.gui.tables.model.AlphanumericTableModel;

@SuppressWarnings("serial")
public class SenhalesTableModel extends AlphanumericTableModel {

    private static final Dimension BOUNDARY = new Dimension(40, 25);

    private final String folderPath = SymbologyFactory.SymbolLibraryPath
	    + File.separator + "senhales" + File.separator;

    private final String extension = ".gif";

    private final String imgPath = PreferencesPage.IMG_UNAVAILABLE;
    private final ImageIcon emptyImage = ImageUtils
	    .getScaled(imgPath, BOUNDARY);

    public SenhalesTableModel(IEditableSource source, String[] colNames,
	    String[] colAliases, IRowFilter filter) {
	super(source, colNames, colAliases, filter);
    }

    public SenhalesTableModel(IEditableSource source, String[] colNames,
	    String[] colAliases) {
	super(source, colNames, colAliases);
    }

    private ImageIcon getIcon(int row) {

	String tipoValue = stringValue(getValueAt(row, 1));
	String codigoValue = stringValue(getValueAt(row, 2));

	ImageIcon icon = null;
	if (tipoValue.equals("Cartel")) {
	    icon = ImageUtils.getScaled(folderPath + "0_cartel.png", BOUNDARY);
	} else if (tipoValue.equals("Placa")
		&& (codigoValue.isEmpty() || (codigoValue.equals("Otro")))) {
	    icon = ImageUtils.getScaled(folderPath + "0_placa.png", BOUNDARY);
	} else if (!codigoValue.isEmpty()) {
	    String imgPath = folderPath + codigoValue + extension;
	    // System.out.println(image.getBounds());
	    // ImageIcon icon = ImageUtils.getScaled(imgPath,
	    // image.getBounds().getSize());
	    icon = ImageUtils.getScaled(imgPath, BOUNDARY);
	}

	if (icon == null) {
	    icon = emptyImage;
	}
	return icon;
    }

    private String stringValue(Object o) {
	return o != null ? o.toString().trim() : "";
    }

    @Override
    public Object getValueAt(int row, int col) {
	if (getColumnCount() - 1 == col) {
	    return getIcon(row);
	} else {
	    return super.getValueAt(row, col);
	}
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
	if (getColumnCount() - 1 == columnIndex) {
	    return ImageIcon.class;
	}
	return super.getColumnClass(columnIndex);
    }
}
