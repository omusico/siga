package es.icarto.gvsig.extpm.filesLink;

import javax.swing.JButton;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.extgex.navtable.NavTableComponentsFactory;
import es.udc.cartolab.gvsig.navtable.AbstractNavTable;

public class NavTableComponentsFilesLinkButton extends NavTableComponentsFactory {

    @Override
    public JButton getFilesLinkButton(FLyrVect layer, AbstractNavTable dialog) {
	FilesLinkData filesLinkData = new FilesLinkData(layer);
	JButton filesLinkB = createButton(PluginServices.getText(
		this, "filesLinkTooltip"), getIcon("/fileslink.png"),
		new FilesLinkObserver(dialog, filesLinkData));
	return filesLinkB;
    }
}
