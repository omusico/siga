package es.icarto.gvsig.extgia;

import javax.swing.JOptionPane;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.extgia.forms.areas_descanso.AreasDescansoForm;
import es.icarto.gvsig.extgia.forms.areas_servicio.AreasServicioForm;
import es.icarto.gvsig.extgia.forms.barrera_rigida.BarreraRigidaForm;
import es.icarto.gvsig.extgia.forms.enlaces.EnlacesForm;
import es.icarto.gvsig.extgia.forms.isletas.IsletasForm;
import es.icarto.gvsig.extgia.forms.juntas.JuntasForm;
import es.icarto.gvsig.extgia.forms.pasos_mediana.PasosMedianaForm;
import es.icarto.gvsig.extgia.forms.taludes.TaludesForm;
import es.icarto.gvsig.extgia.preferences.Preferences;
import es.icarto.gvsig.navtableforms.ormlite.ORMLite;
import es.icarto.gvsig.navtableforms.utils.TOCLayerManager;

public class FormLauncherExtension extends Extension {

    public enum elements {
	Taludes, Isletas, Enlaces, Barrera_Rigida, Areas_Servicio, Areas_Descanso,
	Juntas, Pasos_Mediana;
    }

    private FLyrVect layer;

    @Override
    public void execute(String actionCommand) {
	// TODO. check if layer and tables are correctly loaded

	this.layer = getLayerFromTOC(actionCommand);
	if (this.layer != null) {
	    String layerName = layer.getName();

	    switch (elements.valueOf(layerName)) {
	    case Taludes:
		final TaludesForm taludesForm = new TaludesForm(this.layer);
		if (taludesForm.init()) {
		    PluginServices.getMDIManager().addCentredWindow(taludesForm);
		}
		break;
	    case Isletas:
		final IsletasForm isletasForm = new IsletasForm(this.layer);
		if (isletasForm.init()) {
		    PluginServices.getMDIManager().addCentredWindow(isletasForm);
		}
		break;
	    case Enlaces:
		final EnlacesForm enlacesForm = new EnlacesForm(this.layer);
		if (enlacesForm.init()) {
		    PluginServices.getMDIManager().addCentredWindow(enlacesForm);
		}
		break;
	    case Barrera_Rigida:
		final BarreraRigidaForm barreraRigidaForm = new BarreraRigidaForm(this.layer);
		if (barreraRigidaForm.init()) {
		    PluginServices.getMDIManager().addCentredWindow(barreraRigidaForm);
		}
		break;
	    case Areas_Servicio:
		final AreasServicioForm areasServicioForm = new AreasServicioForm(this.layer);
		if (areasServicioForm.init()) {
		    PluginServices.getMDIManager().addCentredWindow(areasServicioForm);
		}
		break;
	    case Areas_Descanso:
		final AreasDescansoForm areasDescansoForm = new AreasDescansoForm(this.layer);
		if (areasDescansoForm.init()) {
		    PluginServices.getMDIManager().addCentredWindow(areasDescansoForm);
		}
		break;
	    case Juntas:
		final JuntasForm juntasForm = new JuntasForm(this.layer);
		if (juntasForm.init()) {
		    PluginServices.getMDIManager().addCentredWindow(juntasForm);
		}
		break;
	    case Pasos_Mediana:
		final PasosMedianaForm pasosMedianaForm = new PasosMedianaForm(this.layer);
		if (pasosMedianaForm.init()) {
		    PluginServices.getMDIManager().addCentredWindow(pasosMedianaForm);
		}
		break;
	    }
	} else {
	    JOptionPane.showMessageDialog(null, "La capa " + actionCommand
		    + " no está cargada en el TOC", "Capa no encontrada",
		    JOptionPane.ERROR_MESSAGE);
	}
    }

    private FLyrVect getLayerFromTOC(String actionCommand) {
	final String layerName = ORMLite
		.getDataBaseObject(
			Preferences.getPreferences().getXMLFilePath())
			.getTable(actionCommand).getTableName();
	final TOCLayerManager toc = new TOCLayerManager();
	return toc.getLayerByName(layerName);
    }

    protected void registerIcons() {
	PluginServices.getIconTheme().registerDefault(
		"extgia-openform",
		this.getClass().getClassLoader()
		.getResource("images/extgia-openform.png"));
    }

    @Override
    public void initialize() {
	registerIcons();
    }

    @Override
    public boolean isEnabled() {
	return true;
    }

    @Override
    public boolean isVisible() {
	return true;
    }

}
