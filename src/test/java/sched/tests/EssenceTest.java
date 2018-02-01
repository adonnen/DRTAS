package sched.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import algo.sched.Essence;
import algo.sched.hard.RMS;
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
		PeriodicTaskSet p = Essence.generateRandomTaskSet(3, new BigDecimal("50.000"), new BigDecimal("100.000"), 10, 100, 1);
		
		System.out.println(p);
		System.out.println("Task set's utilization is " + p.utilizaton());
		
		try {
			Schedule s = Essence.schedule(p, 0, 50, false, jobList -> RMS.hasHighestPriority(jobList));
			System.out.println(s);
			System.out.println("Total idle time " + Essence.totalIdleTime(s));	
			assertEquals(Essence.totalIdleTime(s), BigDecimal.ONE.subtract(p.utilizaton()).multiply(new BigDecimal("50.000")).longValue());
		} catch (ViolatedDeadlineException e) {
			System.out.println(e);
			System.out.println("Schedule so far:");
			System.out.println(Essence.schedule);
		} catch (Exception e) {
			System.out.println(e);
		}
		

	}
	
	


}
