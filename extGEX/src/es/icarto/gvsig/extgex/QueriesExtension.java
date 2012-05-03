package es.icarto.gvsig.extgex;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;

import es.icarto.gvsig.extgex.queries.QueriesPanel;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class QueriesExtension extends Extension {

    @Override
    public void execute(String actionCommand) {
	QueriesPanel validationPanel = new QueriesPanel(false);
	validationPanel.open();
    }

    @Override
    public void initialize() {
	// TODO Auto-generated method stub

    }

    @Override
    public boolean isEnabled() {
	if (DBSession.getCurrentSession() != null) {
	    return true;
	} else {
	    return false;
	}
    }

    @Override
    public boolean isVisible() {
	return true;
    }

    protected void registerIcons() {
	PluginServices.getIconTheme().registerDefault(
		"extgex-queries",
		this.getClass().getClassLoader().getResource(
			"images/queries.png"));
    }
}
