import java.util.ArrayList;
import java.util.List;

public class Keys {
	double key;

	/** The list of values for the key. Set only for external nodes*/
	List<Records> values;


//	  Instantiates a new key and its value.
	
	public Keys(double key,  Records value) {
		this.key = key;
		if (null == this.values) {
			values = new ArrayList<>();
		}
		this.values.add(value);
	}
	
	
//	  Instantiates a new key

	public Keys(double key) {
		this.key = key;
		this.values = new ArrayList<>();
	}

	
//	  Gets the key.

	public double getKey() {
		return key;
	}


//	  Sets the key.
	
	public void setKey(double key) {
		this.key = key;
	}


//	  Gets the values.
	
	public List<Records> getValues() {
		return values;
	}

	/**
	 * Sets the values.
	 *
	 * @param values
	 *            the new values
	 */
	public void setValues(List<Records> values) {
		this.values = values;
	}

	public String toString() {
		return "Key [key=" + key + ", values=" + values + "]";
	}
	
}
