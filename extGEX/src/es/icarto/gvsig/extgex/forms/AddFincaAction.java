package es.icarto.gvsig.extgex.forms;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.apache.log4j.Logger;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.CADExtension;
import com.iver.cit.gvsig.StartEditing;
import com.iver.cit.gvsig.exceptions.expansionfile.ExpansionFileReadException;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.icarto.gvsig.extgex.cad.AddFincaCADTool;
import es.udc.cartolab.gvsig.navtable.AbstractNavTable;
import es.udc.cartolab.gvsig.navtable.listeners.PositionEvent;
import es.udc.cartolab.gvsig.navtable.listeners.PositionListener;

@SuppressWarnings("serial")
public class AddFincaAction extends AbstractAction implements PositionListener {

    private static final Logger logger = Logger.getLogger(AddFincaAction.class);

    private final AbstractNavTable nt;
    private final FLyrVect layer;

    private final StartEditing startEditingExt;

    public AddFincaAction(FLyrVect layer, AbstractNavTable nt) {
	super();
	this.nt = nt;
	this.layer = layer;
	putValue(SHORT_DESCRIPTION, "Añadir finca");
	ImageIcon icon = new ImageIcon(getClass().getResource("/add_finca.png"));
	putValue(SMALL_ICON, icon);
	// putValue(NAME, "buttonText");
	setEnabled(false);
	startEditingExt = (StartEditing) PluginServices
		.getExtension(StartEditing.class);

	nt.addPositionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	IWindow iWindow = PluginServices.getMDIManager().getActiveWindow();

	if (!(iWindow instanceof View)) {
	    return;
	}
	View view = (View) iWindow;
	nt.removePositionListener(this);

	FLayers layers = view.getMapControl().getMapContext().getLayers();
	layers.setAllActives(false);
	layer.setActive(true);

	nt.clearSelection();
	nt.selectCurrentFeature();

	PluginServices.getMDIManager().closeWindow(nt);

	if (!layer.isEditing()) {
	    startEditingExt.startEditing(view, layer);
	}

	CADExtension.setCADTool(AddFincaCADTool.KEY, true);
	PluginServices.getMainFrame().enableControls();

    }

    @Override
    public void onPositionChange(PositionEvent e) {
	int numReg = (int) nt.getPosition();
	try {
	    IGeometry geometry = layer.getSource().getFeature(numReg)
		    .getGeometry();
	    setEnabled(geometry == null);
	} catch (ExpansionFileReadException e1) {
	    logger.error(e1.getStackTrace(), e1);
	} catch (ReadDriverException e1) {
	    logger.error(e1.getStackTrace(), e1);
	}

    }

}
