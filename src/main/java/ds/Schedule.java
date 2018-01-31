package ds;
import java.util.*;

public class Schedule {
	
	private ArrayList<ScheduleUnit> sched;
	
	public Schedule() {
		sched = new ArrayList<ScheduleUnit>();
	}
	
	public ArrayList<ScheduleUnit> getSched() {
		return sched;
	}

	public void add(Job j, long startTime, long endTime, ArrayList<Resource> resources) {
		sched.add(new ScheduleUnit(j, startTime, endTime, resources));
	}
	
	public void add(ScheduleUnit su) {
		sched.add(su);
	}
	
	public boolean isValid() { 
		/*
		 * The schedule is valid if for all assignments:
		 * - starting time is less or equal than end time
		 * - end time of the one is less or equal then the start time of the next one (ordering)
		 * - the job is not scheduled before it is released
		 * - the job is not scheduled after its deadline
		 */
		if (sched.isEmpty()) return true;
		
		ScheduleUnit []schedArray = new ScheduleUnit[sched.size()];
		schedArray = this.getSched().toArray(schedArray);
		
		for (int i = 0; i < this.getSched().size()-1; ++i)
			if (schedArray[i].endTime > schedArray[i+1].startTime
					|| schedArray[i].startTime > schedArray[i].endTime
					|| schedArray[i].job.getReleaseTime() > schedArray[i].startTime 
					|| schedArray[i].job.getAbsoluteDeadline() < schedArray[i].endTime ) 
				return false;
		
		return (schedArray[getSched().size()-1].startTime <= schedArray[getSched().size()-1].endTime);
	}
	
	public long howManyContextSwitches() {
		return sched.size();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Schedule : \n");
		for (ScheduleUnit s : this.getSched())
			sb.append(s.toString());
		
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sched == null) ? 0 : sched.hashCode());
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
		Schedule other = (Schedule) obj;
		if (sched == null) {
			if (other.getSched() != null)
				return false;
		} else if (sched.size() != other.sched.size())
			return false;
		else if (!sched.equals(other.getSched()))
			return false;
		else {
			for (ScheduleUnit su : sched) {
				if (!su.equals(other.getSched().get(sched.indexOf(su)))) return false;
			}
		}
		return true;
	}
	
	

}
