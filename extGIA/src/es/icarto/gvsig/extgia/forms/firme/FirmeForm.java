package es.icarto.gvsig.extgia.forms.firme;

import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.image.ImageComponent;

import es.icarto.gvsig.audasacommons.PreferencesPage;
import es.icarto.gvsig.extgia.forms.images.AddImageListener;
import es.icarto.gvsig.extgia.forms.images.DeleteImageListener;
import es.icarto.gvsig.extgia.forms.images.ShowImageAction;
import es.icarto.gvsig.extgia.forms.utils.AbstractFormWithLocationWidgets;
import es.icarto.gvsig.extgia.forms.utils.CalculateComponentValue;
import es.icarto.gvsig.extgia.forms.utils.ReconocimientosHandler;
import es.icarto.gvsig.extgia.forms.utils.TrabajosHandler;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

@SuppressWarnings("serial")
public class FirmeForm extends AbstractFormWithLocationWidgets {

    public static final String TABLENAME = "firme";

    public static String[] firmeReconocimientoColNames = { "n_inspeccion",
	    "tipo_inspeccion", "nombre_revisor", "aparato_medicion",
	    "fecha_inspeccion" };
    public static String[] firmeReconocimientoColAlias = { "Nº Inspección",
	    "Tipo", "Revisor", "Aparato", "Fecha Inspección" };

    JTextField firmeIDWidget;
    CalculateComponentValue firmeid;

    public static String[] firmeTrabajoColNames = { "id_trabajo",
	    "fecha_certificado", "pk_inicial", "pk_final", "sentido",
	    "descripcion" };

    public static String[] firmeTrabajoColAlias = { "ID", "Fecha cert",
	    "PK inicio", "PK fin", "Sentido", "Descripción" };

    public FirmeForm(FLyrVect layer) {
	super(layer);

	addTableHandler(new ReconocimientosHandler(
		getReconocimientosDBTableName(), getWidgetComponents(),
		getElementID(), firmeReconocimientoColNames,
		firmeReconocimientoColAlias, this));

	// int[] trabajoColumnsSize = { 1, 1, 1, 1, 30, 250 };
	addTableHandler(new TrabajosHandler(getTrabajosDBTableName(),
		getWidgetComponents(), getElementID(), firmeTrabajoColNames,
		firmeTrabajoColAlias, this));
    }

    private void addNewButtonsToActionsToolBar() {
	super.addNewButtonsToActionsToolBar(DBFieldNames.Elements.Firme);
    }

    @Override
    protected void fillSpecificValues() {
	super.fillSpecificValues();
	if (firmeIDWidget.getText().isEmpty()) {
	    firmeid = new FirmeCalculateIDValue(this, getWidgets(),
		    DBFieldNames.ID_FIRME, DBFieldNames.ID_FIRME);
	    firmeid.setValue(true);
	}

	if (filesLinkButton == null) {
	    addNewButtonsToActionsToolBar();
	}

	if (addImageListener != null) {
	    addImageListener.setPkValue(getElementIDValue());
	}

	if (deleteImageListener != null) {
	    deleteImageListener.setPkValue(getElementIDValue());
	}

	// Element image
	new ShowImageAction(imageComponent, addImageButton,
		getImagesDBTableName(), getElementID(), getElementIDValue());

	repaint();
    }

    @Override
    protected void setListeners() {
	super.setListeners();

	ImageComponent image = (ImageComponent) formBody
		.getComponentByName("image");
	ImageIcon icon = new ImageIcon(PreferencesPage.AUDASA_ICON);
	image.setIcon(icon);

	Map<String, JComponent> widgets = getWidgets();

	imageComponent = (ImageComponent) formBody
		.getComponentByName("element_image");
	addImageButton = (JButton) formBody
		.getComponentByName("add_image_button");
	deleteImageButton = (JButton) formBody
		.getComponentByName("delete_image_button");

	if (addImageListener == null) {
	    addImageListener = new AddImageListener(imageComponent,
		    addImageButton, getImagesDBTableName(), getElementID());
	    addImageButton.addActionListener(addImageListener);
	}

	if (deleteImageListener == null) {
	    deleteImageListener = new DeleteImageListener(imageComponent,
		    addImageButton, getImagesDBTableName(), getElementID());
	    deleteImageButton.addActionListener(deleteImageListener);
	}
	firmeIDWidget = (JTextField) widgets.get(DBFieldNames.ID_FIRME);
    }

    @Override
    public JTable getReconocimientosJTable() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public JTable getTrabajosJTable() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public boolean isSpecialCase() {
	return true;
    }

    @Override
    protected String getBasicName() {
	return TABLENAME;
    }

    @Override
    public String getElement() {
	return DBFieldNames.Elements.Firme.name();
    }

    @Override
    protected boolean hasSentido() {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public String getElementID() {
	return "id_firme";
    }

    @Override
    public String getElementIDValue() {
	return firmeIDWidget.getText();
    }

    @Override
    public String getImagesDBTableName() {
	return "firme_imagenes";
    }

}
