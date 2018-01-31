package demos.sched.edf;

import algo.sched.*;
import algo.sched.Essence;
import algo.sched.hard.EDF;
import ds.PeriodicTask;
import ds.PeriodicTaskSet;
import ds.Schedule;

public class EDFdemo1 {
	public static void main(String[] args) {
		PeriodicTaskSet pts = new PeriodicTaskSet();
		try {
			pts.addPTask(new PeriodicTask("1", 3, 0, 6, 0, 6, 1));
			pts.addPTask(new PeriodicTask("2", 4, 0, 10, 0, 10, 2));
		} catch (Exception e) {		
		}
		
		Schedule edfSchedule = new Schedule();
		try {
			edfSchedule = Essence.schedule(pts, 0, 60, true, jobList -> EDF.hasHighestPriority(jobList));
		} catch (Exception e) {
			System.out.println(e);
		}
		
		System.out.println(edfSchedule);
		System.out.println(Essence.normalizeSchedule(edfSchedule));
				
	}
}
