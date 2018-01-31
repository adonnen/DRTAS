package demos.sched.rms;

import algo.sched.Essence;
import algo.sched.hard.RMS;
import ds.*;

public class RMSdemo1 {

	public static void main(String[] args) {
		PeriodicTaskSet pts = new PeriodicTaskSet();
		try {
			pts.addPTask(new PeriodicTask("1", 3, 0, 6, 0, 6, 1));
			pts.addPTask(new PeriodicTask("2", 4, 0, 10, 0, 10, 2));
		} catch (Exception e) {		
		}
		
		Schedule rmsSchedule = new Schedule();
		try {
			rmsSchedule = Essence.schedule(pts, 0, 60, true, jobList -> RMS.hasHighestPriority(jobList));
		} catch (Exception e) {
			System.out.println(e);
		}
		
		System.out.println(rmsSchedule);
		System.out.println(Essence.normalizeSchedule(rmsSchedule));
				
	}

}
