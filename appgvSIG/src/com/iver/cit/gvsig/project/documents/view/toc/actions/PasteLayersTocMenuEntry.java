package com.iver.cit.gvsig.project.documents.view.toc.actions;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;
import com.iver.utiles.XMLEntity;

public class PasteLayersTocMenuEntry extends AbstractTocContextMenuAction {
	private XMLEntity xml=null;
	private CopyPasteLayersUtiles utiles = CopyPasteLayersUtiles.getInstance();


	public String getGroup() {
		return "copyPasteLayer";
	}

	public int getGroupOrder() {
		return 60;
	}

	public int getOrder() {
		return 2;
	}

	public String getText() {
		return PluginServices.getText(this, "pegar");
	}

	public boolean isEnabled(ITocItem item, FLayer[] selectedItems) {
		if (isTocItemBranch(item)) {
			FLayer lyr = getNodeLayer(item);
			//			if (lyr instanceof FLayers) {
			this.xml = this.getCheckedXMLFromClipboard();
			//				return true;
			//			}
			return this.xml != null; // Podemos hacer paste en cualquier sitio y nos preguntará si queremos meter la capa en ese sitio

		} else if (!isTocItemLeaf(item)) {
			if (getNodeLayer(item) == null) {
				this.xml = this.getCheckedXMLFromClipboard();
				return this.xml != null;
			}
		}
		return false;
	}

	private XMLEntity getCheckedXMLFromClipboard() {
		String sourceString = PluginServices.getFromClipboard();
		if (sourceString == null) return null;

		//		System.out.println(sourceString);
		XMLEntity xml;
		try {
			xml = XMLEntity.parse(sourceString);
		} catch (MarshalException e) {
			return null;
		} catch (ValidationException e) {
			return null;
		}


		if (!this.utiles.checkXMLRootNode(xml)) return null;

		if (xml.findChildren("type","layers") == null) return null;

		return  xml;
	}

	public void execute(ITocItem item, FLayer[] selectedItems) {
		FLayers root;

		if (this.xml == null) return;

		FLayer lyrOn = getNodeLayer(item);
		if (isTocItemBranch(item)) {
			MapContext mapContext = getMapContext();
			mapContext.beginAtomicEvent();
			FLayers all = lyrOn.getParentLayer();

			CopyPasteLayersUtiles.getInstance().loadLayersFromXML(xml, all);
			int position = getPosition(all, lyrOn);
			all.moveTo(0, position);

			mapContext.endAtomicEvent();
			mapContext.invalidate();
		} else if (lyrOn == null) {
			root = getMapContext().getLayers();
			getMapContext().beginAtomicEvent();

			boolean isOK = this.utiles.loadLayersFromXML(this.xml,root);

			getMapContext().endAtomicEvent();

			if (isOK) {
				getMapContext().invalidate();
				Project project=((ProjectExtension)PluginServices.getExtension(ProjectExtension.class)).getProject();
				project.setModified(true);
			}
		}
	}

	private int getPosition(FLayers layerGroup, FLayer lyrSelected){
		int pos = -1;
		for (int j=0; j < layerGroup.getLayersCount(); j++) {
			if (layerGroup.getLayer(j).getName().equalsIgnoreCase(lyrSelected.getName())) {
				pos = j;
				break;
			}
		}
		int offset = 2;
		return layerGroup.getLayersCount() - offset - pos;
	}

}
