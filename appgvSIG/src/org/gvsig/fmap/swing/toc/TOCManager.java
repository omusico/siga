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

import java.util.List;

import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.project.documents.IContextMenuAction;

import org.gvsig.fmap.swing.toc.action.TOCAction;
import org.gvsig.fmap.swing.toc.action.TOCActionFactory;
import org.gvsig.tools.service.Manager;
import org.gvsig.tools.service.ServiceException;
import org.gvsig.tools.service.spi.ServiceManager;

/**
 * Interface to be implementad by any TOC manager.
 * Handles TOC and TOC actions
 * 
 * @author Juan Lucas Dominguez jldominguez prodevelop es
 * @version $Id$
 *
 */
public interface TOCManager extends Manager, ServiceManager {

    public static final String DEFAULT_TOC_STRING = "DEFAULT_TOC_STRING";

    /**
     * Creates an instance of the TOC according to the current registered value.
     * 
     * @param mc The mapcontext to be used as the TOC model
     */
    public TOC createTOC(MapContext mc) throws ServiceException;

    /**
     * Creates an instance of the TOC using the input name.
     * 
     * @param name name of the requested TOC implementation
     * @param mc The mapcontext to be used as the TOC model
     */
    public TOC createTOC(String name, MapContext mc) throws ServiceException;

    /**
     * Returns a list of available TOC factories
     * @see TOCFactory 
     */
    public List<TOCFactory> getTOCFactories() throws ServiceException;

    /**
     * Returns list of TOCAction associated to input TOC
     * 
     * @param t 
     */
    public List<TOCAction> getActions(TOC t) throws ServiceException;

    /**
     * Sets the default TOC factory. After this, calls to createTOC(MapContext mc)
     * will use that factory. If the TOCFactory was not yet registered,
     * it will be registered too
     *  
     * @param tf
     * @throws ServiceException
     */
    public void setDefaultTOCFactory(TOCFactory tf) throws ServiceException;

    /**
     * Sets the default TOC factory. After this, calls to createTOC(MapContext mc)
     * will use that factory. There must be a registered TOCFactory
     * with the input name
     *  
     * @param name hame of the TOC factory to be used (must be already registered)
     * @throws ServiceException
     */
    public void setDefaultTOCFactory(String name) throws ServiceException;

    /**
     * Gets the current default TOC factory
     * 
     * @return
     * @throws ServiceException
     */
    public TOCFactory getDefaultTOCFactory() throws ServiceException;

    /**
     * Adds (registers, makes available) a new TOC implementation
     * @param tf
     */
    public void addServiceFactory(TOCFactory tf);

    /**
     * Adds (registers, makes available) a new TOC action
     * @param taf
     */
    public void addServiceFactory(TOCActionFactory taf);

    /**
     * USed to add old menu actions via
     * @see IContextMenuAction
     * 
     * @deprecated
     * @param icma
     */
    @Deprecated
    public void addServiceFactory(IContextMenuAction icma);

}
