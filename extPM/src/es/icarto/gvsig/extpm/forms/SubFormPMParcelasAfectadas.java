package es.icarto.gvsig.extpm.forms;

import javax.swing.JPanel;

import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.jeta.forms.components.panel.FormPanel;

public class SubFormPMParcelasAfectadas extends JPanel implements IWindow {

    public void SubFormPMParcelasAfectads() {
	FormPanel panel = new FormPanel("forms/pm_parcelas_afectadas.xml");
	this.add(panel);
    }

    @Override
    public WindowInfo getWindowInfo() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Object getWindowProfile() {
	// TODO Auto-generated method stub
	return null;
    }

}
