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

import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.project.documents.view.toc.impl.DefaultToc;

import org.gvsig.fmap.swing.toc.TOCFactory;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynObject;
import org.gvsig.tools.service.Service;
import org.gvsig.tools.service.ServiceException;
import org.gvsig.tools.service.spi.AbstractServiceFactory;
import org.gvsig.tools.service.spi.ServiceManager;

/**
 * TOC factory that creates the default (old) implementation
 * of the TOC.
 * 
 * @see DefaultToc
 * 
 * @author Juan Lucas Dominguez jldominguez prodevelop es
 * @version $Id$
 *
 */
public class DefaultTOCFactory extends AbstractServiceFactory implements
    TOCFactory {

    public static final String NAME = "Default JTree-based TOC";
    public static final String DESCRIPTION = "Default JTree-based TOC";

    public DefaultTOCFactory() {
    }

    @Override
    protected DynClass createParametersDynClass() {

        DynClass dc = ToolsLocator.getDynObjectManager().get(NAME);
        return dc;
    }

    @Override
    protected Service doCreate(DynObject params, ServiceManager mana)
        throws ServiceException {

        MapContext mc =
            (MapContext) params
                .getDynValue(TOCFactory.PARAMETER_MAPCONTEXT_KEY);

        DefaultToc resp = new DefaultToc(this);
        if (mc != null) {
            resp.setMapContext(mc);
        }
        return resp;
    }

    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public String getName() {
        return NAME;
    }

    public void initialize() {

    }

}
