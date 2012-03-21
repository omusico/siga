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

import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.project.documents.view.toc.actions.ChangeSymbolTocMenuEntry;
import com.iver.cit.gvsig.project.documents.view.toc.actions.CopyLayersTocMenuEntry;
import com.iver.cit.gvsig.project.documents.view.toc.actions.CutLayersTocMenuEntry;
import com.iver.cit.gvsig.project.documents.view.toc.actions.EliminarCapaTocMenuEntry;
import com.iver.cit.gvsig.project.documents.view.toc.actions.FLyrVectEditPropertiesTocMenuEntry;
import com.iver.cit.gvsig.project.documents.view.toc.actions.FirstLayerTocMenuEntry;
import com.iver.cit.gvsig.project.documents.view.toc.actions.LayersGroupTocMenuEntry;
import com.iver.cit.gvsig.project.documents.view.toc.actions.LayersUngroupTocMenuEntry;
import com.iver.cit.gvsig.project.documents.view.toc.actions.PasteLayersTocMenuEntry;
import com.iver.cit.gvsig.project.documents.view.toc.actions.ReloadLayerTocMenuEntry;
import com.iver.cit.gvsig.project.documents.view.toc.actions.ShowLayerErrorsTocMenuEntry;
import com.iver.cit.gvsig.project.documents.view.toc.actions.ZoomAlTemaTocMenuEntry;

import org.gvsig.fmap.swing.impl.action.RenameLayerActionFactory;
import org.gvsig.fmap.swing.toc.TOCFactory;
import org.gvsig.fmap.swing.toc.TOCLibrary;
import org.gvsig.fmap.swing.toc.TOCLocator;
import org.gvsig.fmap.swing.toc.TOCManager;
import org.gvsig.tools.library.AbstractLibrary;
import org.gvsig.tools.library.LibraryException;
import org.gvsig.tools.service.ServiceException;

/**
 * This library registers the default TOC manager and the
 * default (old) TOC implementation. It also declares
 * the legacy toc menu actions.
 * 
 * @see DefaultTOCManager
 * @see DefaultTOCFactory
 * @see IContextMenuAction
 * 
 * @author Juan Lucas Dominguez jldominguez prodevelop es
 * @version $Id$
 *
 */
public class TOCDefaultImplLibrary extends AbstractLibrary {

    public TOCDefaultImplLibrary() {
        this.registerAsImplementationOf(TOCLibrary.class);
    }

    @Override
    protected void doInitialize() throws LibraryException {

        TOCLocator.registerDefaultTOCManager(DefaultTOCManager.class);

    }

    @Override
    protected void doPostInitialize() throws LibraryException {


        TOCManager tm = TOCLocator.getInstance().getTOCManager();
        TOCFactory tf = new DefaultTOCFactory();
        tm.addServiceFactory(tf);
        try {
            tm.setDefaultTOCFactory(tf.getName());
        } catch (ServiceException e) {
            NotificationManager.addError("While setting default TOC. ", e);
        }

        // ======================================================

        tm.addServiceFactory(new ChangeSymbolTocMenuEntry());

        // =========== sample action without adapter
        tm.addServiceFactory(new RenameLayerActionFactory());
        // =========================================

        tm.addServiceFactory(new FLyrVectEditPropertiesTocMenuEntry());
        tm.addServiceFactory(new ZoomAlTemaTocMenuEntry());
        tm.addServiceFactory(new EliminarCapaTocMenuEntry());
        tm.addServiceFactory(new ShowLayerErrorsTocMenuEntry());
        tm.addServiceFactory(new ReloadLayerTocMenuEntry());
        tm.addServiceFactory(new LayersGroupTocMenuEntry());
        tm.addServiceFactory(new LayersUngroupTocMenuEntry());
        tm.addServiceFactory(new FirstLayerTocMenuEntry());
        tm.addServiceFactory(new CopyLayersTocMenuEntry());
        tm.addServiceFactory(new CutLayersTocMenuEntry());
        tm.addServiceFactory(new PasteLayersTocMenuEntry());

    }

}
