/* Spatial Operations & Editing Tools for uDig
 * 
 * Axios Engineering under a funding contract with: 
 * 		Diputación Foral de Gipuzkoa, Ordenación Territorial 
 *
 * 		http://b5m.gipuzkoa.net
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
package es.axios.udig.spatialoperations.internal.parameters;

import org.opengis.referencing.crs.CoordinateReferenceSystem;

import org.locationtech.udig.project.ILayer;

/**
 * Parameters required to intersect in an existent layer
 * <p>
 *
 * </p>
 * @author Mauricio Pazos (www.axios.es)
 * @author Aritz Davila (www.axios.es)
 * @since 1.1.0
 */
public interface IIntersectInExistentLayerParameters extends IIntersectParameters {


    /**
     * @return Returns the targetLayer.
     */
    public ILayer getTargetLayer();
    
	/**
	 * 
	 * @return The target CRS.
	 */
	public CoordinateReferenceSystem getTargetCRS();

}
