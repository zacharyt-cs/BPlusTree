import java.util.ArrayList;
import java.util.List;

//MORE CHANGES REQUIRED
public class Blocks {
	// header = 10 bytes
	public int header = 10;
	public List<Records> recordlist= new ArrayList<Records>();
	
	public void addRecord(Records data){
		recordlist.add(data);
	}
}