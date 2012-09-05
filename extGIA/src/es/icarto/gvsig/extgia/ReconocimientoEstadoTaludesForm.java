package es.icarto.gvsig.extgia;

import java.awt.Dimension;

import javax.swing.JTextField;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiFrame.MDIFrame;
import com.iver.andami.ui.mdiManager.WindowInfo;

import es.icarto.gvsig.extgia.preferences.Preferences;
import es.icarto.gvsig.navtableforms.model.TableModelAlphanumeric;
import es.icarto.gvsig.navtableforms.view.AlphanumericFormView;

@SuppressWarnings("serial")
public class ReconocimientoEstadoTaludesForm extends AlphanumericFormView {

    public static final String ABEILLE_FILENAME = "forms/taludes_reconocimiento_estado.xml";

    private WindowInfo windowInfo;

    private CalculateIndiceEstado calculateIndiceEstado;

    public ReconocimientoEstadoTaludesForm(TableModelAlphanumeric tableModel) {
	super(tableModel);
    }

    @Override
    protected String getXMLPath() {
	return Preferences.getPreferences().getXMLFilePath();
    }

    @Override
    protected void setListeners() {
	super.setListeners();
	calculateIndiceEstado = new CalculateIndiceEstado(controller,
		getWidgetsVector(), "indice_estado",
		"existencia_deformaciones_o_grietas",
		"peligro_caida_materiales", "bajante_deteriorada",
		"elementos_proteccion_talud");
	calculateIndiceEstado.setListeners();
    }

    @Override
    protected void removeListeners() {
	calculateIndiceEstado.removeListeners();
	super.removeListeners();
    }

    @Override
    public WindowInfo getWindowInfo() {
	if (windowInfo == null) {
	    windowInfo = new WindowInfo(WindowInfo.MODALDIALOG
		    | WindowInfo.RESIZABLE | WindowInfo.PALETTE);
	    windowInfo.setTitle(PluginServices.getText(this,
		    "Taludes - Reconocimiento Estado"));
	    Dimension dim = getPreferredSize();
	    MDIFrame a = (MDIFrame) PluginServices.getMainFrame();
	    int maxHeight = a.getHeight() - 175;
	    int maxWidth = a.getWidth() - 15;

	    int width, heigth = 0;
	    if (dim.getHeight() > maxHeight) {
		heigth = maxHeight;
	    } else {
		heigth = new Double(dim.getHeight()).intValue();
	    }
	    if (dim.getWidth() > maxWidth) {
		width = maxWidth;
	    } else {
		width = new Double(dim.getWidth()).intValue();
	    }
	    windowInfo.setWidth(width + 20);
	    windowInfo.setHeight(heigth + 15);
	}
	return windowInfo;
    }

    @Override
    public Object getWindowProfile() {
	return WindowInfo.DIALOG_PROFILE;
    }

    @Override
    public String getFormPanelClassPath() {
	return ABEILLE_FILENAME;
    }

    @Override
    protected void fillWidgetsForCreatingRecord() {
	super.fillWidgetsForCreatingRecord();
	((JTextField) getWidgetsVector().get("id_talud"))
		.setText(foreignKeyValue);
	model.getDAL().setValue("id_talud", foreignKeyValue);
    }

}
