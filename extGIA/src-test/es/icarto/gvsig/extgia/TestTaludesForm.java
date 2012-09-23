package es.icarto.gvsig.extgia;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.junit.Ignore;
import org.junit.Test;

import com.iver.cit.gvsig.fmap.crs.CRSFactory;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.extgia.forms.taludes.TaludesForm;
import es.icarto.gvsig.extgia.navtableforms.CommonMethodsForTestForms;
import es.icarto.gvsig.navtableforms.utils.LayerController;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class TestTaludesForm extends CommonMethodsForTestForms {

    @Ignore
    @Test
    public void test_writeValues() throws Exception {
	HashMap<String, String> validValues = getValidValues();
	FLyrVect layer = (FLyrVect) DBSession.getCurrentSession().getLayer(
		"layerNameInTocNoMathers", getTable(), getSchema(), "",
		CRSFactory.getCRS("EPSG:23030"));

	TaludesForm dialog = new TaludesForm(layer);
	dialog.init();
	LayerController formController = dialog.getFormController();
	HashMap<String, JComponent> myWidgets = dialog.getWidgetComponents();

	for (JComponent c : myWidgets.values()) {
	    String name = c.getName();
	    String value = validValues.get(name);
	    if (value == null) {
		System.out.println("un widget al que no he asignado valor: "
			+ name);
		continue;
	    }
	    if (c instanceof JFormattedTextField) {
		((JFormattedTextField) c).setText(value);
		formController.setValue(name, value);
	    } else if (c instanceof JTextField) {
		((JTextField) c).setText(value);
		formController.setValue(name, value);
	    } else if (c instanceof JComboBox) {
		// ((JComboBox) c).setSelectedItem(value);
		// formController.setValue(name, value);
	    } else if (c instanceof JCheckBox) {
		boolean selected = value.equals("true");
		((JCheckBox) c).setSelected(selected);
		formController.setValue(name, value);
	    } else if (c instanceof JTextArea) {
		((JTextArea) c).setText(value);
		formController.setValue(name, value);
	    }
	}

	dialog.setChangedValues();
	dialog.saveRecord();
	dialog.refreshGUI();

	for (String name : validValues.keySet()) {
	    assertEquals(name, validValues.get(name), dialog
		    .getFormController().getValueInLayer(name));
	}

    }

    // test_comboboxWidgetHasADomainValue
    // test_comboboxWidgetsWithOutDomain
    // test_widgetWithOutValidationRule

    // test lanzar los valid values directamente contra la base de datos
    private HashMap<String, String> getValidValues() {
	HashMap<String, String> validValues = new HashMap<String, String>();
	validValues.put("id_talud", "D-005N");
	validValues.put("numero_talud", "5");
	validValues.put("area_mantenimiento", "0");
	validValues.put("base_contratista", "Norte");
	validValues.put("tramo", "0");
	validValues.put("tipo_via_pi", "0");
	validValues.put("nombre_pi", "0");
	validValues.put("pk_inicial", "5");
	validValues.put("ramal_pi", "0");
	validValues.put("direccion_pi", "0");
	validValues.put("tipo_via_pf", "0");
	validValues.put("nombre_pf", "0");
	validValues.put("pk_final", "0");
	validValues.put("ramal_pf", "0");
	validValues.put("direccion_pf", "0");
	validValues.put("sentido", "0");
	validValues.put("margen", "Derecho");
	validValues.put("tipo_talud", "Desmonte");
	validValues.put("roca", "false");
	validValues.put("arboles", "true");
	validValues.put("gunita", "false");
	validValues.put("escollera", "false");
	validValues.put("maleza", "false");
	validValues.put("malla", "false");
	validValues.put("observaciones", "0");
	validValues.put("arcen", "false");
	validValues.put("barrera_seguridad", "false");
	validValues.put("cuneta_pie", "false");
	validValues.put("cuneta_pie_revestida", "false");
	validValues.put("cuneta_cabeza", "false");
	validValues.put("cuneta_cabeza_revestida", "false");
	validValues.put("berma", "false");
	validValues.put("longitud", "0");
	validValues.put("sector_inclinacion", "");
	validValues.put("inclinacion_media", "");
	validValues.put("altura_max_talud", "0");
	validValues.put("sup_total_analitica", "0");
	validValues.put("sup_mecanizada_analitica", "0");
	validValues.put("sup_manual_analitica", "0");
	validValues.put("sup_restada_analitica", "0");
	validValues.put("sup_total_campo", "8");
	validValues.put("sup_mecanizada_real", "0");
	validValues.put("sup_restada_campo", "0");
	validValues.put("sup_manual_real", "0");
	validValues.put("sup_complementaria", "0");
	validValues.put("concepto", "0");

	return validValues;
    }

    @Override
    protected String getAbeilleForm() {
	return TaludesForm.ABEILLE_FILENAME;
    }

    @Override
    protected String getSchema() {
	return "audasa_extgia";
    }

    @Override
    protected String getTable() {
	return "taludes";
    }
}
