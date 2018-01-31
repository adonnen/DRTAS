package algo.resourcep;

import java.util.ArrayList;
import java.util.function.Function;

import algo.sched.*;
import algo.sched.hard.RMS;
import ds.*;
import exceptions.*;

public class NPCS {
	
	public static Schedule schedule;
	
	public static Schedule schedule(PeriodicTaskSet pts, long fromTime, long toTime, boolean onlyIfSchedulable, Function<ArrayList<Job>, Job> hasHighestPriority) 
			throws NotSchedulableException, InvalidTimeInterval, ViolatedDeadlineException {
		/*
		 * This function returns an rms schedule from fromTime to toTime, 
		 * where all of the jobs are generated from the periodic task set pts.
		 * This is no simulation, it just provides a complete schedule in one step.
		 */
		if (onlyIfSchedulable)
				if (!RMS.isSchedulable(pts)) 
					throw new NotSchedulableException("This task set is not schedulable under RMS!");
		if (fromTime < 0 || toTime < fromTime || fromTime >= toTime) 
			throw new InvalidTimeInterval("Scheduling is not possible in such a time interval!");
		
		schedule = new Schedule();	
		ArrayList<Job> totalJobList = pts.generateJobsWithRMSPriorities(fromTime, toTime);		
		totalJobList.sort((o1, o2) -> Long.compare(o1.getReleaseTime(), o2.getReleaseTime()));
//		System.out.println(totalJobList);
		
		// Initialize an active job list
		JobSet activeJobSet = new JobSet();
		long time = fromTime;
		
		while (time < toTime) {

			// new arrivals so far?
			for (Job j : totalJobList)
				if (j.getReleaseTime() <= time) {
					j.setState(STATE.READY);
					activeJobSet.add(j);
				}
			// remove them from the total list, they will not be scheduled
			for (Job j : activeJobSet.getJobList())
				totalJobList.remove(j);	
			
			// If task set was not checked to be schedulable, then there is a possibility of deadline violation during scheduling
			// Every time a new task instance arrives, the active job set is checked if there are two different instances of the same task
			if (!onlyIfSchedulable) {
				if (Essence.twoInstancesActive(activeJobSet.getJobList()))
					throw new ViolatedDeadlineException("A deadline is violated at time point " + time);
			}
			
			long nextRelease = (Essence.nextEarliestRelease(totalJobList, time) == 0) ? toTime : Essence.nextEarliestRelease(totalJobList, time);
			
			// select the active job with the highest priority
			Job currentJob = hasHighestPriority.apply(activeJobSet.getJobList());
			
			// if the current job is a dummy, the active job list is empty
			// jump to the next release
			if (currentJob.getInstanceId() == 0) {
				time += nextRelease;
				continue;
			}
			
			// if the current job can be completely scheduled before the next release, 
			// it is safe to schedule it completely and jump to the time point when its execution ends
			if (currentJob.getExecutionTime() <= nextRelease) {
				long upperLimit = time + currentJob.getExecutionTime();
				schedule.add(currentJob, time, (upperLimit > toTime) ? toTime : upperLimit, null);
				time = time + currentJob.getExecutionTime();
				activeJobSet.remove(currentJob);
			}
			else { // else schedule until the next release, reduce the remaining execution time, and then decide with newly arrived jobs
				schedule.add(currentJob, time, time + nextRelease, null);
				time += nextRelease;
				currentJob.setExecutionTime(currentJob.getExecutionTime() - nextRelease);
			}
		}
		
		return schedule;
	}

}
