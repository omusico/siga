package es.icarto.gvsig.extgex;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;

import es.icarto.gvsig.extgex.queries.QueriesPanel;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class QueriesExtension extends Extension {

    @Override
    public void execute(String actionCommand) {
	QueriesPanel validationPanel = new QueriesPanel();
	validationPanel.openDialog();
    }

    @Override
    public void initialize() {
	PluginServices.getIconTheme().registerDefault(
		"extgex-queries",
		this.getClass().getClassLoader()
			.getResource("images/queries.png"));
    }

    @Override
    public boolean isEnabled() {
	return DBSession.isActive();
    }

    @Override
    public boolean isVisible() {
	return true;
    }
}
