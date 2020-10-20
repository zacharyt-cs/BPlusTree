import java.util.Comparator;

public class Records {
	String tconstant;
	float averagerating;
	int numofvote;
	DiskRecord diskRecord;
	byte[] header = new byte[4];
    
    Records(String constant, float avg, int vote){
		diskRecord = new DiskRecord();
		this.tconstant=constant;
		this.averagerating=avg;
		this.numofvote=vote;
		this.diskRecord.setStorage(this);
	}

	public double getAveragerating() {
		return averagerating;
	}
	public void setAveragerating(float averagerating) {
		this.averagerating = averagerating;
	}
	public int getNumofvote() {
		return numofvote;
	}
	public void setNumofvote(int numofvote) {
		this.numofvote = numofvote;
	}
	public void setTconstant(String tconstant) {
		this.tconstant = tconstant;
	}
	
	public static Comparator<Records> AvgRatingCompare = new Comparator<Records>()
	{
		public int compare(Records s1, Records s2)
		{
			return Float.compare(s1.averagerating, s2.averagerating);
		}
	};
}

