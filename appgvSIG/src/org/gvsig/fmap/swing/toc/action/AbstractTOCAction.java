/* gvSIG. Geographic Information System of the Valencian Government
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Government.
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
 * Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 *
 */
package org.gvsig.fmap.swing.toc.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.iver.cit.gvsig.fmap.layers.FLayer;

import org.gvsig.fmap.swing.toc.TOC;
import org.gvsig.fmap.swing.toc.TOCLocator;
import org.gvsig.tools.service.Manager;

/***
 * Superclass of all TOCActions. Implements some methods common to all subclasses.
 * 
 * @see TOC
 * @see TOCAction
 * 
 * @author Juan Lucas Dominguez jldominguez prodevelop es
 * @version $Id$
 *
 */
public abstract class AbstractTOCAction extends AbstractAction implements
    TOCAction {

    private TOC toc = null;

    /**
     * Constructor needs associated TOC and parameters to group and sort
     * the associated menu item
     * 
     * @param t associated TOC
     * @param order index inside its group
     * @param group group name
     * @param grOrder index of the group
     */
    public AbstractTOCAction(TOC t, int order, String group, int grOrder) {
        toc = t;
        putValue(ORDER, order);
        putValue(GROUP, group);
        putValue(GROUP_ORDER, grOrder);
    }

    /**
     * @return the current active layer
     * @see TOC
     */
    public FLayer getActiveLayer() {
        return toc.getActiveLayer();
    }

    /**
     * 
     * @return the currently selected layers
     * @see TOC
     * 
     */
    public FLayer[] getSelectedLayers() {
        return toc.getSelectedLayers();
    }

    public TOC getTOC() {
        return toc;
    }

    public abstract boolean isVisible();

    public abstract void actionPerformed(ActionEvent e);

    public Manager getManager() {
        return TOCLocator.getInstance().getTOCManager();
    }

}
