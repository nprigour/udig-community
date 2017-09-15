/* uDig - User Friendly Desktop Internet GIS client
 * http://udig.refractions.net
 * (C) 2004, Refractions Research Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */
package es.axios.udig.ui.editingtools.support;

import java.util.Queue;

import org.locationtech.udig.project.ui.render.displayAdapter.MapMouseEvent;
import org.locationtech.udig.tools.edit.EditToolHandler;
import org.locationtech.udig.tools.edit.EventType;
import org.locationtech.udig.tools.edit.MouseTracker;
import org.locationtech.udig.tools.edit.support.Point;

public class TestMouseTracker extends MouseTracker{
    public TestMouseTracker( EditToolHandler handler2 ) {
        super(handler2);
    }

    @Override
    public void setDragStarted( Point dragStarted ) {
        super.setDragStarted(dragStarted);
    }
    
    @Override
    public Queue<MapMouseEvent> getModifiablePreviousEvents() {
        return super.getModifiablePreviousEvents();
    }
    
    @Override
    public void updateState( MapMouseEvent e, EventType type ) {
        super.updateState(e, type);
    }

}