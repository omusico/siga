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
package org.gvsig.fmap.swing.impl.toc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.project.documents.IContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.actions.OldTocContextMenuAction;
import com.iver.utiles.extensionPoints.ExtensionPointsSingleton;

import org.gvsig.fmap.swing.toc.TOC;
import org.gvsig.fmap.swing.toc.TOCFactory;
import org.gvsig.fmap.swing.toc.TOCManager;
import org.gvsig.fmap.swing.toc.action.TOCAction;
import org.gvsig.fmap.swing.toc.action.TOCActionAdapter;
import org.gvsig.fmap.swing.toc.action.TOCActionFactory;
import org.gvsig.fmap.swing.toc.action.TOCActionFactoryAdapter;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.dynobject.DynObject;
import org.gvsig.tools.extensionpoint.ExtensionPoint;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;
import org.gvsig.tools.extensionpoint.ExtensionPoint.Extension;
import org.gvsig.tools.service.Service;
import org.gvsig.tools.service.ServiceException;
import org.gvsig.tools.service.spi.AbstractServiceManager;
import org.gvsig.tools.service.spi.ServiceFactory;

/**
 * Default implementation of the TOC manager.
 *  
 * 
 * @author Juan Lucas Dominguez jldominguez prodevelop es
 * @version $Id$
 *
 */
public class DefaultTOCManager extends AbstractServiceManager implements
    TOCManager {

    @Override
    protected String getRegistryKey() {
        return "DefaultTOCManager";
    }

    @Override
    protected String getRegistryDescription() {
        return "Default TOC Manager";
    }

    public void addServiceFactory(TOCFactory tf) {
        addServiceFactory((ServiceFactory) tf);
    }

    public void addServiceFactory(TOCActionFactory taf) {
        addServiceFactory((ServiceFactory) taf);
    }

    public void addServiceFactory(IContextMenuAction icma) {
        TOCActionFactoryAdapter ada = new TOCActionFactoryAdapter(icma);
        if (icma instanceof OldTocContextMenuAction) {
        	ada.setName(((OldTocContextMenuAction) icma).getEntry().getClass().toString()); 
        }
        addServiceFactory(ada);
    }


    public TOC createTOC(MapContext mc) throws ServiceException {
        return createTOC(TOCManager.DEFAULT_TOC_STRING, mc);
    }


    public TOC createTOC(String name, MapContext mc) throws ServiceException {

        DynObject par = createServiceParameters(name);
        par.setDynValue(TOCFactory.PARAMETER_MAPCONTEXT_KEY, mc);
        Service svc = createService(par);

        if (svc instanceof TOC) {
            return (TOC) svc;
        } else {
            throw new ServiceException("Service is not a TOC (?): "
                + (svc == null ? "" : svc.getClass().getName()), "", -1);
        }
    }


    public List<TOCAction> getActions(TOC t) throws ServiceException {

        ArrayList<TOCAction> resp = new ArrayList<TOCAction>();

        ExtensionPointManager epm = ToolsLocator.getExtensionPointManager();
        ExtensionPoint ep = epm.get(getRegistryKey());
        Iterator iter = ep.iterator();
        Object item = null;
        Extension ext = null;
        TOCActionFactory taf = null;
        DynObject dob = null;
        Service svc = null;

        try {
            while (iter.hasNext()) {
                item = iter.next();
                if (item instanceof Extension) {
                    ext = (Extension) item;
                    item = ext.create();
                    if (item instanceof TOCActionFactory) {
                        taf = (TOCActionFactory) item;
                        dob = taf.createParameters();
                        dob.setDynValue(TOCActionFactory.TOC_PARAMETER_KEY, t);
                        svc = taf.create(dob, this);
                        if (svc instanceof TOCAction) {
                            resp.add((TOCAction) svc);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            throw new ServiceException(ex.getMessage(), "", -1);
        }

        // ===================================================
        // old style registrations
        // ===================================================
        com.iver.utiles.extensionPoints.ExtensionPoint ep_old =
            (com.iver.utiles.extensionPoints.ExtensionPoint) ExtensionPointsSingleton
                .getInstance().get("View_TocActions");

        // ep = epm.get("View_TocActions");
        iter = ep_old.keySet().iterator();
        TOCActionAdapter adap = null;
        try {
            while (iter.hasNext()) {
                item = iter.next();
                item = ep_old.get(item);
                if (item instanceof IContextMenuAction) {
                    adap = new TOCActionAdapter((IContextMenuAction) item, t);
                    resp.add(adap);
                }
            }
        } catch (Exception ex) {
            throw new ServiceException(ex.getMessage(), "", -1);
        }

        return resp;


    }


    public List<TOCFactory> getTOCFactories() throws ServiceException {

        ExtensionPointManager epm = ToolsLocator.getExtensionPointManager();
        ExtensionPoint ep = epm.get(getRegistryKey());
        Iterator iter = ep.iterator();
        Object item = null;
        ArrayList<TOCFactory> resp = new ArrayList<TOCFactory>();
        Extension ext = null;

        while (iter.hasNext()) {
            item = iter.next();
            if (item instanceof Extension) {
                ext = (Extension) item;
                try {
                    item = ext.create();
                    if (item instanceof TOCFactory) {
                        resp.add((TOCFactory) item);
                    }
                } catch (Exception ex) {
                    throw new ServiceException(ex.getMessage(), "", -1);
                }
            }
        }

        return resp;
    }

    public void setDefaultTOCFactory(TOCFactory tf) throws ServiceException {
        addServiceFactory(tf);
        setDefaultTOCFactory(tf.getName());
    }

    public void setDefaultTOCFactory(String name) throws ServiceException {

        // TOCManager
        ExtensionPointManager epm = ToolsLocator.getExtensionPointManager();
        ExtensionPoint ep = epm.get(getRegistryKey());
        ep.addAlias(name, TOCManager.DEFAULT_TOC_STRING);
    }

    public Service getService(DynObject paramDynObject) throws ServiceException {
        return null;
    }

    public TOCFactory getDefaultTOCFactory() throws ServiceException {
        ExtensionPointManager epm = ToolsLocator.getExtensionPointManager();
        ExtensionPoint ep = epm.get(getRegistryKey());
        TOCFactory tff = null;

        try {
            Extension ext = ep.get(TOCManager.DEFAULT_TOC_STRING);
            tff = (TOCFactory) ext.create();
            return tff;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), "", -1);
        }
    }

    // ===========

}
