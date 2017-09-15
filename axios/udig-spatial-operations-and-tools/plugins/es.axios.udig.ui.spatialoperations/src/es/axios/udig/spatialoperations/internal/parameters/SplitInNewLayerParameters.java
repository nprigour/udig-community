/* Spatial Operations & Editing Tools for uDig
 * 
 * Axios Engineering under a funding contract with: 
 *      Diputación Foral de Gipuzkoa, Ordenación Territorial 
 *
 *      http://b5m.gipuzkoa.net
 *      http://www.axios.es 
 *
 * (C) 2006, Diputación Foral de Gipuzkoa, Ordenación Territorial (DFG-OT). 
 * DFG-OT agrees to licence under Lesser General Public License (LGPL).
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

import org.locationtech.udig.project.ILayer;

import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Split Operation parameters.
 * <p>
 * Required parameters to create a new layer with the Split Operation.
 * </p>
 * 
 * @author Aritz Davila (www.axios.es)
 * @author Mauricio Pazos (www.axios.es)
 * @author Alain Jimeno (www.axios.es)
 * @since 1.2.0
 */
final class SplitInNewLayerParameters extends AbstractSplitParameters implements ISplitInNewLayerParameters {

	private String						targetName			= null;
	private Class<? extends Geometry>	targetGeometryClass	= null;

	/**
	 * New instance of IntersectInNewLayerParameters
	 * 
	 * @param firstLayer
	 * @param firstCRS
	 * @param featuresInFirstLayer
	 * @param secondLayer
	 * @param secondCRS
	 * @param featuresInSecondLayer
	 * @param targetName
	 * @param targetGeometryClass
	 */
	public SplitInNewLayerParameters(	final ILayer firstLayer,
										final CoordinateReferenceSystem firstCRS,
										final ILayer secondLayer,
										final CoordinateReferenceSystem secondCRS,
										final String targetName,
										final Class<? extends Geometry> targetGeometryClass,
										final Filter filterInFirstLayer,
										final Filter filterInSecondLayer) {

		super(firstLayer, firstCRS, secondLayer, secondCRS, filterInFirstLayer, filterInSecondLayer);

		assert targetName != null;
		assert targetGeometryClass != null;

		this.targetName = targetName;
		this.targetGeometryClass = targetGeometryClass;
	}

	public Class<? extends Geometry> getTargetGeometryClass() {

		return this.targetGeometryClass;
	}

	public String getTargetName() {

		return this.targetName;
	}

}
