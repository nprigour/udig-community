/* uDig-Spatial Operations plugins
 * http://b5m.gipuzkoa.net
 * (C) 2006, Diputación Foral de Gipuzkoa, Ordenación Territorial.
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
package es.axios.udig.ui.editingtools.split;

import java.util.List;
import java.util.Set;

import org.locationtech.udig.project.ui.tool.IToolContext;
import org.locationtech.udig.tools.edit.AbstractEditTool;
import org.locationtech.udig.tools.edit.Activator;
import org.locationtech.udig.tools.edit.Behaviour;
import org.locationtech.udig.tools.edit.EditToolConfigurationHelper;
import org.locationtech.udig.tools.edit.EnablementBehaviour;
import org.locationtech.udig.tools.edit.activator.DrawCurrentGeomVerticesActivator;
import org.locationtech.udig.tools.edit.activator.DrawGeomsActivator;
import org.locationtech.udig.tools.edit.activator.EditStateListenerActivator;
import org.locationtech.udig.tools.edit.activator.ResetAllStateActivator;
import org.locationtech.udig.tools.edit.activator.SetSnapBehaviourCommandHandlerActivator;
import org.locationtech.udig.tools.edit.behaviour.AcceptOnDoubleClickBehaviour;
import org.locationtech.udig.tools.edit.behaviour.DefaultCancelBehaviour;
import org.locationtech.udig.tools.edit.behaviour.DrawCreateVertexSnapAreaBehaviour;
import org.locationtech.udig.tools.edit.behaviour.RefreshLayersBehaviour;
import org.locationtech.udig.tools.edit.behaviour.SetSnapSizeBehaviour;
import org.locationtech.udig.tools.edit.behaviour.StartEditingBehaviour;
import org.locationtech.udig.tools.edit.enablement.ValidToolDetectionActivator;
import org.locationtech.udig.tools.edit.support.ShapeType;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

import es.axios.udig.ui.editingtools.internal.i18n.Messages;
import es.axios.udig.ui.editingtools.internal.presentation.StatusBar;
import es.axios.udig.ui.editingtools.split.internal.AddSplitVertexBehaviour;
import es.axios.udig.ui.editingtools.split.internal.SplitGeometryBehaviour;

/**
 * Splits one or more SimpleFeature using a {@link SplitGeometryBehaviour} to
 * accomplish the task once the user finished drawing the splitting line.
 * <p>
 * Users can use this tool to either:
 * <ul>
 * <li>Split one or more Simple Features using a line
 * <li>Split one polygon feature by creating a hole
 * </ul>
 * 
 * @author Mauricio Pazos (www.axios.es)
 * @author Aritz Davila (www.axios.es)
 * @since 1.1.0
 */
public class SplitTool extends AbstractEditTool {

	@Override
	public void setActive(final boolean active) {
		super.setActive(active);
		IToolContext context = getContext();
		if (active && context.getMapLayers().size() > 0) {
			String message = Messages.SplitTool_draw_line_to_split;
			StatusBar.setStatusBarMessage(context, message);
		} else {
			StatusBar.setStatusBarMessage(context, "");//$NON-NLS-1$
		}
	}

	/**
	 * Initializes the list of Activators that are ran when the tool is
	 * activated and deactivated.
	 * 
	 * @param activators
	 *            an empty list.
	 */
	@Override
	protected void initActivators(Set<Activator> activators) {
		activators.add(new EditStateListenerActivator());
		activators.add(new DrawGeomsActivator(DrawGeomsActivator.DrawType.LINE));
		activators.add(new DrawCurrentGeomVerticesActivator());
		activators.add(new ResetAllStateActivator());
		activators.add(new SetSnapBehaviourCommandHandlerActivator());
	}

	/**
	 * Initializes the list of Behaviours to run when the current edit has been
	 * accepted. Acceptance is signalled by a double click or the Enter key
	 * 
	 * @param acceptBehaviours
	 *            an empty list
	 */
	@Override
	protected void initAcceptBehaviours(List<Behaviour> acceptBehaviours) {
		acceptBehaviours.add(new SplitGeometryBehaviour());
		acceptBehaviours.add(new RefreshLayersBehaviour());
	}

	/**
	 * Initializes the behaviours that are ran when a cancel signal is received
	 * (the ESC key).
	 * 
	 * @param cancelBehaviours
	 *            an empty list
	 */
	@Override
	protected void initCancelBehaviours(List<Behaviour> cancelBehaviours) {
		cancelBehaviours.add(new DefaultCancelBehaviour());
	}

	/**
	 * Initializes the Event Behaviours that are run when an event occurs. Since
	 * this can be complex a helper class is provided to build the complex
	 * datastructure of Behaviours.
	 * 
	 * @see EditToolConfigurationHelper
	 * @param helper
	 *            a helper for constructing the complicated structure of
	 *            EventBehaviours.
	 */
	@Override
	protected void initEventBehaviours(EditToolConfigurationHelper helper) {
		helper.add(new DrawCreateVertexSnapAreaBehaviour());
		helper.startMutualExclusiveList();
		helper.add(new AddSplitVertexBehaviour());
		// override so that editing will not be started if there are no
		// geometries on the
		// blackboard.
		helper.add(new StartEditingBehaviour(ShapeType.LINE));
		helper.stopMutualExclusiveList();

		helper.add(new SetSnapSizeBehaviour());
		helper.add(new AcceptOnDoubleClickBehaviour());
		helper.done();
	}

	/**
	 * Initializes the list of {@link EnablementBehaviour}s that are ran to
	 * determine if the tool is enabled given an event. For example if the mouse
	 * cursor is outside the valid bounds of a CRS for a layer an
	 * EnablementBehaviour might signal that editing is illegal and provide a
	 * message for the user indicating why.
	 * 
	 * @param enablementBehaviours
	 *            an empty list
	 */
	@Override
	protected void initEnablementBehaviours(List<EnablementBehaviour> enablementBehaviours) {
		/*
		 * enablementBehaviours.add(new WithinLegalLayerBoundsBehaviour());
		 */
		enablementBehaviours.add(new ValidToolDetectionActivator(new Class[] {
				LineString.class,
				MultiLineString.class,
				MultiPolygon.class,
				Polygon.class}));
	}

}
