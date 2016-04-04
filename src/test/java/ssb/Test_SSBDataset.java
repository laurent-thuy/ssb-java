package ssb;

import static org.junit.Assert.assertEquals;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Test_SSBDataset {

	private static SSBDataset ds1052;
	private static final String ds1052Test = "{\"dataset\":{\"status\":{\"24\":\"..\",\"25\":\"..\"},\"dimension\":{\"Kjonn\":{\"label\":\"sex\",\"category\":{\"index\":{\"0\":0},\"label\":{\"0\":\"Both sexes\"}}},\"Alder\":{\"label\":\"age\",\"category\":{\"index\":{\"15-74\":0},\"label\":{\"15-74\":\"15-74 years\"}}},\"Tid\":{\"label\":\"time\",\"category\":{\"index\":{\"2015M01\":0,\"2015M02\":1,\"2015M03\":2,\"2015M04\":3,\"2015M05\":4,\"2015M06\":5,\"2015M07\":6,\"2015M08\":7,\"2015M09\":8,\"2015M10\":9,\"2015M11\":10,\"2015M12\":11,\"2016M01\":12},\"label\":{\"2015M01\":\"2015M01\",\"2015M02\":\"2015M02\",\"2015M03\":\"2015M03\",\"2015M04\":\"2015M04\",\"2015M05\":\"2015M05\",\"2015M06\":\"2015M06\",\"2015M07\":\"2015M07\",\"2015M08\":\"2015M08\",\"2015M09\":\"2015M09\",\"2015M10\":\"2015M10\",\"2015M11\":\"2015M11\",\"2015M12\":\"2015M12\",\"2016M01\":\"2016M01\"}}},\"ContentsCode\":{\"label\":\"contents\",\"category\":{\"index\":{\"Arbeidslause2\":0,\"Arbeidslause4\":1},\"label\":{\"Arbeidslause2\":\"Unemployment (LFS) (1 000 persons), seasonally adjusted\",\"Arbeidslause4\":\"Unemployment rate (LFS), seasonally adjusted\"},\"unit\":{\"Arbeidslause2\":{\"base\":\"1 000 persons\"},\"Arbeidslause4\":{\"base\":\"per cent\"}}}},\"id\":[\"Kjonn\",\"Alder\",\"Tid\",\"ContentsCode\"],\"size\":[1,1,13,2],\"role\":{\"time\":[\"Tid\"],\"metric\":[\"ContentsCode\"]}},\"label\":\"Employment and unemployment for persons aged 15-74, by sex, age, time and contents\",\"source\":\"Statistics Norway\",\"updated\":\"2016-03-14T10:19:18Z\",\"value\":[110,4,114,4.1,115,4.2,117,4.2,118,4.3,123,4.4,120,4.3,127,4.6,128,4.6,129,4.6,127,4.6,126,4.5,null,null]}}";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ds1052 = new SSBDataset(ds1052Test);
	}

	@Test
	public void testLabel() {
		String expected = "Employment and unemployment for persons aged 15-74, by sex, age, time and contents";
		assertEquals(expected, ds1052.label());
	}

	@Test
	public void testSource() {
		String expected = "Statistics Norway";
		assertEquals(expected, ds1052.source());
	}

	@Test
	public void testUpdated() {
		String expectedString = "2016-03-14T10:19:18Z";
		Instant expectedInstant = Instant.parse(expectedString);
		assertEquals(expectedInstant, ds1052.updated().toInstant());
	}

	@Test
	public void testGetDimensionsSizes() {
		List<Integer> expected = Arrays.asList(1, 1, 13, 2);
		assertEquals(expected, ds1052.getDimensionsSizes());
	}

	@Test
	public void testGetDimensionsIds() {
		List<String> expected = Arrays.asList("Kjonn", "Alder", "Tid", "ContentsCode");
		assertEquals(expected, ds1052.getDimensionsIds());
	}

	@Test
	public void testGetDimensionsLabels() {
		List<String> expected = Arrays.asList("sex", "age", "time", "contents");
		assertEquals(expected, ds1052.getDimensionsLabels());
	}

	@Test
	public void testGetDimensionString() throws Exception {
		String Tid = "{\"label\":\"time\",\"category\":{\"index\":{\"2015M01\":0,\"2015M02\":1,\"2015M03\":2,\"2015M04\":3,\"2015M05\":4,\"2015M06\":5,\"2015M07\":6,\"2015M08\":7,\"2015M09\":8,\"2015M10\":9,\"2015M11\":10,\"2015M12\":11,\"2016M01\":12},\"label\":{\"2015M01\":\"2015M01\",\"2015M02\":\"2015M02\",\"2015M03\":\"2015M03\",\"2015M04\":\"2015M04\",\"2015M05\":\"2015M05\",\"2015M06\":\"2015M06\",\"2015M07\":\"2015M07\",\"2015M08\":\"2015M08\",\"2015M09\":\"2015M09\",\"2015M10\":\"2015M10\",\"2015M11\":\"2015M11\",\"2015M12\":\"2015M12\",\"2016M01\":\"2016M01\"}}}";
		Map<String, Object> expectedTid = new ObjectMapper().readValue(Tid, Map.class);
		assertEquals(expectedTid, ds1052.getDimension("Tid"));
	}

	@Test
	public void testGetDimensionInt() throws Exception {
		// index of dimension 'Tid' is 2
		String Tid = "{\"label\":\"time\",\"category\":{\"index\":{\"2015M01\":0,\"2015M02\":1,\"2015M03\":2,\"2015M04\":3,\"2015M05\":4,\"2015M06\":5,\"2015M07\":6,\"2015M08\":7,\"2015M09\":8,\"2015M10\":9,\"2015M11\":10,\"2015M12\":11,\"2016M01\":12},\"label\":{\"2015M01\":\"2015M01\",\"2015M02\":\"2015M02\",\"2015M03\":\"2015M03\",\"2015M04\":\"2015M04\",\"2015M05\":\"2015M05\",\"2015M06\":\"2015M06\",\"2015M07\":\"2015M07\",\"2015M08\":\"2015M08\",\"2015M09\":\"2015M09\",\"2015M10\":\"2015M10\",\"2015M11\":\"2015M11\",\"2015M12\":\"2015M12\",\"2016M01\":\"2016M01\"}}}";
		Map<String, Object> expectedTid = new ObjectMapper().readValue(Tid, Map.class);
		assertEquals(expectedTid, ds1052.getDimension(2));
	}

	@Test
	public void testGetDimensionId() {
		// index of dimension 'Alder' is 1
		String expected = "Alder";
		assertEquals(expected, ds1052.getDimensionId(1));
	}

	@Test
	public void testGetDimensionIndex() {
		// index of dimension 'Kjonn' is 0
		int expected = 0;
		assertEquals(expected, ds1052.getDimensionIndex("Kjonn"));
	}

	@Test
	public void testGetDimensionLabelInt() {
		String expected = "age";
		assertEquals(expected, ds1052.getDimensionLabel(1));
	}

	@Test
	public void testGetDimensionLabelString() {
		String expected = "age";
		assertEquals(expected, ds1052.getDimensionLabel("Alder"));
	}

	@Test
	public void testGetDimensionCategoryLabelsString() {
		List<Object> expected = Arrays.asList("2015M01", "2015M02", "2015M03", "2015M04", "2015M05", "2015M06",
				"2015M07", "2015M08", "2015M09", "2015M10", "2015M11", "2015M12", "2016M01");
		assertEquals(expected, ds1052.getDimensionCategoryLabels("Tid"));
	}

	@Test
	public void testGetDimensionCategoryLabelsInt() {
		List<Object> expected = Arrays.asList("Both sexes");
		assertEquals(expected, ds1052.getDimensionCategoryLabels(0));
	}

	@Test
	public void testGetDimensionsRoles() throws Exception {
		String roles = "{\"time\":[\"Tid\"],\"metric\":[\"ContentsCode\"]}";
		Map<String, Object> expectedRoles = new ObjectMapper().readValue(roles, Map.class);
		assertEquals(expectedRoles, ds1052.getDimensionsRoles());
	}

	@Test
	public void testGetDimensionRoleInt() {
		String expected = "time";
		assertEquals(expected, ds1052.getDimensionRole(2));
	}

	@Test
	public void testGetDimensionRoleString() {
		String expected = null;
		assertEquals(expected, ds1052.getDimensionRole("Alder"));

		expected = "metric";
		assertEquals(expected, ds1052.getDimensionRole("ContentsCode"));
	}

	// @Test
	/*
	 * NOT IMPLEMENTED
	 */
	// public void testGetDimensionUnits(){
	// // no units for this dim in the 1052 dataset
	// List<Object> expected = null;
	// assertEquals(expected, ds1052.getDimensionUnits("Alder"));
	//
	// // units present for this dim in the 1104 dataset
	// expected = ??;
	// System.out.println(ds1104.getDimensionUnits("ContentsCode"));
	// assertEquals(expected, ds1104.getDimensionUnits("ContentsCode"));
	// }

	@Test
	public void testCells() {
		// number of cells must be same as number of values
		assertEquals(ds1052.cells().size(), ds1052.getDs().getValue().size());

		// 26 cells in this dataset
		int expectedSize = 26;
		assertEquals(expectedSize, ds1052.cells().size());

		// testing cell #1
		Object expectedValue = 110;
		List<Integer> ntuple = Arrays.asList(0, 0, 0, 0);
		assertEquals(expectedValue, ds1052.cells().get(ntuple).getValue());

		List<String> expectedLabels = Arrays.asList("Both sexes", "15-74 years", "2015M01",
				"Unemployment (LFS) (1 000 persons), seasonally adjusted");
		assertEquals(expectedLabels, ds1052.cells().get(ntuple).getLabels());

		// testing cell #19
		expectedValue = 4.6;
		ntuple = Arrays.asList(0, 0, 9, 1);
		assertEquals(expectedValue, ds1052.cells().get(ntuple).getValue());

		expectedLabels = Arrays.asList("Both sexes", "15-74 years", "2015M10",
				"Unemployment rate (LFS), seasonally adjusted");
		assertEquals(expectedLabels, ds1052.cells().get(ntuple).getLabels());
	}

	@Test
	public void testGetValue() {
		Object expected = 110;
		List<Integer> ntuple = Arrays.asList(0, 0, 0, 0);
		assertEquals(expected, ds1052.getValue(ntuple));
	}

	@Test
	public void testGetStatus() {
		String expected = null;
		List<Integer> ntuple = Arrays.asList(0, 0, 0, 0);
		assertEquals(expected, ds1052.getStatus(ntuple));

		expected = "..";
		ntuple = Arrays.asList(0, 0, 12, 0);
		assertEquals(expected, ds1052.getStatus(ntuple));
	}

	@Test
	public void testNtupleToIndex() {
		// first index
		int expected = 0;
		List<Integer> ntuple = Arrays.asList(0, 0, 0, 0);
		assertEquals(expected, ds1052.ntupleToIndex(ntuple));

		// last index
		expected = 25;
		ntuple = Arrays.asList(0, 0, 12, 1);
		assertEquals(expected, ds1052.ntupleToIndex(ntuple));
	}
}