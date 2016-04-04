package ssb;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonGetter;

class Dataset {

	private String label;
	private String source;
	private Date updated;
	private List<Object> value;
	private Map<String, Object> status;
	private Map<String, Object> dimension;

	@JsonGetter("label")
	String getLabel() {
		return label;
	}

	@JsonGetter("source")
	String getSource() {
		return source;
	}

	@JsonGetter("updated")
	Date getUpdated() {
		return updated;
	}

	@JsonGetter("value")
	List<Object> getValue() {
		return value;
	}

	@JsonGetter("status")
	Map<String, Object> getStatus() {
		return status;
	}

	@JsonGetter("dimension")
	Map<String, Object> getDimension() {
		return dimension;
	}

}