package demos.analysis;

import java.math.BigDecimal;
import ds.*;
import algo.sched.Essence;
import algo.sched.hard.RMS;
import ds.PeriodicTaskSet;
import exceptions.ViolatedDeadlineException;

public class idleTime {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		PeriodicTaskSet p = Essence.generateRandomTaskSet(3, new BigDecimal("50.000"), new BigDecimal("100.000"), 10, 100, 1);
		
		System.out.println(p);
		System.out.println("Task set's utilization is " + p.utilizaton());
		
		try {
			Schedule s = Essence.schedule(p, 0, 10, false, jobList -> RMS.hasHighestPriority(jobList));
			System.out.println(s);
			System.out.println("Total idle time " + Essence.totalIdleTime(s));			
		} catch (ViolatedDeadlineException e) {
			System.out.println(e);
			System.out.println("Schedule so far:");
			System.out.println(Essence.schedule);
		} catch (Exception e) {
			System.out.println(e);
		}
		
		
		

	}

}
