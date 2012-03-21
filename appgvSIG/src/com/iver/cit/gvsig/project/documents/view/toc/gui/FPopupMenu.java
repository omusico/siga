/*
 * Created on 02-mar-2004
 *
 * gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
package com.iver.cit.gvsig.project.documents.view.toc.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;

import org.gvsig.fmap.swing.toc.TOC;
import org.gvsig.fmap.swing.toc.TOCLocator;
import org.gvsig.fmap.swing.toc.TOCManager;
import org.gvsig.fmap.swing.toc.action.TOCAction;
import org.gvsig.fmap.swing.toc.action.TOCActionAdapter;
import org.gvsig.tools.service.ServiceException;

import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;
import com.iver.cit.gvsig.project.documents.view.toc.TocItemBranch;
import com.iver.cit.gvsig.project.documents.view.toc.TocMenuEntry;
import com.iver.cit.gvsig.project.documents.view.toc.actions.OldTocContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.util.TOCMenuItemComparator;
import com.iver.utiles.extensionPoints.ExtensionPoint;
import com.iver.utiles.extensionPoints.ExtensionPointsSingleton;

/**
 * Menu de botón derecho para el TOC.
 * Se pueden añadir entradas facilmente desde una extensión,
 * creando una clase derivando de TocMenuEntry, y añadiendola en
 * estático (o en tiempo de carga de la extensión) a FPopupMenu.
 * (Las entradas actuales están hechas de esa manera).
 *
 * @author vcn To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and
 *         Comments
 */

public class FPopupMenu extends JPopupMenu {
	//private static ArrayList menuEntrys = new ArrayList();
    private DefaultMutableTreeNode nodo;
    protected MapContext mapContext;
    private TOC toc = null;
    private ExtensionPoint extensionPoint;
    private FLayer[] selecteds;
    //private JMenuItem capa;


    /**
     * @deprecated
     */
    public static void addEntry(TocMenuEntry entry) {

    	OldTocContextMenuAction action = new OldTocContextMenuAction();
    	action.setEntry(entry);
    	System.out.println("Add old TocMenuEntry: " + entry);
    	TOCManager tm = TOCLocator.getInstance().getTOCManager();
		tm.addServiceFactory(action);
    }

    /**
     * @deprecated
     */

    /*
    public static Object getEntry(String className, boolean[][] bb) {
    	ExtensionPoints extensionPoints = ExtensionPointsSingleton.getInstance();
    	OldTocContextMenuAction action = null;
    	try {
			action = (OldTocContextMenuAction)((ExtensionPoint)extensionPoints.get("View_TocActions")).create(className);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassCastException e) {
			action = null;
		}
    	if (action != null) {
    		return action.getEntry();
    	} else {
    		return null;
    	}
    }
    */

    /**
     * Creates a new FPopupMenu object.
     *
     * @param nodo DOCUMENT ME!
     * @param vista DOCUMENT ME!
     */
    public FPopupMenu(TOC _toc, MapContext mc, DefaultMutableTreeNode node) {
        super();
        toc = _toc;
        this.initialize(mc,node);
    }

    private void initialize(MapContext mc, DefaultMutableTreeNode node) {
        this.mapContext = mc;
        this.nodo = node;

        //salir = new MenuItem("Salir");
		extensionPoint = (ExtensionPoint)ExtensionPointsSingleton.getInstance().get("View_TocActions");
		this.selecteds = this.mapContext.getLayers().getActives();

		List<TOCAction> actions = this.getActionList();
		if (actions == null){
			return;
		}
		this.createMenuElements(actions);

		this.loadOldStileOptions();
    }

    public MapContext getMapContext() { return mapContext; }

	public ITocItem getNodeUserObject() {
		if (nodo == null) {
		    return null;
		}
		return (ITocItem)nodo.getUserObject();
	}

	public DefaultMutableTreeNode getNode() {
		return this.nodo;
	}

    private List<TOCAction> getActionList() {

    	TOCManager tm = TOCLocator.getInstance().getTOCManager();
		List<TOCAction> result_list = null;
		List<TOCAction> result_list_sort = null;

		try {
			result_list = tm.getActions(toc);
		} catch (ServiceException se) {
			NotificationManager.addError("While getting actions for fpopupmenu", se);
			return null;
		}



    	AbstractTocContextMenuAction action;
    	boolean contains=false;
    	ITocItem tocItem=(ITocItem)this.getNodeUserObject();
    	if (tocItem instanceof TocItemBranch){
    		for (int i=0;i<this.selecteds.length;i++){
    			if (this.selecteds[i].equals(((TocItemBranch)tocItem).getLayer())) {
			    contains=true;
			}
    		}
    	}else{
    		contains=true;
    	}

    	if (contains){

    		result_list_sort = new ArrayList<TOCAction>();
    		TOCAction[] ta_array = (TOCAction[]) result_list.toArray(new TOCAction[0]);
    		Arrays.sort(ta_array, new TOCMenuItemComparator());
    		int len = ta_array.length;
    		for (int i=0; i<len; i++) {
    			result_list_sort.add(ta_array[i]);
    		}
    		return result_list_sort;


    		//    		Iterator iter = this.extensionPoint.keySet().iterator();
//    		while (iter.hasNext()) {
//    			action = null;
//    			try {
//    				action = (AbstractTocContextMenuAction)this.extensionPoint.create((String)iter.next());
//    			} catch (InstantiationException e) {
//    				// TODO Auto-generated catch block
//    				e.printStackTrace();
//    			} catch (IllegalAccessException e) {
//    				// TODO Auto-generated catch block
//    				e.printStackTrace();
//    			}
//    			if (action != null && !(action instanceof OldTocContextMenuAction)) {
//    				action.setMapContext(this.mapContext);
//    				if (action.isVisible((ITocItem)this.getNodeUserObject(),this.selecteds)) {
//    					actionArrayList.add(action);
//    				}
//    			}
//
//    		}
//    		IContextMenuAction[] result = (IContextMenuAction[])Array.newInstance(IContextMenuAction.class,actionArrayList.size());
//    		System.arraycopy(actionArrayList.toArray(),0,result,0,actionArrayList.size());
//    		Arrays.sort(result,new CompareAction());
//    		return result;
    	}
    	return null;

    }

    /*
	public class CompareAction implements Comparator{
		public int compare(Object o1, Object o2) {
			return this.compare((IContextMenuAction)o1,(IContextMenuAction)o2);
		}

		public int compare(IContextMenuAction o1, IContextMenuAction o2) {
			//FIXME: flata formatear los enteros!!!!
			NumberFormat formater = NumberFormat.getInstance();
			formater.setMinimumIntegerDigits(3);
			String key1= ""+formater.format(o1.getGroupOrder())+o1.getGroup()+formater.format(o1.getOrder());
			String key2= ""+formater.format(o2.getGroupOrder())+o2.getGroup()+formater.format(o2.getOrder());
			return key1.compareTo(key2);
		}
	}
	*/

	private void createMenuElements(List<TOCAction> actions) {

		String group = null;
		String item_group = null;
		TOCAction action = null;

		for (int i=0;i < actions.size();i++) {

			action = actions.get(i);

			if (!action.isVisible() ||
					(action instanceof TOCActionAdapter &&
							((TOCActionAdapter) action).getContextMenuAction() instanceof OldTocContextMenuAction)) {
				if (((action instanceof TOCActionAdapter) && ((TOCActionAdapter) action).getContextMenuAction() instanceof OldTocContextMenuAction)) {
					OldTocContextMenuAction oldAct = (OldTocContextMenuAction) ((TOCActionAdapter) action).getContextMenuAction();
					oldAct.getEntry().initialize(this);
				}
				continue;
			}

			JMenuItem item = new JMenuItem(action);
	    // item.setFont(theFont);
			item.setEnabled(action.isEnabled());
			item_group = action.getValue(TOCAction.GROUP).toString();
			if (group == null || item_group.compareToIgnoreCase(group) != 0) {
				if (group != null) {
				    this.addSeparator();
				}
				group = item_group;
			}
			this.add(item);
		}

		// comprobamos si el ultimo elemento es un seprardor
		// not here
		/*
		if (this.getComponentCount()>0 && this.getComponent(this.getComponentCount()-1) instanceof Separator) {
			//Si lo es lo eliminamos
			this.remove(this.getComponentCount()-1);
		}
		*/


	}


	/*
	public class MenuItem extends JMenuItem implements ActionListener{
		private IContextMenuAction action;
		public MenuItem(String text,IContextMenuAction documentAction) {
			super(text);
			this.action = documentAction;
			String tip = this.action.getDescription();
			if (tip != null && tip.length() > 0) {
				this.setToolTipText(tip);
			}
			this.addActionListener(this);
		}

		public void actionPerformed(ActionEvent e) {
			this.action.execute(FPopupMenu.this.getNodeUserObject(), FPopupMenu.this.selecteds);
		}
	}
	*/

	private void loadOldStileOptions() {
		boolean first = true;
		Iterator iter = this.extensionPoint.keySet().iterator();
		AbstractTocContextMenuAction action;
		while (iter.hasNext()) {
			action = null;
			try {
				action = (AbstractTocContextMenuAction)this.extensionPoint.create((String)iter.next());
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (action != null && (action instanceof OldTocContextMenuAction)) {
				if (first) {
					this.addSeparator();
					first = false;
				}
				action.setMapContext(this.mapContext);
				((OldTocContextMenuAction)action).initializeElement(this);
			}
		}
		//comprobamos si el ultimo elemento es un seprardor
		if (this.getComponentCount()>0 && this.getComponent(this.getComponentCount()-1) instanceof Separator) {
			//Si lo es lo eliminamos
			this.remove(this.getComponentCount()-1);
		}


	}


}
