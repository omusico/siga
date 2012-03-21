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
 * Event class to notify that user has shown interest in a layer
 * (typically user double clicked on it)
 * 
 * @author Juan Lucas Dominguez jldominguez prodevelop es
 * @version $Id$
 *
 */
public class LayerActionEvent extends TOCEvent {

    private FLayer layer = null;

    /**
     * 
     * @param t the TOC where action was performed
     * @param l layer of interes
     */
    public LayerActionEvent(TOC t, FLayer l) {
        super(t);
        layer = l;
    }

    /**
     * 
     * @return layer of interest
     */
    public FLayer getLayer() {
        return layer;
    }

}
