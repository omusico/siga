package es.icarto.gvsig.extgia;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;

import es.icarto.gvsig.extgia.consultas.ConsultasPanel;

public class ConsultasExtension extends Extension {

    @Override
    public void initialize() {
    }

    @Override
    public void execute(String actionCommand) {
	ConsultasPanel panel = new ConsultasPanel();
	PluginServices.getMDIManager().addWindow(panel);
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
