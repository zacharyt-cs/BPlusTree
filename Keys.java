import java.util.ArrayList;
import java.util.List;

public class Keys {
	float key;

	List<Records> values;

	// Instantiates a new key and its value.
	public Keys(float key,  Records value) {
		this.key = key;
		if (null == this.values) {
			values = new ArrayList<>();
		}
		this.values.add(value);
	}
	
	// Instantiates a new key
	public Keys(float key) {
		this.key = key;
		this.values = new ArrayList<>();
	}

	// Gets the key.
	public float getKey() {
		return key;
	}

	// Sets the key.
	public void setKey(float key) {
		this.key = key;
	}

	// Gets the values.
	public List<Records> getValues() {
		return values;
	}

	public void setValues(List<Records> values) {
		this.values = values;
	}

	public String toString() {
		return "Key [key=" + key + ", values=" + values + "]";
	}
}
