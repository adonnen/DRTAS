package demos.sched.rms;

import algo.sched.*;
import algo.sched.hard.RMS;
import ds.PeriodicTask;
import ds.PeriodicTaskSet;
import ds.Schedule;
import exceptions.*;

public class RMSdemoAsFarAsPossible {
	public static void main(String[] args) {
		PeriodicTaskSet pts = new PeriodicTaskSet();
		try {
			pts.addPTask(new PeriodicTask("1", 3, 0, 6, 0, 6, 1));
			pts.addPTask(new PeriodicTask("2", 5, 0, 10, 0, 10, 2));
			// pts.addPTask(new PeriodicTask("3", BigDecimal.ONE, 0, new BigDecimal("4.0"), 0, new BigDecimal("4.0"), 3));
		} catch (Exception e) {		
		}
		
//		System.out.println(pts.utilizaton());
//		System.out.println(RMS.isLiuLaylandSchedulable(pts));
//		System.out.println(RMS.isHyperbolicallySchedulable(pts));
//		System.out.println(TDA.isTDASchedulable(pts, true));
		
		Schedule rmsSchedule = new Schedule();
		try {
			rmsSchedule = Essence.schedule(pts, 0, 50, false, jobList -> RMS.hasHighestPriority(jobList));
			System.out.println(rmsSchedule);
		} catch (ViolatedDeadlineException e) {
			System.out.println(e);
			System.out.println("Schedule so far:");
			System.out.println(Essence.schedule);
		} catch (NotSchedulableException e) {
			System.out.println(e);
			System.out.println("Failed schedulability tests. If you want to schedule as far as possible, set the onlyIfSchedulable-variable to false.");
		} catch (InvalidTimeInterval e) {
			System.out.println(e);
		}
				
	}
}
