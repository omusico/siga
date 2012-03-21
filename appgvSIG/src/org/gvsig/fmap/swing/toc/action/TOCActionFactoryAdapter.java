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

import com.iver.cit.gvsig.project.documents.IContextMenuAction;

import org.gvsig.fmap.swing.toc.TOC;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.dynobject.DynObject;
import org.gvsig.tools.service.Service;
import org.gvsig.tools.service.ServiceException;
import org.gvsig.tools.service.spi.ServiceManager;

/**
 * Utility class to wrap the old style menu actions in a {@link TOCActionFactory}
 * 
 * @author Juan Lucas Dominguez jldominguez prodevelop es
 * @version $Id$
 *
 */
public class TOCActionFactoryAdapter implements TOCActionFactory {

    private IContextMenuAction icma = null;
    private String name = null;

    public static final String DYN_CLASS_NAME =
        TOCActionFactoryAdapter.class.getName() + "_DynClass";

    /**
     * Constructor needs the old style menu action
     * 
     * @param _icma
     */
    public TOCActionFactoryAdapter(IContextMenuAction _icma) {
        icma = _icma;
    }

    public Service create(DynObject pars, ServiceManager paramServiceManager)
        throws ServiceException {

        TOC toc = null;
        try {
            toc = (TOC) pars.getDynValue(TOCActionFactory.TOC_PARAMETER_KEY);
        } catch (Exception ex) {
            throw new ServiceException(ex.getMessage(), "", -1);
        }
        return new TOCActionAdapter(icma, toc);
    }

    public DynObject createParameters() {
        return ToolsLocator.getDynObjectManager().createDynObject(
            DYN_CLASS_NAME);
    }

    public String getName() {
    	if (name == null) {
    		name = icma.getClass().getName() + "_TOCActionFactoryAdapter";
    	}
        return name;
    }
    
    /**
     * Useful to register OldTocMenuEntry wrappers.
     * @param name
     */
    public void setName(String name) {
    	this.name = name + "_TOCActionFactoryAdapter";
    }
 
    public void initialize() {
    }

}
