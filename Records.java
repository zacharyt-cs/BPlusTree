public class Records {
	String tconstant;
	float averagerating;
	int numofvote;
    
    Records(String constant, float avg, int vote){
		this.tconstant=constant;
		this.averagerating=avg;
		this.numofvote=vote;
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
}

