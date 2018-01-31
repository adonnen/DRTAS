package demos.sched.edf;


import algo.sched.*;
import algo.sched.Essence;
import algo.sched.hard.EDF;
import ds.PeriodicTask;
import ds.PeriodicTaskSet;
import ds.Schedule;

public class EDFdemo3 {
	public static void main(String[] args) {
		PeriodicTaskSet pts = new PeriodicTaskSet();
		try {
			pts.addPTask(new PeriodicTask("1", 18, 0, 48, 0, 48, 1));
			pts.addPTask(new PeriodicTask("2", 2, 0, 6, 0, 6, 2));
			pts.addPTask(new PeriodicTask("3", 1, 0, 4, 0, 4, 3));
		} catch (Exception e) {		
		}
		
		Schedule edfSchedule = new Schedule();
		try {
			edfSchedule = Essence.schedule(pts, 0, 50, true, jobList -> EDF.hasHighestPriority(jobList));
		} catch (Exception e) {
		}
		
		System.out.println(edfSchedule);
		System.out.println(Essence.normalizeSchedule(edfSchedule));
				
	}
}
