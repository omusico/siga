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
package org.gvsig.fmap.swing.toc;

import org.gvsig.tools.service.spi.ServiceFactory;

/**
 * Interface to be implemented by TOC factories providing new implementaion
 * of TOC. Follows the service mechanism
 * 
 * @see ServiceFactory
 * 
 * @author Juan Lucas Dominguez jldominguez prodevelop es
 * @version $Id$
 *
 */
public interface TOCFactory extends ServiceFactory {

    /**
     * string constant used in a DynObject to identify the \see MapContext parameter
     */
    public static final String PARAMETER_MAPCONTEXT_KEY =
        "PARAMETER_MAPCONTEXT_KEY";

    /**
     * 
     * @return a short description of the characteristics of the TOC provided by this
     * factory. Will be ised in the gvsig settings dialog to let the user choose
     * a TOC implementation
     */
    public String getDescription();

}
