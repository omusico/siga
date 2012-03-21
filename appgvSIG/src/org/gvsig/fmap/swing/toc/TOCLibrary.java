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

import org.gvsig.fmap.swing.impl.action.RenameLayerActionFactory;
import org.gvsig.fmap.swing.impl.toc.DefaultTOCFactory;
import org.gvsig.fmap.swing.toc.action.TOCActionFactory;
import org.gvsig.fmap.swing.toc.action.TOCActionFactoryAdapter;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.library.AbstractLibrary;
import org.gvsig.tools.library.LibraryException;

/**
 * This lisbrray declares basic DynClasses to be used by 
 * TOC implementations.
 * 
 * @author Juan Lucas Dominguez jldominguez prodevelop es
 * @version $Id$
 *
 */
public class TOCLibrary extends AbstractLibrary {

    public TOCLibrary() {
        this.registerAsAPI(TOCLibrary.class);
    }

    @Override
    protected void doInitialize() throws LibraryException {

    }

    @Override
    protected void doPostInitialize() throws LibraryException {

        DynClass dc =
            ToolsLocator.getDynObjectManager().createDynClass(
                TOCActionFactoryAdapter.DYN_CLASS_NAME,
                TOCActionFactoryAdapter.class.getName() + " DynClass");
        dc.addDynFieldObject(TOCActionFactory.TOC_PARAMETER_KEY);
        ToolsLocator.getDynObjectManager().add(dc);

        dc =
            ToolsLocator.getDynObjectManager().createDynClass(
                DefaultTOCFactory.NAME,
                DefaultTOCFactory.class.getName() + " DynClass");

        dc.addDynFieldObject(TOCFactory.PARAMETER_MAPCONTEXT_KEY);
        ToolsLocator.getDynObjectManager().add(dc);

        // sample menu action factory
        dc =
            ToolsLocator.getDynObjectManager().createDynClass(
                RenameLayerActionFactory.DYN_CLASS_NAME,
                RenameLayerActionFactory.class.getName() + " DynClass");

        dc.addDynFieldObject(TOCActionFactory.TOC_PARAMETER_KEY);
        ToolsLocator.getDynObjectManager().add(dc);

    }

}
