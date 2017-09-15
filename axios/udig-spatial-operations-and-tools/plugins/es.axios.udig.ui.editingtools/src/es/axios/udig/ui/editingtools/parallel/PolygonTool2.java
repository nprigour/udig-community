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

import java.util.Set;

import org.locationtech.udig.project.IBlackboard;
import org.locationtech.udig.tool.edit.internal.Messages;
import org.locationtech.udig.tools.edit.AbstractEditTool;
import org.locationtech.udig.tools.edit.Activator;
import org.locationtech.udig.tools.edit.EditToolConfigurationHelper;
import org.locationtech.udig.tools.edit.EditToolHandler;
import org.locationtech.udig.tools.edit.activator.AdvancedBehaviourCommandHandlerActivator;
import org.locationtech.udig.tools.edit.activator.DeleteGlobalActionSetterActivator;
import org.locationtech.udig.tools.edit.activator.DrawCurrentGeomVerticesActivator;
import org.locationtech.udig.tools.edit.activator.DrawGeomsActivator;
import org.locationtech.udig.tools.edit.activator.EditStateListenerActivator;
import org.locationtech.udig.tools.edit.activator.GridActivator;
import org.locationtech.udig.tools.edit.activator.SetRenderingFilter;
import org.locationtech.udig.tools.edit.behaviour.AcceptWhenOverFirstVertexBehaviour;
import org.locationtech.udig.tools.edit.behaviour.CursorControlBehaviour;
import org.locationtech.udig.tools.edit.behaviour.InsertVertexOnEdgeBehaviour;
import org.locationtech.udig.tools.edit.behaviour.MoveGeometryBehaviour;
import org.locationtech.udig.tools.edit.behaviour.MoveVertexBehaviour;
import org.locationtech.udig.tools.edit.behaviour.SelectFeatureBehaviour;
import org.locationtech.udig.tools.edit.behaviour.SelectVertexBehaviour;
import org.locationtech.udig.tools.edit.behaviour.SelectVertexOnMouseDownBehaviour;
import org.locationtech.udig.tools.edit.behaviour.SelectVerticesWithBoxBehaviour;
import org.locationtech.udig.tools.edit.behaviour.SetSnapSizeBehaviour;
import org.locationtech.udig.tools.edit.behaviour.StartEditingBehaviour;
import org.locationtech.udig.tools.edit.impl.ConditionalProvider;
import org.locationtech.udig.tools.edit.impl.PolygonTool;
import org.locationtech.udig.tools.edit.support.ShapeType;
import org.locationtech.udig.tools.edit.validator.PolygonCreationValidator;

import org.eclipse.swt.SWT;
import org.opengis.filter.spatial.Intersects;

import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

import es.axios.udig.ui.editingtools.internal.commons.behaviour.AddVertexWithProviderWhileCreatingBehaviour;
import es.axios.udig.ui.editingtools.internal.commons.behaviour.DoubleClickRunAcceptWithProviderBehaviour;

/**
 * Temporal replacement for {@link PolygonTool} while we define the framework for adding dynamic
 * behaviour modifiers.
 * <p>
 * TODO: move changes to {@link PolygonTool} and eliminate this class once the approach is accepted
 * </p>
 * 
 * @author Aritz Davila (www.axios.es)
 * @author Mauricio Pazos (www.axios.es)
 * @since 0.2.0
 */
public class PolygonTool2 extends PolygonTool {

    /**
     * Overrides {@link AbstractEditTool#setActive(boolean)} so we can set and remove the
     * {@link EditToolHandler} from the map's blackboard as appropriate.
     * <p>
     * TODO: If the approach we're using to add modes to edit tools is accepted and incorporated
     * into uDig, put this code directly in {@link AbstractEditTool}
     * </p>
     */
    @Override
    public void setActive( boolean active ) {
        super.setActive(active);

        final String key = EditToolHandler.class.getName();
        final IBlackboard blackboard = getContext().getMap().getBlackboard();
        if (active) {
            blackboard.put(key, handler);
        } else {
            blackboard.put(key, null);
        }
    }

    @SuppressWarnings("unchecked")
	@Override
    protected void initEventBehaviours( EditToolConfigurationHelper helper ) {
        // /helper.add(new DrawCreateVertexSnapAreaBehaviour());
        helper.startAdvancedFeatures();
        helper
              .add(new CursorControlBehaviour(
                                              handler,
                                              new ConditionalProvider(
                                                                      handler,
                                                                      Messages.PolygonTool_add_vertex_or_finish,
                                                                      Messages.PolygonTool_create_feature),
                                              new CursorControlBehaviour.SystemCursorProvider(
                                                                                              SWT.CURSOR_SIZEALL),
                                              new ConditionalProvider(
                                                                      handler,
                                                                      Messages.PolygonTool_move_vertex,
                                                                      null),
                                              new CursorControlBehaviour.SystemCursorProvider(
                                                                                              SWT.CURSOR_CROSS),
                                              new ConditionalProvider(
                                                                      handler,
                                                                      Messages.PolygonTool_add_vertex,
                                                                      null)));
        helper.stopAdvancedFeatures();
        // vertex selection OR geometry selection should not both happen so make them a mutual
        // exclusion behaviour
        helper.startMutualExclusiveList();
        helper.startOrderedList(false);

        AddVertexWithProviderWhileCreatingBehaviour addVertexBehaviour = new AddVertexWithProviderWhileCreatingBehaviour();
        addVertexBehaviour.setEditValidator(new PolygonCreationValidator());
        helper.add(addVertexBehaviour);

        helper.add(new AcceptWhenOverFirstVertexBehaviour());
        helper.stopOrderedList();
        helper.startAdvancedFeatures();
        helper.add(new SelectVertexOnMouseDownBehaviour());
        helper.add(new SelectVertexBehaviour());
        helper.stopAdvancedFeatures();

        helper.startAdvancedFeatures();
        SelectFeatureBehaviour selectGeometryBehaviour = new SelectFeatureBehaviour(
        						new Class[]{Polygon.class, MultiPolygon.class}, 
        						Intersects.class);
        
        selectGeometryBehaviour.initDefaultStrategies(ShapeType.POLYGON);
        helper.add(selectGeometryBehaviour);
        helper.add(new InsertVertexOnEdgeBehaviour());

        helper.startElseFeatures();
        helper.add(new StartEditingBehaviour(ShapeType.POLYGON));
        helper.stopElseFeatures();

        helper.stopAdvancedFeatures();
        helper.stopMutualExclusiveList();

        helper.startAdvancedFeatures();
        helper.startMutualExclusiveList();
        helper.add(new MoveVertexBehaviour());
        helper.add(new MoveGeometryBehaviour());
        helper.stopMutualExclusiveList();

        helper.add(new SelectVerticesWithBoxBehaviour());
        helper.stopAdvancedFeatures();
        // /helper.add(new DoubleClickRunAcceptBehaviour());
        helper.add(new DoubleClickRunAcceptWithProviderBehaviour());

        helper.add(new SetSnapSizeBehaviour());
        helper.done();

    }

    @Override
    protected void initActivators( Set<Activator> activators ) {
        activators.add(new EditStateListenerActivator());
        activators.add(new DeleteGlobalActionSetterActivator());
        activators.add(new DrawCurrentGeomVerticesActivator());
        activators.add(new DrawGeomsActivator(DrawGeomsActivator.DrawType.POLYGON));
        // /activators.add(new SetSnapBehaviourCommandHandlerActivator());
        activators.add(new AdvancedBehaviourCommandHandlerActivator());
        activators.add(new SetRenderingFilter());
        activators.add(new GridActivator());
    }

}
