package es.udc.cartolab.gvsig.elle;

import javax.swing.JOptionPane;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.udc.cartolab.gvsig.elle.constants.ConstantUtils;
import es.udc.cartolab.gvsig.elle.gui.wizard.WizardWindow;
import es.udc.cartolab.gvsig.elle.gui.wizard.load.SigaLoadMapWizard;

public class SigaLoadMapExtension extends LoadMapExtension {

    @Override
    public void execute(String actionCommand) {
	if (ConstantUtils.getAreaByConnectedUser() == null) {
	    JOptionPane.showMessageDialog(null,
		    PluginServices.getText(this, "userHasNotAreaDefined"));
	} else {
	    View view = createViewIfNeeded();
	    WizardWindow wizard = new SigaLoadMapWizard(view);
	    wizard.open();
	}
    }

}