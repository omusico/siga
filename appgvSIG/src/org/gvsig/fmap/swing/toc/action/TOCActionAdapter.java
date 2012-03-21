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

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;

import com.iver.cit.gvsig.project.documents.IContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.TocItemBranch;

import org.gvsig.fmap.swing.toc.TOC;
import org.gvsig.fmap.swing.toc.TOCLocator;
import org.gvsig.tools.service.Manager;

/**
 * Utility class to wrap the old menu actions in the new model
 * ( {@link TOCAction}n, etc )
 * 
 * @author Juan Lucas Dominguez jldominguez prodevelop es
 * @version $Id$
 *
 */
public class TOCActionAdapter extends AbstractTOCAction implements TOCAction {

    /**
     * 
     */
    private static final long serialVersionUID = -9126045915768382814L;

    private IContextMenuAction icma = null;

    private List pcl = new ArrayList();

    /**
     * Constructor needs old style menu action and associated TOC
     * 
     * @param _icma old style menu action
     * @param t associated TOC
     */
    public TOCActionAdapter(IContextMenuAction _icma, TOC t) {

        super(t, _icma.getOrder(), _icma.getGroup(), _icma.getGroupOrder());

        icma = _icma;
        if (icma instanceof AbstractTocContextMenuAction) {
            ((AbstractTocContextMenuAction) icma).setMapContext(t
                .getMapContext());
        }

        putValue(Action.NAME, icma.getText());
        putValue(Action.SHORT_DESCRIPTION, icma.getDescription());
    }

    /**
     * 
     * @return the underlying old style menu action
     */
    public IContextMenuAction getContextMenuAction() {
        return icma;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {

        if (getActiveLayer() != null) {
            icma.execute(new TocItemBranch(getActiveLayer()),
                getSelectedLayers());
        } else {
            icma.execute(null, getSelectedLayers());
        }
    }

    @Override
    public boolean isVisible() {

        if (getActiveLayer() == null) {
            return icma.isVisible(null, getSelectedLayers());
        } else {
            return icma.isVisible(new TocItemBranch(getActiveLayer()),
                getSelectedLayers());
        }

    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        if (!pcl.contains(listener)) {
            pcl.add(listener);
        }
    }

    @Override
    public boolean isEnabled() {

        if (getActiveLayer() == null) {
            return icma.isEnabled(null, getSelectedLayers());
        } else {
            return icma.isEnabled(new TocItemBranch(getActiveLayer()),
                getSelectedLayers());
        }

    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        if (pcl.contains(listener)) {
            pcl.remove(listener);
        }
    }

    @Override
    public void setEnabled(boolean b) {
    }

    @Override
    public Manager getManager() {
        return TOCLocator.getInstance().getTOCManager();
    }



}
