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
package org.gvsig.fmap.swing.toc.event;

import org.gvsig.fmap.swing.toc.TOC;

import com.iver.cit.gvsig.fmap.layers.FLayer;

/**
 * Event class to notify a change in the active layer.
 * Typically the active layer is the last one that has received
 * a mouse click 
 * 
 * 
 * @author Juan Lucas Dominguez jldominguez prodevelop es
 * @version $Id$
 *
 */
public class ActiveLayerChangeEvent extends TOCEvent {

    private FLayer prevActiveLayer = null;
    private FLayer newActiveLayer = null;

    /**
     * 
     * @param t the TOC where action was performed
     * @param prev the layer which is not active anymore
     * @param nw the new active layer
     */
    public ActiveLayerChangeEvent(TOC t, FLayer prev, FLayer nw) {
        super(t);
        prevActiveLayer = prev;
        newActiveLayer = nw;
    }

    /**
     * 
     * @return the layer which is not active anymore
     */
    public FLayer getPreviousActiveLAyer() {
        return prevActiveLayer;
    }

    /**
     * 
     * @return the new active layer
     */
    public FLayer getNewActiveLAyer() {
        return newActiveLayer;
    }

}
