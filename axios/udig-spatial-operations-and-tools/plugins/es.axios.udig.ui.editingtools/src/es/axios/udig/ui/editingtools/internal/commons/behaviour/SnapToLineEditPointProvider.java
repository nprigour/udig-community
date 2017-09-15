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
package es.axios.udig.ui.editingtools.internal.commons.behaviour;

import org.locationtech.udig.project.ILayer;
import org.locationtech.udig.project.ui.render.displayAdapter.MapMouseEvent;
import org.locationtech.udig.project.ui.tool.IToolContext;
import org.locationtech.udig.tools.edit.EditToolHandler;
import org.locationtech.udig.tools.edit.preferences.PreferenceUtil;
import org.locationtech.udig.tools.edit.support.EditBlackboard;
import org.locationtech.udig.tools.edit.support.Point;
import org.locationtech.udig.tools.edit.support.SnapBehaviour;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineSegment;

/**
 * @author Aritz Davila (www.axios.es)
 * @author Mauricio Pazos (www.axios.es)
 * @since 0.2.0
 */
public class SnapToLineEditPointProvider implements IEditPointProvider {

    /**
     * Performs the calculation of the point to the closest segment in map coordinates and returns
     * the coordinate in the edit layer's CRS.
     * 
     * @param handler
     * @param point
     * @return
     */
    private Coordinate performSnapCalculation( final EditToolHandler handler, final Point point ) {
        final ILayer editLayer = handler.getEditLayer();
        final EditBlackboard layerBlackboard = handler.getEditBlackboard(editLayer);
        final boolean includeVerticesInCurrent = true;
        final SnapBehaviour snapBehaviour = PreferenceUtil.instance().getSnapBehaviour();
        final IToolContext context = handler.getContext();

        final CoordinateReferenceSystem mapCrs = context.getCRS();
        final int snappingRadius = PreferenceUtil.instance().getSnappingRadius();
        final SnapSegmentFinder finder = new SnapSegmentFinder(mapCrs);

        LineSegment segmentInMapCrs = finder.getClosestSnapSegment(handler, layerBlackboard, point,
                                                                   includeVerticesInCurrent,
                                                                   snapBehaviour, snappingRadius);
        final Coordinate coordInMapCrs = context.pixelToWorld(point.getX(), point.getY());
        Coordinate coordInLayerCrs;
        if (segmentInMapCrs != null) {
            final Coordinate closestInMapCrs = segmentInMapCrs.closestPoint(coordInMapCrs);
            CoordinateReferenceSystem layerCrs = editLayer.getCRS();
            MathTransform transform;
            try {
                transform = CRS.findMathTransform(mapCrs, layerCrs, true);
                coordInLayerCrs = JTS.transform(closestInMapCrs, new Coordinate(), transform);
            } catch (FactoryException e) {
                e.printStackTrace();
                coordInLayerCrs = layerBlackboard.toCoord(point);
            } catch (TransformException e) {
                e.printStackTrace();
                coordInLayerCrs = layerBlackboard.toCoord(point);
            }
        } else {
            coordInLayerCrs = layerBlackboard.toCoord(point);
        }
        return coordInLayerCrs;
    }

    public Coordinate getCoordinate( MapMouseEvent e, EditToolHandler handler ) {
        Point point = Point.valueOf(e.x, e.y);
        Coordinate snapCoord = performSnapCalculation(handler, point);
        return snapCoord;
    }

    public Point getPoint( MapMouseEvent e, EditToolHandler handler ) {
        Coordinate coordinate = getCoordinate(e, handler);
        EditBlackboard board = handler.getEditBlackboard(handler.getEditLayer());
        return board.toPoint(coordinate);
    }

}
