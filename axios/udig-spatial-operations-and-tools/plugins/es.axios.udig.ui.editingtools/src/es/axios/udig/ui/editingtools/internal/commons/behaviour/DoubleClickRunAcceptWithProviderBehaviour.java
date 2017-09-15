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
package es.axios.udig.ui.editingtools.internal.commons.behaviour;

import java.util.ArrayList;
import java.util.List;

import org.locationtech.udig.project.IBlackboard;
import org.locationtech.udig.project.command.UndoableComposite;
import org.locationtech.udig.project.command.UndoableMapCommand;
import org.locationtech.udig.project.internal.Blackboard;
import org.locationtech.udig.project.ui.render.displayAdapter.MapMouseEvent;
import org.locationtech.udig.tools.edit.EditToolHandler;
import org.locationtech.udig.tools.edit.EventType;
import org.locationtech.udig.tools.edit.behaviour.AcceptOnDoubleClickBehaviour;

/**
 * Same as  {@link AcceptOnDoubleClickBehaviour} but uses the {@link IEditPointProvider} held in the
 * map's {@link IBlackboard blackboard} to obtain the actual point to add to the current shape
 * before running the accept behaviours.
 * <p>
 * NOTE the {@link #getCommand(EditToolHandler, MapMouseEvent, EventType)} method in this class is
 * meant to replace the one in {@link AcceptOnDoubleClickBehaviour} and this class is meant to
 * dissapear when this is integrated with the udig.tool.edit plugin
 * </p>
 * 
 * @author Aritz Davila (www.axios.es)
 * @author Mauricio Pazos (www.axios.es)
 * @since 0.2.0
 */
public class DoubleClickRunAcceptWithProviderBehaviour extends AcceptOnDoubleClickBehaviour {

    /**
     * Obtains the point to add through the {@link IEditPointProvider} held in the map's
     * {@link Blackboard} and returns a command to add it to the current edit shape and to run the
     * handler's accept behaviours
     */
    @Override
    public UndoableMapCommand getCommand( EditToolHandler handler, MapMouseEvent e,
                                          EventType eventType ) {
        List<UndoableMapCommand> commands = new ArrayList<UndoableMapCommand>();

        // AddVertextWhileCreatingBehaviour can now close linestrings
        
        /*
        if (handler.getCurrentState() == EditState.CREATING && isAddPoint()) {
            EditBlackboard editBlackboard = handler.getEditBlackboard(handler.getEditLayer());
            IEditPointProvider provider = getProvider(handler);
            Coordinate coordToAdd = provider.getCoordinate(e, handler);
            Point pointToAdd = editBlackboard.toPoint(coordToAdd);
            int vertexRadius = PreferenceUtil.instance().getVertexRadius();
            Point destination = editBlackboard.overVertex(pointToAdd, vertexRadius);
            if (destination == null) {
                commands.add(new AddVertexCommand(handler, editBlackboard, coordToAdd));
            }
        }
        */
        commands.add(handler.getCommand(handler.getAcceptBehaviours()));
        UndoableComposite undoableComposite = new UndoableComposite(commands);
        
        return undoableComposite;
        /*
        try {
            undoableComposite.setMap(handler.getContext().getMap());
            undoableComposite.execute(new NullProgressMonitor());
        } catch (Exception e1) {
            throw (RuntimeException) new RuntimeException().initCause(e1);
        }
        return new UndoRedoCommand(undoableComposite);
        */
    }

//    private IEditPointProvider getProvider( EditToolHandler handler ) {
//        IBlackboard blackboard = handler.getContext().getMap().getBlackboard();
//        String string = IEditPointProvider.BLACKBOARD_KEY;
//        IEditPointProvider provider = (IEditPointProvider) blackboard.get(string);
//        // TODO: this check won't be needed when AbstractEditTool sets a default provider
//        if (provider == null) {
//            provider = new DefaultEditPointProvider();
//        }
//        return provider;
//    }

}
