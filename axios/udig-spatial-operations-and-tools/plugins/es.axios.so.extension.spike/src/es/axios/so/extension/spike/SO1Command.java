/**
 * 
 */
package es.axios.so.extension.spike;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

import es.axios.udig.spatialoperations.ui.parameters.SOCommandException;
import es.axios.udig.spatialoperations.ui.parameters.SpatialOperationCommand;
import es.axios.udig.ui.commons.message.InfoMessage;

/**
 * @author mauro
 *
 */
public final class SO1Command extends SpatialOperationCommand {

	private static final InfoMessage	INITIAL_MESSAGE		= new InfoMessage(
																		"Spatial Operation 1 description",
																		InfoMessage.Type.IMPORTANT_INFO);

	/**
	 * 
	 */
	public SO1Command() {
		super(INITIAL_MESSAGE);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see es.axios.udig.spatialoperations.ui.parameters.SpatialOperationCommand#executeOperation()
	 */
	@Override
	public void executeOperation() throws SOCommandException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see es.axios.udig.spatialoperations.ui.parameters.SpatialOperationCommand#initParameters()
	 */
	@Override
	public void initParameters() {
		// TODO Auto-generated method stub

	}


	/* (non-Javadoc)
	 * @see es.axios.udig.spatialoperations.ui.parameters.ISOCommand#getOperationID()
	 */
	public String getOperationID() {
		// TODO Auto-generated method stub
		return "Op1";
	}

	public String getOperationName() {
		// TODO Auto-generated method stub
		return "so 1 name";
	}

	public String getToolTipText() {
		// TODO Auto-generated method stub
		return "so 1 tool tip";
	}

	protected Object[] getResultLayerGeometry() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Object[] getSourceGeomertyClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Object[] getValidTargetLayerGeometries() {

		Object[] obj = new Object[] { 
					Polygon.class, 
					MultiPolygon.class, 
					Geometry.class };

		return obj;

	}

	@Override
	protected boolean validateParameters() {
		// TODO Auto-generated method stub
		return true;
	}
}
