package ssb;

import static org.junit.Assert.assertEquals;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

public class Test_Table {

	private static SSBDataset ds1052;
	private static final String ds1052Test = "{\"dataset\":{\"status\":{\"24\":\"..\",\"25\":\"..\"},\"dimension\":{\"Kjonn\":{\"label\":\"sex\",\"category\":{\"index\":{\"0\":0},\"label\":{\"0\":\"Both sexes\"}}},\"Alder\":{\"label\":\"age\",\"category\":{\"index\":{\"15-74\":0},\"label\":{\"15-74\":\"15-74 years\"}}},\"Tid\":{\"label\":\"time\",\"category\":{\"index\":{\"2015M01\":0,\"2015M02\":1,\"2015M03\":2,\"2015M04\":3,\"2015M05\":4,\"2015M06\":5,\"2015M07\":6,\"2015M08\":7,\"2015M09\":8,\"2015M10\":9,\"2015M11\":10,\"2015M12\":11,\"2016M01\":12},\"label\":{\"2015M01\":\"2015M01\",\"2015M02\":\"2015M02\",\"2015M03\":\"2015M03\",\"2015M04\":\"2015M04\",\"2015M05\":\"2015M05\",\"2015M06\":\"2015M06\",\"2015M07\":\"2015M07\",\"2015M08\":\"2015M08\",\"2015M09\":\"2015M09\",\"2015M10\":\"2015M10\",\"2015M11\":\"2015M11\",\"2015M12\":\"2015M12\",\"2016M01\":\"2016M01\"}}},\"ContentsCode\":{\"label\":\"contents\",\"category\":{\"index\":{\"Arbeidslause2\":0,\"Arbeidslause4\":1},\"label\":{\"Arbeidslause2\":\"Unemployment (LFS) (1 000 persons), seasonally adjusted\",\"Arbeidslause4\":\"Unemployment rate (LFS), seasonally adjusted\"},\"unit\":{\"Arbeidslause2\":{\"base\":\"1 000 persons\"},\"Arbeidslause4\":{\"base\":\"per cent\"}}}},\"id\":[\"Kjonn\",\"Alder\",\"Tid\",\"ContentsCode\"],\"size\":[1,1,13,2],\"role\":{\"time\":[\"Tid\"],\"metric\":[\"ContentsCode\"]}},\"label\":\"Employment and unemployment for persons aged 15-74, by sex, age, time and contents\",\"source\":\"Statistics Norway\",\"updated\":\"2016-03-14T10:19:18Z\",\"value\":[110,4,114,4.1,115,4.2,117,4.2,118,4.3,123,4.4,120,4.3,127,4.6,128,4.6,129,4.6,127,4.6,126,4.5,null,null]}}";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ds1052 = new SSBDataset(ds1052Test);
	}

	@Test
	public void testTableGeneral() throws SSBDatasetException {
		List<List<Object>> table = ds1052.table(2, 3);

		// number of rows
		// 1st row for table header
		int expectedNumRows = ds1052.getDimensionsSizes().get(2) + 1;
		assertEquals(expectedNumRows, table.size());

		// number of cols
		// 1st col for row name
		int expectedNumCols = ds1052.getDimensionsSizes().get(3) + 1;
		for (List<Object> row : table) {
			assertEquals(expectedNumCols, row.size());
		}
	}

	@Test
	public void testTableFilterAndNoFilter() throws SSBDatasetException {
		List<List<Object>> table;
		Map<Integer, Integer> filters = new HashMap<Integer, Integer>();

		filters.put(2, 0); // same as default
		assertEquals(ds1052.table(0, 1), ds1052.table(0, 1, filters));

		filters.put(3, 0); // same as default
		assertEquals(ds1052.table(0, 1), ds1052.table(0, 1, filters));
	}

	@Test
	public void testTableValues() throws SSBDatasetException {
		List<List<Object>> table;
		Map<Integer, Integer> filters = new HashMap<Integer, Integer>();
		List<Integer> ntuple;
		NumberFormat numberFormat = NumberFormat.getInstance();
		Object expectedValue;

		// test 1st value in dataset
		ntuple = Arrays.asList(0, 0, 0, 0);

		if (ds1052.cells().get(ntuple).getValue() instanceof Number) {
			expectedValue = numberFormat.format(ds1052.cells().get(ntuple).getValue());
		} else {
			expectedValue = ds1052.cells().get(ntuple).getValue();
		}

		table = ds1052.table(0, 1);
		assertEquals(expectedValue, table.get(1).get(1));

		// test last value in dataset (null / ..)
		ntuple = Arrays.asList(0, 0, 12, 1);
		if (ds1052.cells().get(ntuple).getValue() instanceof Number) {
			expectedValue = numberFormat.format(ds1052.cells().get(ntuple).getValue());
		} else {
			expectedValue = ds1052.cells().get(ntuple).getValue();
		}
		// last categories for dimensions used as filters
		filters.put(2, 12);
		filters.put(3, 1);
		table = ds1052.table(0, 1, filters);
		assertEquals(expectedValue, table.get(1).get(1));
	}

	@Test(expected = SSBDatasetException.class)
	public void testTableNoFilterNegativeRow() throws SSBDatasetException {
		// dimensions for rows and columns outside boundaries
		List<List<Object>> table = ds1052.table(-1, 0);
	}

	@Test(expected = SSBDatasetException.class)
	public void testTableNoFilterNegativeCol() throws SSBDatasetException {
		// dimensions for rows and columns outside boundaries
		List<List<Object>> table = ds1052.table(0, -1);
	}

	@Test(expected = SSBDatasetException.class)
	public void testTableNoFilterRowNotExists() throws SSBDatasetException {
		// dimensions for rows and columns outside boundaries
		List<List<Object>> table = ds1052.table(99, 0);
	}

	@Test(expected = SSBDatasetException.class)
	public void testTableNoFilterColNotExists() throws SSBDatasetException {
		// dimensions for rows and columns outside boundaries
		List<List<Object>> table = ds1052.table(0, 99);
	}

	@Test(expected = SSBDatasetException.class)
	public void testTableNoFilterSameRowCol() throws SSBDatasetException {
		// dimensions for rows and columns outside boundaries
		List<List<Object>> table = ds1052.table(1, 1);
	}

	@Test(expected = SSBDatasetException.class)
	public void testTableFilterSameAsRow() throws SSBDatasetException {
		Map<Integer, Integer> filters = new HashMap<>();
		// wrong filter argument, cannot be same as dimension for rows or
		// columns
		filters.put(1, 0);
		List<List<Object>> table = ds1052.table(1, 2, filters);
	}

	@Test(expected = SSBDatasetException.class)
	public void testTableFilterSameAsCol() throws SSBDatasetException {
		Map<Integer, Integer> filters = new HashMap<>();
		// wrong filter argument, cannot be same as dimension for rows or
		// columns
		filters.put(2, 0);
		List<List<Object>> table = ds1052.table(1, 2, filters);
	}

	@Test(expected = SSBDatasetException.class)
	public void testTableFilterCatNegative() throws SSBDatasetException {
		Map<Integer, Integer> filters = new HashMap<>();
		// wrong filter argument, no such category
		filters.put(2, -1);
		List<List<Object>> table = ds1052.table(0, 1, filters);
	}

	@Test(expected = SSBDatasetException.class)
	public void testTableFilterCatNotExists() throws SSBDatasetException {
		Map<Integer, Integer> filters = new HashMap<>();
		// wrong filter argument, no such category
		filters.put(2, 99);
		List<List<Object>> table = ds1052.table(0, 1, filters);
	}
}