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
package es.axios.udig.spatialoperations.internal.ui.parameters.polygontoline;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Image;

import es.axios.udig.spatialoperations.ui.parameters.IImageOperation;
import es.axios.udig.spatialoperations.ui.parameters.ISOAggregatedPresenter;
import es.axios.udig.spatialoperations.ui.parameters.ISOCommand;
import es.axios.udig.spatialoperations.ui.parameters.ISOParametersPresenterFactory;

/**
 * Makes the widgets required by Polygon To Line Operation
 * <p>
 * TODO COMMENT ME!
 * </p>
 * 
 * @author Mauricio Pazos (www.axios.es)
 * @author Aritz Davila (www.axios.es)
 * @since 1.2.0
 */
public final class PolygonToLineParametersFactory implements ISOParametersPresenterFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.axios.udig.spatialoperations.ui.parameters.ISOParametersPresenterFactory
	 * #createDataComposite(org.eclipse.swt.custom.ScrolledComposite, int)
	 */
	public ISOAggregatedPresenter createDataComposite(ScrolledComposite dataParent, int style) {
		ISOAggregatedPresenter p = new PolygonToLineComposite(dataParent, style);
		return p;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.axios.udig.spatialoperations.ui.parameters.ISOParametersPresenterFactory
	 * #createDemoComposite(org.eclipse.swt.custom.ScrolledComposite, int)
	 */
	public IImageOperation createDemoImages() {
		IImageOperation img = new PolygonToLineImages();
		return img;
	}

	public Image createIcon() {

		final String file = "images/polygontoline.gif"; //$NON-NLS-1$

		ImageDescriptor descriptor = ImageDescriptor.createFromFile(PolygonToLineComposite.class, file);

		Image image = descriptor.createImage();

		return image;
	}

}
