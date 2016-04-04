package ssb;

import static org.junit.Assert.assertEquals;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Test_Dataset {

	private static SSBDataset ds;
	private static final String ds1052Test = "{\"dataset\":{\"status\":{\"24\":\"..\",\"25\":\"..\"},\"dimension\":{\"Kjonn\":{\"label\":\"sex\",\"category\":{\"index\":{\"0\":0},\"label\":{\"0\":\"Both sexes\"}}},\"Alder\":{\"label\":\"age\",\"category\":{\"index\":{\"15-74\":0},\"label\":{\"15-74\":\"15-74 years\"}}},\"Tid\":{\"label\":\"time\",\"category\":{\"index\":{\"2015M01\":0,\"2015M02\":1,\"2015M03\":2,\"2015M04\":3,\"2015M05\":4,\"2015M06\":5,\"2015M07\":6,\"2015M08\":7,\"2015M09\":8,\"2015M10\":9,\"2015M11\":10,\"2015M12\":11,\"2016M01\":12},\"label\":{\"2015M01\":\"2015M01\",\"2015M02\":\"2015M02\",\"2015M03\":\"2015M03\",\"2015M04\":\"2015M04\",\"2015M05\":\"2015M05\",\"2015M06\":\"2015M06\",\"2015M07\":\"2015M07\",\"2015M08\":\"2015M08\",\"2015M09\":\"2015M09\",\"2015M10\":\"2015M10\",\"2015M11\":\"2015M11\",\"2015M12\":\"2015M12\",\"2016M01\":\"2016M01\"}}},\"ContentsCode\":{\"label\":\"contents\",\"category\":{\"index\":{\"Arbeidslause2\":0,\"Arbeidslause4\":1},\"label\":{\"Arbeidslause2\":\"Unemployment (LFS) (1 000 persons), seasonally adjusted\",\"Arbeidslause4\":\"Unemployment rate (LFS), seasonally adjusted\"},\"unit\":{\"Arbeidslause2\":{\"base\":\"1 000 persons\"},\"Arbeidslause4\":{\"base\":\"per cent\"}}}},\"id\":[\"Kjonn\",\"Alder\",\"Tid\",\"ContentsCode\"],\"size\":[1,1,13,2],\"role\":{\"time\":[\"Tid\"],\"metric\":[\"ContentsCode\"]}},\"label\":\"Employment and unemployment for persons aged 15-74, by sex, age, time and contents\",\"source\":\"Statistics Norway\",\"updated\":\"2016-03-14T10:19:18Z\",\"value\":[110,4,114,4.1,115,4.2,117,4.2,118,4.3,123,4.4,120,4.3,127,4.6,128,4.6,129,4.6,127,4.6,126,4.5,null,null]}}";
	private static Dataset dataset;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ds = new SSBDataset(ds1052Test);
		dataset = ds.getDs();
	}

	@Test
	public void testGetLabel() {
		String expected = "Employment and unemployment for persons aged 15-74, by sex, age, time and contents";
		assertEquals(expected, dataset.getLabel());
	}

	@Test
	public void testGetSource() {
		String expected = "Statistics Norway";
		assertEquals(expected, dataset.getSource());
	}

	@Test
	public void testGetUpdated() throws Exception {
		String expectedString = "2016-03-14T10:19:18Z";
		Instant expectedInstant = Instant.parse(expectedString);
		assertEquals(expectedInstant, dataset.getUpdated().toInstant());
	}

	@Test
	public void testGetValue() throws Exception {
		int expectedSize = 26;
		assertEquals(expectedSize, dataset.getValue().size());

		String value = "[110,4,114,4.1,115,4.2,117,4.2,118,4.3,123,4.4,120,4.3,127,4.6,128,4.6,129,4.6,127,4.6,126,4.5,null,null]";
		List<Integer> expectedValue = new ObjectMapper().readValue(value, List.class);
		assertEquals(expectedValue, dataset.getValue());
	}

	@Test
	public void testGetStatus() throws Exception {
		int expectedSize = 2;
		assertEquals(expectedSize, dataset.getStatus().size());

		String status = "{\"24\":\"..\",\"25\":\"..\"}";
		Map<String, Object> expectedStatus = new ObjectMapper().readValue(status, Map.class);
		assertEquals(expectedStatus, dataset.getStatus());

	}

	@Test
	public void testGetDimension() throws Exception {
		int expectedSize = 7;
		assertEquals(expectedSize, dataset.getDimension().size());

		String dimension = "{\"Kjonn\":{\"label\":\"sex\",\"category\":{\"index\":{\"0\":0},\"label\":{\"0\":\"Both sexes\"}}},\"Alder\":{\"label\":\"age\",\"category\":{\"index\":{\"15-74\":0},\"label\":{\"15-74\":\"15-74 years\"}}},\"Tid\":{\"label\":\"time\",\"category\":{\"index\":{\"2015M01\":0,\"2015M02\":1,\"2015M03\":2,\"2015M04\":3,\"2015M05\":4,\"2015M06\":5,\"2015M07\":6,\"2015M08\":7,\"2015M09\":8,\"2015M10\":9,\"2015M11\":10,\"2015M12\":11,\"2016M01\":12},\"label\":{\"2015M01\":\"2015M01\",\"2015M02\":\"2015M02\",\"2015M03\":\"2015M03\",\"2015M04\":\"2015M04\",\"2015M05\":\"2015M05\",\"2015M06\":\"2015M06\",\"2015M07\":\"2015M07\",\"2015M08\":\"2015M08\",\"2015M09\":\"2015M09\",\"2015M10\":\"2015M10\",\"2015M11\":\"2015M11\",\"2015M12\":\"2015M12\",\"2016M01\":\"2016M01\"}}},\"ContentsCode\":{\"label\":\"contents\",\"category\":{\"index\":{\"Arbeidslause2\":0,\"Arbeidslause4\":1},\"label\":{\"Arbeidslause2\":\"Unemployment (LFS) (1 000 persons), seasonally adjusted\",\"Arbeidslause4\":\"Unemployment rate (LFS), seasonally adjusted\"},\"unit\":{\"Arbeidslause2\":{\"base\":\"1 000 persons\"},\"Arbeidslause4\":{\"base\":\"per cent\"}}}},\"id\":[\"Kjonn\",\"Alder\",\"Tid\",\"ContentsCode\"],\"size\":[1,1,13,2],\"role\":{\"time\":[\"Tid\"],\"metric\":[\"ContentsCode\"]}}";
		Map<String, Object> expectedDimension = new ObjectMapper().readValue(dimension, Map.class);
		assertEquals(expectedDimension, dataset.getDimension());
	}

}
