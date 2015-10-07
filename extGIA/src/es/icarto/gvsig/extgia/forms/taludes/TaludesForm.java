package es.icarto.gvsig.extgia.forms.taludes;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.iver.andami.Launcher;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.extgia.forms.AbstractFormWithLocationWidgets;
import es.icarto.gvsig.extgia.forms.CalculateComponentValue;
import es.icarto.gvsig.extgia.forms.EnableComponentBasedOnCheckBox;
import es.icarto.gvsig.extgia.forms.GIAAlphanumericTableHandler;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.extgia.preferences.DBFieldNames.Elements;
import es.icarto.gvsig.navtableforms.gui.buttons.fileslink.FilesLinkButton;
import es.icarto.gvsig.navtableforms.gui.buttons.fileslink.FilesLinkData;
import es.icarto.gvsig.siga.PreferencesPage;
import es.udc.cartolab.gvsig.users.utils.DBSession;

@SuppressWarnings("serial")
public class TaludesForm extends AbstractFormWithLocationWidgets {

    public static final String TABLENAME = "taludes";

    JComboBox tipoTaludWidget;
    JTextField numeroTaludWidget;
    JTextField taludIDWidget;
    CalculateComponentValue taludid;

    private CalculateComponentValue inclinacionMedia;
    private EnableComponentBasedOnCheckBox cunetaPie;
    private EnableComponentBasedOnCheckBox cunetaCabeza;

    FilesLinkButton filesLinkButton;

    boolean hasJustOpened = true;

    public TaludesForm(FLyrVect layer) {
	super(layer);

	addTableHandler(new GIAAlphanumericTableHandler(
		getTrabajosDBTableName(), getWidgets(), getElementID(),
		DBFieldNames.trabajosVegetacionColNames,
		DBFieldNames.trabajosVegetacionColAlias,
		DBFieldNames.trabajosColWidths, this,
		TaludesTrabajosSubForm.class));

	addTableHandler(new GIAAlphanumericTableHandler(
		getReconocimientosDBTableName(), getWidgets(), getElementID(),
		DBFieldNames.reconocimientosColNames,
		DBFieldNames.reconocimientosColAlias, null, this,
		TaludesReconocimientosSubForm.class));
    }

    private void addNewButtonsToActionsToolBar() {
	JPanel actionsToolBar = this.getActionsToolBar();

	filesLinkButton = new FilesLinkButton(this, new FilesLinkData() {

	    @Override
	    public String getRegisterField() {
		return DBFieldNames
			.getPrimaryKey(DBFieldNames.Elements.Taludes);
	    }

	    @Override
	    public String getBaseDirectory() {
		String baseDirectory = null;
		try {
		    baseDirectory = PreferencesPage.getBaseDirectory();
		} catch (Exception e) {
		}

		if (baseDirectory == null || baseDirectory.isEmpty()) {
		    baseDirectory = Launcher.getAppHomeDir();
		}

		baseDirectory = baseDirectory + File.separator + "FILES"
			+ File.separator + "inventario" + File.separator
			+ DBFieldNames.Elements.Taludes;

		return baseDirectory;
	    }
	});

	if (hasJustOpened) {
	    actionsToolBar.add(filesLinkButton);
	    hasJustOpened = false;
	}
    }

    @Override
    protected void fillSpecificValues() {
	super.fillSpecificValues();
	cunetaCabeza.fillSpecificValues();
	cunetaPie.fillSpecificValues();
	addNewButtonsToActionsToolBar();
    }

    @Override
    protected void setListeners() {
	super.setListeners();
	Map<String, JComponent> widgets = getWidgets();

	taludIDWidget = (JTextField) widgets.get(DBFieldNames.ID_TALUD);

	taludid = new TaludesCalculateTaludIDValue(this, getWidgets(),
		DBFieldNames.ID_TALUD, DBFieldNames.TIPO_TALUD,
		DBFieldNames.NUMERO_TALUD, DBFieldNames.BASE_CONTRATISTA);
	taludid.setListeners();

	inclinacionMedia = new TaludesCalculateInclinacionMediaValue(this,
		getWidgets(), DBFieldNames.INCLINACION_MEDIA,
		DBFieldNames.SECTOR_INCLINACION);
	inclinacionMedia.setListeners();

	cunetaCabeza = new EnableComponentBasedOnCheckBox(
		(JCheckBox) getWidgets().get("cuneta_cabeza"), getWidgets()
		.get("cuneta_cabeza_revestida"));
	// cunetaCabeza.setRemoveDependentValues(true);

	cunetaCabeza.setListeners();
	cunetaPie = new EnableComponentBasedOnCheckBox((JCheckBox) getWidgets()
		.get("cuneta_pie"), getWidgets().get("cuneta_pie_revestida"));
	// cunetaPie.setRemoveDependentValues(true);
	cunetaPie.setListeners();
    }

    @Override
    protected void removeListeners() {
	taludid.removeListeners();
	inclinacionMedia.removeListeners();
	cunetaCabeza.removeListeners();
	cunetaPie.removeListeners();
	super.removeListeners();
    }

    @Override
    protected boolean validationHasErrors() {
	if (this.getFormController().getValuesChanged().containsKey("id_talud")) {
	    if (taludIDWidget.getText() != "") {
		String query = "SELECT id_talud FROM audasa_extgia.taludes "
			+ " WHERE id_talud = '" + taludIDWidget.getText()
			+ "';";
		PreparedStatement statement = null;
		Connection connection = DBSession.getCurrentSession()
			.getJavaConnection();
		try {
		    statement = connection.prepareStatement(query);
		    statement.execute();
		    ResultSet rs = statement.getResultSet();
		    if (rs.next()) {
			JOptionPane.showMessageDialog(null,
				"El ID está en uso, por favor, escoja otro.",
				"ID en uso", JOptionPane.WARNING_MESSAGE);
			return true;
		    }
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	    }
	}
	return super.validationHasErrors();
    }

    @Override
    public JTable getReconocimientosJTable() {
	return null;
    }

    @Override
    public JTable getTrabajosJTable() {
	return null;
    }

    @Override
    protected String getBasicName() {
	return TABLENAME;
    }

    @Override
    public Elements getElement() {
	return DBFieldNames.Elements.Taludes;
    }

    @Override
    protected boolean hasSentido() {
	return true;
    }

    @Override
    public String getElementID() {
	return "id_talud";
    }

    @Override
    public String getElementIDValue() {
	return taludIDWidget.getText();
    }

    @Override
    public String getImagesDBTableName() {
	return "taludes_imagenes";
    }

}
