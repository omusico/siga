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
package com.iver.cit.gvsig.project.documents.view.toc;

import java.util.Map;

import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.project.documents.contextMenu.AbstractContextMenuAction;
import com.iver.utiles.extensionPoints.IExtensionBuilder;

public abstract class AbstractTocContextMenuAction extends
    AbstractContextMenuAction implements IExtensionBuilder {

    private MapContext mapContext;

    public MapContext getMapContext() {
        return this.mapContext;
    }

    public void setMapContext(MapContext mapContext) {
        this.mapContext = mapContext;
    }

    /**
     * @deprecated use public boolean isEnabled(ITocItem item, FLayer[]
     *             selectedItems)
     */
    @Deprecated
    @Override
    public boolean isEnabled(Object item, Object[] selectedItems) {
        return this.isEnabled((ITocItem) item, (FLayer[]) selectedItems);
    }

    /**
     * @deprecated use public boolean isVisible(ITocItem item, FLayer[]
     *             selectedItems)
     */
    @Deprecated
    @Override
    public boolean isVisible(Object item, Object[] selectedItems) {
        return this.isVisible((ITocItem) item, (FLayer[]) selectedItems);
    }

    /**
     * @deprecated use public void execute(ITocItem item, FLayer[]
     *             selectedItems)
     */
    @Deprecated
    public void execute(Object item, Object[] selectedItems) {
        this.execute((ITocItem) item, (FLayer[]) selectedItems);
    }

    public FLayer getNodeLayer(ITocItem node) {
        if (isTocItemBranch(node)) {
            return ((TocItemBranch) node).getLayer();
        }
        return null;
    }

    public boolean isTocItemLeaf(ITocItem node) {
        return node instanceof TocItemLeaf;
    }

    public boolean isTocItemBranch(ITocItem node) {
        return node instanceof TocItemBranch;
    }

    public boolean isEnabled(ITocItem item, FLayer[] selectedItems) {
        return true;
    }

    public boolean isVisible(ITocItem item, FLayer[] selectedItems) {
        return true;
    }

    public abstract void execute(ITocItem item, FLayer[] selectedItems);

    public Object create() {
        return this;
    }

    public Object create(Map args) {
        // TODO Auto-generated method stub
        return this;
    }

    public Object create(Object[] args) {
        // TODO Auto-generated method stub
        return this;
    }

}
