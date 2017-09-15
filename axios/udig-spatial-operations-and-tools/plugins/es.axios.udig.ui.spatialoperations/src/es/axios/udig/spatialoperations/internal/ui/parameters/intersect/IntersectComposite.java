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
package es.axios.udig.spatialoperations.internal.ui.parameters.intersect;

import org.locationtech.udig.project.ILayer;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;

import es.axios.udig.spatialoperations.internal.i18n.Messages;
import es.axios.udig.spatialoperations.internal.ui.processconnectors.IntersectCommand;
import es.axios.udig.spatialoperations.ui.common.ResultLayerComposite;
import es.axios.udig.spatialoperations.ui.common.TargetLayerListenerAdapter;
import es.axios.udig.spatialoperations.ui.parameters.AggregatedPresenter;
import es.axios.udig.spatialoperations.ui.parameters.ISOCommand;
import es.axios.udig.spatialoperations.ui.parameters.SpatialOperationCommand.ParameterName;
import es.axios.udig.ui.commons.util.LayerUtil;
import es.axios.udig.ui.commons.util.MapUtil;

/**
 * Input data for intersect operation.
 * <p>
 * This contains the widgets required to capture the inputs for intersect
 * operation.
 * </p>
 * 
 * @author Mauricio Pazos (www.axios.es)
 * @author Aritz Davila (www.axios.es)
 * @author Alain Jimeno (www.axios.es)
 * @since 1.1.0
 */
final class IntersectComposite extends AggregatedPresenter {

	private static final int		GRID_DATA_1_WIDTH_HINT		= 105;
	private static final int		GRID_DATA_2_WIDTH_HINT		= 150;
	private static final int		GRID_DATA_4_WIDTH_HINT		= 45;
	private static final String		SOURCE_LEGEND				= "SourceLegend";		//$NON-NLS-1$
	private static final String		REFERENCE_LEGEND			= "ReferenceLegend";	//$NON-NLS-1$

	// widgets
	private Group					groupSourceInputs			= null;
	private Group					groupTargetInputs			= null;
	private ResultLayerComposite	resultComposite				= null;
	private CLabel					cLabel						= null;
	private CLabel					cLabel1						= null;
	private CLabel					cLabelFeaturesInFirstLayer	= null;
	private CLabel					cLabel3						= null;
	private CCombo					comboSecondLayer			= null;
	private CLabel					cLabel4						= null;
	private CLabel					cLabelFeaturesInSecondLayer	= null;
	private CCombo					comboFirstLayer				= null;
	private CLabel					sourceLegend				= null;
	private CLabel					referenceLegend				= null;

	private TabFolder				tabFolder					= null;
	private Composite				basicComposite				= null;
	private Composite				advancedComposite			= null;

	private ImageRegistry			imagesRegistry				= null;

	// parameters
	private ILayer					currentFirstLayer			= null;
	private ILayer					currentSecondLayer			= null;

	public IntersectComposite(Composite parent, int style) {

		super(parent, style);

		super.initialize();
	}

	@Override
	protected final void createContents() {

		GridLayout gridLayout = new GridLayout();
		setLayout(gridLayout);

		this.imagesRegistry = createImageRegistry();

		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = GridData.FILL;
		tabFolder = new TabFolder(this, SWT.NONE);
		tabFolder.setLayoutData(gridData);

		basicComposite = new Composite(tabFolder, SWT.NONE);
		basicComposite.setLayoutData(gridData);
		basicComposite.setLayout(gridLayout);

		advancedComposite = new Composite(tabFolder, SWT.NONE);
		advancedComposite.setLayoutData(gridData);
		advancedComposite.setLayout(gridLayout);

		createGroupSourceInputs();

		createGroupTargetInputs();

		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText(Messages.Composite_tab_folder_basic);
		tabItem.setControl(basicComposite);

	}

	private ImageRegistry createImageRegistry() {

		ImageRegistry registry = new ImageRegistry();

		String imgFile = "images/" + SOURCE_LEGEND + ".gif"; //$NON-NLS-1$ //$NON-NLS-2$
		registry.put(SOURCE_LEGEND, ImageDescriptor.createFromFile(IntersectComposite.class, imgFile));

		imgFile = "images/" + REFERENCE_LEGEND + ".gif"; //$NON-NLS-1$ //$NON-NLS-2$
		registry.put(REFERENCE_LEGEND, ImageDescriptor.createFromFile(IntersectComposite.class, imgFile));

		return registry;
	}

	/**
	 * This method initializes group for source inputs
	 */
	private void createGroupSourceInputs() {

		GridData gridData11 = new GridData();
		gridData11.horizontalAlignment = GridData.BEGINNING;
		gridData11.grabExcessHorizontalSpace = false;
		gridData11.grabExcessVerticalSpace = true;
		gridData11.verticalAlignment = GridData.CENTER;
		gridData11.widthHint = GRID_DATA_1_WIDTH_HINT;

		GridData gridData12 = new GridData();
		gridData12.horizontalAlignment = GridData.BEGINNING;
		gridData12.grabExcessHorizontalSpace = false;
		gridData12.grabExcessVerticalSpace = true;
		gridData12.verticalAlignment = GridData.CENTER;
		gridData12.widthHint = GRID_DATA_2_WIDTH_HINT;

		GridData gridData13 = new GridData();
		gridData13.horizontalAlignment = GridData.BEGINNING;
		gridData13.grabExcessHorizontalSpace = false;
		gridData13.grabExcessVerticalSpace = true;
		gridData13.verticalAlignment = GridData.CENTER;
		// gridData13.widthHint = GRID_DATA_3_WIDTH_HINT;

		GridData gridData14 = new GridData();
		gridData14.horizontalAlignment = GridData.BEGINNING;
		gridData14.grabExcessHorizontalSpace = true;
		gridData14.verticalAlignment = GridData.CENTER;
		gridData14.widthHint = GRID_DATA_4_WIDTH_HINT;

		GridData gridData21 = new GridData();
		gridData21.horizontalAlignment = GridData.BEGINNING;
		gridData21.grabExcessHorizontalSpace = false;
		gridData21.grabExcessVerticalSpace = true;
		gridData21.verticalAlignment = GridData.CENTER;
		gridData21.widthHint = GRID_DATA_1_WIDTH_HINT;

		GridData gridData22 = new GridData();
		gridData22.horizontalAlignment = GridData.BEGINNING;
		gridData22.grabExcessHorizontalSpace = false;
		gridData22.grabExcessVerticalSpace = true;
		gridData22.verticalAlignment = GridData.CENTER;
		gridData22.widthHint = GRID_DATA_2_WIDTH_HINT;

		GridData gridData23 = new GridData();
		gridData23.horizontalAlignment = GridData.BEGINNING;
		gridData23.grabExcessHorizontalSpace = false;
		gridData23.grabExcessVerticalSpace = true;
		gridData23.verticalAlignment = GridData.CENTER;
		// gridData23.widthHint = GRID_DATA_3_WIDTH_HINT;

		GridData gridData24 = new GridData();
		gridData24.horizontalAlignment = GridData.BEGINNING;
		gridData24.grabExcessHorizontalSpace = true;
		gridData24.verticalAlignment = GridData.CENTER;
		gridData24.widthHint = GRID_DATA_4_WIDTH_HINT;

		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = false;
		gridData.verticalAlignment = GridData.CENTER;

		GridData gridData5 = new GridData();
		gridData5.horizontalAlignment = GridData.BEGINNING;
		gridData5.grabExcessHorizontalSpace = false;
		gridData5.verticalAlignment = GridData.CENTER;

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 5;

		groupSourceInputs = new Group(basicComposite, SWT.NONE);
		groupSourceInputs.setText(Messages.IntersectComposite_source);
		groupSourceInputs.setLayout(gridLayout);
		groupSourceInputs.setLayoutData(gridData);

		cLabel = new CLabel(groupSourceInputs, SWT.NONE);
		cLabel.setText(Messages.IntersectComposite_first_layer + ":"); //$NON-NLS-1$
		cLabel.setLayoutData(gridData11);

		comboFirstLayer = new CCombo(groupSourceInputs, SWT.BORDER | SWT.READ_ONLY);
		comboFirstLayer.setLayoutData(gridData12);

		this.comboFirstLayer.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				selectedFirstLayerActions();

			}
		});

		sourceLegend = new CLabel(groupSourceInputs, SWT.NONE);
		sourceLegend.setText(""); //$NON-NLS-1$
		sourceLegend.setLayoutData(gridData5);
		sourceLegend.setImage(this.imagesRegistry.get(SOURCE_LEGEND));

		cLabel1 = new CLabel(groupSourceInputs, SWT.NONE);
		cLabel1.setText(Messages.IntersectComposite_selected_features + ":"); //$NON-NLS-1$
		cLabel1.setLayoutData(gridData13);

		cLabelFeaturesInFirstLayer = new CLabel(groupSourceInputs, SWT.NONE);
		cLabelFeaturesInFirstLayer.setText(""); //$NON-NLS-1$
		cLabelFeaturesInFirstLayer.setLayoutData(gridData14);

		cLabel3 = new CLabel(groupSourceInputs, SWT.NONE);
		cLabel3.setText(Messages.IntersectComposite_second_layer + ":"); //$NON-NLS-1$
		cLabel3.setLayoutData(gridData21);

		comboSecondLayer = new CCombo(groupSourceInputs, SWT.BORDER | SWT.READ_ONLY);
		comboSecondLayer.setLayoutData(gridData22);
		this.comboSecondLayer.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				selectedSecondLayerActions();
			}
		});

		referenceLegend = new CLabel(groupSourceInputs, SWT.NONE);
		referenceLegend.setText(""); //$NON-NLS-1$
		referenceLegend.setLayoutData(gridData5);
		referenceLegend.setImage(this.imagesRegistry.get(REFERENCE_LEGEND));

		cLabel4 = new CLabel(groupSourceInputs, SWT.NONE);
		cLabel4.setText(Messages.IntersectComposite_selected_features + ":"); //$NON-NLS-1$
		cLabel4.setLayoutData(gridData23);

		cLabelFeaturesInSecondLayer = new CLabel(groupSourceInputs, SWT.NONE);
		cLabelFeaturesInSecondLayer.setText(""); //$NON-NLS-1$
		cLabelFeaturesInSecondLayer.setLayoutData(gridData24);
	}

	private void createGroupTargetInputs() {

		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = false;
		gridData.verticalAlignment = GridData.BEGINNING;

		groupTargetInputs = new Group(basicComposite, SWT.NONE);
		groupTargetInputs.setText(Messages.IntersectComposite_result);
		groupTargetInputs.setLayout(new GridLayout());
		groupTargetInputs.setLayoutData(gridData);

		this.resultComposite = new ResultLayerComposite(this, groupTargetInputs, SWT.NONE, GRID_DATA_1_WIDTH_HINT);

		GridData resultCompositeGridData = new GridData();
		resultCompositeGridData.horizontalAlignment = GridData.FILL;
		resultCompositeGridData.grabExcessHorizontalSpace = true;
		resultCompositeGridData.grabExcessVerticalSpace = true;
		resultCompositeGridData.verticalAlignment = GridData.FILL;

		this.resultComposite.setLayoutData(resultCompositeGridData);

		this.resultComposite.addSpecifiedLayerListener(new TargetLayerListenerAdapter() {

			@Override
			public void validateTargetLayer() {
				validateParameters();
			}
		});
	}

	/**
	 * Populates layer comboboxs with the current layers. clipping layer.
	 */
	@Override
	protected void populate() {

		loadComboWithLayerList(this.comboFirstLayer);
		loadComboWithLayerList(this.comboSecondLayer);

		selectDefaultLayer();
	}

	/**
	 * Sets the default layer to intersect.
	 */
	private void selectDefaultLayer() {

		// synchronizes the command parameters with the correspondents widgets
		// values.
		IntersectCommand cmd = (IntersectCommand) getCommand();

		this.currentFirstLayer = cmd.getFirstLayer();
		if (this.currentFirstLayer == null) {
			ILayer selected = MapUtil.getSelectedLayer(getCurrentMap());
			if (LayerUtil.isCompatible(selected, cmd.getDomainValues(ParameterName.SOURCE_GEOMETRY_CLASS))) {
				this.currentFirstLayer = selected;
			}
		}
		setCurrentFirstLayer(this.currentFirstLayer);
		changeSelectedLayer(this.currentFirstLayer, this.comboFirstLayer);

		// sets the second layer as current
		this.currentSecondLayer = cmd.getSecondLayer();
		setCurrentSecondLayer(this.currentSecondLayer);
		changeSelectedLayer(this.currentSecondLayer, this.comboSecondLayer);

	}

	/**
	 * Sets the selected First layer and its features has current.
	 */
	private void selectedFirstLayerActions() {

		ILayer selectedLayer = getSelecedLayer(this.comboFirstLayer);
		if (selectedLayer == null) {
			return;
		}

		setCurrentFirstLayer(selectedLayer);

		validateParameters();

	}

	private void setCurrentFirstLayer(final ILayer selectedLayer) {

		if (selectedLayer == null) {
			return;
		}

		this.currentFirstLayer = selectedLayer;

		presentSelectionAllOrBBox(this.currentFirstLayer, this.currentFirstLayer.getFilter(),
					this.cLabelFeaturesInFirstLayer);
	}

	private void setCurrentSecondLayer(final ILayer selectedLayer) {

		if (selectedLayer == null) {
			return;
		}

		this.currentSecondLayer = selectedLayer;

		presentSelectionAllOrBBox(this.currentSecondLayer, this.currentSecondLayer.getFilter(),
					this.cLabelFeaturesInSecondLayer);
	}

	/**
	 * Sets the selected Second layer and its features has current.
	 */
	private void selectedSecondLayerActions() {

		ILayer selectedLayer = getSelecedLayer(this.comboSecondLayer);
		if (selectedLayer == null) {
			return;
		}

		setCurrentSecondLayer(selectedLayer);

		validateParameters();
	}

	@Override
	public void setEnabled(boolean enabled) {

		groupSourceInputs.setEnabled(enabled);
		groupTargetInputs.setEnabled(enabled);
		resultComposite.setEnabled(enabled);
		comboSecondLayer.setEnabled(enabled);
		comboFirstLayer.setEnabled(enabled);
	}

	/**
	 * Validate parameters, if they are ok enable operation
	 */
	@Override
	protected void setParametersOnCommand(ISOCommand command) {

		// Sets the parameters values in controller to do the validation
		IntersectCommand cmd = (IntersectCommand) command;

		Filter filterInFirstLayer = null;
		Filter filterInSecondLayer = null;
		CoordinateReferenceSystem firstCRS = null;
		CoordinateReferenceSystem secondCRS = null;

		if (this.currentFirstLayer != null) {
			firstCRS = this.currentFirstLayer.getCRS();
			filterInFirstLayer = getFilter(this.currentFirstLayer);
		}
		if (this.currentSecondLayer != null) {
			secondCRS = this.currentSecondLayer.getCRS();
			filterInSecondLayer = getFilter(this.currentSecondLayer);
		}

		// Start setting the parameters on the command.
		cmd.setInputParams(this.currentFirstLayer, firstCRS, this.currentSecondLayer, secondCRS, filterInFirstLayer,
					filterInSecondLayer);

		if (this.resultComposite.isLayerSelected()) {

			final ILayer targetLayer = this.resultComposite.getCurrentTargetLayer();

			cmd.setOutputParams(targetLayer);
		} else {
			// requires create a new layer
			final String layerName = this.resultComposite.getNewLayerName();
			final Class<? extends Geometry> targetClass = this.resultComposite.getTargetClass();
			final CoordinateReferenceSystem targetCRS = getCurrentMapCrs();

			cmd.setOutputParams(layerName, targetCRS, targetClass);
		}
	}

	/**
	 * Maintains the consistency between the presented layers and features in
	 * map model and this view
	 */
	@Override
	protected final void changedLayerListActions() {

		// change the list of layers
		this.comboFirstLayer.removeAll();
		this.comboSecondLayer.removeAll();

		populate();

		// update the selection
		// FIXME this has been already done inside populate()
		//changeSelectedLayer(this.currentFirstLayer, this.comboFirstLayer);
		selectedFirstLayerActions();

		//changeSelectedLayer(this.currentSecondLayer, this.comboSecondLayer);
		selectedSecondLayerActions();

	}

	/**
	 * Changes the count of features selected of the selected layer
	 */
	@Override
	protected void changedFilterSelectionActions(final ILayer layer, final Filter newFilter) {

		// this.layerCount=getLayerCount();

		setLayerAndFilter();

		if (layer.equals(this.currentFirstLayer)) {

			presentSelectionAllOrBBox(this.currentFirstLayer, newFilter, this.cLabelFeaturesInFirstLayer);

		}
		if (layer.equals(this.currentSecondLayer)) {

			presentSelectionAllOrBBox(this.currentSecondLayer, newFilter, this.cLabelFeaturesInSecondLayer);

		}
		validateParameters();
	}

	@Override
	protected void removeLayerListActions(ILayer layer) {

		if (layer.equals(this.currentFirstLayer)) {
			currentFirstLayer = null;
		}
		validateParameters();
	}
} // @jve:decl-index=0:visual-constraint="10,10"
