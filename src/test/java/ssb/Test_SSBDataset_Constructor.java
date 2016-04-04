package ssb;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

public class Test_SSBDataset_Constructor {

	// constructor from json string
	@Test(expected = SSBDatasetException.class)
	public void testSSBDatasetStringBadJson() throws SSBDatasetException {
		new SSBDataset("{");
	}

	@Test(expected = SSBDatasetException.class)
	public void testSSBDatasetStringNoDatasetField() throws SSBDatasetException {
		new SSBDataset("{\"foo\":89}");
	}

	// constructor from url
	@Test(expected = SSBDatasetException.class)
	// url not exists
	public void testSSBDatasetUrlBadUrl() throws MalformedURLException, SSBDatasetException {
		URL url = new URL("https://data.ssb.no/api/v0/foo/bar");
		new SSBDataset(url);
	}

	// resource is not json
	@Test(expected = SSBDatasetException.class)
	public void testSSBDatasetUrlNoJson() throws MalformedURLException, SSBDatasetException {
		URL url = new URL("http://www.google.com");
		new SSBDataset(url);
	}

	// resource is json but no dataset field
	@Test(expected = SSBDatasetException.class)
	public void testSSBDatasetUrlNoDatasetField() throws MalformedURLException, SSBDatasetException {
		URL url = new URL("http://json-stat.org/samples/oecd-canada-col.json");
		new SSBDataset(url);
	}
}