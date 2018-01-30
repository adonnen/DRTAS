package demos.sched.rms;

import algo.sched.Essence;
import algo.sched.RMS;
import ds.PeriodicTask;
import ds.PeriodicTaskSet;
import ds.Schedule;

public class RMSdemo3 {
	public static void main(String[] args) {
		PeriodicTaskSet pts = new PeriodicTaskSet();
		try {
			pts.addPTask(new PeriodicTask("1", 18, 0, 48, 0, 48, 1));
			pts.addPTask(new PeriodicTask("2", 2, 0, 6, 0, 6, 2));
			pts.addPTask(new PeriodicTask("3", 1, 0, 4, 0, 4, 3));
		} catch (Exception e) {		
		}
		
		Schedule rmsSchedule = new Schedule();
		try {
			rmsSchedule = Essence.schedule(pts, 0, 50, true, jobList -> RMS.hasHighestPriority(jobList));
		} catch (Exception e) {
		}
		
		System.out.println(rmsSchedule);
		System.out.println(Essence.normalizeSchedule(rmsSchedule));
				
	}
}
