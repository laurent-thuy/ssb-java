package ssb;

import java.util.List;

/**
 * 
 * @version 1.1.1
 * @author Laurent-Thuy Soublin
 * 
 *         <br>
 *         <br>
 * 
 *         Holds a value and the corresponding labels
 */
public class Cell {

	private List<Object> labels;
	private Object value;

	/**
	 * Instantiates a new cell.
	 *
	 * @param labels
	 *            the labels for the value in this cell
	 * @param value
	 *            the value in this cell
	 */
	Cell(List<Object> labels, Object value) {
		this.labels = labels;
		this.value = value;
	}

	/**
	 * Gets the labels for the value in this cell
	 *
	 * @return the labels for the value in this cell
	 */
	public List<Object> getLabels() {
		return labels;
	}

	/**
	 * Gets the value in this cell
	 *
	 * @return the value in this cell
	 */
	public Object getValue() {
		return value;
	}

}
