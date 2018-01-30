package ds;
import java.util.*;

public class JobSet {
	
	private ArrayList<Job> jobList;
	
	public JobSet() {
		jobList = new ArrayList<Job>();
	}

	public ArrayList<Job> getJobList() {
		return jobList;
	}
	
	public void add (Job j) {
		jobList.add(j);
	}
	
	public void remove (Job j) {
		jobList.remove(j);		
	}

}
