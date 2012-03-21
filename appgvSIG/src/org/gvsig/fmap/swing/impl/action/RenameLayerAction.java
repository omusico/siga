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

import java.awt.event.ActionEvent;

import javax.swing.Action;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.view.toc.gui.ChangeName;

import org.gvsig.fmap.swing.toc.TOC;
import org.gvsig.fmap.swing.toc.action.AbstractTOCAction;

/**
 * This is the TOC menu action that allows the user to change layer name.
 * Shows a small dialog to input new name.
 *  
 * @author Juan Lucas Dominguez jldominguez prodevelop es
 * @version $Id$
 *
 */
public class RenameLayerAction extends AbstractTOCAction {

    /**
     * 
     */
    private static final long serialVersionUID = 7039451989374104367L;

    /**
     * Constructor 
     * @param t associated TOC
     */
    public RenameLayerAction(TOC t) {
        super(t, 1, "group1", 10);
        putValue(Action.NAME, PluginServices.getText(this, "cambio_nombre"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FLayer lyr = getTOC().getActiveLayer();
        if ((lyr == null) || !lyr.isAvailable()) {
            return;
        }
        ChangeName chn = new ChangeName(lyr.getName());
        PluginServices.getMDIManager().addWindow(chn);
        lyr.setName(chn.getName());
        Project project =
            ((ProjectExtension) PluginServices
                .getExtension(ProjectExtension.class)).getProject();
        project.setModified(true);
    }

    @Override
    public boolean isVisible() {
        return isEnabled();
    }

    @Override
    public boolean isEnabled() {
        return getTOC().getActiveLayer() != null;
    }

}
