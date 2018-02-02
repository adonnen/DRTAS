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
		System.out.println("====================================================================");
		System.out.println("Testing random task set generation: ");
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
		System.out.println("====================================================================");
		System.out.println("Testing idle time computation: ");
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
			System.out.println(e);
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
	
	@Test
	void generateRandomTaskTest() {
		System.out.println("====================================================================");
		System.out.println("Testing random task generation: ");
		assertTrue(Essence.generateRandomTask(1, new BigDecimal("1.000"), 5, 6).utilization().compareTo(BigDecimal.ONE) <= 0);
		assertTrue(Essence.generateRandomTask(1, new BigDecimal("0.860"), 5, 15).utilization().compareTo(new BigDecimal("0.860")) <= 0);
		assertTrue(Essence.generateRandomTask(1, new BigDecimal("0.869"), 5, 25).utilization().compareTo(new BigDecimal("0.869")) <= 0);
		
		
		System.out.println("Best utilization was " + Essence.generateRandomTask(1, new BigDecimal("1.000"), 5, 6).utilization());		
		System.out.println("Best utilization was " + Essence.generateRandomTask(1, new BigDecimal("0.860"), 5, 15).utilization());		
		System.out.println("Best utilization was " + Essence.generateRandomTask(1, new BigDecimal("0.869"), 5, 25).utilization());
	}
	
	
	@Test
	void generateFullUtilTest() {
		System.out.println("====================================================================");
		System.out.println("Testing full utilization task set generation: ");
		long time = System.nanoTime();
		PeriodicTaskSet result = Essence.generateTaskSetWithUtilONE(3, 3, 15);
		time = System.nanoTime() - time;
		
		System.out.println("Elapsed time is " + (double) time / 1000000000.0 + "s");
		
		System.out.println(result);
		System.out.println("Resulting utilization: " + result.utilizaton());
		
		assertTrue(BigDecimal.ONE.subtract(result.utilizaton()).compareTo(new BigDecimal ("0.005")) <= 0);
	}
	
	
	@Test
	void generateDifferentEDFtoRMSTest() {
		System.out.println("====================================================================");
		System.out.println("Testing task set with different edf/rms schedules generation: ");
		long time = System.nanoTime();
		PeriodicTaskSet p = Essence.generateDifferentEDFtoRMS(3, 3, 15);
		time = System.nanoTime() - time;
		
		System.out.println("Elapsed time is " + (double) time / 1000000000.0 + "s");
		
		System.out.println(p);
		System.out.println("Total Utilization is " + p.utilizaton());
		
		try {
			System.out.println("Hyperperiod of the task set is " + p.hyperPeriod());
			System.out.println("RMS Schedule:");
			System.out.println(Essence.normalizeSchedule(Essence.schedule(p, 0, 50, true, jobList -> RMS.hasHighestPriority(jobList))));
			System.out.println("EDF Schedule:");
			System.out.println(Essence.normalizeSchedule(Essence.schedule(p, 0, 50, true, jobList -> EDF.hasHighestPriority(jobList))));
		} catch (Exception e) {
			System.out.println(e);
		}
	}


}
