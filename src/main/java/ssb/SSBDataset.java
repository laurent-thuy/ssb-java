package ssb;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;

/**
 * 
 * @version 1.1.1
 * @author Laurent-Thuy Soublin <br>
 *         <br>
 *         Represents a dataset.
 * 
 *         <br>
 *         <br>
 * 
 *         <pre>
 * <strong>Create SSBDataset object</strong> 
 * URL url = new URL("https://data.ssb.no/api/v0/dataset/1052.json?lang=en");
 * SSBDataset ds = new SSBDataset(url);
 * 
 * <strong>Create dataset metadata</strong> 
 * String label = ds.label(); // Employment and unemployment for persons aged 15-74, by sex, age, time and contents
 * String source = ds.source(); // Statistics Norway
 * Date updated = ds.updated(); // Sat Mar 19 10:14:32 GMT+01:00 2016
 * 
 * <strong>Get sizes, ids and labels of dimensions</strong> 
 * List&lt;Integer&gt; sizes = ds.getDimensionsSizes(); // [1, 1, 13, 2]
 * List&lt;String&gt; ids = ds.getDimensionsIds(); // [Kjonn, Alder, Tid, ContentsCode]
 * List&lt;String&gt; labels = ds.getDimensionsLabels(); // [sex, age, time, contents]
 * 
 * <strong>Get categories' labels for the "Tid" dimension</strong> 
 * List&lt;Object&gt; catLabels = ds.getDimensionCategoryLabels("Tid"); // [2015M01, 2015M02, 2015M03, 2015M04, 2015M05, ...]
 * 
 * <strong>Get a value</strong> 
 * // select 1st category in 1st dimension, 1st category in 2nd dimension
 * // select 8th category in 3rd dimension, 2nd category in 4th dimension
 * List&lt;Integer&gt; nTuple =  Arrays.asList(0,0,7,1); 				
 * Cell cell = ds.cells().get(nTuple);
 * List&lt;Object&gt; cellLabels = cell.getLabels(); // [Both sexes, 15-74 years, 2015M08, Unemployment rate (LFS), seasonally adjusted]
 * Object cellValue = cell.getValue(); // 4.6
 * 
 * <strong>Create a table</strong>
 * // 2nd dimension for rows, 4th dimension for columns
 * // 9th category of 3rd dimension as filter
 * // 1st row is the table header with columns' names, 1st cell being empty
 * // 1st cell of each row is the row name
 * Map&lt;Integer, Integer&gt; filters = new HashMap&lt;&gt;();
 * filters.put(2, 8);
 * List&lt;List&lt;Object&gt;&gt; table = ds.table(1, 3, filters);
 *         </pre>
 * 
 * 
 */
public class SSBDataset {

	/** The cells. */
	private Map<List<Integer>, Cell> cells;

	/** The ds. */
	private Dataset ds;

	/** The ds node. */
	private JsonNode dsNode;

	/** The mapper. */
	private ObjectMapper mapper;

	/** The ntuples. */
	private Set<List<Integer>> ntuples;

	/*
	 **************************************************************************
	 * public getters
	 **************************************************************************
	 */

	/**
	 * gets the label of this dataset
	 *
	 * @return the label of this dataset
	 */
	// metadata
	public String label() {
		return ds.getLabel();
	}

	/**
	 * gets the source of this dataset
	 *
	 * @return the source of this dataset
	 */
	public String source() {
		return ds.getSource();
	}

	/**
	 * gets the last update of this dataset
	 *
	 * @return the last update of this dataset
	 */
	public Date updated() {
		return ds.getUpdated();
	}

	/**
	 * gets the list of the dimensions sizes
	 *
	 * @return the list of the dimensions sizes
	 */
	// dimensions
	public List<Integer> getDimensionsSizes() {
		return (List) ds.getDimension().get("size");
	}

	/**
	 * gets the list of the dimensions ids
	 * 
	 * @return the list of the dimensions ids
	 */
	public List<String> getDimensionsIds() {
		return (List) ds.getDimension().get("id");
	}

	/**
	 * gets the id for this dimension by index
	 * 
	 * @param dimIndex
	 *            the index of this dimension
	 * @return the id for this dimension
	 */
	public String getDimensionId(int dimIndex) {
		return getDimensionsIds().get(dimIndex);
	}

	/**
	 * gets the dimensions' labels
	 *
	 * @return the dimensions' labels
	 */
	public List<String> getDimensionsLabels() {
		return getDimensionsIds().stream().map(dimId -> getDimensionLabel(dimId)).collect(Collectors.toList());
	}

	/**
	 * gets the dimension by id
	 *
	 * @param dimId
	 *            the dimension id
	 * @return the dimension
	 */
	public Map<String, Object> getDimension(String dimId) {
		return (Map) ds.getDimension().get(dimId);
	}

	/**
	 * gets the dimension by index
	 *
	 * @param dimIndex
	 *            the index of this dimension
	 * @return the dimension
	 */
	public Map<String, Object> getDimension(int dimIndex) {
		String dimId = getDimensionsIds().get(dimIndex);
		return getDimension(dimId);
	}

	/**
	 * gets the dimension index by id
	 *
	 * @param dimId
	 *            the dimension id
	 * @return the dimension index
	 */
	public int getDimensionIndex(String dimId) {
		List<String> dimIds = (List<String>) ds.getDimension().get("id");
		return dimId.indexOf(dimId);
	}

	/**
	 * Gets the dimension label by id
	 *
	 * @param dimId
	 *            the dimension id
	 * @return the dimension label
	 */
	public String getDimensionLabel(String dimId) {
		Map<String, Object> dimension = getDimension(dimId);
		return (String) dimension.get("label");
	}

	/**
	 * Gets the dimension label by index
	 *
	 * @param dimIndex
	 *            the dimension index
	 * @return the dimension label
	 */
	public String getDimensionLabel(int dimIndex) {
		String dimId = getDimensionsIds().get(dimIndex);
		return getDimensionLabel(dimId);
	}

	/**
	 * Gets the category labels for this dimension by id
	 *
	 * @param dimId
	 *            the dimension id
	 * @return the category labels for this dimension
	 */
	public List<Object> getDimensionCategoryLabels(String dimId) {
		Map<String, Object> dimension = getDimension(dimId);
		Map<String, Object> category = (Map) dimension.get("category");
		Map<String, Object> labels = (Map) category.get("label");
		return labels.values().stream().collect(Collectors.toList());
	}

	/**
	 * Gets the category labels for this dimension by index
	 *
	 * @param dimIndex
	 *            the dimension index
	 * @return the category labels for this dimension
	 */
	public List<Object> getDimensionCategoryLabels(int dimIndex) {
		String dimId = getDimensionsIds().get(dimIndex);
		return getDimensionCategoryLabels(dimId);
	}

	/**
	 * NOT IMPLEMENTED
	 * 
	 * Gets the units for this dimension by id
	 *
	 * @param dimId
	 *            the dimension id
	 * @return the units for this dimension
	 */
	// public List<Object> getDimensionUnits(String dimId) {
	//
	// }

	/**
	 * Gets the dimensions' roles
	 *
	 * @return the dimensions' roles
	 */
	public Map<String, Object> getDimensionsRoles() {
		return (Map<String, Object>) ds.getDimension().get("role");
	}

	/**
	 * Gets the role for this dimension by index
	 *
	 * @param dimIndex
	 *            the dimension index
	 * @return the role for this dimension
	 */
	public String getDimensionRole(int dimIndex) {
		String dimId = getDimensionsIds().get(dimIndex);
		return getDimensionRole(dimId);
	}

	/**
	 * Gets the role for this dimension by id
	 *
	 * @param dimId
	 *            the dimension id
	 * @return the role for this dimension
	 */
	public String getDimensionRole(String dimId) {
		String returnRole = null;
		Map<String, Object> roles = getDimensionsRoles();
		for (Object role : roles.keySet()) {
			List<String> dimensions = (List<String>) roles.get(role);
			for (String currentDimId : dimensions) {
				if (dimId.equals(currentDimId)) {
					returnRole = role.toString();
				}
			}
		}
		return returnRole;
	}

	/**
	 * Gets the cells as a Map.
	 * 
	 * Each {@link ssb.Cell Cell} is accessed via its unique n-tuple.
	 * 
	 * Example for a dataset with 3 dimensions:
	 * 
	 * the n-tuple [0,7,1] gets the {@link ssb.Cell Cell} for the 1st category
	 * in the 1st dimension, the 8th category in the 2nd dimension and the 2nd
	 * category in the 3rd dimension.
	 *
	 * @see ssb.Cell
	 *
	 * @return the cells as a Map
	 */
	public Map<List<Integer>, Cell> cells() {
		return cells;
	}

	/*
	 **************************************************************************
	 * public methods
	 **************************************************************************
	 */

	/**
	 * gets a table as a List for display purposes / default filtering
	 *
	 * @param rowDimIndex
	 *            the index of the dimension for the rows
	 * @param colDimIndex
	 *            the index of the dimension for the columns
	 * 
	 *            <br>
	 *            <br>
	 *            In this overload no filters are passed as arguments. As a
	 *            result the 1st categories of the dimensions that are not rows
	 *            nor columns are taken by default.
	 * 
	 * @return a table as a List
	 * @throws SSBDatasetException
	 *             the SSB dataset exception
	 */
	public List<List<Object>> table(int rowDimIndex, int colDimIndex) throws SSBDatasetException {
		return table(rowDimIndex, colDimIndex, new HashMap<Integer, Integer>());
	}

	/**
	 * gets a table as a List for display purposes / custom filtering
	 *
	 * @param rowDimIndex
	 *            the index of the dimension for the rows
	 * @param colDimIndex
	 *            the index of the dimension for the columns
	 * @param filters
	 *            a Map describing filters. Key is the index of the dimension to
	 *            be used as filter, value is the index of the category.
	 * 
	 *            <br>
	 *            <br>
	 *            Example for a dataset with 3 dimensions: <br>
	 *            Map&lt;Integer,Integer&gt; filters = new HashMap&lt;&gt;();
	 *            <br>
	 *            filters.put(2, 7); <br>
	 *            table(0, 1, filters); <br>
	 *            This creates a table with the 1st dimension as rows, the 2nd
	 *            dimension as columns and the 8th category of the 3rd dimension
	 *            as filter.
	 * 
	 * @return a table as a List
	 * @throws SSBDatasetException
	 *             the SSB dataset exception
	 * 
	 * 
	 */
	public List<List<Object>> table(int rowDimIndex, int colDimIndex, Map<Integer, Integer> filters)
			throws SSBDatasetException {

		verifyArguments(rowDimIndex, colDimIndex, filters);

		List<List<Object>> table = new ArrayList<>();
		List<Integer> selectionList = Collections.nCopies(getDimensionsSizes().size(), 0).stream()
				.collect(Collectors.toList());
		// selectionList.set(rowDimIndex, rowIndex);

		int rowDimSize = getDimensionsSizes().get(rowDimIndex);
		int colDimSize = getDimensionsSizes().get(colDimIndex);

		// first row/header
		List<Object> tableHeader = new ArrayList<>();
		tableHeader.add("");
		for (int i = 0; i < colDimSize; i++) {
			selectionList.set(colDimIndex, i);
			tableHeader.add(cells.get(selectionList).getLabels().get(colDimIndex));
		}
		table.add(tableHeader);

		// next rows
		NumberFormat numberFormat = NumberFormat.getInstance();
		selectionList = Collections.nCopies(getDimensionsSizes().size(), 0).stream().collect(Collectors.toList());

		// set filters
		for (Integer filterIndex : filters.keySet()) {
			selectionList.set(filterIndex, filters.get(filterIndex));
		}

		for (int i = 0; i < rowDimSize; i++) {
			selectionList.set(rowDimIndex, i);
			List<Object> tableRow = new ArrayList<>();
			tableRow.add(cells.get(selectionList).getLabels().get(rowDimIndex));
			for (int j = 0; j < colDimSize; j++) {
				selectionList.set(colDimIndex, j);

				Object value = cells.get(selectionList).getValue();
				if (value instanceof Number) {
					tableRow.add(numberFormat.format(value));
				} else {
					tableRow.add(value);
				}

			}
			table.add(tableRow);
		}

		return table;
	}

	/*
	 **************************************************************************
	 * contructor string
	 **************************************************************************
	 */

	/**
	 * Instantiates a new SSB dataset.
	 *
	 * @param datasetString
	 *            the dataset string
	 * @throws SSBDatasetException
	 *             the SSB dataset exception
	 */
	public SSBDataset(String datasetString) throws SSBDatasetException {
		mapper = new ObjectMapper();

		try {
			dsNode = mapper.readValue(datasetString, JsonNode.class).get("dataset");
			if (dsNode == null) {
				throw new SSBDatasetException("SSBDataset constructor: Could not read from field 'dataset'");
			}
		} catch (Exception e) {
			throw new SSBDatasetException("SSBDataset constructor: Could not retrieve dataset from: " + datasetString,
					e);
		}

		createDataset();
		createNtuples();
		createCells();
	}

	/**
	 * Instantiates a new SSB dataset.
	 *
	 * @param datasetUrl
	 *            the dataset url
	 * @throws SSBDatasetException
	 *             the SSB dataset exception
	 */
	/*
	 **************************************************************************
	 * contructor URL
	 **************************************************************************
	 */
	public SSBDataset(URL datasetUrl) throws SSBDatasetException {
		mapper = new ObjectMapper();

		try {
			dsNode = mapper.readValue(datasetUrl, JsonNode.class).get("dataset");
			if (dsNode == null) {
				throw new SSBDatasetException("SSBDataset constructor: Could not read from field 'dataset'");
			}
		} catch (Exception e) {
			throw new SSBDatasetException("SSBDataset constructor: Could not retrieve dataset from: " + datasetUrl, e);
		}

		createDataset();
		createNtuples();
		createCells();
	}

	/**
	 * Gets the value.
	 *
	 * @param ntuple
	 *            the ntuple
	 * @return the value
	 */
	/*
	 **************************************************************************
	 * package private methods
	 **************************************************************************
	 */
	Object getValue(List<Integer> ntuple) {
		int index = ntupleToIndex(ntuple);
		return ds.getValue().get(index);
	}

	/**
	 * Gets the ds.
	 *
	 * @return the ds
	 */
	Dataset getDs() {
		return ds;
	}

	/**
	 * Ntuple to index.
	 *
	 * @param ntuple
	 *            the ntuple
	 * @return the int
	 */
	int ntupleToIndex(List<Integer> ntuple) {
		int index = 0;
		int mult = 1;
		int nDims = getDimensionsSizes().size();
		for (int i = 0; i < nDims; i++) {
			mult *= (i > 0) ? getDimensionsSizes().get(nDims - i) : 1;
			index += mult * ntuple.get(nDims - i - 1);
		}
		return index;
	}

	/**
	 * Gets the status.
	 *
	 * @param ntuple
	 *            the ntuple
	 * @return the status
	 */
	Object getStatus(List<Integer> ntuple) {
		int index = ntupleToIndex(ntuple);
		String key = String.valueOf(index);
		return ds.getStatus().get(key);
	}

	/*
	 **************************************************************************
	 * private methods
	 **************************************************************************
	 */

	/**
	 * Creates the dataset.
	 */
	private void createDataset() {
		ds = mapper.convertValue(dsNode, Dataset.class);
	}

	// Build n-tuples representing all possible combinations of the categories
	// within each dimension.
	// For each dimension create a set of all categories.
	// Create a list of all theses sets .
	/**
	 * Creates the ntuples.
	 */
	// Get the cartesian product.
	private void createNtuples() {
		List<Set<Integer>> list = new ArrayList<>();

		for (int dimSize : getDimensionsSizes()) {
			IntStream iStream = IntStream.range(0, dimSize);
			Set<Integer> set = iStream.boxed().collect(Collectors.toSet());
			list.add(set);
		}

		ntuples = Sets.cartesianProduct(list);
	}

	// for each n-tuple create cell object containing label and value
	/**
	 * Creates the cells.
	 */
	// put all cells object in a map with n-tuple as key
	private void createCells() {
		cells = new HashMap<>();
		for (List<Integer> ntuple : ntuples) {
			List<Object> labels = new ArrayList<>();
			for (int dimIndex = 0; dimIndex < ntuple.size(); dimIndex++) {
				int catIndex = ntuple.get(dimIndex);
				String dimId = getDimensionId(dimIndex);
				labels.add(getDimensionCategoryLabels(dimId).get(catIndex));
			}

			Object value = getValue(ntuple) != null ? getValue(ntuple) : getStatus(ntuple);
			cells.put(ntuple, new Cell(labels, value));
		}
	}

	/**
	 * Verify arguments.
	 *
	 * @param rowDimIndex
	 *            the row dim index
	 * @param colDimIndex
	 *            the col dim index
	 * @param filters
	 *            the filters
	 * @throws SSBDatasetException
	 *             the SSB dataset exception
	 */
	private void verifyArguments(int rowDimIndex, int colDimIndex, Map<Integer, Integer> filters)
			throws SSBDatasetException {
		// dimensions for rows and columns wihtin boundaries
		if (rowDimIndex < 0 || colDimIndex < 0) {
			throw new SSBDatasetException(
					"Arguments for table: dimensions indexes for rows and columns must be positive");
		}
		// dimensions for rows and columns wihtin boundaries
		if (rowDimIndex >= getDimensionsSizes().size() || colDimIndex >= getDimensionsSizes().size()) {
			throw new SSBDatasetException(
					"Arguments for table: dimensions indexes for rows and columns must be less than number of dimensions");
		}
		// dimensions for rows and columns must be different
		if (rowDimIndex == colDimIndex) {
			throw new SSBDatasetException(
					"Arguments for table: dimensions indexes for rows and columns must be different");
		}

		// filters
		for (Integer filterIndex : filters.keySet()) {
			if (filterIndex == rowDimIndex || filterIndex == colDimIndex) {
				throw new SSBDatasetException(
						"Arguments for table: wrong filter argument, cannot be same as dimension for rows or columns");
			}
			int catIndex = filters.get(filterIndex);
			if (catIndex < 0) {
				throw new SSBDatasetException("Arguments for table: wrong filter argument, no such category");
			}
			if (catIndex >= getDimensionCategoryLabels(filterIndex).size()) {
				throw new SSBDatasetException("Arguments for table: wrong filter argument, no such category");
			}
		}

	}

}
