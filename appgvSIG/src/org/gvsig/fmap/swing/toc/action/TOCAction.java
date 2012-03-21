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

import javax.swing.Action;

import org.gvsig.fmap.swing.toc.TOC;
import org.gvsig.tools.service.Service;

/**
 * Interface to be implemented by all TOCAction.
 * They should normally extend the {@link AbstractTOCAction} class
 * 
 * Extends the {@link Action} interface to better integrate in the Swing GUI framework
 * 
 * @author Juan Lucas Dominguez jldominguez prodevelop es
 * @version $Id$
 *
 */
public interface TOCAction extends Action, Service {

    /**
     * String constant used in the mapof properties of {@link Action}
     */
    public static final String ORDER = "ORDER_KEY";
    /**
     * String constant used in the mapof properties of {@link Action}
     */
    public static final String GROUP = "GROUP_KEY";
    /**
     * String constant used in the mapof properties of {@link Action}
     */
    public static final String GROUP_ORDER = "GROUP_ORDER_KEY";

    /**
     * 
     * @return the associated TOC
     */
    public TOC getTOC();

    /**
     * 
     * @return whether or not this action must be currently visible,
     * depending on the current TOC state
     */
    public boolean isVisible();
}
