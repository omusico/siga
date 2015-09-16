package es.icarto.gvsig.extgex;

import es.icarto.gvsig.extgex.queries.QueriesPanel;
import es.icarto.gvsig.siga.AbstractExtension;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class QueriesExtension extends AbstractExtension {

    @Override
    public void execute(String actionCommand) {
	QueriesPanel validationPanel = new QueriesPanel();
	validationPanel.openDialog();
    }

    @Override
    public boolean isEnabled() {
	return DBSession.isActive();
    }

}
