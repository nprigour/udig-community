/* Spatial Operations & Editing Tools for uDig
 * 
 * Axios Engineering under a funding contract with: 
 *      Diputación Foral de Gipuzkoa, Ordenación Territorial 
 *
 *      http://b5m.gipuzkoa.net
 *      http://www.axios.es 
 *
 * (C) 2006, Diputación Foral de Gipuzkoa, Ordenación Territorial (DFG-OT). 
 * DFG-OT agrees to license under Lesser General Public License (LGPL).
 * 
 * You can redistribute it and/or modify it under the terms of the 
 * GNU Lesser General Public License as published by the Free Software 
 * Foundation; version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */
package es.axios.udig.ui.editingtools.parallel;

import java.util.List;

import org.locationtech.udig.tools.edit.EventBehaviour;
import org.locationtech.udig.tools.edit.behaviour.DrawCreateVertexSnapAreaBehaviour;
import es.axios.udig.ui.editingtools.internal.commons.behaviour.SnapToVertexEditPointProvider;

/**
 * @author Aritz Davila (www.axios.es)
 * @author Mauricio Pazos (www.axios.es)
 * @since 0.2.0
 */
public class SnapAction extends AbstractEditModifierActionDelegate {

    @Override
    protected void activate() {
        List<EventBehaviour> eventBehaviours = getHandler().getBehaviours();
        eventBehaviours.add(new DrawCreateVertexSnapAreaBehaviour());
        setEditPointProvider(new SnapToVertexEditPointProvider());
    }
}
