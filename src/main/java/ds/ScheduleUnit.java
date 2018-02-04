package ds;

import java.util.Optional;
import java.util.ArrayList;

public class ScheduleUnit {
	public Job job;
	public int startTime;
	public int endTime;
	public Optional<ArrayList<Resource>> takenResources;
	
	public ScheduleUnit() {
		this.job = new Job();
		this.startTime = 0;
		this.endTime = 0;
		this.takenResources = Optional.ofNullable(null);
	}
		
	public ScheduleUnit(Job j, int st, int et, ArrayList<Resource> resources) {
		this.job = j;
		this.startTime = st;
		this.endTime = et;
		this.takenResources = Optional.ofNullable(resources);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (endTime ^ (endTime >>> 32));
		result = prime * result + ((job == null) ? 0 : job.hashCode());
		result = prime * result + (int) (startTime ^ (startTime >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ScheduleUnit other = (ScheduleUnit) obj;
		if (endTime != other.endTime)
			return false;
		if (job == null) {
			if (other.job != null)
				return false;
		} else if (!job.equals(other.job))
			return false;
		if (startTime != other.startTime)
			return false;
		return true;
	}
	
	
	@Override
	public String toString() {
		String standard = "Job " + this.job.getTaskId() + "," + this.job.getInstanceId() + ": \t[" + this.startTime + ", " + this.endTime + "]\n";
		return (takenResources.isPresent()) ? standard + "\t " + takenResources.get().toString() : standard;
	}
	
}
