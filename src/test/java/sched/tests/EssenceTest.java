package sched.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import algo.sched.Essence;
import algo.sched.hard.EDF;
import algo.sched.hard.RMS;
import ds.PeriodicTask;
import ds.PeriodicTaskSet;
import ds.Schedule;
import exceptions.ViolatedDeadlineException;

class EssenceTest {

	@Test
	void generateRandomTaskSetTest() {
		long time = System.nanoTime();
		PeriodicTaskSet p = Essence.generateRandomTaskSet(5, new BigDecimal("50.000"), new BigDecimal("100.000"), 10, 20, 0);
		time = System.nanoTime() - time;
		
		System.out.println("Elapsed time for task set generation is " + (double) time / 1000000000.0 + "s");	
		System.out.println(p);
		System.out.println("Utilization is " + p.utilizaton());
		
		assertTrue(p.utilizaton().compareTo(BigDecimal.ONE) <= 0);
		assertTrue(p.utilizaton().compareTo(new BigDecimal(0.500)) >= 0);
		assertEquals(p.getpTaskSet().values().size(), 5);
	}
	
	@Test
	void idleTimeTest() {
		PeriodicTaskSet p = new PeriodicTaskSet();
		try {
			p.addPTask(new PeriodicTask("1", 18, 0, 48, 0, 48, 1));
			p.addPTask(new PeriodicTask("2", 2, 0, 6, 0, 6, 2));
			p.addPTask(new PeriodicTask("3", 1, 0, 4, 0, 4, 3));
		} catch (Exception e) {		
		}
		
		Schedule rmsSchedule = new Schedule();
		try {
			rmsSchedule = Essence.schedule(p, 0, 50, true, jobList -> RMS.hasHighestPriority(jobList));
		} catch (Exception e) {
		}
		
		System.out.println(p);
		System.out.println("Task set's utilization is " + p.utilizaton());
		
		try {
			Schedule s = new Schedule();
			assertEquals(Essence.totalIdleTime(s), 0);
			s = Essence.schedule(p, 0, 50, false, jobList -> EDF.hasHighestPriority(jobList));
			System.out.println(s);
			System.out.println("Total idle time " + Essence.totalIdleTime(s));	
			assertEquals(Essence.totalIdleTime(s), 2);
		} catch (ViolatedDeadlineException e) {
			System.out.println(e);
			System.out.println("Schedule so far:");
			System.out.println(Essence.schedule);
		} catch (Exception e) {
			System.out.println(e);
		}
		

	}
	
	


}
