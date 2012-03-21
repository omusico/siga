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
package org.gvsig.fmap.swing.impl.action;

import com.iver.andami.PluginServices;

import org.gvsig.fmap.swing.toc.TOC;
import org.gvsig.fmap.swing.toc.action.TOCActionFactory;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.dynobject.DynObject;
import org.gvsig.tools.service.Service;
import org.gvsig.tools.service.ServiceException;
import org.gvsig.tools.service.spi.ServiceManager;

/**
 * TOC action factory that creates  instance of
 * @see RenameLayerAction
 * 
 * @author Juan Lucas Dominguez jldominguez prodevelop es
 * @version $Id$
 *
 */
public class RenameLayerActionFactory implements TOCActionFactory {

    public static final String DYN_CLASS_NAME =
        RenameLayerActionFactory.class.getName() + "_DynClass";

    public Service create(DynObject pars, ServiceManager sm)
        throws ServiceException {

        TOC toc = null;
        try {
            toc = (TOC) pars.getDynValue(TOCActionFactory.TOC_PARAMETER_KEY);
        } catch (Exception ex) {
            throw new ServiceException(ex.getMessage(), "", -1);
        }
        return new RenameLayerAction(toc);
    }

    public DynObject createParameters() {
        return ToolsLocator.getDynObjectManager().createDynObject(
            DYN_CLASS_NAME);
    }

    public String getName() {
        return PluginServices.getText(this, "cambio_nombre");
    }

    public void initialize() {

    }

}
