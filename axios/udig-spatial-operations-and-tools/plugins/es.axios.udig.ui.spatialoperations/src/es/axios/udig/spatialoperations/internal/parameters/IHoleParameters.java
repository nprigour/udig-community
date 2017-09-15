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

/**
 * <pre>
 * Parameters needed for hole operation.
 * </pre>
 * 
 * @author Aritz Davila (www.axios.es)
 * @author Mauricio Pazos (www.axios.es)
 * @author Alain Jimeno (www.axios.es)
 * @since 1.2.0
 */
public interface IHoleParameters {

	/**
	 * @return Returns the sourceLayer.
	 */
	public ILayer getSourceLayer();
	
	/**
	 * @return Returns the source layer CRS.
	 */
	public CoordinateReferenceSystem getSourceCRS();

	/**
	 * @return Returns the usingLayer.
	 */
	public ILayer getUsingLayer();

	/**
	 * @return Returns the using layer CRs.
	 */
	public CoordinateReferenceSystem getUsingCRS();
	
	/**
	 * @return Returns the SourceFilter.
	 */
	public Filter getSourceFilter();

	/**
	 * @return Returns the usingFilter.
	 */
	public Filter getUsingFilter();

}
