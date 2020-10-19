public class Records {
	String tconstant;
	double averagerating;
	int numofvote;
    
    Records(String constant, double avg, int vote){
		this.tconstant=constant;
		this.averagerating=avg;
		this.numofvote=vote;
	}

	public double getAveragerating() {
		return averagerating;
	}
	public void setAveragerating(double averagerating) {
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

