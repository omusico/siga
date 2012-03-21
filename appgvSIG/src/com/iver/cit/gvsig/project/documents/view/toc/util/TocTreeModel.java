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
package com.iver.cit.gvsig.project.documents.view.toc.util;

import javax.swing.tree.TreeModel;

import com.iver.cit.gvsig.fmap.AtomicEventListener;
import com.iver.cit.gvsig.fmap.MapContext;

/**
 * Extends Swing tree model. Using directly Swing tree model will 
 * result in a more responsive TOC.
 * 
 * @author Juan Lucas Dominguez jldominguez prodevelop es
 * @version $Id$
 *
 */
public interface TocTreeModel extends TreeModel, AtomicEventListener {

    /**
     * Sets the map context which will be the base of the TOC tree model
     * 
     * @param mc
     */
    public void setMapContext(MapContext mc);

    /**
     * Gets the map context
     * 
     * @return
     */
    public MapContext getMapContext();

}
